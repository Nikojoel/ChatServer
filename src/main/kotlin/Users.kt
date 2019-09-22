/*
Chat Server
Niko Holopainen

Singleton class Users has logic for registering, removing and checking the user names
 */

object Users {

    private val users = hashSetOf<String>()

    // Username register
    fun registerUsername(user: String) {
        users.add(user)
        ChatHistory.topChatters.put(user,0)
    }

    // Username removal
    fun removeUsername(user: String) {
        users.remove(user)
        ChatHistory.topChatters.remove(user)
    }

    // Username check
    fun checkUsername(user: String): Boolean {
        return !users.contains(user)
    }

    // User list
    fun getUsers(): String {
        var temp = "\n"
        for (n in users) {
            temp += "$n\n"
        }
        return temp
    }
}