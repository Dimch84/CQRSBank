package server.db.postgresql.entities

import server.abstractions.user.UserDeleteEventRes
import server.abstractions.user.UserEventRes
import server.events.user.StoreUserEvent
import server.events.user.TypeUserEvent.*
import server.events.user.UserCreateEvent
import server.events.user.UserDeleteEvent
import server.events.user.UserUpdateProfileEvent
import server.exceptions.DeleteException
import javax.persistence.*


@Entity
@Table(name = "UserEvents")
class UserEvents: DomainEvents<StoreUserEvent, UserEventRes> {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "login", nullable = false)
    var loginEnd: String = ""

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
                    USER_UPDATE_PROFILE_EVENT -> update(it.userEvent as UserUpdateProfileEvent, add = false)
                    USER_DELETE_EVENT -> throw DeleteException("user with login=${(it.userEvent as 
                            UserDeleteEvent).login} was deleted")
                }
            }
        }
    }

    fun update(event: UserCreateEvent, add: Boolean = true): UserEventRes {
        val curId = id
        loginEnd = event.login
        eventRes.apply { name=event.name; login=event.login; password=event.password; phone=event.phone;
            email=event.email; id=curId }
        if (add) events.add(StoreUserEvent(event, USER_CREATE_EVENT))
        return eventRes
    }

    fun update(event: UserUpdateProfileEvent, add: Boolean = true): UserEventRes {
        eventRes.apply {
            event.name?.let { name=it }
            event.login?.let { login=it; loginEnd=it }
            event.phone?.let { phone=it }
            event.email?.let { email=it }
        }
        if (add) events.add(StoreUserEvent(event, USER_UPDATE_PROFILE_EVENT))
        return eventRes
    }

    fun update(event: UserDeleteEvent): UserDeleteEventRes {
        events.add(StoreUserEvent(event, USER_DELETE_EVENT))
        return UserDeleteEventRes(id)
    }

    override fun update() = eventRes
}
