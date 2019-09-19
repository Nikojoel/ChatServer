/*
Chat Server
Niko Holopainen 1805828

Interface for observer pattern methods
 */

interface ChatHistoryObserver {

    fun newMessage(message: ChatMessage)
    fun name(): String
    fun getCommands(): String
}