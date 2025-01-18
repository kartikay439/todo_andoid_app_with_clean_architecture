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
import androidx.compose.material.icons.filled.Delete
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
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import kotlinx.coroutines.delay as coroutinesDelay

@Composable
fun ToDoScreen(todoViewModel: ToDoScreenVIewModel = koinViewModel()) {
    val state = todoViewModel.state.collectAsState()
    val openDialog = remember { mutableStateOf(false) }
    val title = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD)) // Light background color
    ) {
        Image(
            painter = painterResource(R.drawable.rocket_pencil_svgrepo_com),
            contentDescription = "background",
            modifier = Modifier
                .align(Alignment.Center)
                .size(400.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp),
            contentPadding = PaddingValues(20.dp)
        ) {

//            Why Providing a Key Works
//            State Isolation : Without a key, LazyColumn may reuse the composable
//            instances, causing states like swipeableState or isDeleted to be shared or misapplied across items.
//            Stable Identity : A key tells Compose which items are the same between recompositions .
//            This ensures that actions or animations on one item don't mistakenly affect others.
//            Efficient Rendering : Compose optimizes updates by only recomposing the items that actually changed,
//            improving performance for large lists.

            items(state.value, key = { it.uid }) { todo ->
//                coroutinesDelay(timeMillis = 500)
                TodoCard(todo = todo, onDelete = { todoViewModel.deleteTodo(todo.uid) })
            }
        }

        FloatingActionButton(
            onClick = { openDialog.value = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    bottom = WindowInsets.navigationBars
                        .asPaddingValues()
                        .calculateBottomPadding() + 20.dp
                )
                .padding(end = 15.dp)
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

    if (openDialog.value) {
        AddTodoDialog(
            onDismiss = { openDialog.value = false },
            onAdd = {
                if (title.value.isNotBlank()) {
                    todoViewModel.addTodo(
                        ToDo(
                            uid = 0,
                            title = title.value
                        )
                    )
                    openDialog.value = false
                }
            },
            titleState = title
        )
    }
}

@Composable
fun Dp.toPx(): Float {
    val density = LocalDensity.current
    return with(density) { this@toPx.toPx() }
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun TodoCard(todo: ToDo, onDelete: (Int) -> Unit) {
    // Create a swipeable state for each card, ensuring independence
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val maxSwipeDistance = with(LocalDensity.current) { 150.dp.toPx() } // Define max swipe distance
    val randomColor = remember { mutableStateOf(randomColor()) }
    var isDeleted by remember { mutableStateOf(false) } // Track if this card is already deleted

    if (!isDeleted) { // Only render if the card is not marked for deletion
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .swipeable(
                    state = swipeableState,
                    anchors = mapOf(
                        0f to 0,
                        maxSwipeDistance to 1
                    ), // Define anchors for swipe states
                    thresholds = { _, _ -> FractionalThreshold(0.7f) }, // Define threshold (70% swipe distance)
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
                Text(text = todo.title, modifier = Modifier.weight(1f))
                IconButton(onClick = { onDelete(todo.uid) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Todo")
                }
            }

            // Trigger deletion when the swipe reaches the threshold and prevent retrigger
            LaunchedEffect(swipeableState.offset.value) {
                if (!isDeleted && swipeableState.offset.value > maxSwipeDistance * 0.7) {
                    isDeleted = true // Mark the card as deleted
                    onDelete(todo.uid) // Trigger the deletion
                }
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
