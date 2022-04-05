package api.security

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component
class UserDetails: UserDetailsService {
    override fun loadUserByUsername(login: String): UserDetails? {  // TODO(Update it, find in user db)
        val authorities = listOf(SimpleGrantedAuthority(""))
        return User(login, "pass", authorities)
    }
}
