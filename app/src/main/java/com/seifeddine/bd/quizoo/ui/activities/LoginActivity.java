package com.seifeddine.bd.quizoo.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.seifeddine.bd.quizoo.R;
public class LoginActivity extends AppCompatActivity {

    private EditText emailField, passwordField;
    private Button loginButton;
    private TextView signupLink, forgotPasswordLink;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check if already logged in
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // User already logged in, go to main activity
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Setup UI components
        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);
        loginButton = findViewById(R.id.login_button);
        signupLink = findViewById(R.id.signup_link);
        forgotPasswordLink = findViewById(R.id.forgot_password_link);
        progressBar = findViewById(R.id.progress_bar);

        // Set up login button click listener
        loginButton.setOnClickListener(v -> login());

        // Set up signup link click listener
        signupLink.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            finish();
        });


        // In LoginActivity.java, update the forgotPasswordLink click listener

//        forgotPasswordLink.setOnClickListener(v -> {
//            // Show dialog to get email
//            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
//            builder.setTitle("Reset Password");
//
//            // Set up the input field
//            final EditText input = new EditText(this);
//            input.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
//            input.setHint("Enter your email");
//            if (!emailField.getText().toString().isEmpty()) {
//                // Pre-fill with email if available
//                input.setText(emailField.getText().toString());
//            }
//            builder.setView(input);
//
//            // Set up the buttons
//            builder.setPositiveButton("Reset", (dialog, which) -> {
//                String email = input.getText().toString().trim();
//
//                // Validate email
//                if (email.isEmpty()) {
//                    Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//                    Toast.makeText(LoginActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                // Show progress
//                progressBar.setVisibility(View.VISIBLE);
//
//                // Send password reset email
//                mAuth.sendPasswordResetEmail(email)
//                        .addOnCompleteListener(task -> {
//                            progressBar.setVisibility(View.GONE);
//                            if (task.isSuccessful()) {
//                                Toast.makeText(LoginActivity.this,
//                                        "Password reset instructions sent to your email",
//                                        Toast.LENGTH_LONG).show();
//                            } else {
//                                Toast.makeText(LoginActivity.this,
//                                        "Failed to send reset email: " + task.getException().getMessage(),
//                                        Toast.LENGTH_LONG).show();
//                            }
//                        });
//            });
//
//            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
//            builder.show();
//        });

//        forgotPasswordLink.setOnClickListener(v -> {
//            // Inflate the custom layout
//            View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot_password, null);
//            TextInputEditText emailInput = dialogView.findViewById(R.id.reset_email_input);
//
//            // Pre-fill with email if available
//            if (!emailField.getText().toString().isEmpty()) {
//                emailInput.setText(emailField.getText().toString());
//            }
//
//            // Use Material Dialog
//            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Rounded);
//            builder.setView(dialogView)
//                    .setPositiveButton("Reset", (dialog, which) -> {
//                        String email = emailInput.getText().toString().trim();
//
//                        // Validate email
//                        if (email.isEmpty()) {
//                            Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//
//                        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//                            Toast.makeText(LoginActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//
//                        // Show progress
//                        progressBar.setVisibility(View.VISIBLE);
//
//                        // Send password reset email
//                        mAuth.sendPasswordResetEmail(email)
//                                .addOnCompleteListener(task -> {
//                                    progressBar.setVisibility(View.GONE);
//                                    if (task.isSuccessful()) {
//                                        Toast.makeText(LoginActivity.this,
//                                                "Password reset instructions sent to your email",
//                                                Toast.LENGTH_LONG).show();
//                                    } else {
//                                        Toast.makeText(LoginActivity.this,
//                                                "Failed to send reset email: " + task.getException().getMessage(),
//                                                Toast.LENGTH_LONG).show();
//                                    }
//                                });
//                    })
//                    .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
//
//            builder.create().show();
//        });
forgotPasswordLink.setOnClickListener(v -> {
    // Inflate the custom layout
    View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot_password, null);
    TextInputEditText emailInput = dialogView.findViewById(R.id.reset_email_input);
    MaterialButton resetButton = dialogView.findViewById(R.id.btn_reset);
    MaterialButton cancelButton = dialogView.findViewById(R.id.btn_cancel);

    // Pre-fill with email if available
    if (!emailField.getText().toString().isEmpty()) {
        emailInput.setText(emailField.getText().toString());
    }

    // Create dialog
    AlertDialog dialog = new MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Rounded)
            .setView(dialogView)
            .create();

    // Set button listeners
    cancelButton.setOnClickListener(view -> dialog.dismiss());

    resetButton.setOnClickListener(view -> {
        String email = emailInput.getText().toString().trim();

        // Validate email
        if (email.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(LoginActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress
        progressBar.setVisibility(View.VISIBLE);
        dialog.dismiss();

        // Send password reset email
        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener(task -> {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this,
                            "Password reset instructions sent to your email",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Failed to send reset email: " + task.getException().getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
    });

    dialog.show();
});
    }

    private void login() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        // Validate inputs
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

        // Show progress bar
        progressBar.setVisibility(View.VISIBLE);

        // Sign in with email and password
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    // Hide progress bar
                    progressBar.setVisibility(View.GONE);

                    if (task.isSuccessful()) {
                        // Navigate to main activity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}