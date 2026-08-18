/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospital.service;

import PatientCategory.PatientCategory;
import hospital.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class HospitalService {
    private final List<Patient> patients = new ArrayList<>();
    private final Ward ward = new Ward();
    private final int wardNumber = 1;

    // Patient CRUD
    public void registerPatient(Patient p) {
        if (p == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }
        if (findPatientById(p.getPatientId()) != null) {
            throw new IllegalArgumentException("Duplicate Patient ID: " + p.getPatientId());
        }
        patients.add(p);
    }

    public Patient findPatientById(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Patient ID cannot be null or empty");
        }
        for (Patient p : patients) if (p.getPatientId().equalsIgnoreCase(id)) return p;
        return null;
    }

    public void updatePatient(String id, Patient updated) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Patient ID cannot be null or empty");
        }
        if (updated == null) {
            throw new IllegalArgumentException("Updated patient cannot be null");
        }
        Patient existing = findPatientById(id);
        if (existing == null) throw new NoSuchElementException("Patient not found: " + id);
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setAge(updated.getAge());
        existing.setGender(updated.getGender());
        existing.setMedicalCondition(updated.getMedicalCondition());
        existing.setCategory(updated.getCategory());
    }

    public void deletePatient(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Patient ID cannot be null or empty");
        }
        Patient p = findPatientById(id);
        if (p == null) throw new NoSuchElementException("Patient not found: " + id);
        
        // If inpatient occupying a bed, release it
        if (p instanceof Inpatient) {
            Inpatient ip = (Inpatient) p;
            if (ip.getBedId() != null) {
                Bed bed = ward.getBedById(ip.getBedId());
                if (bed != null && bed.isOccupied()) bed.release();
            }
        }
        patients.remove(p);
    }

    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients);
    }

    // Sorting
    public List<Patient> sortBySurname() {
        return patients.stream()
                .sorted(Comparator.comparing(Patient::getLastName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public List<Patient> sortByPatientId() {
        return patients.stream()
                .sorted(Comparator.comparing(Patient::getPatientId, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    // Bed allocation
    public void allocateBed(String patientId, String bedId) {
        if (patientId == null || patientId.isEmpty()) {
            throw new IllegalArgumentException("Patient ID cannot be null or empty");
        }
        if (bedId == null || bedId.isEmpty()) {
            throw new IllegalArgumentException("Bed ID cannot be null or empty");
        }
        
        Patient p = findPatientById(patientId);
        if (p == null) throw new NoSuchElementException("Patient not found: " + patientId);
        if (p.getCategory() != PatientCategory.INPATIENT) {
            throw new IllegalArgumentException("Only inpatients may be allocated a bed. Patient category: " + p.getCategory());
        }
        Bed bed = ward.getBedById(bedId);
        if (bed == null) throw new NoSuchElementException("Bed not found: " + bedId);
        if (bed.isOccupied()) throw new IllegalStateException("Bed already occupied: " + bedId);
        
        // assign
        bed.assignTo(patientId);
        
        // update patient to Inpatient if not already
        if (p instanceof Inpatient) {
            Inpatient ip = (Inpatient) p;
            ip.setBedId(bedId);
            ip.setWardNumber(wardNumber);
        } else {
            // replace Patient with Inpatient
            Inpatient newIp = new Inpatient(p.getPatientId(), p.getFirstName(), p.getLastName(),
                    p.getAge(), p.getGender(), p.getMedicalCondition(), p.getCategory(),
                    wardNumber, bedId);
            // replace in list
            patients.remove(p);
            patients.add(newIp);
        }
    }

    public void releaseBed(String bedId) {
        if (bedId == null || bedId.isEmpty()) {
            throw new IllegalArgumentException("Bed ID cannot be null or empty");
        }
        Bed bed = ward.getBedById(bedId);
        if (bed == null) throw new NoSuchElementException("Bed not found: " + bedId);
        if (!bed.isOccupied()) throw new IllegalStateException("Bed is not occupied: " + bedId);
        
        String pid = bed.getPatientId();
        bed.release();
        
        Patient p = findPatientById(pid);
        if (p instanceof Inpatient) {
            Inpatient ip = (Inpatient) p;
            ip.setBedId(null);
        }
    }

    // Reports
    public List<Bed> getAvailableBeds() { return ward.getAvailableBeds(); }
    public List<Bed> getOccupiedBeds() { return ward.getOccupiedBeds(); }
    public int totalRegisteredPatients() { return patients.size(); }
    public int totalOccupiedBeds() { return ward.occupiedCount(); }
    public double wardOccupancyPercentage() { return ward.occupancyPercentage(); }
    public Ward getWard() { return ward; }
}
