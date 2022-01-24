package poxah.config

data class Config(
    val cooking: Profession,
    val herbalism: Profession,
    val mining: Profession,
    val inscription: Profession,
    val alchemy: Profession,
    val jewelcraft: Profession,
    val other: Profession,
    val enchanting: Profession,
    val blacksmith: Profession,
) {
    fun asMap(): Map<String, Profession> =
        mapOf(
            "cooking" to cooking,
            "herbalism" to herbalism,
            "mining" to mining,
            "inscription" to inscription,
            "alchemy" to alchemy,
            "jewelcraft" to jewelcraft,
            "other" to other,
            "enchanting" to enchanting,
            "blacksmith" to blacksmith,
        )

    fun ids(): List<Int> =
        asMap().values.flatMap {
            it.ids
        }

}

data class Profession(
    val ids: List<Int>,
)