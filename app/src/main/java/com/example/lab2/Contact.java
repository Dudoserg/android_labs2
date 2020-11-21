package com.example.lab2;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Contact {
    private String name = "";
    private List<String> phones = new ArrayList<>();
    private int my_id;
}
