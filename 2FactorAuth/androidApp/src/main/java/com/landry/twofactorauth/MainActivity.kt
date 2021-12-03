package com.landry.twofactorauth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.landry.shared.Greeting
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import com.arkivanov.decompose.defaultComponentContext
import com.landry.shared.routers.RootComponent
import com.landry.twofactorauth.ui.RootScreen

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = RootComponent(defaultComponentContext())

        setContent {
            Surface {
                RootScreen(root)
            }
        }
    }
}
