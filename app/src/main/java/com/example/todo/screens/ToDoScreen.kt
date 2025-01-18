import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import com.example.domain.repository.ToDo
import com.example.todo.R
import com.example.todo.screens.ToDoScreenVIewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ToDoScreen(todoViewModel: ToDoScreenVIewModel = koinViewModel()) {
    val state = todoViewModel.state.collectAsState()
    val openDialog = remember { mutableStateOf(false) }
    val title = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD)) // Use a light background color
    ) {
        Image(
            painter = painterResource(R.drawable.rocket_pencil_svgrepo_com),
            contentDescription = "background",
            Modifier.align(Alignment.Center).size(400.dp)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp)
        ) {
            items(state.value) { todo ->
                TodoCard(todo = todo, onDelete = { todoViewModel.deleteTodo(todo.uid) })
            }
        }

        FloatingActionButton(
            onClick = { openDialog.value = true },
            modifier = Modifier
                .align(Alignment.BottomEnd) // Align to the bottom end of the screen
                .padding(
                    bottom = WindowInsets.navigationBars
                        .asPaddingValues()
                        .calculateBottomPadding() + 20.dp
                )
                .padding(
                    end = 15.dp
                )
                .size(70.dp),
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 30.dp,
                pressedElevation = 10.dp
            ),
            containerColor = randomColor()
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add Todo",
                modifier = Modifier.size(30.dp)
            )
        }
    }

    // Dialog to add a new Todo
    if (openDialog.value) {
        AddTodoDialog(
            onDismiss = { openDialog.value = false },
            onAdd = {
                if (title.value.isBlank()) {
                    return@AddTodoDialog
                }
                todoViewModel.addTodo(
                    ToDo(
                        uid = 0,
                        title = title.value
                    )
                )
                openDialog.value = false
            },
            titleState = title
        )
    }
}

@Composable
fun Dp.toPx(): Float {
    val density = LocalDensity.current
    return remember(density) { with(density) { this@toPx.toPx() } }
}


@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun TodoCard(todo: ToDo, onDelete: (Int) -> Unit) {
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val maxSwipeDistance = 200.dp.toPx() // Define the maximum swipe distance
    val randomColor = remember { mutableStateOf(randomColor()) }

    // Box to contain swipeable card and manage state
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .swipeable(
                state = swipeableState,
                anchors = mapOf(0f to 0, maxSwipeDistance to 1), // Anchors for swipe state
                thresholds = { _, _ -> FractionalThreshold(0.2f) }, // Swipe threshold to trigger delete
                orientation = Orientation.Horizontal
            )
            .offset {
                IntOffset(swipeableState.offset.value.toInt(), 0) // Move card on swipe
            }
            .background(
                if (swipeableState.offset.value > maxSwipeDistance / 2) Color.Transparent else randomColor.value,
                shape = RoundedCornerShape(5.dp)
            ) // Background change on swipe
            .padding(16.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = todo.title, modifier = Modifier.weight(1f),
                style = TextStyle(
                    fontSize = 22.sp,
                    letterSpacing = 1.sp
                )
            )
        }

        // Automatically delete when fully swiped (swipe past threshold)
        LaunchedEffect(swipeableState.offset.value) {
            if (swipeableState.offset.value > maxSwipeDistance / 2) {
                onDelete(todo.uid) // Trigger deletion when swipe reaches threshold
            }
        }
    }
}


@Composable
fun AddTodoDialog(
    onDismiss: () -> Unit,
    onAdd: () -> Unit,
    titleState: MutableState<String>
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Todo") },
        text = {
            Column {
                OutlinedTextField(
                    value = titleState.value,
                    onValueChange = { titleState.value = it },
                    label = { Text("Title") }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onAdd) {
                Text("Add")
            }
        },
        dismissButton = {
            titleState.value = ""
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

fun randomColor(): Color {
    val colors = listOf(
        Color(0xFFE57373),
        Color(0xFF81C784),
        Color(0xFF64B5F6),
        Color(0xFFFFD54F),
        Color(0xFFBA68C8)
    )
    return colors.random()
}
