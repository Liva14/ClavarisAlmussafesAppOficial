package com.example.clavarisalmussafesapp

import android.Manifest
import android.os.Build
import java.util.Calendar
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalUriHandler
import coil.compose.AsyncImage
import kotlin.collections.*

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Hero Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.clavaris),
                contentDescription = "Clavaris Almussafes",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
        }
        
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Benvinguts a Clavaris Almussafes",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Aquesta és l'aplicació oficial dels Clavaris d'Almussafes. " +
                "Aquí podràs trobar tota la informació sobre els nosaltres esdeveniments, " +
                "calendari de festes, rifes i les darreres notícies de la nostra activitat.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
        
        // Espai per a un missatge destacat o informació general
        Card(
            modifier = Modifier.padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Properes Festes 2026",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Estem preparant unes festes inoblidables. Consulta la secció de calendari per no perdre't res!",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // Social Media Section
        val uriHandler = LocalUriHandler.current
        
        Text(
            "Segueix-nos a les xarxes socials",
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { uriHandler.openUri("https://www.instagram.com/clavarisalmussafes2026/") },
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE1306C).copy(alpha = 0.1f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color(0xFFE1306C))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Instagram", color = Color(0xFFE1306C), fontWeight = FontWeight.Bold)
                }
            }
            
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { uriHandler.openUri("https://www.tiktok.com/@clavarisalmussafes2026") },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.MusicNote, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("TikTok", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                }
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))

        Text(
            "Els nostres patrocinadors",
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            "Gràcies al suport d'aquestes empreses i comerços que fan possible la nostra activitat.",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        val context = LocalContext.current
        val sponsorLogos = listOf(
            "bit", "c4u", "cga", "cr3", "vmc", "fante", "telca", "andreu", "norita", "ypelos",
            "adrines", "carioca", "colaita", "totobra", "valhala", "yokokan", "almucasa", "dbmethod",
            "fornrosa", "maviclim", "medicser", "myprince", "santacruz", "solopizza", "barhungria",
            "flowcamper", "inelectric", "labarberia", "protecx10n", "silviasalo", "zapatoslara",
            "almontessori", "judezjoyeros", "logovillajos", "abelmarrtipvc", "crossatletica",
            "galaxyeventos", "gervasgimenez", "luissecretari", "pinturascuevas", "cambridgecenter",
            "clinicavillalba", "farmaciaaparici", "salvadorlladosa", "farmaciaguerrero", "parafarmaciavero",
            "veterinariaordas", "clinicaluciasoler", "comercialagricola", "etform_logo2negro",
            "farmaciasebastian", "almacentroestetica", "inmobiliariaferrus", "fontaneria_almussafes",
            "cafeteriacentrecultural"
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            sponsorLogos.chunked(3).forEach { rowLogos ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    rowLogos.forEach { logoName ->
                        val resId = context.resources.getIdentifier(logoName, "drawable", context.packageName)
                        if (resId != 0) {
                            Card(
                                modifier = Modifier.weight(1f).aspectRatio(1.5f),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = resId),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize().padding(8.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    // Fill empty spaces in the last row if necessary
                    repeat(3 - rowLogos.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun PostItem(title: String, description: String, imageResName: String) {
    val context = LocalContext.current
    val imageRes = remember(imageResName) {
        context.resources.getIdentifier(imageResName, "drawable", context.packageName)
    }

    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary))
            Spacer(modifier = Modifier.width(12.dp))
            Text(title, fontWeight = FontWeight.Bold)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            if (imageRes != 0) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Text(
            description,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun NewsScreen() {
    val context = LocalContext.current
    val newsItems by produceState<List<NewsPost>>(initialValue = emptyList(), context) {
        value = DataLoader.loadNews(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        if (newsItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            newsItems.forEach { item ->
                NewsItem(
                    title = item.title,
                    description = item.description,
                    imageResName = item.imageResName,
                    imageUrl = item.imageUrl
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
            }
        }
    }
}

@Composable
fun NewsItem(title: String, description: String, imageResName: String?, imageUrl: String?) {
    val context = LocalContext.current
    val imageRes = remember(imageResName) {
        if (imageResName != null) {
            context.resources.getIdentifier(imageResName, "drawable", context.packageName)
        } else 0
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Event,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        }
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            if (imageUrl != null) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )
            } else if (imageRes != 0) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = title,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )
            }
        }

        Text(
            description,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun CalendarScreen() {
    val context = LocalContext.current
    val calendarInstance = Calendar.getInstance()
    var selectedDay by remember { mutableIntStateOf(calendarInstance.get(Calendar.DAY_OF_MONTH)) }
    var selectedMonth by remember { mutableIntStateOf(calendarInstance.get(Calendar.MONTH) + 1) }
    var selectedYear by remember { mutableIntStateOf(calendarInstance.get(Calendar.YEAR)) }
    val eventsMap = remember { mutableStateMapOf<String, MutableList<CalendarEvent>>() }

    LaunchedEffect(Unit) {
        if (eventsMap.isEmpty()) {
            val allEvents = DataLoader.loadEvents(context)
            allEvents.forEach { event ->
                val list = eventsMap.getOrPut(event.date) { mutableListOf() }
                list.add(event)
            }
            
            // Re-añadir el mercado si no viene del servidor
            for (month in 5..7) {
                val tempCal = Calendar.getInstance()
                tempCal.set(2026, month - 1, 1)
                val totalDays = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH)
                val firstDayOffset = (tempCal.get(Calendar.DAY_OF_WEEK) + 5) % 7
                for (day in 1..totalDays) {
                    if ((firstDayOffset + day - 1) % 7 == 1) {
                        val key = "2026-$month-$day"
                        val list = eventsMap.getOrPut(key) { mutableListOf() }
                        if (list.none { it.title == "Mercat" }) {
                            list.add(0, CalendarEvent(key, "Mercat", "10:00", EventType.MERCADO))
                        }
                    }
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val monthNames = listOf("Gener", "Febrer", "Març", "Abril", "Maig", "Juny", "Juliol", "Agost", "Setembre", "Octubre", "Novembre", "Desembre")
            val monthName = "${monthNames[selectedMonth - 1]} $selectedYear"
            
            IconButton(onClick = { 
                if (selectedMonth == 1) {
                    selectedMonth = 12
                    selectedYear--
                } else {
                    selectedMonth--
                }
            }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }
            Text(monthName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            IconButton(onClick = { 
                if (selectedMonth == 12) {
                    selectedMonth = 1
                    selectedYear++
                } else {
                    selectedMonth++
                }
            }) {
                Icon(Icons.Default.ArrowForward, contentDescription = null)
            }
        }

        // Calendar Grid
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                listOf("L", "M", "X", "J", "V", "S", "D").forEach { day ->
                    Text(day, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            
            val gridCal = Calendar.getInstance().apply {
                set(Calendar.YEAR, selectedYear)
                set(Calendar.MONTH, selectedMonth - 1)
                set(Calendar.DAY_OF_MONTH, 1)
            }
            val totalDays = gridCal.getActualMaximum(Calendar.DAY_OF_MONTH)
            val firstDayOffset = (gridCal.get(Calendar.DAY_OF_WEEK) + 5) % 7

            val calendarItems = List(firstDayOffset) { null } + (1..totalDays).toList()
            
            calendarItems.chunked(7).forEach { week ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                    week.forEach { day ->
                        if (day != null) {
                            val dateKey = "$selectedYear-$selectedMonth-$day"
                            val dayEvents = eventsMap[dateKey] ?: listOf()
                            val isSelected = selectedDay == day

                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) MaterialTheme.colorScheme.onBackground else Color.Transparent)
                                    .clickable { selectedDay = day },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        "$day",
                                        color = if (isSelected) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onSurface,
                                        fontSize = 14.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                        dayEvents.take(3).forEach { event ->
                                            Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(event.type.color))
                                        }
                                    }
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.size(40.dp))
                        }
                    }
                    repeat(7 - week.size) { Spacer(modifier = Modifier.size(40.dp)) }
                }
            }
        }

        HorizontalDivider(modifier = Modifier.padding(top = 16.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))

        // Events List
        val currentEvents = eventsMap["$selectedYear-$selectedMonth-$selectedDay"] ?: listOf()
        Column(
            modifier = Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()),
        ) {
            Text(
                "Events del dia $selectedDay",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            
            currentEvents.forEach { event ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    tonalElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier.height(IntrinsicSize.Min),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(8.dp)
                                .background(event.type.color)
                        )
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(event.type.color))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(event.title, fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.titleMedium)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Hora: ${event.time}", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            if (currentEvents.isEmpty()) {
                Text(
                    "Tranquil·litat... no hi ha res previst.", 
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
fun LotteryScreen() {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
    ) {
        Text(
            "Rifes", 
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge, 
            fontWeight = FontWeight.Bold
        )
        LotteryItem("Rifa Febrer", R.drawable.rifa_febrer)
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))

        LotteryItem("Rifa Gener", R.drawable.rifa_gener)
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))

        LotteryItem("Rifa Nadal", R.drawable.rifa_nadal)
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))

        LotteryItem("Rifa Novembre", R.drawable.rifa_novembre)
    }
}

@Composable
fun LotteryItem(title: String, imageRes: Int) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.ConfirmationNumber,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun SettingsScreen(
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    notificationsEnabled: Boolean,
    onNotificationsChange: (Boolean) -> Unit
) {
    val context = LocalContext.current
    var showRationale by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        onNotificationsChange(isGranted)
        if (isGranted) {
            sendNotification(context)
        }
    }

    if (showRationale) {
        AlertDialog(
            onDismissRequest = { showRationale = false },
            title = { Text("Notificacions") },
            text = { Text("Voleu activar les notificacions per a no perdre cap acte?") },
            confirmButton = {
                TextButton(onClick = {
                    showRationale = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        onNotificationsChange(true)
                        sendNotification(context)
                    }
                }) { Text("Activar") }
            },
            dismissButton = {
                TextButton(onClick = { showRationale = false }) { Text("Més tard") }
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            "Configuració", 
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge, 
            fontWeight = FontWeight.Bold
        )
        
        ListItem(
            headlineContent = { Text("Mode fosc") },
            trailingContent = {
                Switch(checked = isDarkMode, onCheckedChange = onDarkModeChange)
            }
        )
        
        ListItem(
            headlineContent = { Text("Notificacions") },
            trailingContent = {
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { enabled ->
                        if (enabled) showRationale = true else onNotificationsChange(false)
                    }
                )
            }
        )
    }
}
