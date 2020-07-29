package me.bn.fileshare.config;

import me.bn.fileshare.dao.FileDao;
import me.bn.fileshare.dao.UserDao;
import me.bn.fileshare.entity.FileEntity;
import me.bn.fileshare.entity.UsersEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Component
public class LoginUserDetailsService implements UserDetailsService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private FileDao fileDao;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<UsersEntity> usersOptional = userDao.findById(s);
        if (usersOptional.isPresent()) {
            UsersEntity usersEntity = usersOptional.get();
            FileEntity fileEntity = new FileEntity();
            fileEntity.setUser(s);
            Example<FileEntity> fileEntityExample = Example.of(fileEntity, ExampleMatcher.matching().withIgnorePaths("id"));
            List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();

            List<FileEntity> fileEntityList = fileDao.findAll(fileEntityExample);
            for (FileEntity entity : fileEntityList) {
                authorities.add(new SimpleGrantedAuthority(entity.getName()));
            }
            //authorities.add(new SimpleGrantedAuthority("ROLE_" + "admin1"));
            return new User(s,NoOpPasswordEncoder.getInstance().encode(usersEntity.getPassword()),authorities);
        }
        return null;
    }
}
