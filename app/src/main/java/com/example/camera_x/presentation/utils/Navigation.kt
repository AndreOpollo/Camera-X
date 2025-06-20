package com.example.camera_x.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.camera_x.presentation.screens.CameraScreen
import com.example.camera_x.presentation.screens.CapturedImagePreviewScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


sealed class Routes(val route:String){
    data object CameraScreen: Routes("camera_screen")
    data object PreviewScreen:Routes("preview_screen/{imageUri}"){
        fun createRoute(imageUri: String): String {
            val encodedUri = URLEncoder.encode(imageUri, StandardCharsets.UTF_8.toString())
            return "preview_screen/$encodedUri"
        }
    }
}

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = Routes.CameraScreen.route
    ){
        composable(Routes.CameraScreen.route){
            CameraScreen(navController = navController)
        }
        composable(
            Routes.PreviewScreen.route,
            arguments = listOf(navArgument("imageUri") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedUri = backStackEntry.arguments?.getString("imageUri") ?: ""
            val decodedUri = URLDecoder.decode(encodedUri, StandardCharsets.UTF_8.toString())
            CapturedImagePreviewScreen(
                navController = navController,
                imageUri = decodedUri
            )
        }
    }
}
