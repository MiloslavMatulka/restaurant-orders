package com.engeto.order;

import com.engeto.dish.Dish;
import com.engeto.equipment.Table;
import com.engeto.support.RestaurantException;
import com.engeto.worker.Waiter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order {

    private static long currentId = 0;
    private Dish dish;
    private LocalDateTime fulfilmentTime;
    private final long id;
    private boolean isPaid;
    private String note;
    private LocalDateTime orderedTime;
    private Table table;
    private Waiter waiter;

    public Order(Table table, Dish dish, Waiter waiter)
            throws RestaurantException {
        this.table = table;
        if (dish.isInMenu()) {
            this.dish = dish;
        } else {
            throw new RestaurantException("Ordered dish is not in the menu");
        }
        this.waiter = waiter;
        orderedTime = LocalDateTime.now();
        id = generateId();
        isPaid = false;
    }

    /**
     * Constructor used for parsing.
     */
    public Order(long id, LocalDateTime orderedTime, Table table, Dish dish,
                 Waiter waiter) {
        this.table = table;
        this.dish = dish;
        this.waiter = waiter;
        this.orderedTime = orderedTime;
        this.id = id;
        ++currentId;
        isPaid = false;
    }

    //region Getters and setters
    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public LocalDateTime getFulfilmentTime() {
        return fulfilmentTime;
    }

    public void setFulfilmentTime(LocalDateTime fulfilmentTime) {
        this.fulfilmentTime = fulfilmentTime;
    }

    public long getId() {
        return id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
        table.addNote(note);
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Waiter getWaiter() {
        return waiter;
    }

    public void setWaiter(Waiter waiter) {
        this.waiter = waiter;
    }
    //endregion

    public static long generateId() {
        return ++currentId;
    }

    public BigDecimal getPriceOfOrder() {
        return dish.getPrice()
                .multiply(BigDecimal.valueOf(dish.getQuantity()));
    }

    public void exportOrder(String file)
            throws RestaurantException {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(
                new FileWriter(file, true)))) {
            String time;
            if (getFulfilmentTime() == null) {
                time = getOrderedTime().getHour() + ":"
                        + getOrderedTime().getMinute() + "-0";
            } else {
                time = getOrderedTime().getHour() + ":"
                        + getOrderedTime().getMinute() + "-"
                        + getFulfilmentTime().getHour() + ":"
                        + getFulfilmentTime().getMinute();
            }

            writer.println(getId() + ". "
                    + getDish().getTitle() + " "
                    + getDish().getQuantity() + "x ("
                    + getDish().getPrice().multiply(BigDecimal
                    .valueOf(getDish().getQuantity())) + " CZK):\t"
                    + time + "\twaiter No. " + getWaiter().getId());
        } catch (IOException e) {
            throw new RestaurantException("Error while writing to file: "
                    + e. getLocalizedMessage());
        }
    }

    @Override
    public String toString() {
        return "Order{" +
                "dish=" + dish +
                ", fulfilmentTime=" + fulfilmentTime +
                ", id=" + id +
                ", isPaid=" + isPaid +
                ", note='" + note + '\'' +
                ", orderedTime=" + orderedTime +
                ", table=" + table +
                ", waiter=" + waiter +
                '}';
    }
}
