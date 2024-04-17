package com.example.appsent;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class CalendarActivity extends AppCompatActivity {

    private TextView textCurrentDay;
    private DatabaseManager databaseManager;
    private SimpleDateFormat dateFormat;
    private Calendar calendarInstance ;
    private LinearLayout containerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity);

        databaseManager = new DatabaseManager(this);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        textCurrentDay = findViewById(R.id.text_current_day);

        containerLayout = findViewById(R.id.container_layout); // Assuming you have a LinearLayout with this id
        updateCurrentDay();

        displayStudents();
    }

    // Method to update the text with the current day
    private void updateCurrentDay() {
        calendarInstance = Calendar.getInstance();
        int year = calendarInstance.get(Calendar.YEAR);
        int month = calendarInstance.get(Calendar.MONTH) + 1; // Month starts from 0
        int dayOfMonth = calendarInstance.get(Calendar.DAY_OF_MONTH);
        String currentDay = String.format("%d/%d/%d", dayOfMonth, month, year);
        textCurrentDay.setText(currentDay);
    }

    // Method to display students in the list view
    private void displayStudents() {
        containerLayout.removeAllViews();
        // Get list of students from the database
        List<Etudiant> students = databaseManager.getAllEtudiants();
        Date selectedDate=calendarInstance.getTime();

        // Iterate through the list of students
        for (Etudiant student : students) {
            // Create a checkbox for each student
            CheckBox checkBox = new CheckBox(this);
            String fullname = student.getNom() + student.getPrenom();
            checkBox.setText(fullname); // Assuming you have a method to get the student's name
            checkBox.setTag(student.getId()); // Assuming you have a method to get the student's ID

            boolean isAbsent = databaseManager.isStudentAbsentOnDate(student, selectedDate);
            checkBox.setChecked(isAbsent);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                    @Override
                                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                        if (isChecked) {
                                                            databaseManager.markAbsent(student, selectedDate);
                                                            Toast.makeText(CalendarActivity.this, "Student marked as absent", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            databaseManager.unmarkAbsent(student, selectedDate);
                                                            Toast.makeText(CalendarActivity.this, "Student marked as present", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
            });
            // Add the checkbox to the LinearLayout
            containerLayout.addView(checkBox);
        }
    }


    //      List<String> studentNames = new ArrayList<>();

//        for (Etudiant student : students) {
//            studentNames.add(student.getNom());
//        }
//
//        // Create a custom adapter to handle checkboxes
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                this,
//                R.layout.list_item_custom, // Custom layout with checkbox
//                R.id.text, // ID of the TextView in the layout
//                studentNames
//        );
//
//        studentListView.setAdapter(adapter);
//
//        // Check the item if student is absent on the selected date
//        for (int i = 0; i < students.size(); i++) {
//            final int position = i;
//            final Etudiant student = students.get(i);
//            boolean isAbsent = databaseManager.isStudentAbsentOnDate(student, selectedDate);
//            View itemView = getViewByPosition(i, studentListView);
//            CheckBox checkbox = itemView.findViewById(R.id.checkbox);
//            checkbox.setChecked(isAbsent);
//
//            // Set listener for checkbox changes
//            // Set listener for checkbox changes
//            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (checkbox != null) {
//                        Log.d("Checkbox", "Checkbox state changed for student: " + student.getNom() + ", isChecked: " + isChecked);
//                        // Update database when checkbox is checked or unchecked
//                        if (isChecked) {
//                            // Mark student absent
//                            databaseManager.markAbsent(student, selectedDate);
//                            Toast.makeText(CalendarActivity.this, "Student marked as absent", Toast.LENGTH_SHORT).show();
//                        } else {
//                            // Unmark student absent
//                            databaseManager.unmarkAbsent(student, selectedDate);
//                            Toast.makeText(CalendarActivity.this, "Student marked as present", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Log.d("Checkbox", "Checkbox is null");
//                    }
//                }
//            });
//
//        }

    // Method to show the date picker dialog
    public void showCalendarDialog(View view) {
        int year = calendarInstance.get(Calendar.YEAR);
        int month = calendarInstance.get(Calendar.MONTH);
        int dayOfMonth = calendarInstance.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (datePicker, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                    calendarInstance.set(selectedYear, selectedMonth, selectedDayOfMonth); // Update calendarInstance
                    String selectedDate = String.format("%d/%d/%d", selectedDayOfMonth, selectedMonth + 1, selectedYear);
                    textCurrentDay.setText(selectedDate);
                    // Refresh student list when a new date is selected
                    displayStudents();
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }}