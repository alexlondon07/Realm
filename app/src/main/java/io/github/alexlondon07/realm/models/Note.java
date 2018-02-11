package io.github.alexlondon07.realm.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by alexlondon07 on 2/11/18.
 */

public class Note extends RealmObject {

    @PrimaryKey
    private int id;

    @Required
    private String description;

    @Required
    private Date createAt;

    public Note() {
    }

    public Note(String description) {
        this.id = 0;
        this.description = description;
        this.createAt = new Date();
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateAt() {
        return createAt;
    }
}
