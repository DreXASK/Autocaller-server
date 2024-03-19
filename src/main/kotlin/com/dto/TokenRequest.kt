package connectionAdjusterScreen.data.remote.dto

import com.ServerConnectionStatus
import kotlinx.serialization.Serializable

@Serializable
data class TokenRequest(
    val token: String
)