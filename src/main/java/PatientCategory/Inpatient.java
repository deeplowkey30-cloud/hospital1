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
        this.wardNumber = wardNumber;
        this.bedId = bedId;
    }

    public int getWardNumber() { return wardNumber; }
    public void setWardNumber(int wardNumber) { this.wardNumber = wardNumber; }

    public String getBedId() { return bedId; }
    public void setBedId(String bedId) { this.bedId = bedId; }

    @Override
    public String displayDetails() {
        return super.displayDetails() + String.format(" | Ward: %d | Bed: %s", wardNumber, bedId);
    }
}
