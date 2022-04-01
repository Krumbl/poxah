package poxah

import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import poxah.blizzard.auth.AuthClient
import poxah.blizzard.profile.ProfileClient
import java.io.File
import java.util.*

class Main(
    tokenClient: AuthClient
) {
    val logger = LoggerFactory.getLogger(Main::class.java)
    private val client = ProfileClient(tokenClient)

    suspend fun summarizeCharacters() {
        val characterTier = characters.associateWith { summarizeTier(it) }
        File("out/tier.csv").printWriter().use { print ->
            print.println("Name|${tierSlots.joinToString("|")}")
            characterTier.forEach { (name, tier) ->
                print.println("$name|${tierSlots.map { tier[it] }.joinToString("|")}")
            }
        }
    }

    private suspend fun summarizeTier(name: String): Map<String, TierInfo> =
        client.getCharacterEquipment(name.lowercase(Locale.getDefault()))
            .equipped_items.filter { tierSlots.contains(it.slot.type) }.associate {
                it.slot.type to TierInfo(
                    slot = it.slot.type,
                    iLvl = it.level.value,
                    tier = it.set != null,
                )
            }

    companion object {
        // TODO move to enum
        val tierSlots = listOf("HEAD", "SHOULDER", "CHEST", "LEGS", "HANDS")
        // TODO from file
        val characters = listOf("Managrowl", "Kyrigami",
            "Mortmont","Alekzandr","Borsgachef","Isüna","Shotslawl","Drosc","Magegio","Petmybeaver","Furryfighter","Hankyshanky","Nëytiri","Chaoticdurp","Grazzler","Jakdarippa","Lealina",
            "Sarichi","Eveso","Kiraera","Krumbl","Spookylust",
        )
    }
}

data class TierInfo(
    val slot: String,
    val iLvl: Number,
    val tier: Boolean,
) {
    override fun toString(): String =
        "$iLvl|$tier"
}

fun main(args: Array<String>) {

    val main = Main(AuthClient(args[0], args[1]))

    runBlocking {
        main.summarizeCharacters()
    }

}