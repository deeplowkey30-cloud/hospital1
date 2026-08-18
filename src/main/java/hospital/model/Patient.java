/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospital.model;

import PatientCategory.PatientCategory;

public class Patient {
    private String patientId;
    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private String medicalCondition;
    private PatientCategory category;

    public Patient(String patientId, String firstName, String lastName, int age,
                   String gender, String medicalCondition, PatientCategory category) {
        if (patientId == null || patientId.isEmpty()) {
            throw new IllegalArgumentException("Patient ID cannot be null or empty");
        }
        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        if (lastName == null || lastName.isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Age must be between 0 and 150");
        }
        if (gender == null || gender.isEmpty()) {
            throw new IllegalArgumentException("Gender cannot be null or empty");
        }
        if (medicalCondition == null || medicalCondition.isEmpty()) {
            throw new IllegalArgumentException("Medical condition cannot be null or empty");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.medicalCondition = medicalCondition;
        this.category = category;
    }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { 
        if (patientId == null || patientId.isEmpty()) {
            throw new IllegalArgumentException("Patient ID cannot be null or empty");
        }
        this.patientId = patientId; 
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { 
        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        this.firstName = firstName; 
    }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { 
        if (lastName == null || lastName.isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        this.lastName = lastName; 
    }

    public int getAge() { return age; }
    public void setAge(int age) { 
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Age must be between 0 and 150");
        }
        this.age = age; 
    }

    public String getGender() { return gender; }
    public void setGender(String gender) { 
        if (gender == null || gender.isEmpty()) {
            throw new IllegalArgumentException("Gender cannot be null or empty");
        }
        this.gender = gender; 
    }

    public String getMedicalCondition() { return medicalCondition; }
    public void setMedicalCondition(String medicalCondition) { 
        if (medicalCondition == null || medicalCondition.isEmpty()) {
            throw new IllegalArgumentException("Medical condition cannot be null or empty");
        }
        this.medicalCondition = medicalCondition; 
    }

    public PatientCategory getCategory() { return category; }
    public void setCategory(PatientCategory category) { 
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        this.category = category; 
    }

    public String displayDetails() {
        return String.format("ID: %s | Name: %s %s | Age: %d | Gender: %s | Condition: %s | Category: %s",
                patientId, firstName, lastName, age, gender, medicalCondition, category);
    }
}
