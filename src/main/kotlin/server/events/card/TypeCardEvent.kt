package server.events.card

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

enum class TypeCardEvent {
    CARD_CREATE_EVENT,
    CARD_UPDATE_NAME_EVENT,
    CARD_PAY_EVENT,
    CARD_TRANSFER_EVENT,
    CARD_DELETE_EVENT;

    val eventType: Type
        get() = when(this) {
            CARD_CREATE_EVENT -> object : TypeToken<CardCreateEvent>() {}.type
            CARD_UPDATE_NAME_EVENT -> object : TypeToken<CardUpdateNameEvent>() {}.type
            CARD_PAY_EVENT -> object : TypeToken<CardPayEvent>() {}.type
            CARD_TRANSFER_EVENT -> object : TypeToken<CardTransferEvent>() {}.type
            CARD_DELETE_EVENT -> object : TypeToken<CardDeleteEvent>() {}.type
        }
}
