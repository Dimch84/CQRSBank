package server.events.userInfo

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import server.events.AnyStore
import javax.persistence.AttributeConverter
import javax.persistence.Converter


data class StoreUserInfoEvent(val userInfoEvent: UserInfoEvent, val type: TypeUserInfoEvent): AnyStore {
    companion object {
        class StorePreUserInfoEvent(val userInfoEvent: String, val type: TypeUserInfoEvent)

        @Converter(autoApply = true)
        open class PreConverterEvent : AttributeConverter<List<StorePreUserInfoEvent>, String> {
            companion object {
                private val GSON = Gson()
                private val eventsListType = object : TypeToken<List<StorePreUserInfoEvent>>() {}.type
            }

            override fun convertToDatabaseColumn(events: List<StorePreUserInfoEvent>): String = GSON.toJson(events)
            override fun convertToEntityAttribute(dbData: String): List<StorePreUserInfoEvent> = GSON.fromJson(dbData, eventsListType)
        }

        @Converter(autoApply = true)
        open class ConverterEvent : AttributeConverter<List<StoreUserInfoEvent>, String> {
            companion object {
                private val GSON = Gson()
                private val preConverterEvent = PreConverterEvent()
            }

            override fun convertToDatabaseColumn(events: List<StoreUserInfoEvent>): String =
                preConverterEvent.convertToDatabaseColumn(
                    events.map { StorePreUserInfoEvent(GSON.toJson(it.userInfoEvent), it.type) }
                )
            override fun convertToEntityAttribute(dbData: String): List<StoreUserInfoEvent> =
                preConverterEvent.convertToEntityAttribute(dbData).map {
                    StoreUserInfoEvent(GSON.fromJson(it.userInfoEvent, it.type.eventType), it.type)
                }
        }
    }
}
