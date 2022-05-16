package server.events.account

data class AccountUpdatePlanEvent(val planId: Long, val id: Long, val login: String) : AccountEvent
