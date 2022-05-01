package server.events.account

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

enum class TypeAccountEvent {
    ACCOUNT_CREATE_EVENT,
    ACCOUNT_UPDATE_PLAN_EVENT,
    ACCOUNT_DELETE_EVENT;

    val eventType: Type
        get() = when(this) {
            ACCOUNT_CREATE_EVENT -> object : TypeToken<AccountCreateEvent>() {}.type
            ACCOUNT_UPDATE_PLAN_EVENT -> object : TypeToken<AccountUpdatePlanEvent>() {}.type
            ACCOUNT_DELETE_EVENT -> object : TypeToken<AccountDeleteEvent>() {}.type
        }
}
