package server.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import server.commands.userInfo.UserInfoAddCommand
import server.commands.userInfo.UserInfoDeleteCommand
import server.queries.userInfo.UserInfoQuery
import server.queries.userInfo.UserInfoRequestQuery
import server.service.CommandService

@RestController
@Api(description = "user info")
class UserInfoCommandsController @Autowired constructor(private val service: CommandService) {
    @ApiOperation(value = "Get user info all")
    @PostMapping("/userInfoCommands/info")
    suspend fun postUserInfoQuery(@RequestBody userInfoQuery: UserInfoQuery) =
        service.send(userInfoQuery)

    // TODO(not handled yet, update)
    @ApiOperation(value = "Get user info request")
    @PostMapping("/userInfoCommands/info/query")
    suspend fun postUserInfoRequestQuery(@RequestBody userInfoRequestQuery: UserInfoRequestQuery) =
        service.send(userInfoRequestQuery)

    //

    @ApiOperation(value = "Add text to user info, return 'ok'")
    @PostMapping("/userInfoCommands/add")
    suspend fun postUserInfoAdd(@RequestBody userInfoAddCommand: UserInfoAddCommand) =
        service.send(userInfoAddCommand)

    @ApiOperation(value = "Delete user info, return 'ok'")
    @PostMapping("/userInfoCommands/delete")
    suspend fun postUserInfoDelete(@RequestBody userInfoDeleteCommand: UserInfoDeleteCommand) =
        service.send(userInfoDeleteCommand)
}
