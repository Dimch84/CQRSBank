package server.db.postgresql.entities

import server.abstractions.account.AccountDeleteEventRes
import server.abstractions.account.AccountEventRes
import server.events.account.*
import server.events.account.TypeAccountEvent.*
import server.exceptions.DeleteException
import javax.persistence.*


@Entity
@Table(name = "AccountEvents")
class AccountEvents: DomainEvents<StoreAccountEvent, AccountEventRes> {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Convert(converter = StoreAccountEvent.Companion.ConverterEvent::class)
    @Column(columnDefinition = "jsonb")
    override val events: MutableList<StoreAccountEvent> = mutableListOf()

    @Transient
    private val eventRes = AccountEventRes()

    @Transient
    private var initial = true

    override fun initEvents() {
        if (initial) {
            initial = false
            events.forEach {
                when (it.type) {
                    ACCOUNT_CREATE_EVENT        -> update(it.accountEvent as AccountCreateEvent, add = false)
                    ACCOUNT_UPDATE_PLAN_EVENT   -> update(it.accountEvent as AccountUpdatePlanEvent, add = false)
                    ACCOUNT_UPDATE_MONEY_EVENT  -> update(it.accountEvent as AccountUpdateMoneyEvent, add = false)
                    ACCOUNT_DELETE_EVENT        -> throw DeleteException("account with id=${(it.accountEvent as AccountDeleteEvent).id} was deleted")
                }
            }
        }
    }

    fun update(event: AccountCreateEvent, add: Boolean = true): AccountEventRes {
        val curId = id
        eventRes.apply { money=event.money; userId=event.userId; planId=event.planId; id=curId }
        if (add) events.add(StoreAccountEvent(event, ACCOUNT_CREATE_EVENT))
        return eventRes
    }

    fun update(event: AccountUpdatePlanEvent, add: Boolean = true): AccountEventRes {
        eventRes.apply { planId=event.planId }
        if (add) events.add(StoreAccountEvent(event, ACCOUNT_UPDATE_PLAN_EVENT))
        return eventRes
    }

    fun update(event: AccountUpdateMoneyEvent, add: Boolean = true): AccountEventRes {
        eventRes.apply {
            if (money!! - event.money < 0)
                throw Exception("You have only ${money!!}, you cannot pay ${event.money}")
            money = money!! - event.money
        }
        if (add) events.add(StoreAccountEvent(event, ACCOUNT_UPDATE_MONEY_EVENT))
        return eventRes
    }

    fun update(event: AccountDeleteEvent): AccountDeleteEventRes {
        events.add(StoreAccountEvent(event, ACCOUNT_DELETE_EVENT))
        return AccountDeleteEventRes(id)
    }

    override fun update() = eventRes
}
