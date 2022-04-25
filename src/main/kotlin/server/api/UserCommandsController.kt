package server.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import server.commands.user.UserCreateCommand
import server.service.CommandService

@RestController
@Api(description = "user commands")
class UserCommandsController @Autowired constructor(private val service: CommandService) {
//    @ApiOperation(value = "Get card by id")
//    @PostMapping("/userCommands/byId")
//    suspend fun postUserByIdQuery(@RequestBody cardByIdQuery: CardQuery) =
//        service.send(cardByIdQuery)
//
//    @ApiOperation(value = "Get card history")
//    @PostMapping("/userCommands/byIdHistory")
//    suspend fun postUserByIdHistoryQuery(@RequestBody cardHistoryQuery: CardHistoryQuery) =
//        service.send(cardHistoryQuery)

    @ApiOperation(value = "Return id new user")
    @PostMapping("/userCommands/create")
    suspend fun postUserCreate(@RequestBody userCreateCommand: UserCreateCommand) =
        service.send(userCreateCommand)

//    @ApiOperation(value = "Update card name, return 'ok'")
//    @PostMapping("/userCommands/updateName")
//    suspend fun postUserUpdateName(@RequestBody cardUpdateNameCommand: CardUpdateNameCommand) =
//        service.send(cardUpdateNameCommand)
//
//    @ApiOperation(value = "Pay, return 'ok'")
//    @PostMapping("/userCommands/payment")
//    suspend fun postUserPayment(@RequestBody cardPayCommand: CardPayCommand) =
//        service.send(cardPayCommand)
//
//    @ApiOperation(value = "Transfer, return 'ok'")
//    @PostMapping("/userCommands/transfer")
//    suspend fun postUserTransfer(@RequestBody cardTransferCommand: CardTransferCommand) =
//        service.send(cardTransferCommand)
//
//    @ApiOperation(value = "Receipt, return 'ok'")
//    @PostMapping("/userCommands/receipt")
//    suspend fun postUserReceipt(@RequestBody cardReceiptCommand: CardReceiptCommand) =
//        service.send(cardReceiptCommand)
//
//    @ApiOperation(value = "Delete card, return 'ok'")
//    @PostMapping("/userCommands/delete")
//    suspend fun postUserDelete(@RequestBody cardDeleteCommand: CardDeleteCommand) =
//        service.send(cardDeleteCommand)
}
