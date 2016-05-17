package ch.fhnw.labyrinthapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ConnectActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
    }

    protected void onConnectClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
