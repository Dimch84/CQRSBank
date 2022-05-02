package server.events.account

data class AccountCreateEvent(val money: Long, val userId: Long, val planId: Long, val id: Long?=null) : AccountEvent
