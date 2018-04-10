package com.cienciacomputacao.osqr.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cienciacomputacao.osqr.model.Register;

import java.util.ArrayList;
import java.util.List;

public class RegistersDB extends SQLiteOpenHelper {

    private static final String DB_NAME = "com.ciencia.computacao.DATABASE";
    private static final int VERSION = 1;
    private static final String TABLE = "register";

    public RegistersDB(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE + "(_id integer primary key autoincrement, client text, created_at DATETIME DEFAULT CURRENT_TIMESTAMP, service text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void save(Register register) {
        int id = register.id;
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("client", register.getClient());
            values.put("service", register.getJsonEncryptedClientService());
            values.put("created_at", register.getDateTime());

            if (id != 0) {
                String _id = String.valueOf(register.id);
                db.update(TABLE, values, "_id = " + _id, null);
            } else {
                db.insert(TABLE, "", values);
            }

        } finally {
            db.close();
        }
    }

    public int delete(Register register) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            return db.delete(TABLE, "_id = " + register.id, null);
        } finally {
            db.close();
        }
    }

    public List<Register> findAll() {
        SQLiteDatabase db = getWritableDatabase();
        try {
            Cursor c = db.query(TABLE, null, null, null, null, null, null);
            List<Register> list = new ArrayList<>();
            if (c.moveToFirst()) {
                do {
                    Register register = new Register();
                    register.setId(c.getInt(c.getColumnIndex("_id")));
                    register.setClient(c.getString(c.getColumnIndex("client")));
                    register.setDateTime(c.getString(c.getColumnIndex("created_at")));
                    register.setJsonEncryptedClientService(c.getString(c.getColumnIndex("service")));
                    list.add(register);
                } while (c.moveToNext());
            }
            c.close();
            return list;
        } finally {
            db.close();
        }
    }
}
