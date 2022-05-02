package server.abstractions.user

data class UserEventRes(var id: Long?=null, var name: String?=null, var login: String?=null,
                        var password: String?=null, var phone: String?=null, var email: String?=null)
: AnyUserEventRes {
    override fun toString() = "UserEventRes(id=$id, name=$name, login=$login, phone=$phone, email=$email)"
}
