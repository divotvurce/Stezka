package stezka.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ApplicationSecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests()
                .requestMatchers(
                        "/inspirace/vytvorit", "/inspirace/editovat/**"
                )
                .authenticated() // Zde později budeme řešit role (autorizaci)
                .requestMatchers(
                        "/styles/**", "/images/**", "/scripts/**", "/fonts/**",
                        "/inspirace", "/", "/prirucka", "/inspirace/clanek/**", "/",
                        "/ucet/**", "/ucet/registrace"
                )
                .permitAll()
                .anyRequest() // Ostatní stránky jako např. `/articles/**` budou pouze pro přihlášené uživatele
                .authenticated()
                .and()
                .formLogin()
                    .loginPage("/ucet/prihlaseni")
                    .loginProcessingUrl("/ucet/prihlaseni")
                    .defaultSuccessUrl("/inspirace", true) // Nastavení přesměrování po úspěšném přihlášení
                    .usernameParameter("email") // Chceme se přihlašovat pomocí emailu
                    .permitAll() // Povolit vstup na `/account/login` i nepřihlášeným uživatelům
                .and()
                    .logout()
                    .logoutUrl("/ucet/odhlaseni") // Odhlašovací URL adresa
                .and()
                    .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
