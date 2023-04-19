package com.engeto.dish;

import com.engeto.support.RestaurantException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Dishes {

    private static List<Dish> listOfDishes = new ArrayList<>();

    private Dishes() {}

    public static List<Dish> getListOfDishes() {
        return new ArrayList<>(listOfDishes);
    }

    public static void setListOfDishes(List<Dish> dishes) {
        listOfDishes = dishes;
    }

    public static void addDish(Dish dish) {
        listOfDishes.add(dish);
    }

    public static void exportDishes(List<Dish> listOfDishes, String file)
            throws RestaurantException {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(
                new FileWriter(file)))) {
            listOfDishes.forEach(dish -> writer.println(dish.getTitle() + ";"
                            + dish.getPrice() + ";"
                            + dish.getPreparationTime() + ";"
                            + dish.getCategory() + ";"
                            + String.join(",", dish.getImages())));
        } catch (IOException e) {
            throw new RestaurantException("Error while writing to file: "
                    + e. getLocalizedMessage());
        }
    }

    public static List<Dish> importDishes(String file)
            throws RestaurantException {
        File input = new File(file);
        long line = 0L;
        List<Dish> dishes = new ArrayList<>();
        try (Scanner scanner = new Scanner(input)) {
            while(scanner.hasNextLine()) {
                ++line;
                String record = scanner.nextLine();
                dishes.add(parseDish(record));
            }
        } catch (FileNotFoundException e) {
            throw new RestaurantException("File " + e.getLocalizedMessage());
        } catch (RestaurantException e) {
            throw new RestaurantException(e.getLocalizedMessage()
                    + ", line " + line);
        }
        return dishes;
    }

    public static Dish parseDish(String data) throws RestaurantException {
        String[] items = data.split(";");
        Category category;
        int preparationTime;
        BigDecimal price;
        String title = items[0];
        try {
            price = new BigDecimal(items[1]);
            preparationTime = Integer.parseInt(items[2]);
            category = Category.valueOf(items[3]);
        } catch (NumberFormatException e) {
            throw new RestaurantException("Parsing error, incorrect number "
                    + "format: " + e.getLocalizedMessage());
        } catch (IllegalArgumentException e) {
            throw new RestaurantException("Parsing error, incorrect category: "
                    + e.getLocalizedMessage());
        }
        String imgString = items[4];
        String[] imgStrings = imgString.split(",");
        List<String> images = Arrays.asList(imgStrings);

        Dish dish = new Dish(title, price, preparationTime, category);
        dish.setImages(images);
        return dish;
    }

    public static void removeDish(Dish dish) throws RestaurantException {
        if (listOfDishes.contains(dish)) {
            listOfDishes.remove(dish);
        } else {
            throw new RestaurantException("Inserted dish cannot be removed as "
                    + "it does not exist in the list");
        }
    }

    public void sortByCategory(List<Dish> listOfDishes) {
        listOfDishes.sort(Comparator.comparing(Dish::getCategory));
        /* The same in a different way
        listOfDishes.sort((d1, d2) ->
                d1.getCategory().compareTo(d2.getCategory())); */
    }

    public void updateDish(Dish oldDish, Dish updatedDish)
            throws RestaurantException {
        if (listOfDishes.contains(oldDish)) {
            int dishIndex = listOfDishes.indexOf(oldDish);
            listOfDishes.set(dishIndex, updatedDish);
        } else {
            throw new RestaurantException("Inserted dish cannot be updated as "
                    + "it does not exist in the list");
        }
    }
}
