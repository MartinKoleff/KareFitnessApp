package com.koleff.kare_android.common

object Constants {
    const val SCHEME_LOCAL = "http"
    const val SCHEME = "https"
    const val PORT = 8080

    const val BASE_URL = "test" //TODO: add deployed server url
    const val BASE_URL_FULL = "$SCHEME://$BASE_URL/"

    const val BASE_LOCAL_URL = "localhost"
    const val BASE_LOCAL_URL_FULL = "$SCHEME_LOCAL://$BASE_LOCAL_URL"
    const val BASE_LOCAL_URL_FULL_PORT = "$SCHEME_LOCAL://$BASE_LOCAL_URL:$PORT/"

    const val useMockupDataSource = true
}