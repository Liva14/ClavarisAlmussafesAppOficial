package com.example.clavarisalmussafesapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.clavarisalmussafesapp.ui.theme.ClavarisAlmussafesAppTheme

object Screens {
    object Home : Screen("home", "Clavaris Almussafes", Icons.Default.Home)
    object News : Screen("news", "Noticies", Icons.Default.Public)
    object Calendar : Screen("calendar", "Calendari", Icons.Default.CalendarMonth)
    object Lottery : Screen("lottery", "Loteries i Rifes", Icons.Default.ConfirmationNumber)
    object Settings : Screen("settings", "Configuració", Icons.Default.Settings)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val sharedPref = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        
        createNotificationChannel()
        enableEdgeToEdge()
        setContent {
            var isDarkMode by remember { 
                mutableStateOf(sharedPref.getBoolean("isDarkMode", false)) 
            }
            
            ClavarisAlmussafesAppTheme(darkTheme = isDarkMode) {
                MainApp(
                    isDarkMode = isDarkMode, 
                    onDarkModeChange = { 
                        isDarkMode = it
                        sharedPref.edit().putBoolean("isDarkMode", it).apply()
                    }
                )
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Avisos Clavaris"
            val descriptionText = "Canal para notificaciones de Clavaris Almussafes"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CLAVARIS_CH", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

fun sendNotification(context: Context) {
    val builder = NotificationCompat.Builder(context, "CLAVARIS_CH")
        .setSmallIcon(R.drawable.ic_notification_logo)
        .setContentTitle("Notificacions Actives")
        .setContentText("Ara rebràs les noticies de Clavaris Almussafes.")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)

    with(NotificationManagerCompat.from(context)) {
        try {
            notify(1, builder.build())
        } catch (e: SecurityException) {
            // Permission not granted
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(isDarkMode: Boolean, onDarkModeChange: (Boolean) -> Unit) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var notificationsEnabled by remember { mutableStateOf(false) }

    val bottomScreens = listOf(
        Screens.Calendar,
        Screens.Lottery,
        Screens.Home,
        Screens.News,
        Screens.Settings
    )

    Scaffold(
        topBar = {
            val title = bottomScreens.find { it.route == currentRoute }?.title ?: "Clavaris Almussafes"
            Column {
                CenterAlignedTopAppBar(
                    title = { 
                        Text(
                            title, 
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.5).sp,
                            color = MaterialTheme.colorScheme.onSurface
                        ) 
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
            }
        },
        bottomBar = {
            BottomNavigationBar(navController, bottomScreens)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            NavHost(navController = navController, startDestination = Screens.Home.route) {
                composable(Screens.Home.route) { HomeScreen() }
                composable(Screens.News.route) { NewsScreen() }
                composable(Screens.Calendar.route) { CalendarScreen() }
                composable(Screens.Lottery.route) { LotteryScreen() }
                composable(Screens.Settings.route) {
                    SettingsScreen(
                        isDarkMode = isDarkMode,
                        onDarkModeChange = onDarkModeChange,
                        notificationsEnabled = notificationsEnabled,
                        onNotificationsChange = { enabled -> notificationsEnabled = enabled }
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, screens: List<Screen>) {
    Column {
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = 8.dp,
            modifier = Modifier.height(64.dp)
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            for (screen in screens) {
                val isSelected = currentRoute == screen.route
                NavigationBarItem(
                    icon = { 
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = screen.title,
                            modifier = Modifier.size(26.dp)
                        ) 
                    },
                    selected = isSelected,
                    onClick = {
                        if (currentRoute != screen.route) {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onBackground,
                        unselectedIconColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}
