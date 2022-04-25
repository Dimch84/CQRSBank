package server.events.command

import com.google.gson.reflect.TypeToken
import server.commands.card.*
import server.commands.user.UserCreateCommand
import java.lang.reflect.Type

enum class TypeCommand {
    CARD_CREATE_COMMAND,
    CARD_UPDATE_NAME_COMMAND,
    CARD_PAY_COMMAND,
    CARD_TRANSFER_COMMAND,
    CARD_RECEIPT_COMMAND,
    CARD_DELETE_COMMAND,

    USER_CREATE_COMMAND;
    val commandType: Type
        get() = when(this) {
            CARD_CREATE_COMMAND -> object : TypeToken<CardCreateCommand>() {}.type
            CARD_UPDATE_NAME_COMMAND -> object : TypeToken<CardUpdateNameCommand>() {}.type
            CARD_PAY_COMMAND -> object : TypeToken<CardPayCommand>() {}.type
            CARD_TRANSFER_COMMAND -> object : TypeToken<CardTransferCommand>() {}.type
            CARD_RECEIPT_COMMAND -> object : TypeToken<CardReceiptCommand>() {}.type
            CARD_DELETE_COMMAND -> object : TypeToken<CardDeleteCommand>() {}.type

            USER_CREATE_COMMAND -> object : TypeToken<UserCreateCommand>() {}.type
        }
}
