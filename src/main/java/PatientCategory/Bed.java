/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospital.model;

public class Bed {
    private final String bedId; // B01..B20
    private boolean occupied;
    private String patientId; // ID of occupying inpatient

    public Bed(String bedId) {
        this.bedId = bedId;
        this.occupied = false;
        this.patientId = null;
    }

    public String getBedId() { return bedId; }
    public boolean isOccupied() { return occupied; }
    public String getPatientId() { return patientId; }

    public void assignTo(String patientId) {
        if (occupied) throw new IllegalStateException("Bed already occupied");
        this.patientId = patientId;
        this.occupied = true;
    }

    public void release() {
        this.patientId = null;
        this.occupied = false;
    }

    @Override
    public String toString() {
        return bedId + (occupied ? " (Occupied by " + patientId + ")" : " (Available)");
    }
}
