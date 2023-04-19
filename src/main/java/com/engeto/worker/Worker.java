package com.engeto.worker;

public abstract class Worker {

    private static int currentId = 0;
    private final int id;
    private String name;

    public Worker(String name) {
        this.name = name;
        id = generateId();
    }

    public Worker(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private static int generateId() {
        return ++currentId;
    }

    @Override
    public String toString() {
        return String.valueOf(getId());
    }
}
