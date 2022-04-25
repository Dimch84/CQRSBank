package server.commands.user

import server.commands.Command
import server.events.command.TypeCommand
import server.events.user.UserDeleteEvent

class UserDeleteCommand(val login: String=""): Command {
    val event: UserDeleteEvent
        get() = UserDeleteEvent(login)

    override val typeCommand: TypeCommand
        get() = TypeCommand.USER_DELETE_COMMAND

    override fun toMap() = mapOf("login" to login)
}
