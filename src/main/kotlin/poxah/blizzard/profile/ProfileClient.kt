package poxah.blizzard.profile

import io.ktor.client.request.*
import poxah.blizzard.Client
import poxah.blizzard.auth.AuthClient
import poxah.blizzard.profile.model.CharacterEquipment
import poxah.blizzard.profile.model.Character

class ProfileClient(
    private val authClient: AuthClient
): Client (authClient) {

    suspend fun getCharacterEquipment(name: String): CharacterEquipment =
        client.get("https://us.api.blizzard.com/profile/wow/character/area-52/${name.lowercase()}/equipment?namespace=profile-us&locale=en_US")
    suspend fun getCharacter(name: String): Character =
        client.get("https://us.api.blizzard.com/profile/wow/character/area-52/${name.lowercase()}?namespace=profile-us")

}
