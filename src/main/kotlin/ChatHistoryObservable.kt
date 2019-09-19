/*
Chat Server
Niko Holopainen 1805828

Interface for observer pattern methods
 */

interface ChatHistoryObservable {

    fun registerObserver(observer: ChatHistoryObserver)
    fun deregisterObserver(observer: ChatHistoryObserver)
    fun notifyObservers(message: ChatMessage, myself: ChatConnector)
    fun notifyPrivateObserver(message: ChatMessage, myself: ChatConnector)
    fun getHistory(): String
    fun insert(message: ChatMessage)
    fun getTop(): String
}