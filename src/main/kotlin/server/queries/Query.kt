package server.queries

interface Query {
    fun toMap(): Map<String, Any>
}
