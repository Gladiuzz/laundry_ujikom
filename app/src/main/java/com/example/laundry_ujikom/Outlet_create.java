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
import com.example.laundry_ujikom.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Outlet_create extends AppCompatActivity {
    private EditText nama, alamat, tlp;
    private Button btn_submit;

    private DatabaseReference dbr_outlet, dbr_user;
    private FirebaseAuth mAuth;

    private String  name, username, password,role,idOutlet,id_user;
    private Outlet outlet;
    private User owner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet_create);
        nama = (EditText) findViewById(R.id.nama_outlet);
        alamat = (EditText) findViewById(R.id.alamat);
        tlp = (EditText) findViewById(R.id.phone);
        btn_submit = (Button) findViewById(R.id.btn_outlet);

//        id_user = getIntent().getStringExtra("id_user");
        name = getIntent().getStringExtra("name");
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");
        role = getIntent().getStringExtra("role");

        mAuth = FirebaseAuth.getInstance();

        outlet = new Outlet();
        owner = new User();

        dbr_outlet = FirebaseDatabase.getInstance().getReference("Outlet");
        dbr_user = FirebaseDatabase.getInstance().getReference("user");


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOutlet();
//                Toast.makeText(Outlet_create.this, name+""+username+""+password+""+role, Toast.LENGTH_SHORT).show();
            }
        });





    }

    private void addOutlet() {
        String nama_outlet = outlet.setNama(nama.getText().toString().trim());
        String alamat_outlet = outlet.setAlamat(alamat.getText().toString().trim());
        String phone_outlet = outlet.setTlp(tlp.getText().toString());




        idOutlet = dbr_outlet.push().getKey();

        Outlet outlet = new Outlet(idOutlet,nama_outlet,alamat_outlet,phone_outlet);


        mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                id_user = mAuth.getUid();
                User test = new User(id_user,username,password,role,name,idOutlet);
                dbr_user.child(mAuth.getUid()).setValue(test);
//                (String id_user, String email, String password,String role, String nama,String id_outlet)

            }
        });

        dbr_outlet.child(idOutlet).setValue(outlet).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                Toast.makeText(Outlet_create.this, "Berhasil", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}
