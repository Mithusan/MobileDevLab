package com.example.mobiledevlab;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class Edit extends AppCompatActivity {
    long id;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent i = getIntent();
        id = i.getLongExtra("ID",0);
        DBHandler db = new DBHandler(this);
        Note note = db.getNote(id);

        EditText noteTitle = (EditText) findViewById(R.id.note_title);
        EditText noteDescription = (EditText) findViewById(R.id.note_description);
        Spinner spinnerColour = (Spinner) findViewById(R.id.spinner_colourChange);
        ImageButton returnButton = (ImageButton) findViewById(R.id.returnMainBtn);
        Button saveButton = (Button) findViewById(R.id.save);
        ImageButton deleteButton = (ImageButton) findViewById(R.id.deleteBtn);
        int position = 0;

        String[] arraySpinner = new String[] {
                "Yellow", "Blue", "Orange"
        };

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerColour.setAdapter(adapter);

        for (int j = 0; j < arraySpinner.length; j++) {
            if (arraySpinner[j].equals(note.getColour())) {
                position = j;
                break; // Exit the loop once a match is found
            }
        }

        noteTitle.setText(note.getTitle());
        noteDescription.setText(note.getDescription());
        spinnerColour.setSelection(position);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMain();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = noteTitle.getText().toString();
                String description = noteDescription.getText().toString();
                String colour = (String) spinnerColour.getSelectedItem();

                Note editNote = new Note(id, title, description, colour);
                db.editNote(editNote);

                goToMain();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteNote(id);
                goToMain();
            }
        });
    }

    private void goToMain() {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }
}
