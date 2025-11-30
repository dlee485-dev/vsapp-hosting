package com.example.vsapp.database;

import android.app.Application;

import com.example.vsapp.dao.ExcursionDAO;
import com.example.vsapp.dao.VacationDAO;
import com.example.vsapp.dao.CategoryDAO;
import com.example.vsapp.dao.UserSecurityDAO;
import com.example.vsapp.entities.Excursion;
import com.example.vsapp.entities.Vacation;
import com.example.vsapp.entities.Category;
import com.example.vsapp.entities.UserSecurity;
import com.example.vsapp.dao.CategoryDAO;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private ExcursionDAO mExcursionDAO;
    private VacationDAO mVacationDAO;
    private CategoryDAO mCategoryDAO;
    private UserSecurityDAO mUserSecurityDAO;
    private CategoryDAO mCategoryDao;


    private List<Vacation> mAllVacations;
    private List<Excursion> mAllExcursions;
    private List<Category> mAllCategories;
    private UserSecurity mUserSecurity;

    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application) {
        VacationDatabaseBuilder db = VacationDatabaseBuilder.getDatabase(application);
        mExcursionDAO = db.excursionDAO();
        mVacationDAO = db.vacationDAO();
        mCategoryDAO = db.categoryDAO();
        mUserSecurityDAO = db.userSecurityDAO();
    }

    public List<Vacation> getmAllVacations() {
        databaseExecutor.execute(() -> {
            mAllVacations = mVacationDAO.getAllVacations();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllVacations;
    }

    public void insert(Vacation vacation) {
        databaseExecutor.execute(() -> mVacationDAO.insert(vacation));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Vacation vacation) {
        databaseExecutor.execute(() -> mVacationDAO.update(vacation));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Vacation vacation) {
        databaseExecutor.execute(() -> mVacationDAO.delete(vacation));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Excursion> getmAllExcursions() {
        databaseExecutor.execute(() -> {
            mAllExcursions = mExcursionDAO.getAllExcursions();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllExcursions;
    }

    public List<Excursion> getAssociatedExcursions(int vacationID) {
        databaseExecutor.execute(() -> {
            mAllExcursions = mExcursionDAO.getAssociatedExcursions(vacationID);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllExcursions;
    }

    public void insert(Excursion excursion) {
        databaseExecutor.execute(() -> mExcursionDAO.insert(excursion));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Excursion excursion) {
        databaseExecutor.execute(() -> mExcursionDAO.update(excursion));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Excursion excursion) {
        databaseExecutor.execute(() -> mExcursionDAO.delete(excursion));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Category> getAllCategories() {
        databaseExecutor.execute(() -> {
            mAllCategories = mCategoryDAO.getAllCategories();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllCategories;
    }

    public void insert(Category category) {
        databaseExecutor.execute(() -> mCategoryDAO.insert(category));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public UserSecurity getUserSecurity() {
        databaseExecutor.execute(() -> {
            mUserSecurity = mUserSecurityDAO.getUserSecurity();
        });

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mUserSecurity;
    }

    public void savePin(String rawPin) {
        String hashed = hashPin(rawPin);
        UserSecurity security = new UserSecurity(1, hashed);

        databaseExecutor.execute(() -> mUserSecurityDAO.insert(security));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isPinValid(String rawPin) {
        UserSecurity security = getUserSecurity();
        if (security == null || security.getPinHash() == null) {
            return false;
        }
        String hashed = hashPin(rawPin);
        return hashed.equals(security.getPinHash());
    }

    private String hashPin(String rawPin) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawPin.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }
    public Map<Integer, String> getCategoryMap() {
        Map<Integer, String> map = new HashMap<>();
        for (Category c : mCategoryDao.getAllCategories()) {
            map.put(c.getCategoryID(), c.getCategoryName());
        }
        return map;
    }
}
