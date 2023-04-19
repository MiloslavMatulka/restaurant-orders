package com.engeto.order;

import com.engeto.dish.Category;
import com.engeto.dish.Dish;
import com.engeto.equipment.Table;
import com.engeto.support.RestaurantException;
import com.engeto.worker.Waiter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class Orders {

    private static List<Order> listOfOrders = new ArrayList<>();

    private Orders() {}

    public static List<Order> getListOfOrders() {
        return new ArrayList<>(listOfOrders);
    }

    public static void setListOfOrders(List<Order> orders) {
        listOfOrders = orders;
    }

    public static void exportOrders(List<Order> listOfOrders, String file)
            throws RestaurantException {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(
                new FileWriter(file)))) {
            listOfOrders.forEach(order ->
                    writer.println(order.getId() + ";"
                            + order.getTable().getNumber() + ";"
                            + order.getWaiter().getName() + ";"
                            + order.getWaiter().getId() + ";"
                            + order.isPaid() + ";"
                            + order.getOrderedTime() + ";"
                            + order.getFulfilmentTime() + ";"
                            + order.getDish().getTitle() + ";"
                            + order.getDish().getPrice() + ";"
                            + order.getDish().getQuantity() + ";"
                            + order.getDish().getPreparationTime() + ";"
                            + order.getDish().getCategory() + ";"
                            + String.join(",", order.getDish().getImages())));
        } catch (IOException e) {
            throw new RestaurantException("Error while writing to file: "
                    + e. getLocalizedMessage());
        }
    }

    public static void exportOrdersAlt(List<Order> listOfOrders, String file)
            throws RestaurantException {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(
                new FileWriter(file)))) {
            listOfOrders.forEach(order -> {
                    String time;
                    if (order.getFulfilmentTime() == null) {
                        time = order.getOrderedTime().getHour() + ":"
                                + order.getOrderedTime().getMinute() + "-0";
                    } else {
                        time = order.getOrderedTime().getHour() + ":"
                                + order.getOrderedTime().getMinute() + "-"
                                + order.getFulfilmentTime().getHour() + ":"
                                + order.getFulfilmentTime().getMinute();
                    }
                    writer.println(order.getId() + ". "
                            + order.getDish().getTitle() + " "
                            + order.getDish().getQuantity() + "x ("
                            + order.getDish().getPrice().multiply(BigDecimal
                                    .valueOf(order.getDish().getQuantity()))
                            + " CZK):\t"
                            + time
                            + "\twaiter No. " + order.getWaiter().getId());
                    });
        } catch (IOException e) {
            throw new RestaurantException("Error while writing to file: "
                    + e. getLocalizedMessage());
        }
    }

    public static double getAverageOrderProcessingTime(
            LocalDateTime from, LocalDateTime to) {
     return listOfOrders
             .stream()
             .filter(order -> order.getFulfilmentTime() != null)
             .filter(order -> order.getOrderedTime().isAfter(from)
                     && order.getOrderedTime().isBefore(to))
             .map(order -> Duration.between(order.getOrderedTime(),
                     order.getFulfilmentTime()))
             .map(Duration::toMinutes)
             .collect(Collectors.averagingDouble(l ->
                     Double.parseDouble(l.toString())));
    }

    public static List<Dish> getListOfDishesOrderedToday() {
        return listOfOrders
                .stream()
                .filter(order -> order.getOrderedTime().toLocalDate()
                        .equals(LocalDateTime.now().toLocalDate()))
                .map(Order::getDish)
                .toList();
    }

    public static List<Order> getListOfIncompleteOrders() {
        return listOfOrders
                .stream()
                .filter(order -> order.getFulfilmentTime() == null)
                .toList();
    }

    public static long getNumberOfIncompleteOrders() {
        return listOfOrders
                .stream()
                .filter(order -> order.getFulfilmentTime() == null)
                .count();
    }

    public static Map<String, BigDecimal> getTotalOrderPriceForEachWaiter() {
        return listOfOrders
                .stream()
                .collect(Collectors.groupingBy(order ->
                                order.getWaiter().getName(),
                        Collectors.mapping(Order::getPriceOfOrder,
                                Collectors.reducing(BigDecimal.ZERO,
                                        BigDecimal::add))));
    }

    public static List<Order> importOrders(String file)
            throws RestaurantException {
        List<Order> orders = new ArrayList<>();
        File input = new File(file);
        long line = 0L;
        try (Scanner scanner = new Scanner(input)) {
            while (scanner.hasNextLine()) {
                String record = scanner.nextLine();
                orders.add(parseOrder(record));
            }
        } catch (FileNotFoundException e) {
            throw new RestaurantException("File " + e.getLocalizedMessage());
        } catch (RestaurantException e) {
            throw new RestaurantException(e.getLocalizedMessage()
                    + ", line " + line);
        }
        return orders;
    }

    public static void addOrder(Order order) {
        listOfOrders.add(order);

    }

    public static Order parseOrder(String data)
            throws RestaurantException, NumberFormatException,
            DateTimeParseException {
        String[] items = data.split(";");

        Category category;
        String dishTitle = items[7];
        long id;
        LocalDateTime fulfilmentTime = null;
        boolean isPaid = Boolean.parseBoolean(items[4]);
        String nameOfWaiter = items[2];
        LocalDateTime orderedTime;
        int preparationTime;
        BigDecimal price;
        int quantity;
        int tableNumber;
        int waiterId;
        try {
            id = Long.parseLong(items[0]);
            tableNumber = Integer.parseInt(items[1]);
            waiterId = Integer.parseInt(items[3]);
            orderedTime = LocalDateTime.parse(items[5]);
            if (!items[6].equals("null")) {
                fulfilmentTime = LocalDateTime.parse(items[6]);
            }
            price = new BigDecimal(items[8]);
            quantity = Integer.parseInt(items[9]);
            preparationTime = Integer.parseInt(items[10]);
            category = Category.valueOf(items[11]);

        } catch (NumberFormatException e) {
            throw new RestaurantException("Parsing error, incorrect number "
                    + "format: "
                    + e.getLocalizedMessage());
        } catch (DateTimeParseException e) {
            throw new RestaurantException("Parsing error, incorrect date/time "
                    + "format: " + e.getLocalizedMessage());
        } catch (IllegalArgumentException e) {
            throw new RestaurantException("Parsing error, incorrect category: "
                    + e.getLocalizedMessage());
        }
        String imgString = items[12];
        String[] imgStrings = imgString.split(",");
        List<String> images = Arrays.asList(imgStrings);

        Dish dish = new Dish(dishTitle, price, preparationTime,
                category);
        dish.setQuantity(quantity);
        dish.setImages(images);
        Order order = new Order(id, orderedTime, new Table(tableNumber), dish,
                new Waiter(waiterId, nameOfWaiter));
        order.setOrderedTime(orderedTime);
        if (fulfilmentTime != null) {
            order.setFulfilmentTime(fulfilmentTime);
        }
        order.setPaid(isPaid);
        return order;
    }

    public static List<Order> sortOrdersByOrderedTime() {
        return listOfOrders
                .stream()
                .sorted(Comparator.comparing(Order::getOrderedTime))
                /* The same in a different way
                .sorted((order1, order2) -> order1.getOrderedTime()
                        .compareTo(order2.getOrderedTime())) */
                .toList();
    }

    public static List<Order> sortOrdersByWaiter() {
        return listOfOrders
                .stream()
                .sorted(Comparator.comparingLong(
                        order -> order.getWaiter().getId()))
                .toList();
    }


}
