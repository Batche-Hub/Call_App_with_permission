package com.doranco.badad_tp13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int PERMISSION_REQUEST_CALL_PHONE = 0;
    private TextView entrezNumero;
    private Button appeler;
    private Context context;
    private View mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLayout = findViewById(R.id.main_Layout);
        appeler = (Button) findViewById(R.id.buttonAppeler);
        appeler.setOnClickListener(appelerListener);
        entrezNumero = (TextView) findViewById(R.id.editTextTel);
        context = getApplicationContext();
    }

    View.OnClickListener appelerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            passerAppel();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CALL_PHONE) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Permission accordée.", Toast.LENGTH_LONG).show();
                demmarrerAppel();
            } else {
                // Permission request was denied.
                Toast.makeText(context, "Vous devez donner votre permission pour passer l'appel.", Toast.LENGTH_LONG).show();
            }
        }

    }


    private void passerAppel() {
        // BEGIN_INCLUDE(startCallPhone)
        // Check if the Call Phone permission has been granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview
            Toast.makeText(context, "Permission accordée.", Toast.LENGTH_LONG).show();
            demmarrerAppel();
        } else {
            // Permission is missing and must be requested.
            demandePermissionPasserAppel();
        }
        // END_INCLUDE(startCamera)
    }

    private void demandePermissionPasserAppel() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CALL_PHONE)) {

            Snackbar.make(mLayout, R.string.callphone_access_required,
                    Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            PERMISSION_REQUEST_CALL_PHONE);
                }
            }).show();

        } else {
            Snackbar.make(mLayout, R.string.callphone_unavailable, Snackbar.LENGTH_SHORT).show();

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CALL_PHONE);
        }
    }


    private void demmarrerAppel() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + entrezNumero.getText().toString()));
        startActivity(intent);
    }
}
