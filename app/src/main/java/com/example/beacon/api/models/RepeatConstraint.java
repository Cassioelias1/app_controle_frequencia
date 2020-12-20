package com.example.beacon.api.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RepeatConstraint implements Serializable {
    @SerializedName("id")
    private Integer id;

    public RepeatConstraint() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
