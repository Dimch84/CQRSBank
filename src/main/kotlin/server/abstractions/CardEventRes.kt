package server.abstractions

data class CardEventRes(var id: Long? = null, var name: String? = null, var type: String? = null, var account_id: Int? = null): Res
