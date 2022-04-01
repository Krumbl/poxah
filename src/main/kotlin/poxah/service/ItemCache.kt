package poxah.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import org.slf4j.LoggerFactory
import poxah.blizzard.GameDataClient
import java.io.Closeable
import java.io.File


class ItemCache(
    private val client: GameDataClient,
    private val fileService: JsonFileService = JsonFileService(),
): Closeable {
    private val logger = LoggerFactory.getLogger(ItemCache::class.java)

    private var items: MutableMap<Int, Item> = mutableMapOf()

    companion object {
        const val FILE_NAME = "items.json"
    }

    init {
        fileService.read<ItemCacheStore>(FILE_NAME)?.cache?.forEach {
            items[it.id] = it
        }
        logger.trace("Item cache initialized: $items")
    }

    suspend fun getItem(id: Int): Item =
        items[id]?: retrieveItem(id)

    private suspend fun retrieveItem(id: Int): Item {
        logger.debug("Retrieve new item $id")
        val item = client.getItem(id).toItem()
        items[id] = item
        return item
    }

    override fun close() {
        // TODO persist on shutdown
//        File("items.json").writeText(jacksonObjectMapper().writeValueAsString(items))
        fileService.write(FILE_NAME, ItemCacheStore(items.values.toList()))
    }

}

data class ItemCacheStore(
    val cache: List<Item>,
)

data class Item(
    val id: Int,
    val name: String,
)

private fun poxah.blizzard.gamedata.model.Item.toItem(): Item =
    Item(
        id,
        name
    )