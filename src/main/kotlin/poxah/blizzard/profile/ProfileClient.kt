package poxah.blizzard.profile

import io.ktor.client.request.*
import poxah.blizzard.Client
import poxah.blizzard.auth.AuthClient
import poxah.blizzard.gamedata.model.AuctionResponse
import poxah.blizzard.gamedata.model.ConnectedRealm
import poxah.blizzard.profile.model.CharacterEquipment

class ProfileClient(
    private val authClient: AuthClient
): Client (authClient) {

    suspend fun getCharacterEquipment(name: String): CharacterEquipment =
//        client.get("https://us.api.blizzard.com/profile/wow/character/area52/$name/equipment?namespace=profile-us&locale=en_US")
        client.get("https://us.api.blizzard.com/profile/wow/character/area-52/$name/equipment?namespace=profile-us&locale=en_US&access_token=USbr9BjAz7XfpqKDHADrWLyO2wDiz2oCPv")
    //        client.get("https://us.api.blizzard.com/data/wow/connected-realm/3676/auctions?namespace=dynamic-us&locale=en_US&access_token=UShoBgCsONvtNvZ8BzQNBJCA3CK5X7RgRv")
    suspend fun getConnectedRealm(realmId: String = "3676"): ConnectedRealm =
        client.get("https://us.api.blizzard.com/data/wow/connected-realm/$realmId?namespace=dynamic-us&locale=en_US")

}
