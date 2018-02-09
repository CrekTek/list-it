package com.crektek.listit.data;

import android.support.annotation.NonNull;

/**
 * Created on 08/02/2018.
 */

public class ListEntity {

    private int id;
    private String title;
    private int priority;

    public ListEntity(@NonNull int id, @NonNull String title, @NonNull int priority) {
        this.id = id;
        this.title = title;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
