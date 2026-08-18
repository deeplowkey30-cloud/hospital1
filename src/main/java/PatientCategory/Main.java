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
        System.out.print("Patient ID: "); String id = scanner.nextLine().trim();
        System.out.print("First name: "); String fn = scanner.nextLine().trim();
        System.out.print("Last name: "); String ln = scanner.nextLine().trim();
        System.out.print("Age: "); int age = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Gender: "); String gender = scanner.nextLine().trim();
        System.out.print("Medical condition: "); String cond = scanner.nextLine().trim();
        System.out.print("Category (INPATIENT/OUTPATIENT/EMERGENCY): "); 
        PatientCategory cat = PatientCategory.valueOf(scanner.nextLine().trim().toUpperCase());
        Patient p = new Patient(id, fn, ln, age, gender, cond, cat);
        service.registerPatient(p);
        System.out.println("Patient registered.");
    }

    private static void searchPatient() {
        System.out.print("Enter Patient ID: ");
        String id = scanner.nextLine().trim();
        Patient p = service.findPatientById(id);
        if (p == null) System.out.println("Patient not found.");
        else System.out.println(p.displayDetails());
    }

    private static void updatePatient() {
        System.out.print("Enter Patient ID to update: ");
        String id = scanner.nextLine().trim();
        Patient existing = service.findPatientById(id);
        if (existing == null) { System.out.println("Patient not found."); return; }
        System.out.print("First name (" + existing.getFirstName() + "): "); String fn = scanner.nextLine().trim();
        if (fn.isEmpty()) fn = existing.getFirstName();
        System.out.print("Last name (" + existing.getLastName() + "): "); String ln = scanner.nextLine().trim();
        if (ln.isEmpty()) ln = existing.getLastName();
        System.out.print("Age (" + existing.getAge() + "): "); String ageStr = scanner.nextLine().trim();
        int age = ageStr.isEmpty() ? existing.getAge() : Integer.parseInt(ageStr);
        System.out.print("Gender (" + existing.getGender() + "): "); String gender = scanner.nextLine().trim();
        if (gender.isEmpty()) gender = existing.getGender();
        System.out.print("Medical condition (" + existing.getMedicalCondition() + "): "); String cond = scanner.nextLine().trim();
        if (cond.isEmpty()) cond = existing.getMedicalCondition();
        System.out.print("Category (" + existing.getCategory() + "): "); String catStr = scanner.nextLine().trim();
        PatientCategory cat = catStr.isEmpty() ? existing.getCategory() : PatientCategory.valueOf(catStr.toUpperCase());
        Patient updated = new Patient(id, fn, ln, age, gender, cond, cat);
        service.updatePatient(id, updated);
        System.out.println("Patient updated.");
    }

    private static void deletePatient() {
        System.out.print("Enter Patient ID to delete: ");
        String id = scanner.nextLine().trim();
        service.deletePatient(id);
        System.out.println("Patient deleted.");
    }

    private static void displayAllPatients() {
        List<Patient> list = service.getAllPatients();
        if (list.isEmpty()) { System.out.println("No patients registered."); return; }
        for (Patient p : list) System.out.println(p.displayDetails());
    }

    private static void allocateBed() {
        System.out.print("Enter Patient ID to allocate bed: ");
        String pid = scanner.nextLine().trim();
        System.out.print("Enter Bed ID (e.g., B01) or press Enter to auto-assign: ");
        String bid = scanner.nextLine().trim();
        if (bid.isEmpty()) {
            // auto assign first available
            List<hospital.model.Bed> avail = service.getAvailableBeds();
            if (avail.isEmpty()) { System.out.println("No beds available."); return; }
            bid = avail.get(0).getBedId();
        }
        service.allocateBed(pid, bid);
        System.out.println("Bed " + bid + " allocated to " + pid);
    }

    private static void releaseBed() {
        System.out.print("Enter Bed ID to release: ");
        String bid = scanner.nextLine().trim();
        service.releaseBed(bid);
        System.out.println("Bed " + bid + " released.");
    }

    private static void displayWardLayout() {
        service.getWard().displayLayout();
    }

    private static void displayAvailableBeds() {
        List<hospital.model.Bed> list = service.getAvailableBeds();
        if (list.isEmpty()) System.out.println("No available beds.");
        else list.forEach(b -> System.out.println(b));
    }

    private static void displayOccupiedBeds() {
        List<hospital.model.Bed> list = service.getOccupiedBeds();
        if (list.isEmpty()) System.out.println("No occupied beds.");
        else list.forEach(b -> System.out.println(b));
    }

    private static void reports() {
        System.out.println("Total registered patients: " + service.totalRegisteredPatients());
        System.out.println("Total occupied beds: " + service.totalOccupiedBeds());
        System.out.printf("Ward occupancy: %.2f%%\n", service.wardOccupancyPercentage());
        System.out.println("All patients:");
        displayAllPatients();
    }
}
