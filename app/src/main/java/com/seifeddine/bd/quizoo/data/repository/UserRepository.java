package com.seifeddine.bd.quizoo.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.seifeddine.bd.quizoo.data.local.entity.User;

public class UserRepository {
    private static final String TAG = "UserRepository";
    private final FirebaseAuth mAuth;
    private final FirebaseFirestore db;
    private static UserRepository instance;
    private final MutableLiveData<User> userData = new MutableLiveData<>();

    private UserRepository() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public LiveData<User> getCurrentUserData() {
        if (mAuth.getCurrentUser() != null) {
            fetchUserData(mAuth.getCurrentUser().getUid());
        }
        return userData;
    }

    private void fetchUserData(String userId) {
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener(documentSnapshot -> {
                User user = documentSnapshot.toObject(User.class);
                if (user != null) {
                    user.setUserId(userId);
                    userData.postValue(user);
                }
            })
            .addOnFailureListener(e ->
                Log.e(TAG, "Error fetching user data", e));
    }

    public void updateQuizzesTaken() {
        if (mAuth.getCurrentUser() == null) return;

        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId)
            .update("quizzesTaken", FieldValue.increment(1))
            .addOnSuccessListener(aVoid -> fetchUserData(userId))
            .addOnFailureListener(e ->
                Log.e(TAG, "Error updating quizzes taken", e));
    }

    public boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public void signOut() {
        mAuth.signOut();
        userData.postValue(null);
    }
}