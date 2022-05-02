package server.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import server.commands.account.AccountCreateCommand
import server.commands.account.AccountDeleteCommand
import server.commands.account.AccountUpdatePlanCommand
import server.queries.account.AccountAllQuery
import server.queries.account.AccountCardsQuery
import server.queries.account.AccountMoneyQuery
import server.queries.account.AccountQuery
import server.service.CommandService

@RestController
@Api(description = "accounts commands")
class AccountsCommandsController @Autowired constructor(private val service: CommandService) {
    @ApiOperation(value = "Get account by id")
    @PostMapping("/accountsCommands/byId")
    suspend fun postAccountsByIdQuery(@RequestBody accountByIdQuery: AccountQuery) =
        service.send(accountByIdQuery)

    @ApiOperation(value = "Get account money")
    @PostMapping("/accountsCommands/byIdMoney")
    suspend fun getAccountsMoneyByIdQuery(@RequestBody accountByIdMoneyQuery: AccountMoneyQuery) =
        service.send(accountByIdMoneyQuery)

    @ApiOperation(value = "Get account cards")
    @PostMapping("/accountsCommands/byIdCards")
    suspend fun getAccountsCardsByIdQuery(@RequestBody AccountByIdCardsQuery: AccountCardsQuery) =
        service.send(AccountByIdCardsQuery)

    @ApiOperation(value = "Get all accounts")
    @PostMapping("/accountsCommands/all")
    suspend fun postAccountsAllQuery(@RequestBody accountAllQuery: AccountAllQuery) =
        service.send(accountAllQuery)

    //

    @ApiOperation(value = "Return id new account")
    @PostMapping("/accountsCommands/create")
    suspend fun postAccountsCreate(@RequestBody accountCreateCommand: AccountCreateCommand) =
        service.send(accountCreateCommand)

    @ApiOperation(value = "Update account name, return 'ok'")
    @PostMapping("/accountsCommands/updatePlan")
    suspend fun postAccountsUpdatePlan(@RequestBody accountUpdatePlanCommand: AccountUpdatePlanCommand) =
        service.send(accountUpdatePlanCommand)

    @ApiOperation(value = "Delete account, return 'ok'")
    @PostMapping("/accountsCommands/delete")
    suspend fun postAccountsDelete(@RequestBody accountDeleteCommand: AccountDeleteCommand) =
        service.send(accountDeleteCommand)
}
