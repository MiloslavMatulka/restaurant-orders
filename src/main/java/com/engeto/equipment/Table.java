package com.engeto.equipment;

import com.engeto.support.RestaurantException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Table extends Equipment {

    private List<String> notes = new ArrayList<>();

    public Table(int tableNumber) {
        super(tableNumber);
    }

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

    public void addNote(String note) {
        notes.add(note);
    }

    public void exportNotes(String file) throws RestaurantException {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(
                new FileWriter(file, true)))) {
            getNotes().forEach(note -> writer.println(getNumber()
                    + ";" + note));
        } catch (IOException e) {
            throw new RestaurantException("Error while writing to file: "
                    + e. getLocalizedMessage());
        }
    }

    public void importNotes(String file) throws RestaurantException {
        File input = new File(file);
        try (Scanner scanner = new Scanner(input)) {
            while (scanner.hasNextLine()) {
                String record = scanner.nextLine();
                String[] items = record.split(";");
                int tableNumber = Integer.parseInt(items[0]);
                if (getNumber() == tableNumber) {
                    notes.add(items[1]);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RestaurantException("File " + e.getLocalizedMessage());
        } catch (NumberFormatException e) {
            throw new RestaurantException("Incorrect number format "
                    + e.getLocalizedMessage());
        }
    }

    public void clearNotes() {
        notes.clear();
    }

    @Override
    public String toString() {
        return super.toString() + " (table ID)";
    }
}
