package poxah.blizzard.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.slf4j.LoggerFactory
import poxah.blizzard.auth.model.AuthToken
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

// FIXME remove coded secrets
class AuthClient(
    private val id: String,
    private val secret: String,
) {
    val logger = LoggerFactory.getLogger(AuthClient::class.java)

    val defaultMapper: ObjectMapper = jacksonObjectMapper()
    init {
        defaultMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
//        defaultMapper.registerModule(JavaTimeModule())
    }

    val client = HttpClient(CIO) {
        expectSuccess = true
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(JsonFeature) {
//            serializer = JacksonSerializer() {
//                enable(SerializationFeature.INDENT_OUTPUT)
//                dateFormat = DateFormat.getDateInstance()
//            }
        }
//        install(ContentNegotiation) {
//            register(ContentType.Application.Json, JacksonConverter(defaultMapper))
//        }
//        install(Auth) {
//            basic {
//                credentials {
//                    BasicAuthCredentials(
//                        username = id,
//                        password = secret
//                    )
//                }
//                realm = "Access to the '/' path"
//            }
//        }
    }

    // https://github.com/Blizzard/java-signature-generator
    fun javaToken(): AuthToken {
        var con: HttpURLConnection? = null
        val url = URL("https://us.battle.net/oauth/token")
        con = url.openConnection() as HttpURLConnection
        con.setRequestMethod("POST")
        con.setRequestProperty("Authorization", String.format("Basic %s", String(Base64.getEncoder().encode("$id:$secret".toByteArray()))))
        con.setDoOutput(true)
        con.getOutputStream().write("grant_type=client_credentials".toByteArray())

        val responseCode = con.responseCode
        logger.trace(String.format("Result code: %s", responseCode))
        val response = con.inputStream.bufferedReader().use { it.readText() }  // defaults to UTF-8
        logger.trace(String.format("Response: %s", response))

        return defaultMapper.readValue(response, AuthToken::class.java)
    }

    // TODO why doesn't ktor request work??
    suspend fun getToken(): AuthToken =
        client.post<HttpStatement>("https://us.battle.net/oauth/token") {
            headers {
                append(HttpHeaders.Authorization, "Basic ${String(Base64.getEncoder().encode(":".toByteArray()))}")
            }
            body = "grant_type=client_credentials"
        }.receive()
}