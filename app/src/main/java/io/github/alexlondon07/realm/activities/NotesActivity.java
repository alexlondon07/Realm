package io.github.alexlondon07.realm.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

        registerForContextMenu(listView);
    }

    /**********************CRUD ACTIONS***********************/

    private void createNewNote(String note) {

        realm.beginTransaction();
        Note _note = new Note(note);
        realm.copyToRealm(_note);
        board.getNotes().add(_note);
        realm.commitTransaction();
    }

    private void editNote(String newNoteDescription, Note note){

        realm.beginTransaction();
        note.setDescription(newNoteDescription);
        realm.copyToRealmOrUpdate(note);
        realm.commitTransaction();
    }

    private void deleteNote(Note note){

        realm.beginTransaction();
        note.deleteFromRealm();
        realm.commitTransaction();
    }

    private void deleteAll(){
        realm.beginTransaction();
        board.getNotes().deleteAllFromRealm();
        realm.commitTransaction();
    }

    /********************** EVENTS ***********************/

    @Override
    public void onChange(Board board) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.delete_all_notes:
                deleteAll();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu_note_activity, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){

            case R.id.delete_note:

                deleteNote(notes.get(info.position));
                return true;

            case R.id.edit_note:
                showAlertForEditingNote("title_edit_board", "Change description of the note", notes.get(info.position));
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    /**********************DIALOGS***********************/

    private void showAlertForEditingNote(String title, String message, final Note note){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Se valida los campos
        if(title != null)builder.setTitle(title);
        if(message != null)builder.setTitle(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_note, null);
        builder.setView(viewInflated);
        final EditText input = viewInflated.findViewById(R.id.editTextNote);
        input.setText(note.getDescription());

        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String descriptionName = input.getText().toString().trim();
                if(descriptionName.length() == 0){
                    Toast.makeText(NotesActivity.this, R.string.message_edit_name, Toast.LENGTH_LONG).show();
                }else  if(descriptionName.equals(board.getTitle())){
                    Toast.makeText(NotesActivity.this, R.string.message_name_equals, Toast.LENGTH_LONG).show();
                }else{
                    editNote(descriptionName, note);
                }
            }
        });

        AlertDialog dialog =  builder.create();
        dialog.show();
    }



    private void showAlertForCreatingNote(String title, String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Se valida los campos
        if(title != null)builder.setTitle(title);
        if(message != null)builder.setTitle(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_note, null);
        builder.setView(viewInflated);
        final EditText input = viewInflated.findViewById(R.id.editTextNote);

        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String noteName = input.getText().toString().trim();
                if(noteName.length() > 0){
                    createNewNote(noteName);
                }else {
                    Toast.makeText(NotesActivity.this, R.string.note_empty, Toast.LENGTH_LONG).show();
                }
            }
        });

        AlertDialog dialog =  builder.create();
        dialog.show();
    }

}
