package com.koleff.kare_android.common.broadcast

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.domain.usecases.AuthenticationUseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

class LogoutHandler @Inject constructor(
    private val authenticationUseCases: AuthenticationUseCases,
    private val preferences: Preferences,
    private val navigationController: NavigationController
) {

     fun logout(context: Context){
        Log.d("LogoutHandler", "Logout handler received.")

        val credentials = preferences.getCredentials() ?: return

        CoroutineScope(Dispatchers.IO).launch {
            authenticationUseCases.logoutUseCase.invoke(credentials)
                .collect{ logoutState ->
                    if(logoutState.isSuccessful){
                        Log.d("LogoutHandler", "Logout done successfully!")

                        //Navigate to Welcome screen...
                        navigationController.clearBackstackAndNavigateTo(Destination.Welcome)
                    }else if(logoutState.isError){
                        Log.d("LogoutHandler", "Logout failed!")

                        val toast = Toast.makeText(context, "Logout failed.", Toast.LENGTH_SHORT)
                        toast.show()
                    }
                }
        }
    }
}