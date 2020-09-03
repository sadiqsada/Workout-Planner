/**
 * Md Sadiq Sada
 * 112786580
 * CSE 390
 */

package com.example.workout_planner;

/**
 * This is the Workout Class
 */

public class Workout {
    private String id;
    private String title;
    private String description;
    private int duration;
    private boolean isPerformed;
    private String type;

    /**
     * No-arg constructor
     */
    public Workout() {

    }

    /**
     * get ID
     * @return
     * ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets ID
     * @param id
     * the ID to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * gets title
     * @return
     * String title
     */
    public String getTitle() {
        return title;
    }

    /**
     * sets title
     * @param title
     * the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * gets description
     * @return
     * description string
     */
    public String getDescription() {
        return description;
    }

    /**
     * sets description
     * @param description
     * the new description to be set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * gets duration
     * @return
     * duration of workout
     */
    public int getDuration() {
        return duration;
    }

    /**
     * set duration
     * @param duration
     * sets the duration of the workout
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * get performed status
     * @return
     * true if performed, false if not
     */
    public boolean isPerformed() {
        return isPerformed;
    }

    /**
     * sets performed status
     * @param performed
     * the new status to be set
     */
    public void setPerformed(boolean performed) {
        isPerformed = performed;
    }

    /**
     * gets the type of workout
     * @return
     * type of workout
     */
    public String getType() {
        return type;
    }

    /**
     * sets the workout type
     * @param type
     * the new type to be set
     */
    public void setType(String type) {
        this.type = type;
    }
}

