package com.example.laundry_ujikom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.laundry_ujikom.Model.Outlet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class create_outlet extends AppCompatActivity {
    EditText outlet_name,outlet_address,outlet_phone;
    Button btn_sbumit;
    Outlet outlet;
    String id_outlet;

    private LoadingDialog loadingDialog;

    private DatabaseReference dbr_outlet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_outlet);

        iniComponents();
        outlet = new Outlet();

        loadingDialog = new LoadingDialog(create_outlet.this);

        dbr_outlet = FirebaseDatabase.getInstance().getReference("Outlet");
        
        btn_sbumit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createOutlet();
            }
        });
    }

    private void createOutlet() {
        String nama = outlet.setNama(outlet_name.getText().toString().trim());
        String alamat = outlet.setAlamat(outlet_address.getText().toString().trim());
        String tlp = outlet.setTlp(outlet_phone.getText().toString().trim());


        id_outlet = dbr_outlet.push().getKey();

        Outlet outlet = new Outlet(id_outlet,nama,alamat,tlp);
        dbr_outlet.child(id_outlet).setValue(outlet).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                Toast.makeText(create_outlet.this, "Berhasil", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    private void iniComponents() {
        outlet_name = (EditText) findViewById(R.id.outlet_nama);
        outlet_address = (EditText) findViewById(R.id.alamat_outlet);
        outlet_phone = (EditText) findViewById(R.id.telephone_outlet);
        btn_sbumit = (Button) findViewById(R.id.btn_submit10);
    }
}
