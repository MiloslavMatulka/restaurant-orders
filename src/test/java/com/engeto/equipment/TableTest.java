package com.engeto.equipment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
public class TableTest {
    @Test
    void testTable() {
        int expectedTableNumber = 5;
        Table table = new Table(expectedTableNumber);
        int actualTableNumber = table.getNumber();
        Assertions.assertEquals(expectedTableNumber, actualTableNumber);
    }
}
