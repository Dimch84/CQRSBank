package client.api.abstractions

data class AccountBody(val money: Long=0, val userId: Long=-1, val planId: Long=-1) {
    fun toMap() = mapOf("money" to money, "userId" to userId, "plan_id" to planId)
}
