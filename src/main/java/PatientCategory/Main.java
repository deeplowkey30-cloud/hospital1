/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package PatientCategory;

import PatientCategory.PatientCategory;
import hospital.model.*;
import hospital.service.HospitalService;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final HospitalService service = new HospitalService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1": registerPatient(); break;
                    case "2": searchPatient(); break;
                    case "3": updatePatient(); break;
                    case "4": deletePatient(); break;
                    case "5": displayAllPatients(); break;
                    case "6": allocateBed(); break;
                    case "7": releaseBed(); break;
                    case "8": displayWardLayout(); break;
                    case "9": displayAvailableBeds(); break;
                    case "10": displayOccupiedBeds(); break;
                    case "11": reports(); break;
                    case "0": running = false; break;
                    default: System.out.println("Invalid option.");
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
            System.out.println();
        }
        System.out.println("Exiting system. Goodbye.");
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("=== MediCare Ward Admission System ===");
        System.out.println("1. Register new patient");
        System.out.println("2. Search patient by ID");
        System.out.println("3. Update patient details");
        System.out.println("4. Delete patient");
        System.out.println("5. Display all patients");
        System.out.println("6. Allocate bed to inpatient");
        System.out.println("7. Release bed");
        System.out.println("8. Display ward layout");
        System.out.println("9. Display available beds");
        System.out.println("10. Display occupied beds");
        System.out.println("11. Reports");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private static void registerPatient() {
        try {
            System.out.print("Patient ID: "); 
            String id = scanner.nextLine().trim();
            if (id.isEmpty()) {
                System.out.println("Patient ID cannot be empty.");
                return;
            }
            
            System.out.print("First name: "); 
            String fn = scanner.nextLine().trim();
            if (fn.isEmpty()) {
                System.out.println("First name cannot be empty.");
                return;
            }
            
            System.out.print("Last name: "); 
            String ln = scanner.nextLine().trim();
            if (ln.isEmpty()) {
                System.out.println("Last name cannot be empty.");
                return;
            }
            
            System.out.print("Age: ");
            int age;
            try {
                age = Integer.parseInt(scanner.nextLine().trim());
                if (age < 0 || age > 150) {
                    System.out.println("Invalid age. Please enter a value between 0 and 150.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid age. Please enter a valid number.");
                return;
            }
            
            System.out.print("Gender: "); 
            String gender = scanner.nextLine().trim();
            if (gender.isEmpty()) {
                System.out.println("Gender cannot be empty.");
                return;
            }
            
            System.out.print("Medical condition: "); 
            String cond = scanner.nextLine().trim();
            if (cond.isEmpty()) {
                System.out.println("Medical condition cannot be empty.");
                return;
            }
            
            System.out.print("Category (INPATIENT/OUTPATIENT/EMERGENCY): ");
            PatientCategory cat;
            try {
                String catInput = scanner.nextLine().trim().toUpperCase();
                if (catInput.isEmpty()) {
                    System.out.println("Category cannot be empty.");
                    return;
                }
                cat = PatientCategory.valueOf(catInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid category. Please use INPATIENT, OUTPATIENT, or EMERGENCY.");
                return;
            }
            
            Patient p = new Patient(id, fn, ln, age, gender, cond, cat);
            service.registerPatient(p);
            System.out.println("Patient registered successfully.");
        } catch (Exception e) {
            System.out.println("Error registering patient: " + e.getMessage());
        }
    }

    private static void searchPatient() {
        System.out.print("Enter Patient ID: ");
        String id = scanner.nextLine().trim();
        if (id.isEmpty()) {
            System.out.println("Patient ID cannot be empty.");
            return;
        }
        Patient p = service.findPatientById(id);
        if (p == null) System.out.println("Patient not found.");
        else System.out.println(p.displayDetails());
    }

    private static void updatePatient() {
        try {
            System.out.print("Enter Patient ID to update: ");
            String id = scanner.nextLine().trim();
            if (id.isEmpty()) {
                System.out.println("Patient ID cannot be empty.");
                return;
            }
            
            Patient existing = service.findPatientById(id);
            if (existing == null) { 
                System.out.println("Patient not found."); 
                return; 
            }
            
            System.out.print("First name (" + existing.getFirstName() + "): "); 
            String fn = scanner.nextLine().trim();
            if (fn.isEmpty()) fn = existing.getFirstName();
            
            System.out.print("Last name (" + existing.getLastName() + "): "); 
            String ln = scanner.nextLine().trim();
            if (ln.isEmpty()) ln = existing.getLastName();
            
            System.out.print("Age (" + existing.getAge() + "): ");
            String ageStr = scanner.nextLine().trim();
            int age;
            if (ageStr.isEmpty()) {
                age = existing.getAge();
            } else {
                try {
                    age = Integer.parseInt(ageStr);
                    if (age < 0 || age > 150) {
                        System.out.println("Invalid age. Please enter a value between 0 and 150.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid age. Please enter a valid number.");
                    return;
                }
            }
            
            System.out.print("Gender (" + existing.getGender() + "): "); 
            String gender = scanner.nextLine().trim();
            if (gender.isEmpty()) gender = existing.getGender();
            
            System.out.print("Medical condition (" + existing.getMedicalCondition() + "): "); 
            String cond = scanner.nextLine().trim();
            if (cond.isEmpty()) cond = existing.getMedicalCondition();
            
            System.out.print("Category (" + existing.getCategory() + "): ");
            String catStr = scanner.nextLine().trim();
            PatientCategory cat;
            if (catStr.isEmpty()) {
                cat = existing.getCategory();
            } else {
                try {
                    cat = PatientCategory.valueOf(catStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid category. Please use INPATIENT, OUTPATIENT, or EMERGENCY.");
                    return;
                }
            }
            
            Patient updated = new Patient(id, fn, ln, age, gender, cond, cat);
            service.updatePatient(id, updated);
            System.out.println("Patient updated successfully.");
        } catch (Exception e) {
            System.out.println("Error updating patient: " + e.getMessage());
        }
    }

    private static void deletePatient() {
        try {
            System.out.print("Enter Patient ID to delete: ");
            String id = scanner.nextLine().trim();
            if (id.isEmpty()) {
                System.out.println("Patient ID cannot be empty.");
                return;
            }
            service.deletePatient(id);
            System.out.println("Patient deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error deleting patient: " + e.getMessage());
        }
    }

    private static void displayAllPatients() {
        List<Patient> list = service.getAllPatients();
        if (list.isEmpty()) { 
            System.out.println("No patients registered."); 
            return; 
        }
        for (Patient p : list) System.out.println(p.displayDetails());
    }

    private static void allocateBed() {
        try {
            System.out.print("Enter Patient ID to allocate bed: ");
            String pid = scanner.nextLine().trim();
            if (pid.isEmpty()) {
                System.out.println("Patient ID cannot be empty.");
                return;
            }
            
            System.out.print("Enter Bed ID (e.g., B01) or press Enter to auto-assign: ");
            String bid = scanner.nextLine().trim();
            if (bid.isEmpty()) {
                List<Bed> avail = service.getAvailableBeds();
                if (avail.isEmpty()) { 
                    System.out.println("No beds available."); 
                    return; 
                }
                bid = avail.get(0).getBedId();
                System.out.println("Auto-assigning bed: " + bid);
            }
            service.allocateBed(pid, bid);
            System.out.println("Bed " + bid + " allocated to patient " + pid);
        } catch (Exception e) {
            System.out.println("Error allocating bed: " + e.getMessage());
        }
    }

    private static void releaseBed() {
        try {
            System.out.print("Enter Bed ID to release: ");
            String bid = scanner.nextLine().trim();
            if (bid.isEmpty()) {
                System.out.println("Bed ID cannot be empty.");
                return;
            }
            service.releaseBed(bid);
            System.out.println("Bed " + bid + " released successfully.");
        } catch (Exception e) {
            System.out.println("Error releasing bed: " + e.getMessage());
        }
    }

    private static void displayWardLayout() {
        try {
            service.getWard().displayLayout();
        } catch (Exception e) {
            System.out.println("Error displaying ward layout: " + e.getMessage());
        }
    }

    private static void displayAvailableBeds() {
        try {
            List<Bed> list = service.getAvailableBeds();
            if (list.isEmpty()) System.out.println("No available beds.");
            else {
                System.out.println("Available beds:");
                list.forEach(b -> System.out.println("  " + b));
            }
        } catch (Exception e) {
            System.out.println("Error displaying available beds: " + e.getMessage());
        }
    }

    private static void displayOccupiedBeds() {
        try {
            List<Bed> list = service.getOccupiedBeds();
            if (list.isEmpty()) System.out.println("No occupied beds.");
            else {
                System.out.println("Occupied beds:");
                list.forEach(b -> System.out.println("  " + b));
            }
        } catch (Exception e) {
            System.out.println("Error displaying occupied beds: " + e.getMessage());
        }
    }

    private static void reports() {
        try {
            System.out.println("\n========== WARD REPORTS ==========");
            System.out.println("Total registered patients: " + service.totalRegisteredPatients());
            System.out.println("Total occupied beds: " + service.totalOccupiedBeds());
            System.out.printf("Ward occupancy: %.2f%%\n", service.wardOccupancyPercentage());
            System.out.println("\nAll registered patients:");
            displayAllPatients();
            System.out.println("==================================\n");
        } catch (Exception e) {
            System.out.println("Error generating reports: " + e.getMessage());
        }
    }
}
