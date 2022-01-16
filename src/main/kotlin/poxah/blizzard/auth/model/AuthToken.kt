package poxah.blizzard.auth.model

data class AuthToken (
    val access_token: String,
    val token_type: String,
    val expires_in: Number,
    val sub: String,
)

