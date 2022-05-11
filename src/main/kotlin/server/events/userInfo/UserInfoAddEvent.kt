package server.events.userInfo

data class UserInfoAddEvent(val id: Long, val data: List<String>) : UserInfoEvent
