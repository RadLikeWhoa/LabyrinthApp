package ch.fhnw.labyrinthapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ConnectActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
    }

    protected void onConnectClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        TextView ipAddr = (TextView) findViewById(R.id.ipAddrTextView);
        TextView ipPort = (TextView) findViewById(R.id.ipPortTextView);

        if (ipAddr != null && ipPort != null) {
            intent.putExtra("ipAddress", ipAddr.getText());
            intent.putExtra("ipPort", ipPort.getText());
        }

        startActivity(intent);
    }
}
