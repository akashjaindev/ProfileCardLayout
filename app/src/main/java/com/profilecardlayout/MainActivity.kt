package com.profilecardlayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.profilecardlayout.ui.theme.LightGreen
import com.profilecardlayout.ui.theme.ProfileCardLayoutTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileCardLayoutTheme {
                UserApplication()
            }
        }
    }
}

@Composable
fun UserApplication(userProfiles:List<UserProfile> = userProfileList){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "user_list"){
        composable("user_list"){
            UserListScreen(userProfiles,navController)
        }
        composable(route="user_detail/{userId}", arguments = listOf(navArgument("userId"){
            type = NavType.IntType
        })){ navbackStackEntry->
            UserProfileDetailScreen(navbackStackEntry.arguments!!.getInt("userId"),navController)
        }
    }

}

@Composable
fun UserListScreen(userProfiles: List<UserProfile>, navController: NavHostController?) {
    Scaffold(topBar = { AppBar(title = "Messaging Application Users", icon = Icons.Default.Home){} }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            LazyColumn{
                items(userProfiles){ userProfile->
                    ProfileCard(userProfile){
                        navController?.navigate("user_detail/${userProfile.id}")
                    }
                }
            }
        }
    }
}

@Composable
fun UserProfileDetailScreen(userId:Int, navController: NavHostController?) {
    val userProfile:UserProfile = userProfileList.first { userProfile -> userId==userProfile.id }
    Scaffold(topBar = { AppBar(title = "User Detail", icon = Icons.Default.ArrowBack){
        navController?.navigateUp()
    } }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                ProfilePicture(userProfile.pictureUrl, userProfile.status,240.dp)
                ProfileContent(userProfile.name, userProfile.status,Alignment.CenterHorizontally)
            }
        }
    }
}

@Composable
fun AppBar(title:String,icon:ImageVector,iconClickAction:()->Unit) {
    TopAppBar(
        navigationIcon = {
            Icon(
                icon,
                "content description",
                modifier = Modifier.padding(12.dp).clickable { iconClickAction.invoke() }
            )
        },
        title = { Text(title) }
    )
}

@Composable
fun ProfileCard(userProfile: UserProfile,clickAction:()->Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
            .wrapContentHeight(align = Alignment.Top)
            .clickable { clickAction.invoke() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            ProfilePicture(userProfile.pictureUrl, userProfile.status,72.dp)
            ProfileContent(userProfile.name, userProfile.status,Alignment.Start)
        }
    }
}

@Composable
fun ProfilePicture(pictureUrl: String, status: Boolean, imageSize: Dp) {
    Card(
        shape = CircleShape,
        border = BorderStroke(
            width = 2.dp, color = if (status) {
                MaterialTheme.colorScheme.LightGreen
            } else {
                Color.Red
            }
        ),
        modifier = Modifier.padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(pictureUrl).crossfade(true).build(),
//            placeholder = painterResource(drawableId),
            contentDescription = "Content Description",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(imageSize)
        )
    }
}

@Composable
fun ProfileContent(name: String, status: Boolean,alignment: Alignment.Horizontal) {
    Column(
        modifier = Modifier
            .padding(8.dp),
        horizontalAlignment = alignment
    ) {
        Text(text = name, style = MaterialTheme.typography.labelLarge,
            color = if(status) {
                LocalContentColor.current.copy(alpha = 1f)
            } else{
                LocalContentColor.current.copy(alpha = ContentAlpha.medium)
            })
        Text(
            text = if (status) {
                "Active Now"
            } else {
                "Offline"
            }, style = MaterialTheme.typography.bodyMedium,
            color = if(status) {
                LocalContentColor.current.copy(alpha = 1f)
            } else{
                LocalContentColor.current.copy(alpha = ContentAlpha.medium)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserListScreenPreview() {
    ProfileCardLayoutTheme {
        UserListScreen(userProfileList,null)
    }
}

@Preview(showBackground = true)
@Composable
fun UserProfileDetailScreenPreview() {
    ProfileCardLayoutTheme {
        UserProfileDetailScreen(0,null)
    }
}