package me.bn.fileshare.dao;

import me.bn.fileshare.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<UsersEntity,String> {
}
