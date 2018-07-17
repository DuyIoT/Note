package assignment.rekkeitrainning.com.note.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import assignment.rekkeitrainning.com.note.model.Note;

/**
 * Created by hoang on 7/17/2018.
 */

public class DBNote extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "notes_db";

    public static final String TABLE_NAME = "notes";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_ALARAMDATE = "alaramdate";
    public static final String COLUMN_ALARAMTIME = "alaramtime";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_TITLE + " TEXT, "
                    + COLUMN_CONTENT + " TEXT, "
                    + COLUMN_IMAGE + " TEXT, "
                    + COLUMN_DATE + " DATETIME DEFAULT CURRENT_DATE, "
                    + COLUMN_TIME + " DATETIME DEFAULT CURRENT_TIME, "
                    + COLUMN_ALARAMDATE + " DATETIME DEFAULT CURRENT_DATE, "
                    + COLUMN_ALARAMTIME + " DATETIME DEFAULT CURRENT_TIME"
                    + ")";
    public DBNote(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
    public long insertNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_CONTENT, note.getContent());
        values.put(COLUMN_DATE, note.getDate());
        values.put(COLUMN_TIME, note.getTime());
        values.put(COLUMN_IMAGE, note.getImage());
        values.put(COLUMN_ALARAMDATE, note.getAlaramDate());
        values.put(COLUMN_ALARAMTIME, note.getAlaramTime());
        long id = db.insert(TABLE_NAME, null, values);

        db.close();
        return id;
    }
    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                note.setContent(cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT)));
                note.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)));
                note.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                note.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_TIME)));
                note.setAlaramDate(cursor.getString(cursor.getColumnIndex(COLUMN_ALARAMDATE)));
                note.setAlaramTime(cursor.getString(cursor.getColumnIndex(COLUMN_ALARAMTIME)));
                notes.add(note);
            } while (cursor.moveToNext());
        }
        db.close();
        return notes;
    }
    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        return count;
    }
    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_CONTENT, note.getContent());
        values.put(COLUMN_DATE, note.getDate());
        values.put(COLUMN_TIME, note.getTime());
        values.put(COLUMN_IMAGE, note.getImage());
        values.put(COLUMN_ALARAMDATE, note.getAlaramDate());
        values.put(COLUMN_ALARAMTIME, note.getAlaramTime());

        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }
    public void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }
}
