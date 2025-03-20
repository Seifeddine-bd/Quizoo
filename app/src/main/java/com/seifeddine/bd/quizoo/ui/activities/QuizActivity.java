package com.seifeddine.bd.quizoo.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.firebase.auth.FirebaseAuth;
import com.seifeddine.bd.quizoo.data.repository.UserRepository;
import com.google.android.material.navigation.NavigationView;
import com.seifeddine.bd.quizoo.R;
import com.seifeddine.bd.quizoo.data.local.AppDatabase;
import com.seifeddine.bd.quizoo.data.remote.RetrofitClient;
import com.seifeddine.bd.quizoo.data.repository.QuizRepository;

public class QuizActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "QuizActivity";
    private QuizRepository repository;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private long backPressedTime;

    public static void start(Context context, int categoryId) {
        Intent intent = new Intent(context, QuizActivity.class);
        intent.putExtra("categoryId", categoryId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repository = new QuizRepository(
                AppDatabase.getDatabase(this).quizDao(),
                AppDatabase.getDatabase(this).categoryDao(),
                AppDatabase.getDatabase(this).analyticsDao(),
                RetrofitClient.getApiService()
        );

        // Setup Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Setup Navigation Controller
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        } else {
            throw new IllegalStateException("NavHostFragment not found");
        }

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.mainFragment, R.id.categoriesFragment, R.id.quizFragment, R.id.resultsFragment)
                .setOpenableLayout(drawerLayout)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
       // NavigationUI.setupWithNavController(navigationView, navController);

        // Initial navigation if categoryId is provided
        if (savedInstanceState == null) {
            int categoryId = getIntent().getIntExtra("categoryId", -1);
            if (categoryId != -1) {
                Bundle args = new Bundle();
                args.putInt("categoryId", categoryId);
                navController.navigate(R.id.action_mainFragment_to_quizFragment, args);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mainFragment) {
            navController.navigate(R.id.mainFragment);
        } else if (id == R.id.categoriesFragment) {
            navController.navigate(R.id.categoriesFragment);
        } else if (id == R.id.resultsFragment) {
            navController.navigate(R.id.resultsFragment);
        } else if (id == R.id.action_logout) {
            try {
                // Log for debugging
                Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();

                // Sign out from Firebase
                FirebaseAuth.getInstance().signOut();

                // Clear repository data if needed
                if (UserRepository.getInstance() != null) {
                    UserRepository.getInstance().signOut();
                }

                // Navigate to login with flags to clear stack
                Intent intent = new Intent(QuizActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Logout error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            return true;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (navController.getCurrentDestination() != null &&
                navController.getCurrentDestination().getId() != R.id.mainFragment) {
            // If not on MainFragment, navigate back to it
            navController.navigate(R.id.mainFragment);
        } else {
            // Double back press to exit
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            }
            backPressedTime = System.currentTimeMillis();
        }
    }
}