package com.engeto.dish;

import com.engeto.support.Settings;
import com.engeto.support.RestaurantException;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class Dish {

    private Category category;
    private List<String> images;
    private boolean isInMenu;
    private int quantity;
    private int preparationTime;
    private BigDecimal price;
    private String title;

    public Dish(String title, BigDecimal price,
                int preparationTime, Category category) {
        this.title = title;
        this.price = price;
        quantity = 1;
        this.preparationTime = preparationTime;
        this.category = category;
        images = new LinkedList<>();
        images.add(Settings.getImage());
        isInMenu = false;
    }

    //region Getters and setters
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<String> getImages() {
        return new LinkedList<>(images);
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public boolean isInMenu() {
        return isInMenu;
    }

    public void setInMenu(boolean inMenu) {
        isInMenu = inMenu;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    //endregion

    public void addImage(String image) {
        if (images.size() == 1 && images.get(0).equals(Settings.getImage())) {
            images.remove(0);
        }
        images.add(image);
    }

    public void removeImage(String image) throws RestaurantException {
        if (images.size() > 1) {
            images.remove(image);
        } else {
            throw new RestaurantException("The only image cannot be removed");
        }
    }

    @Override
    public String toString() {
        return "Dish{" +
                "category=" + category +
                ", images=" + images +
                ", isInMenu=" + isInMenu +
                ", quantity=" + quantity +
                ", preparationTime=" + preparationTime +
                ", price=" + price +
                ", title='" + title + '\'' +
                '}';
    }

    //    @Override
//    public String toString() {
//        return getTitle() + " (" + getPrice() + "CZK)";
//    }
}
