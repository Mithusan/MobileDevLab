package com.example.mobiledevlab;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_Name = "notesDb";
    private static final int DB_Version = 1;

    public DBHandler(Context context) {
        super(context, DB_Name, null, DB_Version);
    }

    private static final String DB_Table = "myNotes";
    private static final String Col_ID = "id";
    private static final String Col_Title = "title";
    private static final String col_Description = "description";
    private static final String col_Colour = "colour";

    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        String query = "CREATE TABLE " + DB_Table + " ("
                + Col_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Col_Title + " TEXT, "
                + col_Description + " TEXT, "
                + col_Colour + " TEXT " +
                ")";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + DB_Table);
        onCreate(db);
    }

    public void addNewNote(String noteTitle, String noteDescription, String noteColour) {
        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(Col_Title, noteTitle);
        values.put(col_Description, noteDescription);
        values.put(col_Colour, noteColour);


        // after adding all values we are passing
        // content values to our table.
        db.insert(DB_Table, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }

    public List<Note> getNotes(){
        List<Note> notes = new ArrayList<>();
        String query = "SELECT * FROM " + DB_Table +" ORDER BY "+ Col_ID +" DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                Note note = new Note();
                note.setId(Long.parseLong(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setDescription(cursor.getString(2));
                note.setColour(cursor.getString(3));
                notes.add(note);
            }while (cursor.moveToNext());
        }
        return notes;
    }
}