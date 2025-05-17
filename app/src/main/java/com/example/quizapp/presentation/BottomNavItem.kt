package com.example.quizapp.presentation

sealed class BottomNavItem(val route: String, val title: String) {
    object  Quiz : BottomNavItem("quiz_nav", "Quiz")
    object Result : BottomNavItem("result", "Result")
    object Profile : BottomNavItem("profile_nav", "Profil")
}