/*
Chat Server
Niko Holopainen

Singleton class ChatHistory has logic for top chatters, message inserting and observer pattern methods
 */

object ChatHistory: ChatHistoryObservable {

    private val observers = mutableSetOf<ChatHistoryObserver>()
    private val messages = mutableListOf<ChatMessage>()
    val topChatters = mutableMapOf<String,Int>()

    // Observer pattern methods
    override fun notifyObservers(message: ChatMessage, myself: ChatConnector) {
        observers.minus(myself).forEach{it.newMessage(message)}
    }

    // Private message
    override fun notifyPrivateObserver(message: ChatMessage, myself: ChatConnector) {
        for (n in observers) {
            if (n.name() == message.user) {
                n.newMessage(ChatMessage("${myself.name()} whispers",message.message))
            }
        }
    }

    // Observer removal
    override fun deregisterObserver(observer: ChatHistoryObserver) {
        observers.remove(observer)
    }

    // Observer register
    override fun registerObserver(observer: ChatHistoryObserver) {
        observers.add(observer)
    }

    // Message, top chatter insert
    override fun insert(message: ChatMessage) {
        messages.add(message)
        val oldNumber = topChatters.getValue(message.user)
        val newNumber = oldNumber + 1
        topChatters.replace(message.user, oldNumber, newNumber)
    }

    // Chat history
    override fun getHistory(): String {
        var temp = "\n"
        for (n in messages) {
            temp += "${n.user}: ${n.message}\n"
        }
        return temp
    }

    // Top chatter
    override fun getTop(): String {
        var i = 1
        var topToString = ""
        val sortedTop = topChatters.toList().sortedBy { (_, value) -> value}.reversed().toMap()
        for (n in sortedTop) {
            topToString += "\n$i: ${n.key} ${n.value} messages"
            i++
            if (i == 5) break
        }
        return topToString

    }
}