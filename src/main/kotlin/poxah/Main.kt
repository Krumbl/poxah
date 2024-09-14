package poxah

import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import poxah.blizzard.GameDataClient
import poxah.blizzard.auth.AuthClient
import poxah.blizzard.model.Auction
import poxah.blizzard.model.AuctionResponse
import poxah.config.Config
import poxah.service.ItemCache
import poxah.service.JsonFileService
import poxah.service.ResourceFileService
import java.io.File
import java.time.Instant

val logger = LoggerFactory.getLogger(Main::class.java)
class Main(
    tokenClient: AuthClient
) {
    private val client = GameDataClient(tokenClient)
    private val itemCache = ItemCache(client)


    private val config: Config = ResourceFileService().read("config.json")
    init {
        logger.debug("Config $config")
    }

    private fun getAuctions(): Map<Int, List<Auction>> {
        val auctions = runBlocking { client.getAuctions() }
        JsonFileService().write("auctions_${Instant.now().epochSecond}.json", auctions)
//        val auctions = requireNotNull(JsonFileService().read<AuctionResponse>("auctions_1726332296.json"))
        val filteredAuctions = auctions.auctions.filter {
            config.ids().contains(it.item.id)
        }.groupBy { it.item.id }
        logger.debug("filtered auctions ${filteredAuctions.size}")

        return filteredAuctions
    }

    private suspend fun summarizeAuctions(auctions: Map<Int, List<Auction>>): List<Summary> =
        auctions.map {
            // TODO iterating items multiple times
//            if (it.key == 172043) {
//                it.value.sortedBy { it.unit_price }.forEach { auction ->
//                    logger.info("$auction")
//                }
//            }
            val quantity = it.value.fold(0L) { acc, auction -> acc + auction.quantity }


            // TODO legendaries not separated by ilvl
            val totalCost =
                it.value.fold(0L) { acc, auction ->
                    acc + (auction.unit_price ?: (auction.buyout * auction.quantity))
                    //                        requireNotNull(auction.unit_price) { "Invalid auction $auction" } *
                }
            val min: Long = it.value.minOf { auction -> (auction.unit_price ?: (auction.buyout * auction.quantity)) }
//                requireNotNull(auction.unit_price)}

            Summary(
                it.key,
                itemCache.getItem(it.key).name,
                Price(min),
                Price(totalCost / quantity),
                quantity
            )
        }

    /**
     * Prints output to `summary.csv`
     */
    suspend fun printSummary() {
        val summaries = summarizeAuctions(getAuctions())
//        logger.info("Summary")
//        logger.info(Summary.HEADER)
//        summaries.forEach {
//            logger.info("$it")
//        }

        File("out/summary.csv").printWriter().use { print ->
//            print.println(Summary.HEADER)
            summaries.sortedBy { it.itemId }.forEach {
                print.println("$it")
            }
        }
    }

    fun stop() {
        itemCache.close()
    }

}


fun main(args: Array<String>) {
    logger.warn("Program arguments: ${args.joinToString()}")

    val main = Main(AuthClient(args[0], args[1]))

    runBlocking {
        main.printSummary()
        main.stop()
    }

//    runBlocking {
//        logger.info("${GameDataClient(AuthClient(args[0], args[1])).getItem(178786)}")
//    }




//    auctions.auctions.filter { it.item.id == 171276 }.sortedBy { it.unit_price }.forEach {
//        logger.info(it.toString())
//    }
//    val petCageId = 82800
//    auctions.auctions.filter { it.item.id == petCageId && it.item.pet_species_id == 1152 }.forEach {
//        logger.info(it.toString())
//    }
}

data class Summary(
    val itemId: Int,
    val name: String,
    val min: Price,
    val avg: Price,
    val quantity: Long,
) {

    companion object {
        const val HEADER = "item,name,quantity,min,avg"
    }
    override fun toString(): String =
        "$itemId|$name|$quantity|${min.price}|${avg.price}"

}

@JvmInline
value class Price(val price: Long) {
    fun pretty(): String =
        "${(price / 10000)}g ${price / 100 % 100}s ${price % 100}c"
}
