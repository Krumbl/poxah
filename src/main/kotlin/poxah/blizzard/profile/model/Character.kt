package poxah.blizzard.profile.model

import com.fasterxml.jackson.annotation.JsonProperty
import poxah.blizzard.gamedata.model.Link

data class Character(
    val _links: Link,
    val id: Number,
    val name: String,
    @JsonProperty("character_class")
    val characterClass: CharacterClass,
)
data class CharacterClass(
    val id: Number,
)