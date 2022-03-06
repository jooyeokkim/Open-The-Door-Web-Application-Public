package com.example.otd.rest;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class User {
    private String response;
    private String id;
    private int rank;
    private int users;
    private int level;
    private int diamonds;
    private List<Map<String, String>> records;
}
