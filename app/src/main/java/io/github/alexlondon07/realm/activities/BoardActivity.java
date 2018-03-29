package io.github.alexlondon07.realm.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import io.github.alexlondon07.realm.R;
import io.github.alexlondon07.realm.adapters.BoardAdapter;
import io.github.alexlondon07.realm.models.Board;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class BoardActivity extends AppCompatActivity implements RealmChangeListener<Board>, AdapterView.OnItemClickListener{

    private Realm realm;

    private FloatingActionButton fab;
    private ListView listView;
    private BoardAdapter boardAdapter;
    private RealmResults<Board> boards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boar);

        //DB Realm
        realm = Realm.getDefaultInstance();
        boards = realm.where(Board.class).findAll();

        boardAdapter = new BoardAdapter(this, boards, R.layout.list_view_board);
        listView = findViewById(R.id.listViewBoard);
        listView.setAdapter(boardAdapter);

        loadView();
    }

    private void loadView() {
        fab = findViewById(R.id.floatActionButtonAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertForCreatingBoard("Add new board", "Type a name for your board");
            }
        });
    }



    /**********************CRUD ACTIONS***********************/

    /**
     *
     * @param boardName
     */
    private void createNewBoard(String boardName) {

        realm.beginTransaction();
        Board board = new Board(boardName);
        realm.copyToRealm(board);
        realm.commitTransaction();

        /*
        Cuando son acciones muy grandes
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Board board = new Board(boardName);
                realm.copyToRealm(board);
            }
        });*/
    }

    /**********************DIALOGS***********************/

    private void showAlertForCreatingBoard(String title, String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Se valida los campos
        if(title != null)builder.setTitle(title);
        if(message != null)builder.setTitle(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_board, null);
        builder.setView(viewInflated);
        final EditText input = viewInflated.findViewById(R.id.editTextBoard);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String boardName = input.getText().toString().trim();
                if(boardName.length() > 0){
                    createNewBoard(boardName);
                }else {
                    Toast.makeText(BoardActivity.this, "The name is required to create a new Board", Toast.LENGTH_SHORT).show();
                }
            }
        });

        AlertDialog dialog =  builder.create();
        dialog.show();
    }

    @Override
    public void onChange(Board board) {
        boardAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(BoardActivity.this, NotesActivity.class);
        intent.putExtra("id", boards.get(position).getId());
        startActivity(intent);
    }
}

