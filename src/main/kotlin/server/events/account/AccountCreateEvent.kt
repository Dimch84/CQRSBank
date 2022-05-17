package server.events.account

data class AccountCreateEvent(val name: String, val money: Long, val userId: Long, val planId: Long, val id: Long?=null) : AccountEvent
