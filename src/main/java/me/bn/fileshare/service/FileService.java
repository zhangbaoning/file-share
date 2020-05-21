package me.bn.fileshare.service;

import me.bn.fileshare.dao.FileDao;
import me.bn.fileshare.entity.FileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.UUID;

@Service
public class FileService {
    @Autowired
    FileDao dao;
    File download(String name) {
        return new File(name);
    }

    void upload(File file) {
        File writeFile = new File("/" + file.getName());
        try (
                FileWriter fileWriter = new FileWriter(writeFile);
                FileReader reader = new FileReader(file);) {

            int read;
            while ((read = reader.read()) != -1) {
                fileWriter.write(read);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void save(String name,boolean isEncrypt){
        FileEntity fileEntity = new FileEntity();
        fileEntity.setName(name);
        fileEntity.setUuid(UUID.randomUUID().toString());
        fileEntity.setIsEncrypt(isEncrypt);
        dao.save(fileEntity);
    }
}
