package com.meetme.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	 
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "sessionManager";
 
    private static final String TABLE_SESSION = "session";
 
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_Session_TABLE = "CREATE TABLE " + TABLE_SESSION + "("
                + KEY_EMAIL + " TEXT PRIMARY KEY," + KEY_PASSWORD + " TEXT)";
        db.execSQL(CREATE_Session_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSION);
        onCreate(db);
    }
    
    // CRUD methods
    public void addSession(Session session) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	 
        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, session.getEmail()); 
        values.put(KEY_PASSWORD, session.getPassword()); 
     
        db.insert(TABLE_SESSION, null, values);
        db.close(); 
    }
     
    /**
     * Take the first session
     * @return the Session if found or null if no session
     */
    public Session getSession() {
    	SQLiteDatabase db = this.getReadableDatabase();
    	Session session = null;
    	
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SESSION, null);
        
        if (cursor.moveToFirst()) {
            session = new Session(
        			cursor.getString(0),
        			cursor.getString(1)
    			);
        } 
        
        cursor.close();
        
        return session;
    }
     
    public int updateSession(Session session) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	 
        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, session.getEmail());
        values.put(KEY_PASSWORD, session.getPassword());
     
        return db.update(TABLE_SESSION, values, KEY_EMAIL + " = ?",
                new String[] { session.getEmail() });
    }
     
    public void deleteSession(Session session) {
    	SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SESSION, KEY_EMAIL + " = ?",
                new String[] { session.getEmail() });
        db.close();
    }
}
