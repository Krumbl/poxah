package poxah.blizzard.model

data class AuctionResponse(
    val _links: Link,
    val connected_realm: Href,
    val auctions: List<Auction>,
)

data class Auction(
    val id: Int,
    val item: AuctionItem,
    val bid: Long? = null,
    val buyout: Long,
    val quantity: Int,
    val unit_price: Price? = null, // TODO what should default be?
    val time_left: String, // TODO enum
)

typealias Price = Long
fun Price.toString(): String =
    "PRICE"
//    (this / 10000).toString()

data class AuctionItem(
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

// {"id":725091716,"item":{"id":191328},"quantity":2,"unit_price":7300,"time_left":"SHORT"}
data class CommodityResponse(
    val _links: Link,
    val auctions: List<Commodity>,
)

data class Commodity(
    val id: Int,
    val item: CommodityItem,
)

data class CommodityItem(
    val id: Int,
)