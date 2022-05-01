package server.abstractions.account

data class AccountEventRes(var id: Long? = null, var money: Long? = null, var user_id: Long? = null,
                           var plan_id: Long? = null): AnyAccountEventRes
