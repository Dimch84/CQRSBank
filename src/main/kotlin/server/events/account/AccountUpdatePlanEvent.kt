package server.events.account

data class AccountUpdatePlanEvent(val planId: Long, val id: Long) : AccountEvent
