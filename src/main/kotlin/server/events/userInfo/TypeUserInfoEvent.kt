package server.events.userInfo

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


enum class TypeUserInfoEvent {
    USER_INFO_ADD_EVENT,
    USER_INFO_DELETE_EVENT;

    val eventType: Type
        get() = when(this) {
            USER_INFO_ADD_EVENT -> object : TypeToken<UserInfoAddEvent>() {}.type
            USER_INFO_DELETE_EVENT -> object : TypeToken<UserInfoDeleteEvent>() {}.type
        }
}
