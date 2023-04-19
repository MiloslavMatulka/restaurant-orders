package com.engeto.worker;

public class Waiter extends Worker {

    public Waiter(String name) {
        super(name);
    }

    public Waiter(int id, String name) {
        super(id, name);
    }

    @Override
    public String toString() {
        return super.toString() + " (waiter ID) - " + getName();
    }
}
