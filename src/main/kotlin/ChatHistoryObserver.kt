/*
Chat Server
Niko Holopainen

Interface for observer pattern methods
 */

interface ChatHistoryObserver {

    fun newMessage(message: ChatMessage)
    fun name(): String
    fun getCommands(): String
}