package me.bn.fileshare.service;

import org.springframework.stereotype.Service;

import java.io.*;
@Service
public class FileService {
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
}
