package octopus.inc.spotifysearch.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.auth.LoginActivity.REQUEST_CODE
import octopus.inc.spotifysearch.R
import octopus.inc.spotifysearch.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.login.setOnClickListener {
            val builder =
                AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)
            builder.setScopes(arrayOf("streaming"))
            val request = builder.build()

            AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, intent)

            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    SPOTIFY_ACCESS_TOKEN = "Bearer ${response.accessToken}"
                    startActivity(Intent(this, MainActivity::class.java))
                }

                AuthorizationResponse.Type.ERROR -> {
                    Toast.makeText(this, "Login Error", Toast.LENGTH_SHORT).show()
                }

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