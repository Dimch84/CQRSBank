package server.abstractions.account

data class AccountEventRes(var id: Long?=null, var name : String?=null, var money: Long?=null, var userId: Long?=null,
                           var planId: Long?=null): AnyAccountEventRes
