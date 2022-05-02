package server.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import server.commands.user.UserCreateCommand
import server.commands.user.UserDeleteCommand
import server.commands.user.UserUpdateProfileCommand
import server.queries.user.UserAccountsQuery
import server.queries.user.UserAllQuery
import server.queries.user.UserQuery
import server.service.CommandService

@RestController
@Api(description = "user commands")
class UserCommandsController @Autowired constructor(private val service: CommandService) {
    @ApiOperation(value = "Get user by login")
    @PostMapping("/userCommands/byLogin")
    suspend fun postUserByLoginQuery(@RequestBody userByLoginQuery: UserQuery) =
        service.send(userByLoginQuery)

    @ApiOperation(value = "Get user accounts")
    @PostMapping("/userCommands/byLoginAccounts")
    suspend fun postUserByLoginAccountsQuery(@RequestBody userAccountsQuery: UserAccountsQuery) =
        service.send(userAccountsQuery)

    @ApiOperation(value = "Get all users")
    @PostMapping("/userCommands/all")
    suspend fun postUserAllQuery(@RequestBody userAllQuery: UserAllQuery) =
        service.send(userAllQuery)

    //

    @ApiOperation(value = "Return id new user")
    @PostMapping("/userCommands/create")
    suspend fun postUserCreate(@RequestBody userCreateCommand: UserCreateCommand) =
        service.send(userCreateCommand)

    @ApiOperation(value = "Update user profile, return 'ok'")
    @PostMapping("/userCommands/updateProfile")
    suspend fun postUserUpdateProfile(@RequestBody userUpdateProfileCommand: UserUpdateProfileCommand) =
        service.send(userUpdateProfileCommand)

    @ApiOperation(value = "Delete user, return 'ok'")
    @PostMapping("/userCommands/delete")
    suspend fun postUserDelete(@RequestBody userDeleteCommand: UserDeleteCommand) =
        service.send(userDeleteCommand)
}
