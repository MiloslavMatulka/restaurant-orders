package com.engeto.dish;

import com.engeto.support.RestaurantException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DishTest {

    private static Dish dish;

    @BeforeAll
    static void init() {
    }

    @BeforeEach
    void setUp() {
        dish = new Dish("coke", new BigDecimal(35), 3, Category.DRINK);
    }

    @Test
    @DisplayName("Create dish instance and test initial values")
    @Order(1)
    void testDish() {
        Assertions.assertAll(
                () -> Assertions.assertEquals("coke", dish.getTitle()),
                () -> Assertions.assertEquals(
                        new BigDecimal(35), dish.getPrice()),
                () -> Assertions.assertEquals(3, dish.getPreparationTime()),
                () -> Assertions.assertEquals(
                        Category.DRINK, dish.getCategory()),
                () -> Assertions.assertFalse(dish.isInMenu()),
                () -> Assertions.assertEquals(1, dish.getQuantity()),
                () -> Assertions.assertEquals("blank", dish.getImages().get(0))
        );
    }

    @Test
    @Order(2)
    void testDishSetters() {
        String expectedTitle = "cake";
        dish.setTitle(expectedTitle);
        String actualTitle = dish.getTitle();

        int expectedQuantity = 2;
        dish.setQuantity(expectedQuantity);
        int actualQuantity = dish.getQuantity();

        boolean expectedIsInMenu = true;
        dish.setInMenu(expectedIsInMenu);
        boolean actualIsInMenu = dish.isInMenu();

        Category expectedCategory = Category.DESSERT;
        dish.setCategory(expectedCategory);
        Category actualCategory = dish.getCategory();

        BigDecimal expectedPrice = new BigDecimal(60);
        dish.setPrice(expectedPrice);
        BigDecimal actualPrice = dish.getPrice();

        int expectedPreparationTime = 5;
        dish.setPreparationTime(expectedPreparationTime);
        int actualPreparationTime = dish.getPreparationTime();

        String expectedImage = "cake-image1";
        List<String> images = new ArrayList<>();
        images.add(expectedImage);
        dish.setImages(images);
        String actualImage = dish.getImages().get(0);

        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedTitle, actualTitle),
                () -> Assertions.assertEquals(expectedPrice, actualPrice),
                () -> Assertions.assertEquals(expectedPreparationTime,
                        actualPreparationTime),
                () -> Assertions.assertEquals(expectedCategory,
                        actualCategory),
                () -> Assertions.assertEquals(expectedIsInMenu,
                        actualIsInMenu),
                () -> Assertions.assertEquals(expectedQuantity,
                        actualQuantity),
                () -> Assertions.assertEquals(expectedImage, actualImage)
        );
    }

    @Test
    @Order(3)
    void testAddImage() {
        String expectedImage = "test-image";
        dish.addImage(expectedImage);
        String actualImage = dish.getImages().get(0);
        int expectedSize = 1;
        int actualSize = dish.getImages().size();

        Assertions.assertAll(
                () -> Assertions.assertEquals(
                        expectedImage, actualImage),
                () -> Assertions.assertEquals(
                        expectedSize, actualSize)
        );
    }

    @Test
    @Order(5)
    @Disabled
    void testImagesListSize() {
        int expectedSize = 1;
        int actualSize = dish.getImages().size();
        Assertions.assertEquals(expectedSize, actualSize);

        dish.addImage("test-image1");
        // Value should still be: expectedSize = 1;
        actualSize = dish.getImages().size();
        Assertions.assertEquals(expectedSize, actualSize);

        dish.addImage("test-image2");
        expectedSize = 2;
        actualSize = dish.getImages().size();
        Assertions.assertEquals(expectedSize, actualSize);
    }

    @Test
    @Order(4)
    void testRemoveImageFailure() {
        String generatedImage = dish.getImages().get(0);

        Assertions.assertThrows(RestaurantException.class,
                () -> dish.removeImage(generatedImage));
    }
}
