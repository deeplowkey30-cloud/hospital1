/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package hospital.service;

import PatientCategory.PatientCategory;
import hospital.model.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class HospitalServiceTest {
    
    private HospitalService service;
    
    @Before
    public void setUp() {
        service = new HospitalService();
    }
    
    // ==================== CRUD Operation Tests ====================
    
    /**
     * Test 1: Register a new patient successfully
     */
    @Test
    public void testRegisterPatient() {
        Patient patient = new Patient("P001", "John", "Doe", 30, "Male", "Flu", PatientCategory.OUTPATIENT);
        service.registerPatient(patient);
        
        Patient found = service.findPatientById("P001");
        assertNotNull("Patient should be registered", found);
        assertEquals("Patient ID should match", "P001", found.getPatientId());
        assertEquals("Patient name should match", "John", found.getFirstName());
    }
    
    /**
     * Test 2: Prevent duplicate Patient IDs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicatePatientID() {
        Patient patient1 = new Patient("P001", "John", "Doe", 30, "Male", "Flu", PatientCategory.OUTPATIENT);
        service.registerPatient(patient1);
        
        Patient patient2 = new Patient("P001", "Jane", "Smith", 25, "Female", "Cold", PatientCategory.OUTPATIENT);
        service.registerPatient(patient2); // Should throw IllegalArgumentException
    }
    
    /**
     * Test 3: Search for a patient by ID
     */
    @Test
    public void testSearchPatientById() {
        Patient patient = new Patient("P002", "Jane", "Smith", 25, "Female", "Migraine", PatientCategory.INPATIENT);
        service.registerPatient(patient);
        
        Patient found = service.findPatientById("P002");
        assertNotNull("Patient should be found", found);
        assertEquals("Patient name should match", "Jane", found.getFirstName());
    }
    
    /**
     * Test 4: Search for non-existent patient returns null
     */
    @Test
    public void testSearchNonExistentPatient() {
        Patient found = service.findPatientById("P999");
        assertNull("Non-existent patient should return null", found);
    }
    
    /**
     * Test 5: Update patient details
     */
    @Test
    public void testUpdatePatient() {
        Patient patient = new Patient("P003", "Robert", "Johnson", 45, "Male", "Diabetes", PatientCategory.INPATIENT);
        service.registerPatient(patient);
        
        Patient updated = new Patient("P003", "Robert", "Johnson", 46, "Male", "Type 2 Diabetes", PatientCategory.INPATIENT);
        service.updatePatient("P003", updated);
        
        Patient found = service.findPatientById("P003");
        assertEquals("Age should be updated", 46, found.getAge());
        assertEquals("Condition should be updated", "Type 2 Diabetes", found.getMedicalCondition());
    }
    
    /**
     * Test 6: Update non-existent patient throws exception
     */
    @Test(expected = java.util.NoSuchElementException.class)
    public void testUpdateNonExistentPatient() {
        Patient updated = new Patient("P999", "Test", "Patient", 25, "Male", "Test", PatientCategory.OUTPATIENT);
        service.updatePatient("P999", updated);
    }
    
    /**
     * Test 7: Delete a patient
     */
    @Test
    public void testDeletePatient() {
        Patient patient = new Patient("P004", "Alice", "Brown", 35, "Female", "Asthma", PatientCategory.OUTPATIENT);
        service.registerPatient(patient);
        
        service.deletePatient("P004");
        Patient found = service.findPatientById("P004");
        assertNull("Patient should be deleted", found);
    }
    
    /**
     * Test 8: Delete non-existent patient throws exception
     */
    @Test(expected = java.util.NoSuchElementException.class)
    public void testDeleteNonExistentPatient() {
        service.deletePatient("P999");
    }
    
    /**
     * Test 9: Get all patients
     */
    @Test
    public void testGetAllPatients() {
        Patient p1 = new Patient("P005", "Tom", "Wilson", 50, "Male", "Heart Disease", PatientCategory.INPATIENT);
        Patient p2 = new Patient("P006", "Lisa", "Anderson", 28, "Female", "Broken Arm", PatientCategory.EMERGENCY);
        
        service.registerPatient(p1);
        service.registerPatient(p2);
        
        List<Patient> all = service.getAllPatients();
        assertEquals("Should have 2 patients", 2, all.size());
    }
    
    // ==================== Bed Management Tests ====================
    
    /**
     * Test 10: Allocate bed to inpatient
     */
    @Test
    public void testAllocateBedToInpatient() {
        Patient patient = new Patient("P007", "Michael", "Davis", 42, "Male", "Pneumonia", PatientCategory.INPATIENT);
        service.registerPatient(patient);
        
        service.allocateBed("P007", "B01");
        
        Patient found = service.findPatientById("P007");
        assertTrue("Patient should be Inpatient", found instanceof Inpatient);
        assertEquals("Bed should be B01", "B01", ((Inpatient)found).getBedId());
    }
    
    /**
     * Test 11: Cannot allocate bed to outpatient
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAllocateBedToOutpatient() {
        Patient patient = new Patient("P008", "Sarah", "Miller", 30, "Female", "Check-up", PatientCategory.OUTPATIENT);
        service.registerPatient(patient);
        
        service.allocateBed("P008", "B01"); // Should throw exception
    }
    
    /**
     * Test 12: Cannot allocate occupied bed
     */
    @Test(expected = IllegalStateException.class)
    public void testAllocateOccupiedBed() {
        Patient p1 = new Patient("P009", "Chris", "Taylor", 55, "Male", "Surgery", PatientCategory.INPATIENT);
        Patient p2 = new Patient("P010", "Emily", "Thomas", 40, "Female", "Recovery", PatientCategory.INPATIENT);
        
        service.registerPatient(p1);
        service.registerPatient(p2);
        
        service.allocateBed("P009", "B01");
        service.allocateBed("P010", "B01"); // Should throw exception - bed already occupied
    }
    
    /**
     * Test 13: Release bed successfully
     */
    @Test
    public void testReleaseBed() {
        Patient patient = new Patient("P011", "David", "Martinez", 50, "Male", "Recovery", PatientCategory.INPATIENT);
        service.registerPatient(patient);
        service.allocateBed("P011", "B02");
        
        service.releaseBed("B02");
        
        List<Bed> available = service.getAvailableBeds();
        assertTrue("Bed B02 should be available", available.stream().anyMatch(b -> b.getBedId().equals("B02")));
    }
    
    /**
     * Test 14: Prevent bed allocation when all beds are occupied
     */
    @Test(expected = IllegalStateException.class)
    public void testAllocateBedWhenAllOccupied() {
        // Create 20 patients and allocate all 20 beds
        for (int i = 0; i < 20; i++) {
            Patient patient = new Patient("P" + String.format("%03d", i), "Patient", String.valueOf(i), 25 + i, "Male", "Condition", PatientCategory.INPATIENT);
            service.registerPatient(patient);
            service.allocateBed("P" + String.format("%03d", i), "B" + String.format("%02d", i + 1));
        }
        
        // Try to allocate 21st patient - should fail because no beds available
        Patient patient21 = new Patient("P020", "Last", "Patient", 45, "Female", "Condition", PatientCategory.INPATIENT);
        service.registerPatient(patient21);
        
        // This should throw IllegalStateException because all beds are occupied
        List<Bed> available = service.getAvailableBeds();
        assertEquals("No beds should be available", 0, available.size());
        
        // Try to allocate to an available bed (there are none)
        service.allocateBed("P020", "B01"); // Should throw exception - bed already occupied
    }
    
    /**
     * Test 15: Get available beds
     */
    @Test
    public void testGetAvailableBeds() {
        Patient patient = new Patient("P021", "Kevin", "Jackson", 35, "Male", "Treatment", PatientCategory.INPATIENT);
        service.registerPatient(patient);
        service.allocateBed("P021", "B03");
        
        List<Bed> available = service.getAvailableBeds();
        assertEquals("Should have 19 available beds (20-1 occupied)", 19, available.size());
    }
    
    /**
     * Test 16: Get occupied beds
     */
    @Test
    public void testGetOccupiedBeds() {
        Patient p1 = new Patient("P022", "Mark", "White", 50, "Male", "Surgery", PatientCategory.INPATIENT);
        Patient p2 = new Patient("P023", "Nancy", "Harris", 45, "Female", "Recovery", PatientCategory.INPATIENT);
        
        service.registerPatient(p1);
        service.registerPatient(p2);
        
        service.allocateBed("P022", "B04");
        service.allocateBed("P023", "B05");
        
        List<Bed> occupied = service.getOccupiedBeds();
        assertEquals("Should have 2 occupied beds", 2, occupied.size());
    }
    
    // ==================== Validation and Boundary Tests ====================
    
    /**
     * Test 17: Total registered patients count
     */
    @Test
    public void testTotalRegisteredPatients() {
        Patient p1 = new Patient("P024", "Frank", "Green", 60, "Male", "Monitoring", PatientCategory.INPATIENT);
        Patient p2 = new Patient("P025", "Grace", "King", 55, "Female", "Check-up", PatientCategory.OUTPATIENT);
        
        service.registerPatient(p1);
        service.registerPatient(p2);
        
        int total = service.totalRegisteredPatients();
        assertEquals("Should have 2 registered patients", 2, total);
    }
    
    /**
     * Test 18: Total occupied beds count
     */
    @Test
    public void testTotalOccupiedBeds() {
        Patient p1 = new Patient("P026", "Henry", "Wright", 65, "Male", "Intensive Care", PatientCategory.INPATIENT);
        service.registerPatient(p1);
        service.allocateBed("P026", "B06");
        
        int occupied = service.totalOccupiedBeds();
        assertEquals("Should have 1 occupied bed", 1, occupied);
    }
    
    /**
     * Test 19: Ward occupancy percentage calculation
     */
    @Test
    public void testWardOccupancyPercentage() {
        Patient p1 = new Patient("P027", "Iris", "Lopez", 40, "Female", "Treatment", PatientCategory.INPATIENT);
        Patient p2 = new Patient("P028", "Jack", "Hill", 45, "Male", "Recovery", PatientCategory.INPATIENT);
        
        service.registerPatient(p1);
        service.registerPatient(p2);
        
        service.allocateBed("P027", "B07");
        service.allocateBed("P028", "B08");
        
        double occupancy = service.wardOccupancyPercentage();
        assertEquals("Occupancy should be 10%", 10.0, occupancy, 0.1);
    }
    
    /**
     * Test 20: Sort patients by surname
     */
    @Test
    public void testSortPatientsBySurname() {
        Patient p1 = new Patient("P029", "Zoe", "Adams", 30, "Female", "Condition", PatientCategory.OUTPATIENT);
        Patient p2 = new Patient("P030", "Tom", "Brown", 35, "Male", "Condition", PatientCategory.OUTPATIENT);
        Patient p3 = new Patient("P031", "Sarah", "Collins", 40, "Female", "Condition", PatientCategory.OUTPATIENT);
        
        service.registerPatient(p1);
        service.registerPatient(p2);
        service.registerPatient(p3);
        
        List<Patient> sorted = service.sortBySurname();
        assertEquals("First sorted should be Adams", "Adams", sorted.get(0).getLastName());
        assertEquals("Second sorted should be Brown", "Brown", sorted.get(1).getLastName());
        assertEquals("Third sorted should be Collins", "Collins", sorted.get(2).getLastName());
    }
    
    /**
     * Test 21: Sort patients by Patient ID
     */
    @Test
    public void testSortPatientsByID() {
        Patient p1 = new Patient("P100", "Alice", "Johnson", 30, "Female", "Condition", PatientCategory.OUTPATIENT);
        Patient p2 = new Patient("P050", "Bob", "Smith", 35, "Male", "Condition", PatientCategory.OUTPATIENT);
        Patient p3 = new Patient("P075", "Carol", "Williams", 40, "Female", "Condition", PatientCategory.OUTPATIENT);
        
        service.registerPatient(p1);
        service.registerPatient(p2);
        service.registerPatient(p3);
        
        List<Patient> sorted = service.sortByPatientId();
        assertEquals("First sorted should be P050", "P050", sorted.get(0).getPatientId());
        assertEquals("Second sorted should be P075", "P075", sorted.get(1).getPatientId());
        assertEquals("Third sorted should be P100", "P100", sorted.get(2).getPatientId());
    }
    
    /**
     * Test 22: Inpatient displays ward and bed information
     */
    @Test
    public void testInpatientDisplayDetails() {
        Patient patient = new Patient("P032", "Richard", "Scott", 50, "Male", "Treatment", PatientCategory.INPATIENT);
        service.registerPatient(patient);
        service.allocateBed("P032", "B09");
        
        Patient found = service.findPatientById("P032");
        assertTrue("Should be instance of Inpatient", found instanceof Inpatient);
        
        String details = found.displayDetails();
        assertTrue("Should contain ward information", details.contains("Ward"));
        assertTrue("Should contain bed information", details.contains("B09"));
    }
    
    /**
     * Test 23: Release bed from inpatient
     */
    @Test
    public void testReleaseBedFromInpatient() {
        Patient patient = new Patient("P033", "Patricia", "Green", 38, "Female", "Recovery", PatientCategory.INPATIENT);
        service.registerPatient(patient);
        service.allocateBed("P033", "B10");
        
        service.releaseBed("B10");
        
        Patient found = service.findPatientById("P033");
        assertTrue("Should still be Inpatient", found instanceof Inpatient);
        assertNull("Bed ID should be null after release", ((Inpatient)found).getBedId());
    }
    
    /**
     * Test 24: Cannot release unoccupied bed
     */
    @Test(expected = IllegalStateException.class)
    public void testReleaseUnoccupiedBed() {
        service.releaseBed("B20"); // Bed 20 is not occupied
    }
    
    /**
     * Test 25: Ward contains exactly 20 beds
     */
    @Test
    public void testWardHas20Beds() {
        List<Bed> allBeds = service.getWard().getAllBeds();
        assertEquals("Ward should have exactly 20 beds", 20, allBeds.size());
    }
}
