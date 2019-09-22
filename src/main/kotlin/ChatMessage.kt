import kotlinx.serialization.Serializable
/*
Chat Server
Niko Holopainen
 */
@Serializable
class ChatMessage(val user: String, val message: String) {

    override fun toString(): String {
        return "$user: $message"
    }
}
