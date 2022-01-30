package octopus.inc.spotifysearch.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.auth.LoginActivity.REQUEST_CODE
import octopus.inc.spotifysearch.R

class LoginActivity : AppCompatActivity() {

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

        if (requestCode == REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, intent)

            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    SPOTIFY_ACCESS_TOKEN = "Bearer ${response.accessToken}"
                }

                AuthorizationResponse.Type.ERROR -> {}

                else -> {}
            }
        }
    }

    companion object {
        private const val CLIENT_ID = "6dd55bb0935d4d04906f220b8466aaf3"
        private const val REDIRECT_URI = "http://localhost:8080"
        private var SPOTIFY_ACCESS_TOKEN: String? = null

        fun getSpotifyToken(): String? {
            return SPOTIFY_ACCESS_TOKEN
        }
    }
}