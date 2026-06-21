package com.skillswap.app.ui.navigation

sealed class NavRoutes(val route: String) {
    // Auth / Onboarding
    data object Welcome : NavRoutes("welcome")
    data object SignIn : NavRoutes("sign_in")
    data object SignUp : NavRoutes("sign_up")
    data object LocationSetup : NavRoutes("location_setup")
    data object SkillsSetup : NavRoutes("skills_setup")

    // Main
    data object Home : NavRoutes("home")
    data object Discover : NavRoutes("discover")
    data object Messages : NavRoutes("messages")
    data object Wallet : NavRoutes("wallet")
    data object Profile : NavRoutes("profile")
    data object EditProfile : NavRoutes("edit_profile")
}
