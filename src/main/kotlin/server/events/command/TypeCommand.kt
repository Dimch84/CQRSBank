package server.events.command

import com.google.gson.reflect.TypeToken
import server.commands.account.AccountCreateCommand
import server.commands.account.AccountDeleteCommand
import server.commands.account.AccountUpdateMoneyCommand
import server.commands.account.AccountUpdatePlanCommand
import server.commands.card.*
import server.commands.user.UserCreateCommand
import server.commands.user.UserDeleteCommand
import server.commands.user.UserUpdateProfileCommand
import server.commands.userInfo.UserInfoAddCommand
import server.commands.userInfo.UserInfoDeleteCommand
import java.lang.reflect.Type


enum class TypeCommand {
    CARD_CREATE_COMMAND,
    CARD_UPDATE_NAME_COMMAND,
    CARD_PAY_COMMAND,
    CARD_TRANSFER_COMMAND,
    CARD_RECEIPT_COMMAND,
    CARD_LOCAL_TRANSFER_COMMAND,
    CARD_DELETE_COMMAND,
    CARD_HISTORY_COMMAND,

    USER_CREATE_COMMAND,
    USER_UPDATE_PROFILE_COMMAND,
    USER_DELETE_COMMAND,

    ACCOUNT_CREATE_COMMAND,
    ACCOUNT_UPDATE_PLAN_COMMAND,
    ACCOUNT_UPDATE_MONEY_COMMAND,
    ACCOUNT_DELETE_COMMAND,

    SEND_ONLY_COMMAND,

    USER_INFO_ADD_COMMAND,
    USER_INFO_DELETE_COMMAND;

    val commandType: Type
        get() = when(this) {
            CARD_CREATE_COMMAND         -> object : TypeToken<CardCreateCommand>() {}.type
            CARD_UPDATE_NAME_COMMAND    -> object : TypeToken<CardUpdateNameCommand>() {}.type
            CARD_PAY_COMMAND            -> object : TypeToken<CardPayCommand>() {}.type
            CARD_TRANSFER_COMMAND       -> object : TypeToken<CardTransferCommand>() {}.type
            CARD_RECEIPT_COMMAND        -> object : TypeToken<CardReceiptCommand>() {}.type
            CARD_LOCAL_TRANSFER_COMMAND -> object : TypeToken<CardLocalTransferCommand>() {}.type
            CARD_DELETE_COMMAND         -> object : TypeToken<CardDeleteCommand>() {}.type
            CARD_HISTORY_COMMAND        -> object : TypeToken<CardHistoryCommand>() {}.type

            USER_CREATE_COMMAND         -> object : TypeToken<UserCreateCommand>() {}.type
            USER_UPDATE_PROFILE_COMMAND -> object : TypeToken<UserUpdateProfileCommand>() {}.type
            USER_DELETE_COMMAND         -> object : TypeToken<UserDeleteCommand>() {}.type

            ACCOUNT_CREATE_COMMAND      -> object : TypeToken<AccountCreateCommand>() {}.type
            ACCOUNT_UPDATE_PLAN_COMMAND -> object : TypeToken<AccountUpdatePlanCommand>() {}.type
            ACCOUNT_UPDATE_MONEY_COMMAND-> object : TypeToken<AccountUpdateMoneyCommand>() {}.type
            ACCOUNT_DELETE_COMMAND      -> object : TypeToken<AccountDeleteCommand>() {}.type

            SEND_ONLY_COMMAND           -> throw Exception("not exist command type")

            USER_INFO_ADD_COMMAND       -> object : TypeToken<UserInfoAddCommand>() {}.type
            USER_INFO_DELETE_COMMAND    -> object : TypeToken<UserInfoDeleteCommand>() {}.type
        }
}
