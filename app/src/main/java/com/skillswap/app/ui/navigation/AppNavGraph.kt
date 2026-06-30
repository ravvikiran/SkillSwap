package com.skillswap.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.skillswap.app.ui.screens.auth.SignInScreen
import com.skillswap.app.ui.screens.auth.SignUpScreen
import com.skillswap.app.ui.screens.auth.WelcomeScreen
import com.skillswap.app.ui.screens.home.HomeScreen
import com.skillswap.app.ui.screens.onboarding.LocationSetupScreen
import com.skillswap.app.ui.screens.onboarding.OnboardingViewModel
import com.skillswap.app.ui.screens.onboarding.SkillsSetupScreen
import com.skillswap.app.ui.screens.profile.ProfileScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Auth flow
        composable(NavRoutes.Welcome.route) {
            WelcomeScreen(
                onSignInClick = { navController.navigate(NavRoutes.SignIn.route) },
                onSignUpClick = { navController.navigate(NavRoutes.SignUp.route) },
                onGoogleSignIn = {
                    navController.navigate("onboarding") {
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
                    navController.navigate("onboarding") {
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

        // Onboarding flow — shared ViewModel scoped to this nested nav graph
        navigation(
            startDestination = NavRoutes.LocationSetup.route,
            route = "onboarding"
        ) {
            composable(NavRoutes.LocationSetup.route) { entry ->
                val parentEntry = remember(entry) {
                    navController.getBackStackEntry("onboarding")
                }
                val viewModel: OnboardingViewModel = hiltViewModel(parentEntry)

                LocationSetupScreen(
                    onContinue = { navController.navigate(NavRoutes.SkillsSetup.route) },
                    onBackClick = { navController.popBackStack() },
                    viewModel = viewModel
                )
            }

            composable(NavRoutes.SkillsSetup.route) { entry ->
                val parentEntry = remember(entry) {
                    navController.getBackStackEntry("onboarding")
                }
                val viewModel: OnboardingViewModel = hiltViewModel(parentEntry)

                SkillsSetupScreen(
                    onComplete = {
                        navController.navigate(NavRoutes.Home.route) {
                            popUpTo("onboarding") { inclusive = true }
                        }
                    },
                    onBackClick = { navController.popBackStack() },
                    viewModel = viewModel
                )
            }
        }

        // Main tabs
        composable(NavRoutes.Home.route) {
            HomeScreen()
        }

        composable(NavRoutes.Discover.route) {
            HomeScreen() // Placeholder
        }

        composable(NavRoutes.Messages.route) {
            HomeScreen() // Placeholder
        }

        composable(NavRoutes.Wallet.route) {
            HomeScreen() // Placeholder
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
            ProfileScreen(
                onEditProfile = {},
                onSignOut = {}
            )
        }
    }
}
