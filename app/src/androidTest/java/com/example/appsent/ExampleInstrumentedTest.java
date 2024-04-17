package com.example.appsent;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private DatabaseManager dbManager;


    @Before
    public void setUp() {
        // Initialize DatabaseManager instance
        Context context = ApplicationProvider.getApplicationContext();
        dbManager = new DatabaseManager(context);
    }

    @Test
    public void testDatabaseOperations() {
        // Test adding an Etudiant to the database
        Etudiant etudiant1 = new Etudiant(1, "John", "Doe", "12345");
        dbManager.addEtudiant(etudiant1);

        // Test getting an Etudiant by ID
        Etudiant retrievedEtudiant1 = dbManager.getEtudiant(1);
        assertEquals(etudiant1.getNom(), retrievedEtudiant1.getNom());
        assertEquals(etudiant1.getPrenom(), retrievedEtudiant1.getPrenom());
        assertEquals(etudiant1.getCNE(), retrievedEtudiant1.getCNE());

        // Test getting all Etudiants from the database
        List<Etudiant> allEtudiants = dbManager.getAllEtudiants();
        assertEquals(1, allEtudiants.size());

        // Test updating an Etudiant
        Etudiant updatedEtudiant = new Etudiant(1, "Jane", "Smith", "54321");
        dbManager.updateEtudiant(1,updatedEtudiant);
        Etudiant retrievedUpdatedEtudiant = dbManager.getEtudiant(1);
        assertEquals(updatedEtudiant.getNom(), retrievedUpdatedEtudiant.getNom());
        assertEquals(updatedEtudiant.getPrenom(), retrievedUpdatedEtudiant.getPrenom());
        assertEquals(updatedEtudiant.getCNE(), retrievedUpdatedEtudiant.getCNE());

        // Test deleting an Etudiant
        boolean deleted = dbManager.deleteEtudiant(1);
        assertTrue(deleted);
        assertNull(dbManager.getEtudiant(1));
    }

    @Test
    public void testAbsencesOperations() {
        // Create a sample Etudiant object
        Etudiant etudiant1 = new Etudiant(1, "John", "Doe", "12345");

        // Generate an example date for marking the absence
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.APRIL, 16); // Example date: April 16, 2024
        Date exampleDate = calendar.getTime();

        // Add Etudiant to the database
        dbManager.addEtudiant(etudiant1);

        // Test marking an absence
        dbManager.markAbsent(etudiant1,exampleDate);

        // Test getting absences for the Etudiant
        List<Date> absences = dbManager.getAbsencesForStudent(etudiant1);
        assertEquals(1, absences.size());

        // Test unmarking an absence
        Date lastAbsence = absences.get(0);
        dbManager.unmarkAbsent(etudiant1, lastAbsence);

        // Verify the absence is removed
        List<Date> absencesAfterUnmark = dbManager.getAbsencesForStudent(etudiant1);
        assertEquals(0, absencesAfterUnmark.size());
    }

    @Test
    public void testIsStudentAbsentOnDate() {
        // Create a sample Etudiant object
        Etudiant etudiant1 = new Etudiant(1, "John", "Doe", "12345");

        // Add Etudiant to the database
        dbManager.addEtudiant(etudiant1);

        // Choose a specific date for testing
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.APRIL, 16); // Set the desired date
        Date testDate = calendar.getTime();

        // Initially, the student should not be absent on this date
        assertFalse(dbManager.isStudentAbsentOnDate(etudiant1, testDate));

        // Mark the student absent on the test date
        dbManager.markAbsent(etudiant1, testDate);

        // Now, the student should be absent on this date
        assertTrue(dbManager.isStudentAbsentOnDate(etudiant1, testDate));

        // Unmark the student absent on the test date
        dbManager.unmarkAbsent(etudiant1, testDate);

        // After unmarking, the student should not be absent on this date anymore
        assertFalse(dbManager.isStudentAbsentOnDate(etudiant1, testDate));
    }

}