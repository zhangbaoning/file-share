package me.bn.fileshare.dao;

import me.bn.fileshare.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileDao extends JpaRepository<FileEntity,Integer> {
}
