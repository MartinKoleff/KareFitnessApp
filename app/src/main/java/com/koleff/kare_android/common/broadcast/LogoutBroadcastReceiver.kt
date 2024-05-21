package com.koleff.kare_android.common.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.domain.usecases.AuthenticationUseCases
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LogoutBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var authenticationUseCases: AuthenticationUseCases

    @Inject
    lateinit var preferences: Preferences

    @Inject
    lateinit var navigationController: NavigationController
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("LogoutBroadcastReceiver", "Logout broadcast received.")

        val credentials = preferences.getCredentials() ?: return

        CoroutineScope(Dispatchers.IO).launch {
            authenticationUseCases.logoutUseCase.invoke(credentials)
                .collect{ logoutState ->
                if(logoutState.isSuccessful){
                    Log.d("LogoutBroadcastReceiver", "Logout done successfully!")

                    //Navigate to Welcome screen...
                    navigationController.clearBackstackAndNavigateTo(Destination.Welcome)
                }else if(logoutState.isError){
                    Log.d("LogoutBroadcastReceiver", "Logout failed!")

                    val toast = Toast.makeText(context, "Logout failed.", LENGTH_SHORT)
                    toast.show()
                }
            }
        }
    }
}