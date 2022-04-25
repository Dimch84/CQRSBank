package server.handlers

import com.google.gson.Gson
import server.queries.Query

abstract class AnyQueryHandler<T: Query> {
    companion object {
        val GSON = Gson()
    }
    abstract fun handle(query: T): String
}
