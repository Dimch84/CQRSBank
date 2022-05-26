package server.abstractions.userInfo

data class UserInfoEventRes(var id: Long?=null, val data: MutableList<String> = mutableListOf()) : AnyUserInfoEventRes {
    override fun toString() = "UserInfoEventRes(id=$id, data=$data)"
}
