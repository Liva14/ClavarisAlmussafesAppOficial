package com.example.clavarisalmussafesapp

import android.Manifest
import android.os.Build
import java.util.Calendar
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalUriHandler
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.*

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    var sponsors by remember { mutableStateOf<List<Sponsor>>(emptyList()) }

    LaunchedEffect(Unit) {
        sponsors = DataLoader.loadSponsors(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Hero Carousel
        HomeCarousel()
        
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Benvinguts a Clavaris Almussafes",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Benvolgut poble d'Almussafes, som els Clavaris de la Divina Aurora d'Almussafes per a les festes de 2026‼️\n\n" +
                "Tenim un year molt especial per davant, que ens ompli d'il·lusió compartir amb tot el poble, així que comptem amb cadascú de vosaltres perquè ens ajudeu a formar part del poble i de les seues festes.🫂🎆\n\n" +
                "En aquesta aplicació, vos mantindrem al dia de totes les activitats que organitzem, així que vos esperem en cada acte i en cada celebració!🗓️🔔\n\n" +
                "Visca Almussafes i visca la Divina Aurora!",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
        
        // Destacado
        Card(
            modifier = Modifier.padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("DISCOMÒBIL EL 30 DE MAIG", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Esteu preparats per a la pròxima gran festa dels Clavaris? \nNo vos pergau la festa en la pistapolivalent el proper 30 de Maig. \nVOS ESPEREM A TOTS!", style = MaterialTheme.typography.bodyMedium)
            }
        }

        // Social Media
        val uriHandler = LocalUriHandler.current
        Text("Segueix-nos a les xarxes socials", modifier = Modifier.padding(horizontal = 16.dp), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SocialCard("Instagram", Color(0xFFE1306C), Icons.Default.CameraAlt, Modifier.weight(1f)) { uriHandler.openUri("https://www.instagram.com/clavarisalmussafes2026/") }
            SocialCard("TikTok", MaterialTheme.colorScheme.onSurface, Icons.Default.MusicNote, Modifier.weight(1f)) { uriHandler.openUri("https://www.tiktok.com/@clavarisalmussafes2026") }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))

        // Patrocinadores 2x2
        Text("Els nostres patrocinadors", modifier = Modifier.padding(horizontal = 16.dp), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text("Gràcies al suport d'aquestes empreses i comerços.", modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

        SponsorGrid(sponsors)
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun HomeCarousel() {
    val images = remember {
        val baseUrl = "https://raw.githubusercontent.com/Liva14/ClavarisAlmussafesAppOficial/master/app/src/main/res/drawable/"
        listOf(
            "${baseUrl}Carru1.jpg",
            "${baseUrl}Carru2.jpg",
            "${baseUrl}Carru3.jpg",
            "${baseUrl}Carru4.jpg",
            "${baseUrl}Carru5.jpg",
            "${baseUrl}Carru6.jpg"
        )
    }
    
    // Para hacer el carrusel infinito, usamos un número de páginas muy alto
    val startIndex = Int.MAX_VALUE / 2
    val initialPage = startIndex - (startIndex % images.size)
    
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { Int.MAX_VALUE }
    )
    
    // Auto-scroll logic
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val actualIndex = page % images.size
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(images[actualIndex])
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun SocialCard(text: String, color: Color, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Icon(icon, contentDescription = null, tint = color)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text, color = color, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SponsorGrid(sponsors: List<Sponsor>) {
    val rows = remember(sponsors) { sponsors.chunked(2) }
    val context = LocalContext.current
    
    Column(modifier = Modifier.padding(16.dp)) {
        rows.forEach { rowSponsors ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                rowSponsors.forEach { sponsor ->
                    Card(
                        modifier = Modifier.weight(1f).aspectRatio(1.5f),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        val imageResId = remember(sponsor.imageResName) {
                            if (sponsor.imageResName != null) context.resources.getIdentifier(sponsor.imageResName, "drawable", context.packageName) else 0
                        }

                        if (sponsor.imageUrl != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(context).data(sponsor.imageUrl).crossfade(true).build(),
                                contentDescription = sponsor.name,
                                modifier = Modifier.fillMaxSize().padding(12.dp),
                                contentScale = ContentScale.Fit
                            )
                        } else if (imageResId != 0) {
                            Image(
                                painter = painterResource(id = imageResId),
                                contentDescription = sponsor.name,
                                modifier = Modifier.fillMaxSize().padding(12.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }
                if (rowSponsors.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var newsItems by remember { mutableStateOf<List<NewsPost>>(emptyList()) }
    var isRefreshing by remember { mutableStateOf(false) }
    var selectedNews by remember { mutableStateOf<NewsPost?>(null) }

    LaunchedEffect(Unit) {
        isRefreshing = true
        newsItems = DataLoader.loadNews(context)
        isRefreshing = false
    }

    if (selectedNews != null) {
        BackHandler { selectedNews = null }
        NewsDetailView(post = selectedNews!!, onBack = { selectedNews = null })
    } else {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                scope.launch {
                    isRefreshing = true
                    newsItems = DataLoader.loadNews(context)
                    isRefreshing = false
                }
            },
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.background)
            ) {
                if (newsItems.isEmpty() && !isRefreshing) {
                    Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("No hi ha notícies disponibles", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                } else if (newsItems.isNotEmpty()) {
                    val heroPost = newsItems.first()
                    NewsHeroItem(post = heroPost, onClick = { selectedNews = heroPost })
                    Spacer(modifier = Modifier.height(16.dp))
                    newsItems.drop(1).forEach { post ->
                        NewsSmallItem(post = post, onClick = { selectedNews = post })
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun NewsHeroItem(post: NewsPost, onClick: () -> Unit) {
    val context = LocalContext.current
    val imageResId = remember(post.imageResName) {
        if (post.imageResName != null) context.resources.getIdentifier(post.imageResName, "drawable", context.packageName) else 0
    }

    Column(modifier = Modifier.fillMaxWidth().clickable { onClick() }) {
        Box(modifier = Modifier.fillMaxWidth().height(240.dp).background(MaterialTheme.colorScheme.surfaceVariant)) {
            NewsImage(imageUrl = post.imageUrl, resId = imageResId, contentScale = ContentScale.Crop)
        }
        Column(modifier = Modifier.padding(16.dp)) {
            Text("CLAVARIS 2026", style = MaterialTheme.typography.labelLarge, color = Color(0xFFE53935), fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(post.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, lineHeight = 28.sp)
        }
    }
}

@Composable
fun NewsSmallItem(post: NewsPost, onClick: () -> Unit) {
    val context = LocalContext.current
    val imageResId = remember(post.imageResName) {
        if (post.imageResName != null) context.resources.getIdentifier(post.imageResName, "drawable", context.packageName) else 0
    }

    Row(modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            Text("CLAVARIS 2026", style = MaterialTheme.typography.labelSmall, color = Color(0xFFE53935), fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(post.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 3, overflow = TextOverflow.Ellipsis)
        }
        Box(modifier = Modifier.size(100.dp).clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.surfaceVariant)) {
            NewsImage(imageUrl = post.imageUrl, resId = imageResId, contentScale = ContentScale.Crop)
        }
    }
}

@Composable
fun NewsImage(imageUrl: String?, resId: Int, contentScale: ContentScale) {
    val context = LocalContext.current
    if (imageUrl != null) {
        AsyncImage(
            model = ImageRequest.Builder(context).data(imageUrl).crossfade(true).build(),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = contentScale
        )
    } else if (resId != 0) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = contentScale
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailView(post: NewsPost, onBack: () -> Unit) {
    val context = LocalContext.current
    val imageResId = remember(post.imageResName) {
        if (post.imageResName != null) context.resources.getIdentifier(post.imageResName, "drawable", context.packageName) else 0
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)) {
                CenterAlignedTopAppBar(
                    title = { Text(post.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                    navigationIcon = {
                        IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Tornar") }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent, titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant),
                    windowInsets = WindowInsets(0, 0, 0, 0)
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding).verticalScroll(rememberScrollState()).background(MaterialTheme.colorScheme.background)) {
            Box(modifier = Modifier.fillMaxWidth().wrapContentHeight().background(MaterialTheme.colorScheme.surfaceVariant)) {
                NewsImage(imageUrl = post.imageUrl, resId = imageResId, contentScale = ContentScale.FillWidth)
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(post.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text(post.description, style = MaterialTheme.typography.bodyLarge, lineHeight = 24.sp)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
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
            val tempMap = mutableMapOf<String, MutableList<CalendarEvent>>()
            
            allEvents.forEach { event ->
                tempMap.getOrPut(event.date) { mutableListOf() }.add(event)
            }
            
            for (month in 5..7) {
                val tempCal = Calendar.getInstance()
                tempCal.set(2026, month - 1, 1)
                val totalDays = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH)
                val firstDayOffset = (tempCal.get(Calendar.DAY_OF_WEEK) + 5) % 7
                for (day in 1..totalDays) {
                    if ((firstDayOffset + day - 1) % 7 == 1) {
                        val key = "2026-$month-$day"
                        val list = tempMap.getOrPut(key) { mutableListOf() }
                        if (list.none { it.title == "Mercat" }) {
                            list.add(0, CalendarEvent(key, "Mercat", "10:00", EventType.MERCADO))
                        }
                    }
                }
            }
            eventsMap.putAll(tempMap)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            val monthNames = listOf("Gener", "Febrer", "Març", "Abril", "Maig", "Juny", "Juliol", "Agost", "Setembre", "Octubre", "Novembre", "Desembre")
            Text("${monthNames[selectedMonth - 1]} $selectedYear", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Row {
                IconButton(onClick = { if (selectedMonth == 1) { selectedMonth = 12; selectedYear-- } else selectedMonth-- }) { Icon(Icons.Default.ArrowBack, null) }
                IconButton(onClick = { if (selectedMonth == 12) { selectedMonth = 1; selectedYear++ } else selectedMonth++ }) { Icon(Icons.Default.ArrowForward, null) }
            }
        }

        // Calendar Grid
        val gridCal = Calendar.getInstance().apply { set(Calendar.YEAR, selectedYear); set(Calendar.MONTH, selectedMonth - 1); set(Calendar.DAY_OF_MONTH, 1) }
        val totalDays = gridCal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val firstDayOffset = (gridCal.get(Calendar.DAY_OF_WEEK) + 5) % 7
        val calendarItems = List(firstDayOffset) { null } + (1..totalDays).toList()

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                listOf("L", "M", "X", "J", "V", "S", "D").forEach { day ->
                    Text(day, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            calendarItems.chunked(7).forEach { week ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                    week.forEach { day ->
                        if (day != null) {
                            val dateKey = "$selectedYear-$selectedMonth-$day"
                            val dayEvents = eventsMap[dateKey] ?: listOf()
                            val isSelected = selectedDay == day
                            Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent).clickable { selectedDay = day }, contentAlignment = Alignment.Center) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("$day", color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface, fontSize = 14.sp)
                                    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                        dayEvents.take(3).forEach { event -> Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(event.type.color)) }
                                    }
                                }
                            }
                        } else Spacer(modifier = Modifier.size(40.dp))
                    }
                    repeat(7 - week.size) { Spacer(modifier = Modifier.size(40.dp)) }
                }
            }
        }

        HorizontalDivider(modifier = Modifier.padding(top = 16.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))

        val currentEvents = eventsMap["$selectedYear-$selectedMonth-$selectedDay"] ?: listOf()
        Column(modifier = Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()).padding(16.dp)) {
            Text("Events del dia $selectedDay", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            currentEvents.forEach { event ->
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(event.type.color))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(event.title, fontWeight = FontWeight.Bold)
                            Text("Hora: ${event.time}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
            if (currentEvents.isEmpty()) Text("Tranquil·litat... no hi ha res previst.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LotteryScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var lotteryItems by remember { mutableStateOf<List<LotteryPost>>(emptyList()) }
    var isRefreshing by remember { mutableStateOf(false) }
    var selectedLottery by remember { mutableStateOf<LotteryPost?>(null) }

    LaunchedEffect(Unit) {
        isRefreshing = true
        lotteryItems = DataLoader.loadLotteries(context)
        isRefreshing = false
    }

    if (selectedLottery != null) {
        BackHandler { selectedLottery = null }
        LotteryDetailView(post = selectedLottery!!, onBack = { selectedLottery = null })
    } else {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                scope.launch {
                    isRefreshing = true
                    lotteryItems = DataLoader.loadLotteries(context)
                    isRefreshing = false
                }
            },
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.background)
            ) {
                if (lotteryItems.isEmpty() && !isRefreshing) {
                    Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("No hi ha rifes disponibles", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                } else if (lotteryItems.isNotEmpty()) {
                    val heroPost = lotteryItems.first()
                    LotteryHeroItem(post = heroPost, onClick = { selectedLottery = heroPost })
                    Spacer(modifier = Modifier.height(16.dp))
                    lotteryItems.drop(1).forEach { post ->
                        LotterySmallItem(post = post, onClick = { selectedLottery = post })
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun LotteryHeroItem(post: LotteryPost, onClick: () -> Unit) {
    val context = LocalContext.current
    val imageResId = remember(post.imageResName) {
        if (post.imageResName != null) context.resources.getIdentifier(post.imageResName, "drawable", context.packageName) else 0
    }

    Column(modifier = Modifier.fillMaxWidth().clickable { onClick() }) {
        Box(modifier = Modifier.fillMaxWidth().height(240.dp).background(MaterialTheme.colorScheme.surfaceVariant)) {
            LotteryImage(imageUrl = post.imageUrl, resId = imageResId, contentScale = ContentScale.Crop)
        }
        Column(modifier = Modifier.padding(16.dp)) {
            Text("RIFES CLAVARIS 2026", style = MaterialTheme.typography.labelLarge, color = Color(0xFFE53935), fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(post.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, lineHeight = 28.sp)
        }
    }
}

@Composable
fun LotterySmallItem(post: LotteryPost, onClick: () -> Unit) {
    val context = LocalContext.current
    val imageResId = remember(post.imageResName) {
        if (post.imageResName != null) context.resources.getIdentifier(post.imageResName, "drawable", context.packageName) else 0
    }

    Row(modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            Text("RIFES CLAVARIS 2026", style = MaterialTheme.typography.labelSmall, color = Color(0xFFE53935), fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(post.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 3, overflow = TextOverflow.Ellipsis)
        }
        Box(modifier = Modifier.size(100.dp).clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.surfaceVariant)) {
            LotteryImage(imageUrl = post.imageUrl, resId = imageResId, contentScale = ContentScale.Crop)
        }
    }
}

@Composable
fun LotteryImage(imageUrl: String?, resId: Int, contentScale: ContentScale) {
    val context = LocalContext.current
    if (imageUrl != null) {
        AsyncImage(
            model = ImageRequest.Builder(context).data(imageUrl).crossfade(true).build(),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = contentScale
        )
    } else if (resId != 0) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = contentScale
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LotteryDetailView(post: LotteryPost, onBack: () -> Unit) {
    val context = LocalContext.current
    val imageResId = remember(post.imageResName) {
        if (post.imageResName != null) context.resources.getIdentifier(post.imageResName, "drawable", context.packageName) else 0
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)) {
                CenterAlignedTopAppBar(
                    title = { Text(post.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                    navigationIcon = {
                        IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Tornar") }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent, titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant),
                    windowInsets = WindowInsets(0, 0, 0, 0)
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding).verticalScroll(rememberScrollState()).background(MaterialTheme.colorScheme.background)) {
            Box(modifier = Modifier.fillMaxWidth().wrapContentHeight().background(MaterialTheme.colorScheme.surfaceVariant)) {
                LotteryImage(imageUrl = post.imageUrl, resId = imageResId, contentScale = ContentScale.FillWidth)
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(post.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text(post.description, style = MaterialTheme.typography.bodyLarge, lineHeight = 24.sp)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SettingsScreen(isDarkMode: Boolean, onDarkModeChange: (Boolean) -> Unit, notificationsEnabled: Boolean, onNotificationsChange: (Boolean) -> Unit) {
    val context = LocalContext.current
    var showRationale by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        onNotificationsChange(isGranted)
        if (isGranted) NotificationHelper.sendNotification(context, "Notificacions Actives", "Ara rebràs les noticies.")
    }

    if (showRationale) {
        AlertDialog(
            onDismissRequest = { showRationale = false },
            title = { Text("Notificacions") },
            text = { Text("Voleu activar les notificacions per a no perdre cap acte?") },
            confirmButton = {
                TextButton(onClick = {
                    showRationale = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    else { onNotificationsChange(true); NotificationHelper.sendNotification(context, "Notificacions Actives", "Ara rebràs les noticies.") }
                }) { Text("Activar") }
            },
            dismissButton = { TextButton(onClick = { showRationale = false }) { Text("Més tard") } }
        )
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text("Acerca de") },
            text = { Text("Aquesta aplicació ha estat desenvolupada com a part d'un Treball de Final de Grau (TFG).\n\nEs tracta d'un projecte sense ànim de lucre creat per a facilitar la comunicació i organització dels Clavaris d'Almussafes.") },
            confirmButton = { TextButton(onClick = { showAboutDialog = false }) { Text("Tancar") } }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text("Configuració", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        ListItem(headlineContent = { Text("Mode fosc") }, trailingContent = { Switch(checked = isDarkMode, onCheckedChange = onDarkModeChange) })
        ListItem(headlineContent = { Text("Notificacions") }, trailingContent = { Switch(checked = notificationsEnabled, onCheckedChange = { if (it) showRationale = true else onNotificationsChange(false) }) })
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
        ListItem(headlineContent = { Text("Acerca de") }, modifier = Modifier.clickable { showAboutDialog = true }, trailingContent = { Icon(Icons.Default.Info, null) })
    }
}
