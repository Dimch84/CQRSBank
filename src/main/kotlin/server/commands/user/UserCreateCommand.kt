package server.commands.user

import server.commands.Command
import server.events.command.TypeCommand
import server.events.user.UserCreateEvent

class UserCreateCommand(val name: String?=null, val login: String?=null, val password: String?=null,
                        val phone: String?=null, val email: String?=null): Command {
    val event: UserCreateEvent
        get() = UserCreateEvent(name!!, login!!, password!!, phone, email)

    override val typeCommand: TypeCommand
        get() = TypeCommand.USER_CREATE_COMMAND

    override fun toMap() = mapOf("name" to name, "login" to login, "password" to password, "phone" to phone,
        "email" to email)
}
