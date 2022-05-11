package server.db.postgresql.entities

import server.abstractions.userInfo.UserInfoEventRes
import server.events.userInfo.StoreUserInfoEvent
import server.events.userInfo.TypeUserInfoEvent.*
import server.events.userInfo.UserInfoAddEvent
import server.events.userInfo.UserInfoDeleteEvent
import javax.persistence.*


@Entity
@Table(name = "userInfoEvents")
class UserInfoEvents(@Id @Column(name = "id", nullable = false) var id: Long? = null)
    : DomainEvents<StoreUserInfoEvent, UserInfoEventRes> {

    @Convert(converter = StoreUserInfoEvent.Companion.ConverterEvent::class)
    @Column(columnDefinition = "jsonb")
    override val events: MutableList<StoreUserInfoEvent> = mutableListOf()

    @Transient
    private val eventRes = UserInfoEventRes()

    @Transient
    private var initial = true

    override fun initEvents() {
        if (initial) {
            initial = false
            events.forEach {
                when (it.type) {
                    USER_INFO_ADD_EVENT     -> update(it.userInfoEvent as UserInfoAddEvent, add = false)
                    USER_INFO_DELETE_EVENT  -> update(it.userInfoEvent as UserInfoDeleteEvent, add = false)
                }
            }
        }
    }

    fun update(event: UserInfoAddEvent, add: Boolean = true): UserInfoEventRes {
        eventRes.apply { id=event.id; data.addAll(event.data) }
        if (add) events.add(StoreUserInfoEvent(event, USER_INFO_ADD_EVENT))
        return eventRes
    }

    fun update(event: UserInfoDeleteEvent, add: Boolean = true): UserInfoEventRes {
        eventRes.apply { data.clear() }
        if (add) events.add(StoreUserInfoEvent(event, USER_INFO_DELETE_EVENT))
        return eventRes
    }

    override fun update() = eventRes
}
