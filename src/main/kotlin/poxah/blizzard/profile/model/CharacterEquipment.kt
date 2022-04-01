package poxah.blizzard.profile.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import poxah.blizzard.gamedata.model.Link

data class CharacterEquipment(
    val _links: Link,
    val character: Character,
    val equipped_items: List<EquippedItem>,
)

data class Character(
    val name: String,
)
data class EquippedItem(
    val item: Item,
    val slot: Slot,
    val level: Level,
    val set: Set? = null,
)
data class Item(
    val id: Long
)
data class Slot(
    val type: String,
    val name: String,
)
data class Level (
    val value: Int
)
data class Set(
    val display_string: String
)