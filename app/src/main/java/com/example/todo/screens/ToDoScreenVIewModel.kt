import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repository.ToDo
import com.example.domain.usecases.AddTodoUseCase
import com.example.domain.usecases.DeleteTodoUseCase
import com.example.domain.usecases.GetAllTodoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ToDoScreenVIewModel(

    private val addTodoUseCase: AddTodoUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
    private val getAllTodoUseCase: GetAllTodoUseCase
) : ViewModel() {

    // The state representing all the todos
    private val _todosState = MutableStateFlow<List<ToDo>>(emptyList())
    val state = _todosState.asStateFlow()

    init {
        // Fetch all todos when the ViewModel is created
        getAllTodo()
    }

    // Add a new Todo
    fun addTodo(todo: ToDo) {
        viewModelScope.launch {
            addTodoUseCase(todo) // Add the todo
            getAllTodo()          // Fetch the updated list of todos
        }
    }

    // Delete a Todo by UID
    fun deleteTodo(uid: Int) {
        viewModelScope.launch {
            deleteTodoUseCase(uid) // Delete the todo
            getAllTodo()           // Fetch the updated list of todos
        }
    }

    // Fetch all todos and update the state
    fun getAllTodo() {
        viewModelScope.launch {
            getAllTodoUseCase().collect { todos ->
                _todosState.value = todos // Update the state with the fetched todos
            }
        }
    }
}
