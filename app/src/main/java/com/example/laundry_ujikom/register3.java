package com.example.laundry_ujikom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.laundry_ujikom.Model.Outlet;
import com.example.laundry_ujikom.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Key;
import java.util.ArrayList;

public class register3 extends AppCompatActivity {
    Spinner outlet_kategori;
    Outlet outlet, nama1;
    ArrayList<String> mOutlet;
    ArrayAdapter adapter;
    DatabaseReference dbr_outlet,dbr_user;
    String key, keys, name,username,password,role, id_user,item;
    Button btn_kasir;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    private User kasir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);
        outlet_kategori = (Spinner) findViewById(R.id.outlet_spinner);
        btn_kasir = (Button) findViewById(R.id.btn_register3);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        outlet = new Outlet();
        mOutlet = new ArrayList<>();
        dbr_outlet = FirebaseDatabase.getInstance().getReference("Outlet");
        dbr_user = FirebaseDatabase.getInstance().getReference("user");
        kasir = new User();



        name = getIntent().getStringExtra("name");
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");
        role = getIntent().getStringExtra("role");


//        dbr_outlet.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                outlet = dataSnapshot.getValue(Outlet.class);
//                for (DataSnapshot kategoriItem: dataSnapshot.getChildren()){
////                    Outlet kategori1 = kategoriItem.getValue(Outlet.class);
//                    Outlet kategori1 = kategoriItem.getValue(Outlet.class);
//                    mOutlet.add(kategori1);
//                }
//                test();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        dbr_outlet.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
//                outlet = dataSnapshot.getValue(Outlet.class);
//                for (DataSnapshot outletSnapshot : dataSnapshot.getChildren()){
                    nama1 = dataSnapshot.getValue(Outlet.class);
                    keys = dataSnapshot.child("nama").getValue(String.class);
//                    Outlet hah = outletSnapshot.child("nama").getValue(String.class);
                    mOutlet.add(keys);

                final ArrayAdapter<String> test = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,mOutlet);
                test.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                outlet_kategori.setAdapter(test);
                outlet_kategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        item = adapterView.getItemAtPosition(i).toString();
                        if (keys.equals(item)){
                            key = dataSnapshot.child(item).getKey();
                        }

                        dbr_outlet.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               for (DataSnapshot outletsnapshot : dataSnapshot.getChildren()){
                                   if (outletsnapshot.child("nama").getValue(String.class).equals(item)){
                                       key = outletsnapshot.getKey();
                                   }
//                                   String haha = dataSnapshot.child(key).child("id_outlet").getValue(String.class);
                                   Toast.makeText(register3.this, key, Toast.LENGTH_SHORT).show();

                               }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                }
//            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        kasir_register();



    }

    private void kasir_register(){
        btn_kasir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(register3.this, keys, Toast.LENGTH_SHORT).show();



                mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        id_user = mAuth.getUid();

                        kasir = new User(id_user,username,password,role,name,key);
                        dbr_user.child(mAuth.getUid()).setValue(kasir).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(register3.this, "berhasil login", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                        });


                    }
                });
            }
        });
    }
}
