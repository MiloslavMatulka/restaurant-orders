package com.engeto.support;

import java.nio.file.FileSystems;

public class Settings {

    private static final String SEPARATOR =
            FileSystems.getDefault().getSeparator();
    private static final String RESOURCES =
            "src" + SEPARATOR + "main" + SEPARATOR + "resources" + SEPARATOR;
    private static final String FILE_DISHES = RESOURCES + "dishes.csv";
    private static final String FILE_MENU = RESOURCES + "menu.csv";
    private static final String FILE_NOTES = RESOURCES + "notes.csv";
    private static final String FILE_ORDERS = RESOURCES + "orders.csv";
    private static final String FILE_ORDERS_ALT =
            RESOURCES + "orders_alt.txt";
    private static final String FILENAME_ORDERS_ALT_FOR_TABLE =
            RESOURCES + "orders_for_table";
    private static final String EXT_PNG = "png";
    private static final String IMAGE = "blank";
    private static final String URL = "https://server/path/";
    private static final String IMAGE_URL =
            getUrl() + getImage() + "." + getExtPng();

    public static String getExtPng() {
        return EXT_PNG;
    }

    public static String getFileDishes() {
        return FILE_DISHES;
    }

    public static String getFileMenu() {
        return FILE_MENU;
    }

    public static String getFileNotes() {
        return FILE_NOTES;
    }

    public static String getFileOrders() {
        return FILE_ORDERS;
    }

    public static String getFileOrdersAlt() {
        return FILE_ORDERS_ALT;
    }

    public static String getFilenameOrdersAltForTable() {
        return FILENAME_ORDERS_ALT_FOR_TABLE;
    }

    public static String getImage() {
        return IMAGE;
    }

    public static String getImageUrl() {
        return IMAGE_URL;
    }

    public static String getUrl() {
        return URL;
    }
}
