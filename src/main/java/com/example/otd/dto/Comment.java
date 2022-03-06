package com.example.otd.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class Comment {
    private int id;
    private String name;
    private String password;
    private String sentiment;
    private float percent;
    private String datetime;
    private String content;
}
