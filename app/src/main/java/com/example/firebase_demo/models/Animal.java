package com.example.firebase_demo.models;

public class Animal {

    public String name, description;

    public Animal() {
        // required for firebase firestore model
    }

    public Animal(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
