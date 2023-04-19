package com.engeto.dish;

import com.engeto.worker.Cook;

import java.util.ArrayList;
import java.util.List;

public class Menu {

    private Cook cook;
    private List<Dish> menu = new ArrayList<>();

    public Menu() {}

    public Menu(List<Dish> menu) {
        this.menu = menu;
    }

    public List<Dish> getMenu() {
        return new ArrayList<>(menu);
    }

    public void setMenu(List<Dish> menu) {
        this.menu = menu;
    }

    public void addItem(Dish dish, Cook cook) {
        menu.add(dish);
        dish.setInMenu(true);
    }

    public void removeAll(Cook cook) {
        menu.clear();
    }

    public void removeItem(Dish dish, Cook cook) {
        menu.remove(dish);
    }

    public void removeItem(int index, Cook cook) {
        menu.remove(index);
    }
}
