package com.engeto.equipment;

public abstract class Equipment {

    private int number;

    public Equipment(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return String.valueOf(getNumber());
    }
}
