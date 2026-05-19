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
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.clavarisalmussafesapp.ui.theme.ClavarisAlmussafesAppTheme
import java.util.concurrent.TimeUnit

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
        NotificationHelper.createNotificationChannel(this)
        scheduleUpdateWorker()

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

    private fun scheduleUpdateWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val updateRequest = PeriodicWorkRequestBuilder<UpdateWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "clavaris_update_worker",
            ExistingPeriodicWorkPolicy.UPDATE, // Actualizar para aplicar el cambio de 15 min
            updateRequest
        )
    }
}

fun sendNotification(context: Context) {
    NotificationHelper.sendNotification(
        context,
        "Notificacions Actives",
        "Ara rebràs les noticies de Clavaris Almussafes."
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(isDarkMode: Boolean, onDarkModeChange: (Boolean) -> Unit) {
    val context = LocalContext.current
    val sharedPref = remember { context.getSharedPreferences("prefs", Context.MODE_PRIVATE) }
    
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    var notificationsEnabled by remember { 
        mutableStateOf(sharedPref.getBoolean("notifications_enabled", false)) 
    }

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
                        onNotificationsChange = { enabled -> 
                            notificationsEnabled = enabled
                            sharedPref.edit().putBoolean("notifications_enabled", enabled).apply()
                        }
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
