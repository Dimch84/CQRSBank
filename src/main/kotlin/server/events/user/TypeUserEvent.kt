package server.events.user

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


enum class TypeUserEvent {
    USER_CREATE_EVENT,
    USER_UPDATE_PROFILE_EVENT,
    USER_DELETE_EVENT;

    val eventType: Type
        get() = when(this) {
            USER_CREATE_EVENT -> object : TypeToken<UserCreateEvent>() {}.type
            USER_UPDATE_PROFILE_EVENT -> object : TypeToken<UserUpdateProfileEvent>() {}.type
            USER_DELETE_EVENT -> object : TypeToken<UserDeleteEvent>() {}.type
        }
}
