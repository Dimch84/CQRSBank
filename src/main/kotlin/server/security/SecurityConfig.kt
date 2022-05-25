package server.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import com.zaxxer.hikari.SQLExceptionOverride.Override
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
@EnableWebSecurity
@EnableConfigurationProperties
class SecurityConfig : WebSecurityConfigurerAdapter(), WebMvcConfigurer {
    @Autowired
    private lateinit var userDetailsService: UserDetails

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun addCorsMappings(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("http://localhost:8081", "http://localhost:8081/", "http://127.0.0.1:8081/", "*")
//        configuration.allowedMethods = listOf("GET", "POST")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        source.registerCorsConfiguration("/cqrs/register/**", configuration)
        return source
    }

//    // permit all
//    override fun configure(http: HttpSecurity) {
//        http.csrf().disable()
//            .authorizeRequests()
//            .antMatchers("/**").permitAll()
//            .and().httpBasic()
//            .and().sessionManagement().disable()
//    }

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .authorizeRequests()
                // swagger
            .antMatchers("/v2/api-docs").permitAll()
            .antMatchers("/swagger-resources").permitAll()
            .antMatchers("/swagger-resources/**").permitAll()
            .antMatchers("/configuration/ui").permitAll()
            .antMatchers("/configuration/security").permitAll()
            .antMatchers("/swagger-ui.html").permitAll()
            .antMatchers("/webjars/**").permitAll()
            .antMatchers("/v3/api-docs/**").permitAll()
            .antMatchers("/swagger-ui/**").permitAll()
                // main part
            .antMatchers("/cqrs/accounts/**").hasAnyAuthority("ADMIN", "USER")
            .antMatchers("/cqrs/cards/**").hasAnyAuthority("ADMIN", "USER")
            .antMatchers("/cqrs/user/**").hasAnyAuthority("ADMIN", "USER")
            .antMatchers("/cqrs/register/**").permitAll()
//                // outdated
//            .antMatchers("/common/**").hasAuthority("ADMIN")
//            .antMatchers("/accounts/**").hasAnyAuthority("ADMIN", "USER")
//            .antMatchers("/cards/**").hasAnyAuthority("ADMIN", "USER")
//            .antMatchers("/user/**").hasAnyAuthority("ADMIN", "USER")
//            .antMatchers("/register/**").permitAll()
                // service
            .antMatchers("/userCommands/**").permitAll()
            .antMatchers("/accountsCommands/**").permitAll()
            .antMatchers("/cardsCommands/**").permitAll()
//            .antMatchers("/userCommands/create").permitAll()
//            .antMatchers("/userCommands/all").hasAuthority("ADMIN")
//            .antMatchers("/userCommands/**").hasAnyAuthority("ADMIN", "USER")
//            .antMatchers("/accountsCommands/all").hasAuthority("ADMIN")
//            .antMatchers("/accountsCommands/**").hasAnyAuthority("ADMIN", "USER")
//            .antMatchers("/cardsCommands/all").hasAuthority("ADMIN")
//            .antMatchers("/cardsCommands/**").hasAnyAuthority("ADMIN", "USER")
//            .antMatchers("/**").hasAuthority("ADMIN")

            .and().httpBasic()
            .and().cors()
            .and().sessionManagement().disable()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder())
    }
}
