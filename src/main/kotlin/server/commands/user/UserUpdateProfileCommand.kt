package server.commands.user

import server.commands.Command
import server.events.command.TypeCommand
import server.events.user.UserUpdateProfileEvent

class UserUpdateProfileCommand(val name: String="", val login: String="", val phone: String? = null,
                               val email: String? = null): Command {
    val event: UserUpdateProfileEvent
        get() = UserUpdateProfileEvent(name, login, phone, email)

    override val typeCommand: TypeCommand
        get() = TypeCommand.USER_UPDATE_PROFILE_COMMAND

    override fun toMap() = mapOf("name" to name, "login" to login, "phone" to phone, "email" to email)
}
