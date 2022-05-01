package server.events.account

data class AccountCreateEvent(val money: Long, val user_id: Long, val plan_id: Long, val id: Long? = null) : AccountEvent
