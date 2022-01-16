package poxah.blizzard.model

data class AuctionResponse(
    val _links: Link,
    val connected_realm: Href,
    val auctions: List<Auction>,
)

data class Auction(
    val id: Int,
    val item: Item,
    val bid: Long? = null,
    val buyout: Long,
    val quantity: Int,
    val unit_price: Long? = null, // TODO what should default be?
    val time_left: String, // TODO enum
)

data class Item(
    val id: Int,
    val context: Int,
    val bonus_lists: Collection<Int> = emptyList(),
    val modifiers: Collection<Modifier> = emptyList(),
    // TODO PetItem subclass
    val pet_breed_id: Int? = null,
    val pet_level: Int? = null,
    val pet_quality_id: Int? = null,
    val pet_species_id: Int? = null,
)

data class Modifier(
    val type: Int,
    val value: Int,
)