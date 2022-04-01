package poxah.blizzard

import com.fasterxml.jackson.databind.DeserializationFeature
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import poxah.blizzard.auth.AuthClient
import poxah.blizzard.auth.model.AuthToken

abstract class Client(
    private val authClient: AuthClient
) {

    val client = HttpClient(CIO) {
        expectSuccess = true
//        install(Logging) {
//            logger = Logger.DEFAULT
//            level = LogLevel.HEADERS
//        }
        Json {
            serializer = JacksonSerializer {
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            }
        }
        // TODO fix jackson
//        install(JsonFeature) {
//            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
//                prettyPrint = true
//                isLenient = true
//                ignoreUnknownKeys = true
//            })
//        }
        install(Auth) {
            lateinit var token: AuthToken

            bearer {
                loadTokens {
                    token = authClient.javaToken()
                    BearerTokens(
                        accessToken = token.access_token,
                        refreshToken = token.sub
                    )
                }
            }
        }
    }

}