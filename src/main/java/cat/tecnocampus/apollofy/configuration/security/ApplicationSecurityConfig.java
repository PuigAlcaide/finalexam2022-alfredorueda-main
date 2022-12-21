package cat.tecnocampus.apollofy.configuration.security;

import cat.tecnocampus.apollofy.configuration.security.jwt.JwtAuthenticationFilter;
import cat.tecnocampus.apollofy.configuration.security.jwt.JwtConfig;
import cat.tecnocampus.apollofy.configuration.security.jwt.JwtTokenVerifierFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ApplicationSecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final ApollofyUserDetailsService apollofyUserDetailsService;
    private final JwtConfig jwtConfig;

    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, ApollofyUserDetailsService apollofyUserDetailsService,
                                     JwtConfig jwtConfig) {
        this.passwordEncoder = passwordEncoder;
        this.apollofyUserDetailsService = apollofyUserDetailsService;
        this.jwtConfig = jwtConfig;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                //TODO 2 Here you can authorize users. You may want to use hasAnyRole()
                .antMatchers("**").permitAll()
                /*
                //.antMatchers(HttpMethod.GET, "/api/tracks").permitAll()
                .antMatchers("/me").authenticated()

                .antMatchers(HttpMethod.GET, "/api/me").hasRole("FREE")
                .antMatchers(HttpMethod.GET, "/api/me/tracks").hasRole("FREE")
                .antMatchers(HttpMethod.GET, "/api/me/tracks/{trackId}").hasRole("FREE")


                .antMatchers(HttpMethod.POST, "/api/tracks").hasRole("PREMIUM")
                .antMatchers(HttpMethod.PUT, "/api/tracks/{trackId}/artists").hasRole("PREMIUM")
                .antMatchers(HttpMethod.PUT, "/api/tracks/{trackId}/genres").hasRole("PREMIUM")
                .antMatchers(HttpMethod.PUT, "/api/me/likedTracks/{trackId}").hasRole("PREMIUM")
                .antMatchers(HttpMethod.GET, "/api/me/likedTracks").hasRole("PREMIUM")

                .antMatchers( "/api/me/playlists").hasRole("PROFESSIONAL")
                .antMatchers(HttpMethod.PUT, "/api/users").hasRole("PROFESSIONAL")
                .antMatchers(HttpMethod.GET, "/api/top/genres").hasRole("PROFESSIONAL")
                .antMatchers(HttpMethod.GET, "/api/top/tracks").hasRole("PROFESSIONAL")

                .antMatchers(HttpMethod.POST, "/api/djlist/{id}/tracks").hasAnyRole()

                 */
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationProvider(), jwtConfig))
                .addFilterAfter(new JwtTokenVerifierFilter(jwtConfig), JwtAuthenticationFilter.class)

                .csrf().disable()
                .cors().and()
                .headers().frameOptions().disable().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return httpSecurity.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(apollofyUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }
}
