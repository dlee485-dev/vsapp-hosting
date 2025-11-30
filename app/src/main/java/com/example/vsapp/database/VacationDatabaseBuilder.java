package com.example.vsapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.vsapp.dao.ExcursionDAO;
import com.example.vsapp.dao.VacationDAO;
import com.example.vsapp.dao.CategoryDAO;
import com.example.vsapp.dao.UserSecurityDAO;
import com.example.vsapp.entities.Excursion;
import com.example.vsapp.entities.Vacation;
import com.example.vsapp.entities.Category;
import com.example.vsapp.entities.UserSecurity;

@Database(
        entities = {
                Vacation.class,
                Excursion.class,
                Category.class,
                UserSecurity.class
        },
        version = 14, // bumped again (was 13 before)
        exportSchema = false
)
public abstract class VacationDatabaseBuilder extends RoomDatabase {
    public abstract VacationDAO vacationDAO();
    public abstract ExcursionDAO excursionDAO();
    public abstract CategoryDAO categoryDAO();
    public abstract UserSecurityDAO userSecurityDAO();

    private static volatile VacationDatabaseBuilder INSTANCE;

    static VacationDatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (VacationDatabaseBuilder.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    VacationDatabaseBuilder.class,
                                    "MyVacationDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
