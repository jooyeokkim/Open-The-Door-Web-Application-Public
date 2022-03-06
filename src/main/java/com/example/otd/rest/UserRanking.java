package com.example.otd.rest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRanking {
    private String response;
    private int rank;
    private String id;
    private int currentLevel;
    private int totalDia;
}
