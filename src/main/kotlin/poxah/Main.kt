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
        val characterToken = characters.associateWith {
            Token.forClass(
                Class.forId(client.getCharacter(it).characterClass.id)
            )
        }
        File("out/tier.csv").printWriter().use { print ->

            print.println("Name|${tierSlots.joinToString("|")}")
            Token.values().forEach { token ->
                print.println(token.name)
                print.println()

                characterToken.filter { (_, characterToken) -> characterToken == token }.forEach { (name, _) ->
                    val tier = requireNotNull(characterTier[name])
                    val tierCount = tier.values.filter { it.tier }.size
                    print.println("$name|" +
                            tierSlots.map { tier[it] }.joinToString("|") +
                            "|" + (tierCount >= 2) +
                            "|" + (tierCount >= 4) +
                            "|$tierCount"
                    )
                }

            }
        }
    }

    private suspend fun summarizeTier(name: String): Map<String, TierInfo> =
        client.getCharacterEquipment(name)
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

enum class Token(val classes: Collection<Class>) {
    MYSTIC(listOf(Class.DRUID, Class.MAGE, Class.HUNTER)),
    VENERATED(listOf(Class.PALADIN, Class.PRIEST, Class.SHAMAN)),
    ZENITH(listOf(Class.MONK, Class.ROGUE, Class.WARRIOR)),
    DREADFUL(listOf(Class.DEMONHUNTER, Class.DEATHKNIGHT, Class.WARLOCK));

    companion object {
        fun forClass(clas: Class): Token =
            values().first {it.classes.contains(clas)}
    }
}

enum class Class(val id: Number) {
    WARRIOR(1),
    PALADIN(2),
    HUNTER(3),
    ROGUE(4),
    PRIEST(5),
    DEATHKNIGHT(6),
    SHAMAN(7),
    MAGE(8),
    WARLOCK(9),
    MONK(10),
    DRUID(11),
    DEMONHUNTER(12);

    companion object {
        // not safe if bad id passed
        fun forId(id: Number): Class =
            values().first { it.id == id }
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