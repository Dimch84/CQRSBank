package server.events.card

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import server.events.AnyStore
import javax.persistence.AttributeConverter
import javax.persistence.Converter
import server.events.card.TypeCardEvent.*


data class StoreCardEvent(val cardEvent: CardEvent, val type: TypeCardEvent): AnyStore {
    companion object {
        class StorePreCardEvent(val cardEvent: String, val type: TypeCardEvent)

        @Converter(autoApply = true)
        open class PreConverterEvent : AttributeConverter<List<StorePreCardEvent>, String> {
            companion object {
                private val GSON = Gson()
                private val eventsListType = object : TypeToken<List<StorePreCardEvent>>() {}.type
            }

            override fun convertToDatabaseColumn(events: List<StorePreCardEvent>): String = GSON.toJson(events)
            override fun convertToEntityAttribute(dbData: String): List<StorePreCardEvent> = GSON.fromJson(dbData, eventsListType)
        }

        @Converter(autoApply = true)
        open class ConverterEvent : AttributeConverter<List<StoreCardEvent>, String> {
            companion object {
                private val GSON = Gson()
                private val preConverterEvent = PreConverterEvent()
                private val cardCreateType = object : TypeToken<CardCreateEvent>() {}.type
                private val cardUpdateNameType = object : TypeToken<CardUpdateNameEvent>() {}.type
            }

            override fun convertToDatabaseColumn(events: List<StoreCardEvent>): String =
                preConverterEvent.convertToDatabaseColumn(
                    events.map { StorePreCardEvent(GSON.toJson(it.cardEvent), it.type) }
                )
            override fun convertToEntityAttribute(dbData: String): List<StoreCardEvent> =
                preConverterEvent.convertToEntityAttribute(dbData).map {
                    when(it.type) {
                        CARD_CREATE_EVENT -> StoreCardEvent(GSON.fromJson(it.cardEvent, cardCreateType), it.type)
                        CARD_UPDATE_NAME_EVENT -> StoreCardEvent(GSON.fromJson(it.cardEvent, cardUpdateNameType), it.type)
                    }
                }
        }
    }
}
