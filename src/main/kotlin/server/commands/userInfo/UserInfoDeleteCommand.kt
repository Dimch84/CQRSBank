package server.commands.userInfo

import server.commands.Command
import server.events.command.TypeCommand
import server.events.userInfo.UserInfoDeleteEvent

class UserInfoDeleteCommand(val id: Long?=null): Command {
    val event: UserInfoDeleteEvent
        get() = UserInfoDeleteEvent(id!!)

    override val typeCommand: TypeCommand
        get() = TypeCommand.USER_INFO_DELETE_COMMAND

    override fun toMap() = mapOf("id" to id)
}
