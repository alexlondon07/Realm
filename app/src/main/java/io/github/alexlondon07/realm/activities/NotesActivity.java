package io.github.alexlondon07.realm.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import io.github.alexlondon07.realm.R;
import io.github.alexlondon07.realm.adapters.NoteAdapter;
import io.github.alexlondon07.realm.models.Board;
import io.github.alexlondon07.realm.models.Note;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;

public class NotesActivity extends AppCompatActivity  implements RealmChangeListener<Board>{

    private ListView listView;
    private FloatingActionButton fab;
    private NoteAdapter adapter;
    private RealmList<Note> notes;
    private Realm realm;

    private int boardId;
    private Board board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        realm = Realm.getDefaultInstance();

        if(getIntent().getExtras() !=null)
            boardId = getIntent().getExtras().getInt("id");

            //Buscamos la Informacion con el Identificador del Board
            board = realm.where(Board.class).equalTo("id", boardId).findFirst();

            loadView();

    }

    private void loadView() {

        board.addChangeListener(this);

        //Obtemos todas las notas
        notes = board.getNotes();

        this.setTitle(board.getTitle());

        listView = findViewById(R.id.listViewNote);
        adapter = new NoteAdapter(this, notes , R.layout.list_view_note_item);
        listView.setAdapter(adapter);

        fab = findViewById(R.id.floatActionButtonAddNote);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertForCreatingNote("Add new Note", "Type a note for " + board.getTitle() + ".");
            }
        });
    }

    /**********************DIALOGS***********************/

    private void showAlertForCreatingNote(String title, String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Se valida los campos
        if(title != null)builder.setTitle(title);
        if(message != null)builder.setTitle(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_note, null);
        builder.setView(viewInflated);
        final EditText input = viewInflated.findViewById(R.id.editTextNote);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String noteName = input.getText().toString().trim();

                if(noteName.length() > 0){
                    createNewNote(noteName);
                }else {
                    Toast.makeText(NotesActivity.this, "The note can't be empty", Toast.LENGTH_LONG).show();
                }
            }
        });

        AlertDialog dialog =  builder.create();
        dialog.show();
    }

    /**
     * Funcion para registrar una Nota de una Board
     * @param note
     */
    private void createNewNote(String note) {

        realm.beginTransaction();
        Note _note = new Note(note);
        realm.copyToRealm(_note);
        board.getNotes().add(_note);
        realm.commitTransaction();
    }

    @Override
    public void onChange(Board board) {
        adapter.notifyDataSetChanged();
    }
}
