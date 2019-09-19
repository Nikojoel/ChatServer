import kotlinx.serialization.Serializable
/*
Chat Server
Niko Holopainen 1805828
 */
@Serializable
class ChatMessage(val user: String, val message: String) {

    override fun toString(): String {
        return "$user: $message"
    }
}
