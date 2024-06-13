package lammbph37723.fpoly.thithulan3

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay


data class Flower(val id: Int, val name: String, val imageUrl: String, val description: String)

val flowerList = listOf(
    Flower(1,"Rose", "https://e7.pngegg.com/pngimages/119/921/png-clipart-flower-dahlia-flower-flower-garden-annual-plant-thumbnail.png", "Roses are beautiful flowers."),
    Flower(2,"Tulip", "https://png.pngtree.com/png-clipart/20230321/original/pngtree-red-roses-with-swirls-lovely-flowers-png-image_8998751.png", "Tulips come in many colors."),
    Flower(3,"Daisy", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQQoTwEvEAlJLZGwzzgXONYba_sR3gRM0n1jg&s", "Daisies are cheerful flowers."),
    Flower(4,"Sunflower", "https://totalpng.com//public/uploads/preview/christmas-flower-png-hd-11667555925q6lavws5wl.png", "Sunflowers turn towards the sun."),
    Flower(5,"Rose", "https://e7.pngegg.com/pngimages/119/921/png-clipart-flower-dahlia-flower-flower-garden-annual-plant-thumbnail.png", "Roses are beautiful flowers."),
    Flower(6,"Rose", "https://e7.pngegg.com/pngimages/119/921/png-clipart-flower-dahlia-flower-flower-garden-annual-plant-thumbnail.png", "Roses are beautiful flowers."),

)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlowerApp()
        }
    }
}

@Composable
fun FlowerApp() {
    var showWelcomeScreen by remember { mutableStateOf(true) }
    var flowerList by remember { mutableStateOf(flowerList) }
    var showDialog by remember { mutableStateOf(false) } // Thêm biến state cho dialog mới
    if (showWelcomeScreen) {
        WelcomeScreen { showWelcomeScreen = false }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(flowerList) { flower ->
                    FlowerItem(
                        flower = flower,
                        onDeleteFlower = { flowerList = flowerList.filter { it.id != flower.id } },
                        onRefreshData = { /* Tải lại dữ liệu */ },
                        onEditFlower = { updatedFlower ->
                            flowerList = flowerList.map {
                                if (it.id == updatedFlower.id) updatedFlower else it
                            }
                        }
                    )
                }
            }
            FloatingActionButton(
                onClick = { showDialog = true },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Thêm")
            }
        }
    }
    // Hiển thị hộp thoại khi showDialog là true
    if (showDialog) {
        AddFlowerDialog(
            onAddFlower = { newFlower -> flowerList = flowerList + newFlower },
            onDismiss = { showDialog = false }
        )
    }
}




@Composable
fun WelcomeScreen(onStart: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(3000L) // Chờ 3 giây
        onStart() // Gọi hàm onStart để chuyển sang màn hình tiếp theo
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onStart) {
            Text("Start")
        }
    }
}

@Composable
fun FlowerList(
    flowerList: List<Flower>,
    onDeleteFlower: (Flower) -> Unit,
    onRefreshData: () -> Unit,
    onEditFlower: (Flower) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(flowerList) { flower ->
            FlowerItem(
                flower = flower,
                onDeleteFlower = onDeleteFlower,
                onRefreshData = onRefreshData,
                onEditFlower = onEditFlower
            )
        }
    }
}


@Composable
fun FlowerItem(
    flower: Flower,
    onDeleteFlower: (Flower) -> Unit,
    onRefreshData: () -> Unit,
    onEditFlower: (Flower) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { showDialog = true },
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color.Black),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .border(1.dp, Color.Black, RoundedCornerShape(20.dp))
                ) {
                    AsyncImage(
                        model = flower.imageUrl,
                        contentDescription = flower.name,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Văn bản với khoảng cách và kích thước chữ tùy chỉnh
                Text(
                    text = flower.name,
                    modifier = Modifier
                        .padding(start = 8.dp).weight(1f),// Chuyển văn bản xuống góc dưới cùng bên trái
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )


            Column {
                Button(onClick = { showEditDialog = true }) {
                    Text(text = "Sửa")
                }
                Button(onClick = { showDeleteConfirmationDialog = true }) {
                    Text(text = "Xóa")
                }
            }
        }
    }

    if (showDialog) {
        FlowerDetailDialog(flower = flower, onDismiss = { showDialog = false })
    }

    if (showDeleteConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmationDialog = false },
            title = { Text("Xóa") },
            text = { Text("Bạn có muốn xóa không?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteFlower(flower)
                        onRefreshData()
                        showDeleteConfirmationDialog = false
                        Toast.makeText(context, "Xoá thành công", Toast.LENGTH_SHORT).show()

                    }
                ) {
                    Text("Xóa")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDeleteConfirmationDialog = false }
                ) {
                    Text("Hủy")
                }
            }
        )
    }

    if (showEditDialog) {
        FlowerEditDialog(
            flower = flower,
            onDismiss = { showEditDialog = false },
            onSave = { updatedFlower ->
                onEditFlower(updatedFlower)
                onRefreshData()
            }
        )
    }
}

@Composable
fun AddFlowerDialog(
    onAddFlower: (Flower) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thêm") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Tên hoa") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Mô tả") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    label = { Text("URL hình ảnh") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val newFlower = Flower(
                        id = flowerList.size + 1, // Tạo id mới
                        name = name,
                        description = description,
                        imageUrl = imageUrl
                    )
                    onAddFlower(newFlower)
                    Toast.makeText(context, "Thêm hoa mới thành công", Toast.LENGTH_SHORT).show()
                    onDismiss()
                }
            ) {
                Text("Thêm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}


@Composable
fun FlowerEditDialog(
    flower: Flower,
    onDismiss: () -> Unit,
    onSave: (Flower) -> Unit
) {
    var name by remember { mutableStateOf(flower.name) }
    var description by remember { mutableStateOf(flower.description) }
    var imageUrl by remember { mutableStateOf(flower.imageUrl) }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Sửa") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Tên hoa") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Mô tả") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    label = { Text("URL hình ảnh") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(flower.copy(name = name, description = description, imageUrl = imageUrl))
                    Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show()
                    onDismiss()
                }
            ) {
                Text("Lưu")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}
@Composable
fun FlowerDetailDialog(flower: Flower, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Thông tin chi tiết",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    AsyncImage(
                        model = flower.imageUrl,
                        contentDescription = flower.name,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Tên hoa: ${flower.name}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Mô tả: ${flower.description}",
                    fontSize = 16.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Đóng")
            }
        },
        modifier = Modifier.padding(16.dp)
    )
}
