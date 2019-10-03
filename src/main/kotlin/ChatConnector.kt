import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintStream
import java.lang.Exception
import java.util.*
/*
Chat server & client
Niko Holopainen
 */

class ChatConnector(ins: InputStream, outs: OutputStream) : ChatHistoryObserver, Runnable {

    private val scanner = Scanner(ins)
    private val outPut = PrintStream(outs)
    private var userName = ""

    override fun run() {

            // Username checking
            while (true) {
                val userQuery = scanner.nextLine() // Input from client

                // Parse to ChatMessage (command, user, message)
                val queryToChatMessage = Json.parse(ChatMessage.serializer(), userQuery)

                // Check that the user is not registered and that the username is valid
                if (Users.checkUsername(queryToChatMessage.user) && queryToChatMessage.user != "") {
                    Users.registerUsername(queryToChatMessage.user)
                    userName = queryToChatMessage.user
                    ChatHistory.registerObserver(this)
                    Users.registerUsername(userName)
                    newMessage(ChatMessage("valid", queryToChatMessage.user,"valid"))
                    ChatHistory.notifyObservers(ChatMessage("say",userName,"has joined the chat"))
                    break
                    // If the user already exists or is invalid, sends invalid message to the client
                } else if (!Users.checkUsername(queryToChatMessage.user) || queryToChatMessage.user == "") {
                    val invalidName = ChatMessage("invalid", queryToChatMessage.user, "invalid")
                    newMessage(invalidName)
                }
            }
            try {
                // Chatting
                while (true) {
                    val incomingMessage = scanner.nextLine() // Input from client

                    // Parse to ChatMessage (command, user, message)
                    val jsonToChatMessage = Json.parse(ChatMessage.serializer(), incomingMessage)

                    // Statements for the commands
                    when (jsonToChatMessage.command) {
                        "say" -> {
                            ChatHistory.insert(jsonToChatMessage)
                            ChatHistory.notifyObservers(jsonToChatMessage)
                        }
                        "disconnect" -> {
                            ChatHistory.deregisterObserver(this)
                            Users.removeUsername(userName)
                            ChatHistory.notifyObservers(ChatMessage("say",userName,"has left the chat"))

                        }
                        "users" -> {
                            newMessage(ChatMessage("users","",Users.getUsers()))
                        }
                        "history" -> {
                            newMessage(ChatMessage("history","",ChatHistory.getHistory()))
                        }
                        "top" -> {
                            newMessage(ChatMessage("top","",ChatHistory.getTop()))
                        }
                    }
                }
            } catch (e: Exception) {
                println(e)
                ChatHistory.deregisterObserver(this)
                Users.removeUsername(userName)
            }
    }

    override fun newMessage(message: ChatMessage) {
        outPut.println(Json.stringify(ChatMessage.serializer(), message))
    }

    override fun getCommands(): String {
        return "\n.users // Displays current users \n.top // Displays current top chatters \n.history // Displays message history \n.quit // Disconnects from the server \n.private // Sends a private message"
    }

    override fun name(): String {
        return userName
    }
}
