package client.api.abstractions

data class CardBody(val name: String = "", val type: String = "", val account_id: Int = -1) {
    fun toMap() = mapOf("name" to name, "type" to type, "account_id" to account_id)
}
