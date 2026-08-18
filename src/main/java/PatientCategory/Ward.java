/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospital.service;

import hospital.model.Bed;

import java.util.ArrayList;
import java.util.List;

public class Ward {
    private final Bed[][] beds; // 4 rows x 5 cols
    private final int rows = 4;
    private final int cols = 5;

    public Ward() {
        beds = new Bed[rows][cols];
        int counter = 1;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                String id = String.format("B%02d", counter++);
                beds[r][c] = new Bed(id);
            }
        }
    }

    public Bed getBedById(String bedId) {
        for (Bed[] row : beds) {
            for (Bed b : row) {
                if (b.getBedId().equalsIgnoreCase(bedId)) return b;
            }
        }
        return null;
    }

    public List<Bed> getAllBeds() {
        List<Bed> list = new ArrayList<>();
        for (Bed[] row : beds) for (Bed b : row) list.add(b);
        return list;
    }

    public List<Bed> getAvailableBeds() {
        List<Bed> list = new ArrayList<>();
        for (Bed b : getAllBeds()) if (!b.isOccupied()) list.add(b);
        return list;
    }

    public List<Bed> getOccupiedBeds() {
        List<Bed> list = new ArrayList<>();
        for (Bed b : getAllBeds()) if (b.isOccupied()) list.add(b);
        return list;
    }

    public void displayLayout() {
        for (int r = 0; r < rows; r++) {
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < cols; c++) {
                Bed b = beds[r][c];
                sb.append(String.format("%-12s", b.isOccupied() ? b.getBedId() + "*": b.getBedId()));
            }
            System.out.println(sb.toString());
        }
        System.out.println("* indicates occupied");
    }

    public int totalBeds() { return rows * cols; }
    public int occupiedCount() { return getOccupiedBeds().size(); }
    public double occupancyPercentage() {
        return (occupiedCount() * 100.0) / totalBeds();
    }
}

