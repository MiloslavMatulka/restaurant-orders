package com.engeto.restaurantorders;

import com.engeto.dish.Category;
import com.engeto.dish.Dish;
import com.engeto.dish.Dishes;
import com.engeto.dish.Menu;
import com.engeto.equipment.Table;
import com.engeto.order.Order;
import com.engeto.order.Orders;
import com.engeto.support.Settings;
import com.engeto.support.RestaurantException;
import com.engeto.worker.Cook;
import com.engeto.worker.Waiter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
* Application implementing a data model for tracking restaurant orders.
*
* @author Miloslav Matulka
*/
public class Main {

    private static final Logger logger =
            Logger.getLogger("Restaurant orders");
    private static final Menu menu = new Menu();
    private static final List<Table> tablesUsed = new ArrayList<>();

    private static void exportDishesInApp() {
        try {
            Dishes.exportDishes(Dishes.getListOfDishes(),
                    Settings.getFileDishes());
        } catch (RestaurantException e) {
            logger.log(Level.WARNING, e.getLocalizedMessage());
        }
    }

    private static void exportMenuInApp(Menu menu) {
        try {
            Dishes.exportDishes(menu.getMenu(), Settings.getFileMenu());
        } catch (RestaurantException e) {
            logger.log(Level.WARNING, e.getLocalizedMessage());
        }
    }

    private static void exportNotesInApp() {
        tablesUsed.stream()
                .filter(table -> table.getNotes() != null)
                .forEach(table -> {
                    try {
                        table.exportNotes(Settings.getFileNotes());
                    } catch (RestaurantException e) {
                        logger.log(Level.WARNING, e.getLocalizedMessage());
                    }
                });
    }

    /**
     * Use after checking if dishes are in menu.
     */
    private static void exportOrdersInApp() {
        try {
            Orders.exportOrders(Orders.getListOfOrders(),
                    Settings.getFileOrders());
        } catch (RestaurantException e) {
            logger.log(Level.WARNING, e.getLocalizedMessage());
        }
    }

    private static void exportOrdersInAppAlt() {
        Orders.getListOfOrders()
                .forEach(order -> {
                    try {
                        order.exportOrder(Settings.getFileOrdersAlt());
                    } catch (RestaurantException e) {
                        logger.log(Level.WARNING, e.getLocalizedMessage());
                    }
                });
    }

    private static void extractOrdersPerTable(int tableNumber)
            throws RestaurantException {
        String fileTable = Settings.getFilenameOrdersAltForTable()
                + tableNumber + ".txt";
        String tableNumberString = String.valueOf(tableNumber);
        if (tableNumber < 10) {
            tableNumberString = " " + tableNumberString;
        }

        AtomicInteger index = new AtomicInteger(0);
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(
                new FileWriter(fileTable)))) {
            writer.println("** Orders for table No. "
                    + tableNumberString + " **\n****");
            writer.flush();
        } catch (IOException e) {
            throw new RestaurantException("Error while writing to file: "
                    + e.getLocalizedMessage());
        }

        Orders.getListOfOrders()
                .stream()
                .filter(order ->
                        order.getTable().getNumber() == tableNumber)
                .forEachOrdered(order -> {
                    try {
                        order.exportOrder(fileTable);
                    } catch (RestaurantException e) {
                        System.err.println(e.getLocalizedMessage());
                    }
                });

        try (PrintWriter writer = new PrintWriter(new BufferedWriter(
                new FileWriter(fileTable, true)))) {
            writer.println("******");
        } catch (IOException e) {
            throw new RestaurantException("Error while writing to file: "
                    + e.getLocalizedMessage());
        }
    }

    private static void importDishesInApp() {
        try {
            Dishes.setListOfDishes(
                    Dishes.importDishes(Settings.getFileDishes()));
        } catch (RestaurantException e) {
            logger.log(Level.WARNING, e.getLocalizedMessage());
        }
    }

    private static void importMenuInApp() {
        try {
            menu.setMenu(
                    Dishes.importDishes(Settings.getFileMenu()));
        } catch (RestaurantException e) {
            logger.log(Level.WARNING, e.getLocalizedMessage());
        }
    }

    private static void importNotesInApp(Table table) {
        try {
            table.importNotes(Settings.getFileNotes());
        } catch (RestaurantException e) {
            logger.log(Level.WARNING, e.getLocalizedMessage());
        }
    }

    private static void importOrdersInApp() {
        try {
            Orders.setListOfOrders(
                    Orders.importOrders(Settings.getFileOrders()));
        } catch (RestaurantException e) {
            logger.log(Level.WARNING, e.getLocalizedMessage());
        }
    }

    private static void printOrdersPerTable(int tableNumber)
            throws RestaurantException {
        File input = new File(Settings.getFilenameOrdersAltForTable()
                + tableNumber + ".txt");
        try (Scanner scanner = new Scanner(input)) {
            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new RestaurantException("File " + e.getLocalizedMessage());
        }
    }

    private static Order registerOrder(Table table, Dish dish, Waiter waiter) {
            Order order = null;
            try {
                order = new Order(table, dish, waiter);
                Orders.addOrder(order);
            } catch (RestaurantException e) {
                logger.log(Level.WARNING, e.getLocalizedMessage());
            }
            return order;
    }

    public static void main(String[] args) {
        importDishesInApp();
        importMenuInApp();
        importOrdersInApp();

        Table table01 = new Table(1);
        Table table02 = new Table(2);
        Table table15 = new Table(15);
        importNotesInApp(table01);
        importNotesInApp(table02);
        importNotesInApp(table15);
        tablesUsed.add(table01);
        tablesUsed.add(table02);
        tablesUsed.add(table15);

        Cook cook1 = new Cook("cook 1");
        Waiter waiter1 = new Waiter("waiter 1");
        Waiter waiter2 = new Waiter("waiter 2");

        if (!new File(Settings.getFileDishes()).exists()) {
            Dish dish01 = new Dish("Fried chicken fillet 150g",
                    BigDecimal.valueOf(160), 20, Category.ENTRÉE);
            Dish dish02 = new Dish("French fries 150g",
                    BigDecimal.valueOf(40), 20,
                    Category.SIDE_DISH);
            Dish dish03 = new Dish("Trout on wine 200g",
                    BigDecimal.valueOf(190), 30, Category.ENTRÉE);
            Dish dish04 = new Dish("Boiled potatoes 200g",
                    BigDecimal.valueOf(35), 15,
                    Category.SIDE_DISH);
            Dish dish05 = new Dish("Coca cola 0.5l",
                    BigDecimal.valueOf(30), 10, Category.DRINK);

            dish05.addImage("coke-image1");
            dish05.addImage("coke-image2");
            try {
                dish05.removeImage("coke-image2");
                dish05.removeImage("coke-image1");
            } catch (RestaurantException e) {
                logger.log(Level.WARNING, e.getLocalizedMessage());
            }
            dish05.addImage("coke-image3");

            Dishes.addDish(dish01);
            Dishes.addDish(dish02);
            Dishes.addDish(dish03);
            Dishes.addDish(dish04);
            Dishes.addDish(dish05);

            if (!new File(Settings.getFileMenu()).exists()) {
                menu.addItem(dish01, cook1);
                menu.addItem(dish03, cook1);
                menu.addItem(dish04, cook1);
                menu.addItem(dish05, cook1);

                if (!new File(Settings.getFileOrders()).exists()) {
                    Order order01 = registerOrder(table02, dish01, waiter1);
                    Order order02 = registerOrder(table15, dish02, waiter2);
                    Order order03 = registerOrder(table15, dish03, waiter2);
                    Order order04 = registerOrder(table15, dish04, waiter2);
                    Order order05 = registerOrder(table15, dish05, waiter2);
                    dish03.setQuantity(2);
                    dish04.setQuantity(2);
                    dish05.setQuantity(2);

                    exportDishesInApp();
                    exportMenuInApp(menu);

                    order04.setFulfilmentTime(
                            order04.getOrderedTime().plusMinutes(3)
                    );
                    order04.setPaid(true);
                    table02.addNote("Good job");
                    table02.addNote("Fine service");

                    if (!new File(Settings.getFileNotes()).exists()) {
                        exportNotesInApp();
                    }
                    exportOrdersInApp();
                    if (!new File(Settings.getFileOrdersAlt()).exists()) {
                        exportOrdersInAppAlt();
                    }
                }
            }
        }

        // Uncomment the following if the files are generated
        // to test additional order
//        Dish dish06 = new Dish("Peach cake 100g",
//                BigDecimal.valueOf(50), 5, Category.DESSERT);
//        Dishes.addDish(dish06);
//        menu.addItem(dish06, cook1);
//        Order order06 = registerOrder(table15, dish06, waiter1);
//        dish06.setQuantity(2);
//        exportDishesInApp();
//        exportMenuInApp(menu);
//        try (PrintWriter writer = new PrintWriter(Settings.getFileNotes())) {
//            writer.close();
//        } catch (IOException e) {
//            logger.log(Level.WARNING, e.getLocalizedMessage());
//        }
//        exportNotesInApp();
//        exportOrdersInApp();
//        try {
//            order06.exportOrder(Settings.getFileOrdersAlt());
//        } catch (RestaurantException e) {
//            logger.log(Level.WARNING, e.getLocalizedMessage());
//        }

        System.out.println("---");
        System.out.println("Number of incomplete orders:");
        System.out.println(Orders.getNumberOfIncompleteOrders());

        System.out.println("---");
        System.out.println("List of incomplete orders:");
        Orders.getListOfIncompleteOrders().forEach(System.out::println);

        System.out.println("---");
        System.out.println("List of orders sorted by waiter:");
        Orders.sortOrdersByWaiter().forEach(System.out::println);

        System.out.println("---");
        System.out.println("List of orders sorted by ordered time:");
        Orders.sortOrdersByOrderedTime().forEach(System.out::println);

        System.out.println("---");
        System.out.println("Total price of orders for each waiter:");
        System.out.println(Orders.getTotalOrderPriceForEachWaiter());

        System.out.println("---");
        System.out.println("Average order processing time in a specified "
                + "time frame:");
        System.out.println(Orders.getAverageOrderProcessingTime(
                LocalDateTime.of(2023, 4, 13, 0, 0), LocalDateTime.now()));

        System.out.println("---");
        System.out.println("List of dishes ordered today:");
        Orders.getListOfDishesOrderedToday().forEach(System.out::println);

        try {
            extractOrdersPerTable(2);
            extractOrdersPerTable(15);

            System.out.println("---");
            printOrdersPerTable(2);
            printOrdersPerTable(15);
        } catch (RestaurantException e) {
            logger.log(Level.WARNING, e.getLocalizedMessage());
        }

        /*
        System.out.println("---");
        System.out.println(table01);
        System.out.println(table02);
        System.out.println(table15);
        System.out.println(waiter1);
        System.out.println(waiter2);
        System.out.println(dish01);
        System.out.println(dish02);
        System.out.println(dish03);
        System.out.println(dish04);
        System.out.println(dish05);
        System.out.println("---");

        System.out.println("Dishes:");
        Dishes.getListOfDishes().forEach(System.out::println);
        System.out.println("---");

        System.out.println("Menu:");
        menu.getMenu().forEach(System.out::println);
        System.out.println("---");

        System.out.println("Valid orders:");
        Orders.getListOfOrders().forEach(System.out::println);
        */
    }
}
