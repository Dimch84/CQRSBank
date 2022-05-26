package server.events.account

data class AccountUpdateMoneyEvent(val money: Long, val id: Long) : AccountEvent
