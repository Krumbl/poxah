package poxah

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import poxah.blizzard.GameDataClient
import poxah.blizzard.auth.AuthClient
import poxah.blizzard.model.AuctionResponse
import java.io.File

class Main

val logger = LoggerFactory.getLogger(Main::class.java)

fun main(args: Array<String>) {
    logger.warn("Program arguments: ${args.joinToString()}")

//    val token = runBlocking { AuthClient().javaToken() }
//    logger.info("request {}", token)
//    val realm = runBlocking { GameDataClient(AuthClient(args[0], args[1])).getConnectedRealm() }
//    File("auctions_format.json").writeText(auctions)
//    logger.info("realm $realm")

    val auctions = jacksonObjectMapper()
//        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .readValue(
        Main::class.java.classLoader.getResource("auctions.json"),
        AuctionResponse::class.java)

    logger.warn("auctions ${auctions.auctions.size}")

//    auctions.auctions.filter { it.item.id == 171276 }.sortedBy { it.unit_price }.forEach {
//        logger.info(it.toString())
//    }
//    val petCageId = 82800
//    auctions.auctions.filter { it.item.id == petCageId && it.item.pet_species_id == 1152 }.forEach {
//        logger.info(it.toString())
//    }

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
}