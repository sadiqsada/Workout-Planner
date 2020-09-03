/**
 * Md Sadiq Sada
 * 112786580
 * CSE 390
 */
package com.example.workout_planner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * This is the WorkoutAdapter Class
 */
public class WorkoutAdapter extends RecyclerView.Adapter {
    private ArrayList<Workout> workoutData;
    private Context context;
    private boolean isDeleting;

    /**
     * This is the WorkoutViewHolder Class
     */
    public class WorkoutViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener,
        View.OnClickListener {
        private TextView textTitle;
        private TextView textDuration;
        private TextView textDescription;
        private ImageView imageItem;
        private CheckBox isPerformed;
        private Button editButton;
        private Button deleteButton;
        private ToggleButton detailsButton;

        /**
         * Gets title
         * @return
         * title
         */
        public TextView getTextTitle() {
            return textTitle;
        }

        /**
         * Gets duration
         * @return
         * duration
         */
        public TextView getTextDuration() {
            return textDuration;
        }

        /**
         * Gets description
         * @return
         * description
         */
        public TextView getTextDescription() {
            return textDescription;
        }

        /**
         * gets image item
         * @return
         * image item
         */
        public ImageView getImageItem() {
            return imageItem;
        }

        /**
         * returns whether the workout is performed
         * @return
         * true if yes, false if not
         */
        public CheckBox getIsPerformed() {
            return isPerformed;
        }

        /**
         * gets the edit button
         * @return
         * edit button
         */
        public Button getEditButton() {
            return editButton;
        }

        /**
         * gets delete button
         * @return
         * delete button
         */
        public Button getDeleteButton() {
            return deleteButton;
        }

        /**
         * gets details button
         * @return
         * details button
         */
        public ToggleButton getDetailsButton() {
            return detailsButton;
        }

        /**
         * Constructor
         * @param itemView
         * The view to be used
         */
        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.itemTitle);
            textDuration = itemView.findViewById(R.id.itemDuration);
            textDescription = itemView.findViewById(R.id.itemDescription);
            imageItem = itemView.findViewById(R.id.itemImage);
            isPerformed = itemView.findViewById(R.id.itemPerformed);
            editButton = itemView.findViewById(R.id.itemEdit);
            detailsButton = itemView.findViewById(R.id.itemDetails);
            deleteButton = itemView.findViewById(R.id.itemDelete);

            detailsButton.setOnCheckedChangeListener(this);
            editButton.setOnClickListener(this);
        }

        /**
         * Onclick for edit button
         * @param view
         * The view that was clicked
         */
        @Override
        public void onClick(View view) {
            if (view.getId() == editButton.getId()) {
                FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                int position = getAdapterPosition();
                AddDialog editItemDialog = new AddDialog(workoutData.get(position));
                editItemDialog.show(fm, "ItemEdit");
            }
        }

        /**
         * Listender for details button
         * @param buttonView
         * The button
         * @param isChecked
         * whether it is on or off
         */
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.getId() == detailsButton.getId()) {
                if (isChecked) {
                    textDescription.setVisibility(View.VISIBLE);
                } else {
                    textDescription.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * Constructor for Adapter
     * @param arrayList
     * The data being passed in
     * @param context
     * the context
     */
    public WorkoutAdapter(ArrayList<Workout> arrayList, Context context) {
        workoutData = arrayList;
        this.context = context;
    }

    /**
     * Constructor with boolean value for delete
     * @param arrayList
     * Data being passed in
     * @param context
     * context
     * @param delete
     * delete value
     */
    public WorkoutAdapter(ArrayList<Workout> arrayList, Context context, Boolean delete) {
        workoutData = arrayList;
        this.context = context;
        isDeleting = delete;
    }

    /**
     * Executed when a new cell is created
     * @param parent
     * parent
     * @param viewType
     * type
     * @return
     * ViewHolder
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_workout,
                parent, false);
        return new WorkoutViewHolder(v);
    }

    /**
     * populates viewHolder with data
     * @param holder
     * current cell
     * @param position
     * current position
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        WorkoutViewHolder ivh = (WorkoutViewHolder) holder;
        Workout currentWorkout = workoutData.get(position);

        // set the text components
        ivh.getTextTitle().setText(currentWorkout.getTitle());
        ivh.getTextDuration().setText(context.getResources().getString(R.string.adapter_duration,
                currentWorkout.getDuration()));
        ivh.getTextDescription().setText(currentWorkout.getDescription());

        // set the image
        if (currentWorkout.getType().equals("Cardio")) {
            ivh.getImageItem().setImageDrawable(context.getResources().getDrawable(R.drawable.exercise));
        } else if (currentWorkout.getType().equals("Upper Body")) {
            ivh.getImageItem().setImageDrawable(context.getResources().getDrawable(R.drawable.strong));
        } else if (currentWorkout.getType().equals("Lower Body")) {
            ivh.getImageItem().setImageDrawable(context.getResources().getDrawable(R.drawable.leg));
        }

        // set the checkbox
        ivh.getIsPerformed().setChecked(currentWorkout.isPerformed());

        if (isDeleting) {
            ivh.getEditButton().setVisibility(View.INVISIBLE);
            ivh.getDetailsButton().setVisibility(View.INVISIBLE);
            ivh.getDeleteButton().setVisibility(View.VISIBLE);
            ivh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItem(position);
                }
            });
        } else {
            ivh.getDeleteButton().setVisibility(View.INVISIBLE);
            ivh.getEditButton().setVisibility(View.VISIBLE);
            ivh.getDetailsButton().setVisibility(View.VISIBLE);
        }

    }

    /**
     * gets number of items in list
     * @return
     * size of workoutData
     */
    @Override
    public int getItemCount() {
        return workoutData.size();
    }

    /**
     * gets isDeleting
     * @return
     * true if isDeleting = true, false if not
     */
    public Boolean getIsDeleting() {
        return isDeleting;
    }

    /**
     * Sets the value of isDeleting
     * @param b
     * The value to be set
     */
    public void setDelete(boolean b) {
        isDeleting = b;
    }

    /**
     * Deletes an item from the list
     * @param position
     * position of item to be deleted
     */
    public void deleteItem(int position) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = mDatabase.child("workouts");
        Workout w = workoutData.get(position);
        workoutData.remove(position);
        ref.child(w.getId()).removeValue();
    }

}