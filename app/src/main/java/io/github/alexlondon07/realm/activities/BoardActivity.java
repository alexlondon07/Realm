package io.github.alexlondon07.realm.activities;

import android.content.DialogInterface;
import android.content.Intent;
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
import io.github.alexlondon07.realm.adapters.BoardAdapter;
import io.github.alexlondon07.realm.models.Board;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class BoardActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<Board>>, AdapterView.OnItemClickListener{

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

        loadView();
        //deleteAll();
    }

    private void loadView() {

        boards.addChangeListener(this);

        boardAdapter = new BoardAdapter(this, boards, R.layout.list_view_board_item);
        listView = findViewById(R.id.listViewBoard);
        listView.setAdapter(boardAdapter);
        listView.setOnItemClickListener(this);

        fab = findViewById(R.id.floatActionButtonAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertForCreatingBoard("Add new board", "Type a name for your board");
            }
        });

        registerForContextMenu(listView);
    }


    /**********************CRUD ACTIONS***********************/

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

    private void deleteAll() {
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }

    private void deleteBoard(Board board) {
        realm.beginTransaction();
        board.deleteFromRealm();
        realm.commitTransaction();
    }

    private void editBoard(String name, Board board) {
        realm.beginTransaction();
        board.setTitle(name);
        realm.copyToRealmOrUpdate(board);
        realm.commitTransaction();
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

        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String boardName = input.getText().toString().trim();
                if(boardName.length() > 0){
                    createNewBoard(boardName);
                }else {
                    Toast.makeText(BoardActivity.this, R.string.message_edit_name_board, Toast.LENGTH_SHORT).show();
                }
            }
        });

        AlertDialog dialog =  builder.create();
        dialog.show();
    }

    private void showAlertForEditingBoard(String title, String message, final Board board){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Se valida los campos
        if(title != null)builder.setTitle(title);
        if(message != null)builder.setTitle(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_board, null);
        builder.setView(viewInflated);
        final EditText input = viewInflated.findViewById(R.id.editTextBoard);
        input.setText(board.getTitle());

        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String boardName = input.getText().toString().trim();

                if(boardName.length() == 0){
                    Toast.makeText(BoardActivity.this, R.string.message_edit_name_board, Toast.LENGTH_LONG).show();
                }else  if(boardName.equals(board.getTitle())){
                    Toast.makeText(BoardActivity.this, R.string.message_name_equals, Toast.LENGTH_LONG).show();
                }else{
                    editBoard(boardName, board);
                }
            }
        });

        AlertDialog dialog =  builder.create();
        dialog.show();
    }


    /********************** EVENTS ***********************/

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(BoardActivity.this, NotesActivity.class);
        intent.putExtra("id", boards.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onChange(RealmResults<Board> boards) {
        boardAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_board, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.delete_all:
                deleteAll();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(boards.get(info.position).getTitle());
        getMenuInflater().inflate(R.menu.context_menu_board_activity, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()){

            case R.id.delete_board:
                deleteBoard(boards.get(info.position));
                return true;

            case R.id.edit_board:

                showAlertForEditingBoard("Edit Board<", "Change the name of the board", boards.get(info.position));
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

}

