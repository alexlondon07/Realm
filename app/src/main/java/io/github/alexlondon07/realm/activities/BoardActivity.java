package io.github.alexlondon07.realm.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import io.github.alexlondon07.realm.R;

public class BoardActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boar);
        loadView();
    }

    private void loadView() {
        floatingActionButton = findViewById(R.id.floatActionButtonAdd);
    }


    private void showAlertForCreatingboard(String title, String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

    }
}
