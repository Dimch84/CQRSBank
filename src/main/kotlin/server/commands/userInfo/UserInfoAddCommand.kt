package server.commands.userInfo

import server.commands.Command
import server.events.command.TypeCommand
import server.events.userInfo.UserInfoAddEvent

class UserInfoAddCommand(val id: Long?=null, val data: List<String>?=null): Command {
    val event: UserInfoAddEvent
        get() = UserInfoAddEvent(id!!, data!!)

    override val typeCommand: TypeCommand
        get() = TypeCommand.USER_INFO_ADD_COMMAND

    override fun toMap() = mapOf("id" to id, "data" to data)
}
