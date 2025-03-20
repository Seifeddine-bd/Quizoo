package com.seifeddine.bd.quizoo.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.seifeddine.bd.quizoo.R;

import java.util.HashMap;
import java.util.Map;
public class SignupActivity extends AppCompatActivity {

    private EditText emailField, passwordField, confirmPasswordField, usernameField;
    private Button signupButton;
    private TextView loginLink;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private static final String TAG = "SignupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Setup UI components
        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);
        confirmPasswordField = findViewById(R.id.confirm_password_field);
        usernameField = findViewById(R.id.username_field);
        signupButton = findViewById(R.id.signup_button);
        loginLink = findViewById(R.id.login_link);
        progressBar = findViewById(R.id.progress_bar);

        // Set up sign up button click listener
        signupButton.setOnClickListener(v -> signUp());

        // Set up login link click listener
        loginLink.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void signUp() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String confirmPassword = confirmPasswordField.getText().toString().trim();
        String username = usernameField.getText().toString().trim();

        // Validate inputs (same as before)
        if (email.isEmpty()) {
            emailField.setError("Email is required");
            emailField.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("Please enter a valid email");
            emailField.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordField.setError("Password is required");
            passwordField.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordField.setError("Password must be at least 6 characters");
            passwordField.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordField.setError("Passwords don't match");
            confirmPasswordField.requestFocus();
            return;
        }

        if (username.isEmpty()) {
            usernameField.setError("Username is required");
            usernameField.requestFocus();
            return;
        }

        // Show progress bar and disable sign up button to prevent multiple clicks
        progressBar.setVisibility(View.VISIBLE);
        signupButton.setEnabled(false);

        // Create user with email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Get user ID
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();

                            // Create user object
                            Map<String, Object> user = new HashMap<>();
                            user.put("email", email);
                            user.put("username", username);
                            user.put("createdAt", FieldValue.serverTimestamp());
                            user.put("quizzesTaken", 0);

                            // Add user to Firestore
                            db.collection("users").document(userId)
                                    .set(user)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d(TAG, "User profile created successfully");
                                        // Always hide progress and re-enable button
                                        progressBar.setVisibility(View.GONE);
                                        signupButton.setEnabled(true);

                                        Toast.makeText(SignupActivity.this, "Account created successfully!", Toast.LENGTH_SHORT).show();

                                        // Navigate to main activity
                                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Error handling
                                        progressBar.setVisibility(View.GONE);
                                        signupButton.setEnabled(true);
                                        Log.e(TAG, "Error adding user to Firestore", e);
                                        Toast.makeText(SignupActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            // Something went wrong after createUserWithEmailAndPassword succeeded
                            progressBar.setVisibility(View.GONE);
                            signupButton.setEnabled(true);
                            Toast.makeText(SignupActivity.this, "Authentication successful but user details not available", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Authentication failed
                        progressBar.setVisibility(View.GONE);
                        signupButton.setEnabled(true);
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthUserCollisionException e) {
                            Toast.makeText(SignupActivity.this, "Email already in use", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(SignupActivity.this, "Authentication failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}