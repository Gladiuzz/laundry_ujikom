package com.example.laundry_ujikom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.laundry_ujikom.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    EditText txt_name, txt_email, txt_password;
    Button btn_register;
    String id;
    DatabaseReference dbr;
    FirebaseAuth mAuth;
    private String role;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loadingDialog = new LoadingDialog(Register.this);

        txt_name = (EditText) findViewById(R.id.nama_lengkap);
        txt_email = (EditText) findViewById(R.id.username2);
        txt_password = (EditText) findViewById(R.id.Password2);
        btn_register = (Button) findViewById(R.id.btn_register);

        mAuth = FirebaseAuth.getInstance();
        dbr = FirebaseDatabase.getInstance().getReference("user");

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

    }

    private void register() {
        final String name = txt_name.getText().toString().trim();
        final String username = txt_email.getText().toString().trim()+"@gmail.com";
        final String password = txt_password.getText().toString().trim();
        final String role = "Admin";

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(username)){
            Toast.makeText(this, "Enter username", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
        }

//        mAuth.createUserWithEmailAndPassword(username, password)
//                .addOnCompleteListener(Register.this ,new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (!task.isSuccessful()){
//                            Toast.makeText(Register.this, "Register failed", Toast.LENGTH_SHORT).show();
//                        }
//                        else {
        id = mAuth.getUid();
        String id_outlet = "123321";
        Intent i = new Intent(getApplicationContext(), register2.class);
        i.putExtra("id_user",id);
        i.putExtra("username",username);
        i.putExtra("password",password);
        i.putExtra("name",name);
        startActivity(i);

//                            User user = new User(id,username,password,role,name,id_outlet);
//
////                            if (user.getRole().equals("Admin")){
//                                dbr.child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        Toast.makeText(Register.this, "Register Success", Toast.LENGTH_SHORT).show();
//                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                                    }
//                                });
//                            }
//
//                        }
//                    }
//                });



    }
}
