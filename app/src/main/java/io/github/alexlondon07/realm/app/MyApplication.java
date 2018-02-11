package io.github.alexlondon07.realm.app;

import android.app.Application;

import java.util.concurrent.atomic.AtomicInteger;

import io.github.alexlondon07.realm.models.Board;
import io.github.alexlondon07.realm.models.Note;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by alexlondon07 on 2/11/18.
 */

public class MyApplication extends Application {

    public static AtomicInteger BoardID = new AtomicInteger();
    public static AtomicInteger NoteID = new AtomicInteger();

    @Override
    public void onCreate() {
        super.onCreate();

        Realm realm = Realm.getDefaultInstance();
        BoardID = getIdByTable(realm, Board.class);
        NoteID = getIdByTable(realm, Note.class);
        realm.close();
    }

    /**
     * Metodo para obtener el Ultimo registro segun la Entidad
     * @param realm
     * @param anyClass
     * @param <T>
     * @return
     */
    private  <T extends RealmObject> AtomicInteger getIdByTable(Realm realm, Class<T> anyClass){
        RealmResults<T> results = realm.where(anyClass).findAll();
        return (results.size()> 0) ? new AtomicInteger(results.max("id").intValue()) : new AtomicInteger();
    }

}
