package server.events.account

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import server.events.AnyStore
import javax.persistence.AttributeConverter
import javax.persistence.Converter


data class StoreAccountEvent(val accountEvent: AccountEvent, val type: TypeAccountEvent): AnyStore {
    companion object {
        class StorePreAccountEvent(val accountEvent: String, val type: TypeAccountEvent)

        @Converter(autoApply = true)
        open class PreConverterEvent : AttributeConverter<List<StorePreAccountEvent>, String> {
            companion object {
                private val GSON = Gson()
                private val eventsListType = object : TypeToken<List<StorePreAccountEvent>>() {}.type
            }

            override fun convertToDatabaseColumn(events: List<StorePreAccountEvent>): String = GSON.toJson(events)
            override fun convertToEntityAttribute(dbData: String): List<StorePreAccountEvent> = GSON.fromJson(dbData, eventsListType)
        }

        @Converter(autoApply = true)
        open class ConverterEvent : AttributeConverter<List<StoreAccountEvent>, String> {
            companion object {
                private val GSON = Gson()
                private val preConverterEvent = PreConverterEvent()
            }

            override fun convertToDatabaseColumn(events: List<StoreAccountEvent>): String =
                preConverterEvent.convertToDatabaseColumn(
                    events.map { StorePreAccountEvent(GSON.toJson(it.accountEvent), it.type) }
                )
            override fun convertToEntityAttribute(dbData: String): List<StoreAccountEvent> =
                preConverterEvent.convertToEntityAttribute(dbData).map {
                    StoreAccountEvent(GSON.fromJson(it.accountEvent, it.type.eventType), it.type)
                }
        }
    }
}
