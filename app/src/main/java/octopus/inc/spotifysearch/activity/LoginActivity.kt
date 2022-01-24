package octopus.inc.spotifysearch.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.spotify.sdk.android.auth.AccountsQueryParameters
import octopus.inc.spotifysearch.R
import com.spotify.sdk.android.auth.LoginActivity.REQUEST_CODE

import com.spotify.sdk.android.auth.AuthorizationClient

import com.spotify.sdk.android.auth.AuthorizationRequest

import com.spotify.sdk.android.auth.AuthorizationResponse

import com.spotify.sdk.android.auth.AccountsQueryParameters.CLIENT_ID
import com.spotify.sdk.android.auth.LoginActivity.REQUEST_CODE

import android.content.Intent
import android.util.Log


class LoginActivity : AppCompatActivity() {

    private val CLIENT_ID = "6dd55bb0935d4d04906f220b8466aaf3"
    private val REDIRECT_URI = "http://localhost:8080"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val builder =
            AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)
        builder.setScopes(arrayOf("streaming"))
        val request = builder.build()
        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request)

        setContentView(R.layout.activity_login)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, intent)
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    Log.d("MyTAG", "onActivityResult: ${response.accessToken}")
                    SPOTIFY_ACCESS_TOKEN = "Bearer ${response.accessToken}"
                }
                AuthorizationResponse.Type.ERROR -> {}
                else -> {}
            }
        }
    }

    companion object {
        var SPOTIFY_ACCESS_TOKEN  = ""
    }
}