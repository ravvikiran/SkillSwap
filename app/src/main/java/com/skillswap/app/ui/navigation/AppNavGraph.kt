package com.skillswap.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.skillswap.app.ui.screens.auth.SignInScreen
import com.skillswap.app.ui.screens.auth.SignUpScreen
import com.skillswap.app.ui.screens.auth.WelcomeScreen
import com.skillswap.app.ui.screens.home.HomeScreen
import com.skillswap.app.ui.screens.onboarding.LocationSetupScreen
import com.skillswap.app.ui.screens.onboarding.SkillsSetupScreen
import com.skillswap.app.ui.screens.profile.ProfileScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Auth flow
        composable(NavRoutes.Welcome.route) {
            WelcomeScreen(
                onSignInClick = { navController.navigate(NavRoutes.SignIn.route) },
                onSignUpClick = { navController.navigate(NavRoutes.SignUp.route) },
                onGoogleSignIn = {
                    navController.navigate(NavRoutes.LocationSetup.route) {
                        popUpTo(NavRoutes.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.SignIn.route) {
            SignInScreen(
                onSignInSuccess = {
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Welcome.route) { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(NavRoutes.SignUp.route) {
                        popUpTo(NavRoutes.SignIn.route) { inclusive = true }
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.SignUp.route) {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate(NavRoutes.LocationSetup.route) {
                        popUpTo(NavRoutes.Welcome.route) { inclusive = true }
                    }
                },
                onNavigateToSignIn = {
                    navController.navigate(NavRoutes.SignIn.route) {
                        popUpTo(NavRoutes.SignUp.route) { inclusive = true }
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // Onboarding flow
        composable(NavRoutes.LocationSetup.route) {
            LocationSetupScreen(
                onContinue = { navController.navigate(NavRoutes.SkillsSetup.route) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.SkillsSetup.route) {
            SkillsSetupScreen(
                onComplete = {
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.LocationSetup.route) { inclusive = true }
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // Main tabs
        composable(NavRoutes.Home.route) {
            HomeScreen()
        }

        composable(NavRoutes.Discover.route) {
            // Placeholder
            HomeScreen()
        }

        composable(NavRoutes.Messages.route) {
            // Placeholder
            HomeScreen()
        }

        composable(NavRoutes.Wallet.route) {
            // Placeholder
            HomeScreen()
        }

        composable(NavRoutes.Profile.route) {
            ProfileScreen(
                onEditProfile = { navController.navigate(NavRoutes.EditProfile.route) },
                onSignOut = {
                    navController.navigate(NavRoutes.Welcome.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.EditProfile.route) {
            // Placeholder
            ProfileScreen(
                onEditProfile = {},
                onSignOut = {}
            )
        }
    }
}
