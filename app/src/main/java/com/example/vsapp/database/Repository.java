package com.example.vsapp.database;

import android.app.Application;

import com.example.vsapp.dao.ExcursionDAO;
import com.example.vsapp.dao.VacationDAO;
import com.example.vsapp.entities.Excursion;
import com.example.vsapp.entities.Vacation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private ExcursionDAO mExcursionDAO;
    private VacationDAO mVacationDAO;

    private List<Vacation> mAllVacations;
    private List<Excursion> mAllExcursions;

    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application) {
        VacationDatabaseBuilder db = VacationDatabaseBuilder.getDatabase(application);
        mExcursionDAO = db.excursionDAO();
        mVacationDAO = db.vacationDAO();
    }

    public List<Vacation> getmAllVacations() {
        databaseExecutor.execute(() -> {
            mAllVacations = mVacationDAO.getAllVacations();
        });

        try{
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllVacations;
    }

    public void insert (Vacation vacation) {
        databaseExecutor.execute(() -> {
            mVacationDAO.insert(vacation);
        });

        try{
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public void update(Vacation vacation) {
        databaseExecutor.execute(() -> {
            mVacationDAO.update(vacation);
        });

        try{
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Vacation vacation) {
        databaseExecutor.execute(() -> {
            mVacationDAO.delete(vacation);
        });

        try{
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Excursion> getmAllExcursions() {
        databaseExecutor.execute(() -> {
            mAllExcursions = mExcursionDAO.getAllExcursions();
        });

        try{
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllExcursions;
    }


    public List<Excursion> getAssociatedExcursions(int vacationID) {
        databaseExecutor.execute(() -> {
            mAllExcursions = mExcursionDAO.getAssociatedExcursions(vacationID);
        });

        try{
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllExcursions;
    }


    public void insert(Excursion excursion) {
        databaseExecutor.execute(() -> {
            mExcursionDAO.insert(excursion);
        });
        try{
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public void update(Excursion excursion) {
        databaseExecutor.execute(() -> {
            mExcursionDAO.update(excursion);
        });

        try{
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public void delete(Excursion excursion) {
        databaseExecutor.execute(() -> {
            mExcursionDAO.delete(excursion);
        });

        try{
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}