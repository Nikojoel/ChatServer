import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintStream
import java.lang.Exception
import java.util.*
/*
Chat Server
Niko Holopainen 1805828

ChatConnector class has all the logic for user name inputting and the chatting operations
 */

class ChatConnector(ins: InputStream, outs: OutputStream) : ChatHistoryObserver, Runnable {

    private val scanner = Scanner(ins)
    private val outPut = PrintStream(outs)
    private var userName = ""

    override fun run() {
        // Username inputting
        while(true) {
            outPut.println("Type a username")
            userName = scanner.nextLine()

            // Check if username is already taken
            if (Users.checkUsername(userName)) {
                Users.registerUsername(userName)
                outPut.println("Welcome to the server $userName!")
                outPut.println("You can now start chatting")
                outPut.println("Type .commands to see available commands\n")
                ChatHistory.notifyObservers(ChatMessage(userName, "has connected to the server"),this)
                break

            } else if (!Users.checkUsername(userName)) {
                outPut.println("Username is already taken")
            }
        }
        try {
            // Chatting
            while (true) {

                ChatHistory.registerObserver(this)
                val userMessage = scanner.nextLine()

                // Commands
                if (userMessage == ".quit") {
                    outPut.println("Disconnected from server")
                    ChatHistory.notifyObservers(ChatMessage(userName, "has disconnected from the server"), this)
                    ChatHistory.deregisterObserver(this)
                    Users.removeUsername(userName)
                    outPut.flush()
                    break

                } else if (userMessage == ".commands") {
                    outPut.println(getCommands())

                } else if (userMessage == ".users") {
                    outPut.println(Users.getUsers())

                } else if (userMessage == ".top") {
                    outPut.println(ChatHistory.getTop())

                } else if (userMessage == ".history") {
                    outPut.println(ChatHistory.getHistory())

                } else if (userMessage == ".private") {
                    outPut.println("Type the receiver username and message (user:message)")

                    // Private message
                    val line = scanner.nextLine()
                    try {
                        val receiver = line.split(":")
                        val privateMessage = ChatMessage(receiver[0], receiver[1])
                    // Check that the user exists
                    if (!Users.checkUsername(privateMessage.user)) {
                        ChatHistory.notifyPrivateObserver(privateMessage,this)
                    } else {
                        outPut.println("Invalid username")
                    }
                } catch (e: Exception) {
                    outPut.println("Invalid input")
                    println(e)
                }
                // Invalid commands
                } else if (userMessage.split(".")[0] == "") {
                    outPut.println("Invalid command")

                } else {
                    val chatMessage = ChatMessage(userName,userMessage)

                    /* Stringify to JSON {"user","message"}
                    val jsonMessage = Json.stringify(ChatMessage.serializer(),chatMessage)

                    // Parse to ChatMessage (user, message)
                    val jsonToChatMessage = Json.parse(ChatMessage.serializer(), jsonMessage)
                    */

                    ChatHistory.insert(chatMessage)
                    ChatHistory.notifyObservers(chatMessage, this)
                }
            }
        } catch (e: Exception) {
            println(e)
        }
    }

    override fun newMessage(message: ChatMessage) {
        outPut.println(message)
    }

    override fun getCommands(): String {
        return "\n.users // Displays current users \n.top // Displays current top chatters \n.history // Displays message history \n.quit // Disconnects from the server \n.private // Sends a private message"
    }

    override fun name(): String {
        return userName
    }
}