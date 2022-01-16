package poxah.blizzard

import com.fasterxml.jackson.databind.DeserializationFeature
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable
import poxah.blizzard.auth.AuthClient
import poxah.blizzard.auth.model.AuthToken
import poxah.blizzard.model.AuctionResponse
import poxah.blizzard.model.ConnectedRealm
import poxah.blizzard.model.Item

class GameDataClient(
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

    suspend fun getAuctions(): AuctionResponse =
        client.get("https://us.api.blizzard.com/data/wow/connected-realm/3676/auctions?namespace=dynamic-us&locale=en_US&access_token=UShoBgCsONvtNvZ8BzQNBJCA3CK5X7RgRv")


    suspend fun getConnectedRealm(realmId: String = "3676"): ConnectedRealm =
        client.get("https://us.api.blizzard.com/data/wow/connected-realm/$realmId?namespace=dynamic-us&locale=en_US")

    suspend fun getItem(id: Int): Item =
        client.get("https://us.api.blizzard.com/data/wow/item/$id?namespace=static-us&locale=en_US")

}
@Serializable
data class Customer(val id: Int, val firstName: String, val lastName: String)
