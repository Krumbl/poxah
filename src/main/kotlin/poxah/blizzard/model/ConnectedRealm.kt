package poxah.blizzard.model

import kotlinx.serialization.Serializable

data class ConnectedRealm(
    val _links: Link,
    val id: Int,
    val has_queue: Boolean,
    // TODO finish
)

data class Link(
    val self: Href,
)
data class Href(
    val href: String,
)