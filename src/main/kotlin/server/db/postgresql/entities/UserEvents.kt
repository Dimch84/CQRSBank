package server.db.postgresql.entities

import server.abstractions.user.UserEventRes
import server.events.user.StoreUserEvent
import server.events.user.TypeUserEvent.*
import server.events.user.UserCreateEvent
import javax.persistence.*


@Entity
@Table(name = "UserEvents")
class UserEvents: DomainEvents<StoreUserEvent, UserEventRes> {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Convert(converter = StoreUserEvent.Companion.ConverterEvent::class)
    @Column(columnDefinition = "jsonb")
    override val events: MutableList<StoreUserEvent> = mutableListOf()

    @Transient
    private val eventRes = UserEventRes()

    @Transient
    private var initial = true

    override fun initEvents() {
        if (initial) {
            initial = false
            events.forEach {
                when (it.type) {
                    USER_CREATE_EVENT -> update(it.userEvent as UserCreateEvent, add = false)
                }
            }
        }
    }

    fun update(event: UserCreateEvent, add: Boolean = true): UserEventRes {
        val curId = id
        eventRes.apply { name=event.name; login=event.login; password=event.password; phone=event.phone;
            email=event.email; id=curId }
        if (add) events.add(StoreUserEvent(event, USER_CREATE_EVENT))
        return eventRes
    }

    override fun update() = eventRes
}
