import kotlinx.serialization.Serializable
/*
Chat Server
Niko Holopainen
 */
@Serializable
class ChatMessage(val command: String, val user: String, val message: String) {

}
