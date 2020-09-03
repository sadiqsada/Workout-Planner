/**
 * Md Sadiq Sada
 * 112786580
 * CSE 390
 */
package com.example.workout_planner;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 *This is the settings activity
 */
public class SettingsActivity extends AppCompatActivity {
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initSettings();
        initSortByClick();
        initSortOrderClick();
    }

    /**
     * Initializing settings
     */
    private void initSettings() {
        DatabaseReference ref = mDatabase.child("settings");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String sortKey = "";
                String sortValue = "";
                sortKey = snapshot.getKey();
                sortValue = snapshot.getValue().toString();
                updateSettings(sortKey, sortValue);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Updating settings based on sortfield and sortorder
     * @param sortKey
     * whether sortfield or sortorder is being updated
     * @param sortVal
     * the value to update it with
     */
    public void updateSettings(String sortKey, String sortVal) {
        RadioButton rbTitle = findViewById(R.id.radioTitle);
        RadioButton rbDuration = findViewById(R.id.radioDuration);
        RadioButton rbPerformed = findViewById(R.id.radioPerformed);
        RadioButton rbAscending = findViewById(R.id.radioAscending);
        RadioButton rbDescending = findViewById(R.id.radioDescending);

        if(sortKey.equals("sortfield")) {
            if (sortVal.equalsIgnoreCase("title")) {
                rbTitle.setChecked(true);
            } else if (sortVal.equalsIgnoreCase("duration")) {
                rbDuration.setChecked(true);
            } else {
                rbPerformed.setChecked(true);
            }
        }

        else if(sortKey.equals("sortorder")) {
            if (sortVal.equalsIgnoreCase("ASC")) {
                rbAscending.setChecked(true);
            } else {
                rbDescending.setChecked(true);
            }
        }
    }

    /**
     * initialize sortfields
     */
    public void initSortByClick() {
        RadioGroup rgSortBy = findViewById(R.id.radioGroupSortBy);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                DatabaseReference ref = mDatabase.child("settings");
                RadioButton rbTitle = findViewById(R.id.radioTitle);
                RadioButton rbDuration = findViewById(R.id.radioDuration);
                if(rbTitle.isChecked()) {
                    ref.child("sortfield").setValue("title");
                }
                else if(rbDuration.isChecked()) {
                    ref.child("sortfield").setValue("duration");
                }
                else {
                    ref.child("sortfield").setValue("performed");
                }
            }
        });
    }

    /**
     * initialize sort order fields
     */
    public void initSortOrderClick() {
        RadioGroup rgSortOrder = findViewById(R.id.radioGroupSortOrder);
        rgSortOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                DatabaseReference ref = mDatabase.child("settings");
                RadioButton rbAscending = findViewById(R.id.radioAscending);
                if(rbAscending.isChecked()) {
                    ref.child("sortorder").setValue("ASC");
                }
                else {
                    ref.child("sortorder").setValue("DESC");
                }
            }
        });
    }

}


