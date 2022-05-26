package server.abstractions.card

data class CardEventRes(var id: Long?=null, var name: String?=null, var type: String?=null,
                        var cardNumber: String?=null, var expDate: String?=null, var cvv: String? = null,
                        var accountId: Long?=null): AnyCardEventRes
