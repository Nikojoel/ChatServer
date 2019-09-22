import java.lang.Exception
import java.net.ServerSocket
/*
Chat Server
Niko Holopainen

ChatServer class opens a new ServerSocket and starts listening to incoming connections
 */

class ChatServer {

    private val ss = ServerSocket(50000)

    fun serve() {
        try {
            while(true) {
                println("Accepting")
                // Stops code execution and waits for connection
                val s = ss.accept()
                println("Accepted")
                // Creates a new thread and starts it
                Thread(ChatConnector(s.getInputStream(), s.getOutputStream())).start()
            }
        } catch (e: Exception) {
            println(e)
        }
        // Closes the socket
        ss.close()
    }
}