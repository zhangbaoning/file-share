package me.bn.fileshare;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
@Component
public class ApplicationInit implements ApplicationRunner {

    @Value("${upload-file-path}")
    private  String uploadFilePath;
    @Override
    public void run(ApplicationArguments args) throws Exception {

        File file = new File(uploadFilePath);
        if (!file.exists()){
            try {
                file.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
