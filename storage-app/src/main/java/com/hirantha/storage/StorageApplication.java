package com.hirantha.storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing
@SpringBootApplication
public class StorageApplication {
  public static void main(String[] args) {
    SpringApplication.run(StorageApplication.class, args);
  }

}
