package io.github.alexlondon07.realm.models;

import java.util.Date;

import io.github.alexlondon07.realm.app.MyApplication;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by alexlondon07 on 2/11/18.
 */

public class Board extends RealmObject {

    @PrimaryKey
    private int id;

    @Required
    private String title;

    @Required
    private Date createAt;

    private RealmList<Note> notes;

    public Board(String title) {
        this.id = MyApplication.BoardID.incrementAndGet();
        this.title = title;
        this.notes = new RealmList<>();
        this.createAt = new Date();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public RealmList<Note> getNotes() {
        return notes;
    }

    public void setNotes(RealmList<Note> notes) {
        this.notes = notes;
    }
}
