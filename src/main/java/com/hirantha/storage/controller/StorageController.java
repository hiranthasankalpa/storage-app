package com.hirantha.storage.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
public class StorageController {

  @GetMapping("/getAll")
  public String getAllFiles(@RequestHeader("user-name") String userName) {
    return userName;
  }

}
