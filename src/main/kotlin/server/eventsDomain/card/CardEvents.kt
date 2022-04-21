package server.eventsDomain.card

import server.abstractions.CardEventRes
import server.db.postgresql.events.card.CardCreateEvent
import server.db.postgresql.events.card.CardUpdateNameEvent
import server.db.postgresql.events.card.StoreCardEvent
import server.db.postgresql.events.card.TypeCardEvent.*
import server.eventsDomain.DomainEvents
import javax.persistence.*


@Entity
@Table(name = "CardEvents")
class CardEvents: DomainEvents<StoreCardEvent, CardEventRes> {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
                    CARD_CREATE_EVENT -> update(it.cardEvent as CardCreateEvent, add = false)
                    CARD_UPDATE_NAME_EVENT -> update(it.cardEvent as CardUpdateNameEvent, add = false)
                }
            }
        }
    }

    fun update(event: CardCreateEvent, add: Boolean = true): CardEventRes {
        val curId = id
        eventRes.apply { name=event.name; type=event.type; account_id=event.account_id; id=curId }
        if (add) events.add(StoreCardEvent(event, CARD_CREATE_EVENT))
        return eventRes
    }

    fun update(event: CardUpdateNameEvent, add: Boolean = true): CardEventRes {
        eventRes.apply { name=event.name }
        if (add) events.add(StoreCardEvent(event, CARD_UPDATE_NAME_EVENT))
        return eventRes
    }

    override fun update() = eventRes
}
