package com.example.laundry_ujikom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class kasir_home extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference dbr;
    private TextView txtName;

    ConstraintLayout customer,transaction,generate_laporan;
    private String id_user;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kasir_home);
        customer = (ConstraintLayout) findViewById(R.id.feature_1_kasir);
        transaction = (ConstraintLayout) findViewById(R.id.feature_2_kasir);
        generate_laporan = (ConstraintLayout) findViewById(R.id.feature_3_kasir);
        txtName = (TextView) findViewById(R.id.textView4);

        customer.setOnClickListener(this);
        transaction.setOnClickListener(this);
        generate_laporan.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        id_user = currentUser.getUid();

        loadingDialog = new LoadingDialog(kasir_home.this);
        dbr = FirebaseDatabase.getInstance().getReference("user").child(id_user);

        signout();
    }

    private void signout() {
        txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismissDialog();
                        Toast.makeText(kasir_home.this, "Berhasil Logout", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }, 2000);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.feature_1_kasir:
                startActivity(new Intent(getApplicationContext(), laundry_customer.class));
                break;
            case R.id.feature_2_kasir:
                startActivity(new Intent(getApplicationContext(), laundry_transaksi.class));
                break;
            case R.id.feature_3_kasir:
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
    }
}
