package server.events.user

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

enum class TypeUserEvent {
    USER_CREATE_EVENT;

    val eventType: Type
        get() = when(this) {
            USER_CREATE_EVENT -> object : TypeToken<UserCreateEvent>() {}.type
        }
}
