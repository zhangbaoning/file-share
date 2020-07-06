package me.bn.fileshare.config;

import me.bn.fileshare.dao.UserDao;
import me.bn.fileshare.entity.UsersEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


import javax.sql.DataSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

@Configuration
@EnableWebSecurity
public class SecurityConf extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDao userDao;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().mvcMatchers("download","")
                .authenticated()
                .and().formLogin().successForwardUrl("/");
        http.rememberMe();
        http.csrf().disable();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        List<UserDetails> userDetailsList = new ArrayList<>();
        User.UserBuilder users = User.withDefaultPasswordEncoder();
        for (UsersEntity usersEntity : userDao.findAll()) {
            UserDetails userDetails = users
                    .username(usersEntity.getUsername())
//                    .password(new BCryptPasswordEncoder().encode(usersEntity.getPassword()))
                    .password(usersEntity.getPassword())
                    .authorities("admin")
                    .build();
            userDetailsList.add(userDetails);
        }

        return new InMemoryUserDetailsManager(userDetailsList);
    }

}
