package com.example.vsapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.vsapp.dao.ExcursionDAO;
import com.example.vsapp.dao.VacationDAO;
import com.example.vsapp.entities.Excursion;
import com.example.vsapp.entities.Vacation;

@Database(entities = {Vacation.class, Excursion.class}, version= 1, exportSchema = false)
public abstract class VacationDatabaseBuilder extends RoomDatabase {
    public abstract VacationDAO vacationDAO();
    public abstract ExcursionDAO excursionDAO();
    private static volatile VacationDatabaseBuilder INSTANCE;

    static VacationDatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE==null) {
            synchronized (VacationDatabaseBuilder.class) {
                if(INSTANCE==null) {
                    INSTANCE= Room.databaseBuilder(context.getApplicationContext(), VacationDatabaseBuilder.class, "MyVacationDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
