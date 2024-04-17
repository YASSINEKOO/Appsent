package com.example.appsent;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION = 100;
    private static final int REQUEST_CODE_FILE_PICKER = 101;
    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize DatabaseManager instance
        dbManager = new DatabaseManager(this);

        // Request permission to read external storage
        requestPermission();
    }

    // Method to initiate file picker intent
    private void pickFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/plain");  // Set MIME type to allow only text files
        startActivityForResult(intent, REQUEST_CODE_FILE_PICKER);
    }

    private void uploadFile() {
        pickFile(); // Initiate file picker intent
    }

    public void fetchData(View view) {
        // Call the uploadFile() method to fetch data
        uploadFile();
    }

    public void showStudents(View view) {
        Intent intent = new Intent(this, StudentListActivity.class);
        startActivity(intent);
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION);
        } /*else {
            // Permission has already been granted
            uploadFile();
        }*/
    }

    // Override onActivityResult to handle the result of file picker intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FILE_PICKER && resultCode == RESULT_OK) {
            if (data != null) {
                Uri fileUri = data.getData();
                // Now you have the URI of the selected file, you can process it as needed
                try {
                    // Open a InputStream to read the file
                    InputStream inputStream = getContentResolver().openInputStream(fileUri);
                    if (inputStream != null) {
                        // Use Scanner to read the content line by line
                        Scanner scanner = new Scanner(inputStream);
                        while (scanner.hasNextLine()) {
                            // Assuming each line represents student information in the format: id,firstName,lastName,CNE
                            String line = scanner.nextLine();
                            String[] parts = line.split(","); // Split the line by comma
                            // Check if there are enough parts to create a student object
                            if (parts.length >= 4) {
                                // Extract student information
                                int id = Integer.parseInt(parts[0]);
                                String firstName = parts[1];
                                String lastName = parts[2];
                                String cne = parts[3];
                                // Create a new Etudiant object
                                Etudiant etudiant = new Etudiant(id, firstName, lastName, cne);
                                // Add the student to the database
                                dbManager.addEtudiant(etudiant);
                            }
                        }
                        // Close the scanner and inputStream
                        scanner.close();
                        inputStream.close();
                        // Show success message
                        Toast.makeText(this, "Data fetched and inserted successfully", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    // Show failure message
                    Toast.makeText(this, "Failed to read file", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            //if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with fetching data
            //}
       // else {
                // Permission denied, show a message or handle accordingly
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        //    }
        }
    }

    // Method to handle button click to view calendar
    public void viewCalendar(View view) {
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
    }
}