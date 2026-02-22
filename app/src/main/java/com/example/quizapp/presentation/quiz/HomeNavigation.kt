package com.example.quizapp.presentation.quiz

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import com.example.quizapp.presentation.components.DifficultySelectionBottomSheet
import com.example.quizapp.presentation.notifications.NotificationScreen
import com.example.quizapp.presentation.profile.ProfileScreen
import com.example.quizapp.presentation.quiz.model.Category
import com.example.quizapp.presentation.quiz.screens.AllCategoriesScreen
import com.example.quizapp.presentation.quiz.screens.AllQuizResultScreen
import com.example.quizapp.presentation.quiz.screens.HomeScreen
import com.example.quizapp.presentation.quiz.screens.QuestionScreen
import com.example.quizapp.presentation.quiz.screens.QuizResultScreen
import com.example.quizapp.presentation.quiz.screens.ReviewQuestionsScreen
import kotlinx.serialization.Serializable

@Composable
fun HomeNavigation(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val backStack = remember { mutableStateListOf<Any>(HomeKey) }
    val savedDarkMode by viewModel.isDarkMode.collectAsState()
    val activity = LocalActivity.current

    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var quizCategoryName by remember { mutableStateOf("") }
    var quizCategoryId by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is HomeNavigationEvent.NavigateToQuestion -> {
                    quizCategoryName = selectedCategory?.name?.az ?: ""
                    quizCategoryId = selectedCategory?.id ?: ""
                    selectedCategory = null
                    backStack.add(QuestionKey)
                }

                else -> {}
            }
        }
    }

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                if (backStack.last() is QuestionResultKey) {
                    backStack.clear()
                    backStack.add(HomeKey)
                } else {
                    backStack.removeLastOrNull()
                }
            } else {
                activity?.finish()
            }
        },
        entryProvider = { key ->
            when (key) {
                is HomeKey -> NavEntry(key) {
                    HomeScreen(
                        viewModel = viewModel,
                        onViewAllClick = {
                            backStack.add(AllCategoriesKey)
                        },
                        onCategoryClick = { category ->
                            selectedCategory = category
                        },
                        onNotificationClick = {
                            backStack.add(NotificationsKey)
                        },
                        onProfileClick = {
                            backStack.add(ProfileKey)
                        }
                    )
                }

                is AllCategoriesKey -> NavEntry(key) {
                    AllCategoriesScreen(
                        viewModel = viewModel,
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

                is ProfileKey -> NavEntry(key) {
                    ProfileScreen(
                        isDarkMode = savedDarkMode ?: isSystemInDarkTheme(),
                        onDarkModeToggle = { viewModel.toggleDarkMode(it) },
                        onBackClick = {
                            backStack.removeLastOrNull()
                        },
                        onSignOutClick = {
                            viewModel.signOut()
                        }
                    )
                }

                is QuestionKey -> NavEntry(key) {
                    QuestionScreen(
                        viewModel = viewModel,
                        onBackClick = {
                            backStack.removeLastOrNull()
                        },
                        categoryName = quizCategoryName,
                        onQuizFinish = {
                            backStack.add(QuestionResultKey)
                        }
                    )
                }

                is QuestionResultKey -> NavEntry(key) {
                    QuizResultScreen(
                        viewModel = viewModel,
                        categoryName = quizCategoryName,
                        onBackClick = {
                            backStack.clear()
                            backStack.add(HomeKey)
                        },
                        onViewAllClick = {
                            backStack.add(AllQuizResultKey)
                        },
                        onReviewClick = {
                            backStack.add(ReviewQuestionsKey)
                        }
                    )
                }

                is AllQuizResultKey -> NavEntry(key) {
                    AllQuizResultScreen(
                        viewModel = viewModel,
                        categoryId = quizCategoryId,
                        categoryName = quizCategoryName,
                        onBackClick = {
                            backStack.removeLastOrNull()
                        }
                    )
                }

                is ReviewQuestionsKey -> NavEntry(key) {
                    ReviewQuestionsScreen(
                        viewModel = viewModel,
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
                viewModel.startQuiz(category.id, difficulty.keyword.uppercase())
            },
            isLoading = viewModel.isQuestionsLoading
        )
    }
}

@Serializable
data object HomeKey : NavKey

@Serializable
data object AllCategoriesKey : NavKey

@Serializable
data object NotificationsKey : NavKey

@Serializable
data object ProfileKey : NavKey

@Serializable
data object QuestionKey : NavKey

@Serializable
data object QuestionResultKey : NavKey

@Serializable
data object AllQuizResultKey : NavKey

@Serializable
data object ReviewQuestionsKey : NavKey
