package server.db.postgresql.entities

import server.abstractions.card.CardDeleteEventRes
import server.abstractions.card.CardEventRes
import server.abstractions.card.CardHistoryRes
import server.abstractions.card.HistoryMode.FULL
import server.abstractions.card.HistoryMode.PAYS
import server.events.card.*
import server.events.card.TypeCardEvent.*
import server.exceptions.DeleteException
import javax.persistence.*


@Entity
@Table(name = "CardEvents")
class CardEvents: DomainEvents<StoreCardEvent, CardEventRes> {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Convert(converter = StoreCardEvent.Companion.ConverterEvent::class)
    @Column(columnDefinition = "jsonb")
    override val events: MutableList<StoreCardEvent> = mutableListOf()

    @Transient
    private val eventRes = CardEventRes()

    @Transient
    private var initial = true

    override fun initEvents() {
        if (initial) {
            initial = false
            events.forEach {
                when (it.type) {
                    CARD_CREATE_EVENT           -> update(it.cardEvent as CardCreateEvent, add = false)
                    CARD_UPDATE_NAME_EVENT      -> update(it.cardEvent as CardUpdateNameEvent, add = false)
                    CARD_PAY_EVENT              -> update(it.cardEvent as CardPayEvent, add = false)
                    CARD_TRANSFER_EVENT         -> update(it.cardEvent as CardTransferEvent, add = false)
                    CARD_LOCAL_TRANSFER_EVENT   -> update(it.cardEvent as CardLocalTransferEvent, add = false)
                    CARD_DELETE_EVENT           -> throw DeleteException("card with id=${(it.cardEvent as CardDeleteEvent).id} was deleted")
                }
            }
        }
    }

    private val history: List<CardEvent>
        get() = events.map { it.cardEvent }

    fun update(event: CardCreateEvent, add: Boolean = true): CardEventRes {
        val curId = id
        eventRes.apply { name=event.name; type=event.type; accountId=event.accountId; cardNumber = event.cardNumber; expDate = event.expDate; cvv = event.cvv; id=curId }
        if (add) events.add(StoreCardEvent(event, CARD_CREATE_EVENT))
        return eventRes
    }

    fun update(event: CardUpdateNameEvent, add: Boolean = true): CardEventRes {
        eventRes.apply { name=event.name }
        if (add) events.add(StoreCardEvent(event, CARD_UPDATE_NAME_EVENT))
        return eventRes
    }

    fun update(event: CardPayEvent, add: Boolean = true): CardEventRes {
        if (add) events.add(StoreCardEvent(event, CARD_PAY_EVENT))
        return eventRes
    }

    fun update(event: CardTransferEvent, add: Boolean = true): CardEventRes {
        if (add) events.add(StoreCardEvent(event, CARD_TRANSFER_EVENT))
        return eventRes
    }

    fun update(event: CardDeleteEvent): CardDeleteEventRes {
        events.add(StoreCardEvent(event, CARD_DELETE_EVENT))
        return CardDeleteEventRes(id)
    }

    fun update(event: CardHistoryEvent): CardHistoryRes {
        return when(event.mode) {
            FULL -> CardHistoryRes(history)
            PAYS -> CardHistoryRes(history.filter { it is CardPayEvent || it is CardTransferEvent || it is CardLocalTransferEvent })
        }
    }

    fun update(event: CardLocalTransferEvent, add: Boolean = true): CardEventRes {
        if (add) events.add(StoreCardEvent(event, CARD_LOCAL_TRANSFER_EVENT))
        return eventRes
    }

    override fun update() = eventRes
}
