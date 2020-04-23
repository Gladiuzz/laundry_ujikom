package com.example.laundry_ujikom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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

public class create_user extends AppCompatActivity {
    private EditText name,username,password;
    private Button btn_create;
    private Spinner role;
    private LoadingDialog loadingDialog;
    private DatabaseReference dbr;
    private User user;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    String id_outlet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        name = (EditText) findViewById(R.id.name_user);
        username = (EditText) findViewById(R.id.username_user);
        password = (EditText) findViewById(R.id.Password_user);
        role = (Spinner) findViewById(R.id.role_user);
        btn_create = (Button) findViewById(R.id.btn_submit112);
        user = new User();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();



        loadingDialog = new LoadingDialog(create_user.this);
        dbr = FirebaseDatabase.getInstance().getReference("user").child(currentUser.getUid());
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                id_outlet = user.getId_outlet();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });






    }

    private void createUser() {
        final String nama = user.setNama(name.getText().toString().trim());
        final String usernama = user.setEmail(username.getText().toString()+"@gmail.com");
        final String Password = user.setPassword(password.getText().toString().trim());
        final String Role = user.setRole(role.getSelectedItem().toString());


        mAuth.createUserWithEmailAndPassword(usernama, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String id_user = mAuth.getUid();
                User test = new User(id_user,usernama,Password,Role,nama,id_outlet);
                dbr.child(mAuth.getUid()).setValue(test);
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(create_user.this, "Berhasil", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Laundry_user.class));
//                (String id_user, String email, String password,String role, String nama,String id_outlet)

            }
        });
    }
}
