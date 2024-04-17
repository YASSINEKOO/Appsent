package com.example.appsent;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class StudentListActivity extends AppCompatActivity {

    List<Etudiant> students;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        // Get the list of students from the database
        List<Etudiant> students = getStudentsFromDatabase();

        // Display the list of students in a table
        displayStudents(students);
    }
    private List<Etudiant> getStudentsFromDatabase() {
        students = new ArrayList<>();

        // Assuming you have a method in your DatabaseManager class to get all students
        DatabaseManager dbManager = new DatabaseManager(getApplicationContext());
        students = dbManager.getAllEtudiants();

        return students;
    }
    private void displayStudents(List<Etudiant> students) {
        TableLayout tableLayout = findViewById(R.id.tableLayout);
        // Remove all rows except the first one (assuming it's the header)
        tableLayout.removeViews(1, tableLayout.getChildCount() - 1);

        for (final Etudiant student : students) {
            TableRow row = new TableRow(this);

            TextView tvId = createTextView(String.valueOf(student.getId()));
            TextView tvFirstName = createTextView(student.getPrenom());
            TextView tvLastName = createTextView(student.getNom());
            TextView tvCNE = createTextView(student.getCNE());
            //  TODO : holding

            Button removeButton = new Button(this);
            removeButton.setText("❌");
            removeButton.setBackgroundColor(Color.TRANSPARENT);
            removeButton.setPadding(0, 0, 0, 0);
            removeButton.setOnClickListener((view) -> removeStudent(student));

            row.addView(tvId);
            row.addView(tvFirstName);
            row.addView(tvLastName);
            row.addView(tvCNE);
            row.addView(removeButton);

            tableLayout.addView(row);
        }
        TableRow newRow = new TableRow(this);
        TableRow newRow2 = new TableRow(this);
        EditText etId = createEditText();
        EditText etFirstName = createEditText();
        EditText etLastName = createEditText();
        EditText etCNE = createEditText();

        Button confirmButton = new Button(this);

        Button addButton = new Button(this);
        addButton.setText("➕");
        addButton.setBackgroundColor(Color.TRANSPARENT);
        addButton.setOnClickListener((view) -> {
            // Show the input fields
            etId.setVisibility(View.VISIBLE);
            etFirstName.setVisibility(View.VISIBLE);
            etLastName.setVisibility(View.VISIBLE);
            etCNE.setVisibility(View.VISIBLE);
            confirmButton.setVisibility(View.VISIBLE); // Show the confirm button
            addButton.setVisibility(View.GONE); // Hide the add button
        });
        newRow2.addView(addButton,0);
        tableLayout.addView(newRow2);

        confirmButton.setText("✅");
        confirmButton.setBackgroundColor(Color.TRANSPARENT);
        confirmButton.setVisibility(View.GONE); // Initially hidden
        confirmButton.setOnClickListener((view) -> {
            String id = etId.getText().toString();
            String firstName = etFirstName.getText().toString();
            String lastName = etLastName.getText().toString();
            String cne = etCNE.getText().toString();
            if (!id.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty() && !cne.isEmpty()) {
                try {
                    int studentId = Integer.parseInt(id);
                    // Create a new student object with the entered details
                    Etudiant newStudent = new Etudiant(studentId, firstName, lastName, cne);
                    // Add the student to the database
                    addStudent(newStudent);
                    // Refresh the student list and redraw the table
                    List<Etudiant> updatedStudents = getStudentsFromDatabase();
                    displayStudents(updatedStudents);
                    // Hide the input fields after adding the student
                    etId.setVisibility(View.GONE);
                    etFirstName.setVisibility(View.GONE);
                    etLastName.setVisibility(View.GONE);
                    etCNE.setVisibility(View.GONE);
                    confirmButton.setVisibility(View.GONE);
                    addButton.setVisibility(View.VISIBLE); // Show the add button again
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Please enter a valid ID", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
            }
        });

        // Hide the input fields and confirmButton after adding student
        etId.setVisibility(View.GONE);
        etFirstName.setVisibility(View.GONE);
        etLastName.setVisibility(View.GONE);
        etCNE.setVisibility(View.GONE);
        confirmButton.setVisibility(View.GONE);

        addButton.setVisibility(View.VISIBLE);

        newRow.addView(etId);
        newRow.addView(etFirstName);
        newRow.addView(etLastName);
        newRow.addView(etCNE);
        newRow.addView(confirmButton);

        tableLayout.addView(newRow);

    }
    private EditText createEditText() {
        EditText editText = new EditText(this);
        editText.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        editText.setText("");
        return editText;

    }
    private void addStudent(Etudiant student) {
        DatabaseManager dbManager = new DatabaseManager(getApplicationContext());
        dbManager.addEtudiant(student);


    }
    private void removeStudent(Etudiant student) {
        // Initialize the DatabaseManager
        DatabaseManager dbManager = new DatabaseManager(getApplicationContext());

        // Attempt to remove the student from the database
        boolean removed = dbManager.deleteEtudiant(student.getId());

        // Check if the student was successfully removed
        if (removed) {
            List<Etudiant> updatedStudents = getStudentsFromDatabase();
            displayStudents(updatedStudents);

            // Show a toast message indicating successful removal
            Toast.makeText(this, "Student removed successfully", Toast.LENGTH_SHORT).show();
        } else {
            // Handle the case where student removal failed
            Toast.makeText(this, "Failed to remove student", Toast.LENGTH_SHORT).show();
        }
    }
    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(16, 16, 16, 16);
        return textView;
    }
}