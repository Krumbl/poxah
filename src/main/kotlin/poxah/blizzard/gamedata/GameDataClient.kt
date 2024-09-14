package poxah.blizzard

import io.ktor.client.request.*
import kotlinx.serialization.Serializable
import poxah.blizzard.auth.AuthClient
import poxah.blizzard.gamedata.model.AuctionResponse
import poxah.blizzard.gamedata.model.CommodityResponse
import poxah.blizzard.gamedata.model.ConnectedRealm
import poxah.blizzard.gamedata.model.Item

class GameDataClient(
    private val authClient: AuthClient
): Client (authClient) {

    suspend fun getAuctions(): AuctionResponse =
        client.get("https://us.api.blizzard.com/data/wow/connected-realm/3676/auctions?namespace=dynamic-us&locale=en_US")

    suspend fun getCommodities(): CommodityResponse =
        client.get("https://us.api.blizzard.com/data/wow/commodities?namespace=dynamic-us&locale=en_US&access_token=UShoBgCsONvtNvZ8BzQNBJCA3CK5X7RgRv")


    suspend fun getConnectedRealm(realmId: String = "3676"): ConnectedRealm =
        client.get("https://us.api.blizzard.com/data/wow/connected-realm/$realmId?namespace=dynamic-us&locale=en_US")

    suspend fun getItem(id: Int): Item =
        client.get("https://us.api.blizzard.com/data/wow/item/$id?namespace=static-us&locale=en_US")

}
@Serializable
data class Customer(val id: Int, val firstName: String, val lastName: String)
