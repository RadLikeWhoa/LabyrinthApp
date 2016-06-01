package ch.fhnw.labyrinthapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
            intent.putExtra("ipAddress", ipAddr.getText().toString());
            intent.putExtra("ipPort", ipPort.getText().toString());
        }

        startActivityForResult(intent, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String title = "";
        String message = "";

        if (resultCode == -1) {
            title = "Connection error";
            message = "Error when attempting to connect to the server using the IP address and port given.";
        } else if (resultCode == -2) {
            title = "Invalid port";
            message = "The port is not a valid number.";
        }

        if (resultCode == -1 || resultCode == -2) {
            new AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }
    }
}
