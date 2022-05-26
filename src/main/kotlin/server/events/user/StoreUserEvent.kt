package server.events.user

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import server.events.AnyStore
import javax.persistence.AttributeConverter
import javax.persistence.Converter


data class StoreUserEvent(val userEvent: UserEvent, val type: TypeUserEvent): AnyStore {
    companion object {
        class StorePreUserEvent(val userEvent: String, val type: TypeUserEvent)

        @Converter(autoApply = true)
        open class PreConverterEvent : AttributeConverter<List<StorePreUserEvent>, String> {
            companion object {
                private val GSON = Gson()
                private val eventsListType = object : TypeToken<List<StorePreUserEvent>>() {}.type
            }

            override fun convertToDatabaseColumn(events: List<StorePreUserEvent>): String = GSON.toJson(events)
            override fun convertToEntityAttribute(dbData: String): List<StorePreUserEvent> = GSON.fromJson(dbData, eventsListType)
        }

        @Converter(autoApply = true)
        open class ConverterEvent : AttributeConverter<List<StoreUserEvent>, String> {
            companion object {
                private val GSON = Gson()
                private val preConverterEvent = PreConverterEvent()
            }

            override fun convertToDatabaseColumn(events: List<StoreUserEvent>): String =
                preConverterEvent.convertToDatabaseColumn(
                    events.map { StorePreUserEvent(GSON.toJson(it.userEvent), it.type) }
                )
            override fun convertToEntityAttribute(dbData: String): List<StoreUserEvent> =
                preConverterEvent.convertToEntityAttribute(dbData).map {
                    StoreUserEvent(GSON.fromJson(it.userEvent, it.type.eventType), it.type)
                }
        }
    }
}
