/**
 * Md Sadiq Sada
 * 112786580
 * CSE 390
 */
package com.example.workout_planner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * This is the Add Dialog Class
 */
public class AddDialog extends DialogFragment {
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private Workout currentWorkout;

    Spinner spinner;
    EditText title;
    EditText description;
    EditText duration;
    CheckBox isPerformed;

    /**
     * No-arg constructor
     */
    public AddDialog() {

    }

    /**
     * Arg constructor
     * @param w
     * the workout passed in
     */
    public AddDialog(Workout w) {
        currentWorkout = w;
    }

    /**
     * Executes when loaded
     * @param savedInstanceState
     * the state of the dialog
     * @return
     * a Dialog
     */
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_add, null);
        if(currentWorkout == null) {
            currentWorkout = new Workout();
            builder.setView(view);
            builder.setTitle("Add Workout")
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseReference workoutRef = mDatabase.child("workouts");
                            DatabaseReference newWorkoutRef = workoutRef.push();
                            currentWorkout.setId(newWorkoutRef.getKey());
                            newWorkoutRef.setValue(currentWorkout)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("Creation", "success");
                                        }
                                    });
                        }
                    });
        }
        else {
            builder.setView(view);
            builder.setTitle("Edit Workout")
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final DatabaseReference workoutRef = mDatabase.child("workouts").child(currentWorkout.getId());
                            workoutRef.setValue(currentWorkout);
                        }
                    });
        }
        title = view.findViewById(R.id.workoutTitle);
        description = view.findViewById(R.id.workoutDescription);
        duration = view.findViewById(R.id.workoutDuration);
        spinner = view.findViewById(R.id.workoutSpinner);
        isPerformed = view.findViewById(R.id.workoutPerformed);
        initTextChangedEvents();
        return builder.create();
    }

    /**
     * Listen for text change events
     */
    public void initTextChangedEvents() {
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentWorkout.setTitle(title.getText().toString());
            }
        });

        duration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentWorkout.setDuration(Integer.parseInt(duration.getText().toString()));
            }
        });

        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentWorkout.setDescription(description.getText().toString());
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentWorkout.setType(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                currentWorkout.setType("Cardio");
            }
        });

        isPerformed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) currentWorkout.setPerformed(true);
                else {
                    currentWorkout.setPerformed(false);
                }
            }
        });
    }
}
