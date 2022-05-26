package server.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import server.db.mongo.UserRepository

@Component
class UserDetails @Autowired constructor(private val userRepository: UserRepository): UserDetailsService {
    override fun loadUserByUsername(login: String): UserDetails? {  // TODO(Update it, find in user db)
        val user = userRepository.findByLogin(login) ?: throw Exception("User not find")
        val authorities =
            if (user.login == "admin") listOf(SimpleGrantedAuthority("ADMIN"), SimpleGrantedAuthority("USER"))
            else listOf(SimpleGrantedAuthority("USER"))
        return User(login, BCryptPasswordEncoder().encode(user.password), authorities)
    }
}
