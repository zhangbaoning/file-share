package me.bn.fileshare;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class FileShareApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileShareApplication.class, args);
	}

}
