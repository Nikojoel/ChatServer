/*
Chat Server
Niko Holopainen

Interface for observer pattern methods
 */

interface ChatHistoryObservable {

    fun registerObserver(observer: ChatHistoryObserver)
    fun deregisterObserver(observer: ChatHistoryObserver)
    fun notifyObservers(message: ChatMessage)
    fun notifyPrivateObserver(message: ChatMessage, myself: ChatConnector)
    fun getHistory(): String
    fun insert(message: ChatMessage)
    fun getTop(): String
}