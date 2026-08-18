/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospital.model;

import PatientCategory.PatientCategory;

public class Inpatient extends Patient {
    private int wardNumber;
    private String bedId; // e.g., B01

    public Inpatient(String patientId, String firstName, String lastName, int age,
                     String gender, String medicalCondition, PatientCategory category,
                     int wardNumber, String bedId) {
        super(patientId, firstName, lastName, age, gender, medicalCondition, category);
        if (wardNumber <= 0) {
            throw new IllegalArgumentException("Ward number must be positive");
        }
        if (bedId == null || bedId.isEmpty()) {
            throw new IllegalArgumentException("Bed ID cannot be null or empty");
        }
        this.wardNumber = wardNumber;
        this.bedId = bedId;
    }

    public int getWardNumber() { return wardNumber; }
    public void setWardNumber(int wardNumber) { 
        if (wardNumber <= 0) {
            throw new IllegalArgumentException("Ward number must be positive");
        }
        this.wardNumber = wardNumber; 
    }

    public String getBedId() { return bedId; }
    public void setBedId(String bedId) { 
        if (bedId != null && bedId.isEmpty()) {
            throw new IllegalArgumentException("Bed ID cannot be empty if provided");
        }
        this.bedId = bedId; 
    }

    @Override
    public String displayDetails() {
        return super.displayDetails() + String.format(" | Ward: %d | Bed: %s", wardNumber, bedId != null ? bedId : "None");
    }
}
