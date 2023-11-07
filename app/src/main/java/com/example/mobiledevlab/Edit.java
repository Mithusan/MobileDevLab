package com.example.mobiledevlab;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;

public class Edit extends AppCompatActivity {
    long id;
    ArrayAdapter<String> adapter;
    Boolean validBit = false;
    ImageView notePhoto;
    Bitmap pBit = null;

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

        ImageButton returnButton = (ImageButton) findViewById(R.id.returnMainBtn);
        ImageButton deleteButton = (ImageButton) findViewById(R.id.deleteBtn);

        Spinner spinnerColour = (Spinner) findViewById(R.id.spinner_colourChange);
        Button uploadButton = (Button) findViewById(R.id.uploadBtn);
        Button captureButton = (Button) findViewById(R.id.captureBtn);
        Button saveButton = (Button) findViewById(R.id.save);

        notePhoto = (ImageView) findViewById(R.id.notePhoto);
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

        String photoDetails = note.getPhoto();

        if(!photoDetails.equals("empty")){
            pBit = photoUtility.StringToBitMap(photoDetails);
            notePhoto.setImageBitmap(pBit);
        }

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMain();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });

        if(ContextCompat.checkSelfPermission(Edit.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(Edit.this, new String[]{
                        Manifest.permission.CAMERA
                }, 100);
        }


        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = noteTitle.getText().toString();
                String description = noteDescription.getText().toString();
                String colour = (String) spinnerColour.getSelectedItem();
                String photo = "";

                if (validBit) {
                    photo = photoUtility.BitMapToString(pBit);
                }else{
                    photo = photoDetails;
                }

                Note editNote = new Note(id, title, description, colour, photo);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            pBit = bitmap;
            validBit = true;
            notePhoto.setImageBitmap(bitmap);
        }
        if(requestCode == 3) {
            if (resultCode == RESULT_OK && data != null) {
                Uri selectedImage = data.getData();
                try {
                    pBit = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                validBit = true;
                notePhoto.setImageURI(selectedImage);
            }
        }
    }


}
