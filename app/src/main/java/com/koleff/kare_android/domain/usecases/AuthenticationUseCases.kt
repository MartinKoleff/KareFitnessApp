package com.koleff.kare_android.domain.usecases

data class AuthenticationUseCases(
    val loginUseCase: LoginUseCase,
    val registerUseCase: RegisterUseCase,
    val logoutUseCase: LogoutUseCase,
    val regenerateTokenUseCase: RegenerateTokenUseCase
)