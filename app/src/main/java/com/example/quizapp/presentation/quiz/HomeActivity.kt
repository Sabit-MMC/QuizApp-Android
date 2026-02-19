package com.example.quizapp.presentation.quiz

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import com.example.quizapp.presentation.components.DifficultySelectionBottomSheet
import com.example.quizapp.presentation.quiz.model.Category
import com.example.quizapp.presentation.quiz.screens.AllCategoriesScreen
import com.example.quizapp.presentation.quiz.screens.HomeScreen
import com.example.quizapp.presentation.quiz.screens.NotificationScreen
import com.example.quizapp.ui.theme.QuizAppTheme
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
data object HomeKey : NavKey

@Serializable
data object AllCategoriesKey : NavKey

@Serializable
data object NotificationsKey : NavKey

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizAppTheme {
                Scaffold { innerPadding ->
                    HomeNavigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun HomeNavigation(modifier: Modifier = Modifier) {
    val backStack = remember { mutableStateListOf<Any>(HomeKey) }
    val homeViewModel: HomeViewModel = koinViewModel()
    val context = LocalContext.current

    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeLastOrNull()
            }
        },
        entryProvider = { key ->
            when (key) {
                is HomeKey -> NavEntry(key) {
                    HomeScreen(
                        viewModel = homeViewModel,
                        onViewAllClick = {
                            backStack.add(AllCategoriesKey)
                        },
                        onCategoryClick = { category ->
                            selectedCategory = category
                        },
                        onNotificationClick = {
                            backStack.add(NotificationsKey)
                        }
                    )
                }
                is AllCategoriesKey -> NavEntry(key) {
                    AllCategoriesScreen(
                        viewModel = homeViewModel,
                        onBackClick = {
                            backStack.removeLastOrNull()
                        },
                        onCategoryClick = { category ->
                            selectedCategory = category
                        }
                    )
                }
                is NotificationsKey -> NavEntry(key) {
                    NotificationScreen(
                        onBackClick = {
                            backStack.removeLastOrNull()
                        }
                    )
                }
                else -> NavEntry(Unit) { /* Handle unknown keys */ }
            }
        }
    )

    selectedCategory?.let { category ->
        DifficultySelectionBottomSheet(
            category = category,
            onDismissRequest = { selectedCategory = null },
            onStartQuiz = { difficulty ->
                selectedCategory = null
                Toast.makeText(
                    context,
                    "Starting ${category.name.en} quiz with ${difficulty.title} difficulty",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }
}
