/**
 * Md Sadiq Sada
 * 112786580
 * CSE 390
 */

package com.example.workout_planner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This is the MainActivity Class
 */

public class MainActivity extends AppCompatActivity {
    ArrayList<Workout> workouts = new ArrayList<>();
    WorkoutAdapter workoutAdapter;
    RecyclerView workoutList;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    /**
     * Executed on creation
     * @param savedInstanceState
     * the state that is passed in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initAddButton();
        initDeleteSwitch();
        initSettingsButton();
        listeningToDatabase();
    }

    /**
     * executed when main activity comes to the foreground
     */
    @Override
    protected void onResume() {
        super.onResume();
        listeningToDatabase();
    }

    /**
     * listeninf to the database for changes
     */
    public void listeningToDatabase() {
        final DatabaseReference settingsRef = mDatabase.child("settings");
        settingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String sortField = "";
                String sortOrder = "";
                boolean shouldReverse = false;
                for(DataSnapshot d: snapshot.getChildren()) {
                    if(d.getKey().equals("sortfield")) {
                        sortField = d.getValue(String.class);
                    }
                    else if(d.getKey().equals("sortorder")) {
                        sortOrder = d.getValue().toString();
                        if(sortOrder.equals("DESC")) shouldReverse = true;
                        else if(sortOrder.equals("ASC")) shouldReverse = false;
                    }
                }
                update(sortField, shouldReverse);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * initializing add button
     */
    public void initAddButton() {
        FloatingActionButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                AddDialog newDialog = new AddDialog();
                newDialog.show(fm, "AddDialog");
            }
        });
    }

    /**
     * initializing delete switch
     */
    public void initDeleteSwitch() {
        Switch s = findViewById(R.id.deleteSwitch);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Boolean status = buttonView.isChecked();
                workoutAdapter.setDelete(status);
                workoutAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * initializing settings button
     */
    public void initSettingsButton() {
        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    /**
     * update the list
     * @param sortValue
     * the parameter for sorting the list
     * @param reverse
     * whether to sort in ascending or descending
     */
    public void update(String sortValue, final boolean reverse) {
        final DatabaseReference workoutRef = mDatabase.child("workouts");
        workoutRef.orderByChild(sortValue).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                workouts.clear();
                for(DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Workout w = postSnapshot.getValue(Workout.class);
                    workouts.add(w);
                }
                updateList(reverse);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * update list helper
     * @param shouldReverse
     * whether to reverse the list based on sort order input
     */
    public void updateList(boolean shouldReverse) {
        if(shouldReverse) Collections.reverse(workouts);
        workoutList = findViewById(R.id.rvItems);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        workoutList.setLayoutManager(layoutManager);
        if(workoutAdapter != null) {
            Boolean isDeleting = workoutAdapter.getIsDeleting();
            workoutAdapter = new WorkoutAdapter(workouts, this, isDeleting);
        }
        else {
            workoutAdapter = new WorkoutAdapter(workouts, this);
        }
        workoutList.setAdapter(workoutAdapter);

    }
}