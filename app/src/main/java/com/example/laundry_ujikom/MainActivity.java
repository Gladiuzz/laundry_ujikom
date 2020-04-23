package com.example.laundry_ujikom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laundry_ujikom.Model.PaketLaundry;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private Button login_btn;
    private FirebaseAuth mAuth;
    private EditText txt_username, txt_password;
    public FirebaseAuth.AuthStateListener authStateListener;
    public DatabaseReference  dbr;
    String RegisteredIdUser;

    private User user;

    public TextView test;

    private LoadingDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        User user = new User();

        dbr = FirebaseDatabase.getInstance().getReference("user");

        test = (TextView) findViewById(R.id.textView3);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(), Register.class));
            }
        });


        txt_username = (EditText) findViewById(R.id.username);
        txt_password = (EditText) findViewById(R.id.Password);
        login_btn = findViewById(R.id.btn_login);
        loadingDialog = new LoadingDialog(MainActivity.this);

        Login();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() != null){
                    dbr.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String userType = dataSnapshot.child("role").getValue().toString();
                            if (userType.equals("Owner")){

                                Toast.makeText(MainActivity.this, userType, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), kasir_home.class));
                            }
                            else if (userType.equals("Admin")){
                                Toast.makeText(MainActivity.this, userType, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), home.class));
                            }

                            else if (userType.equals("Kasir")){
                                Toast.makeText(MainActivity.this, userType, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), kasir_home.class));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        };
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        mAuth.addAuthStateListener(authStateListener);
//    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != authStateListener) {
            mAuth.removeAuthStateListener(authStateListener);
        }
    }





    private void Login(){
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = txt_username.getText().toString().trim()+"@gmail.com";
                final String Password = txt_password.getText().toString().trim();
//                String role = user.getRole();
//                User user = new User();

                if (TextUtils.isEmpty(username)){
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(Password)){
                    Toast.makeText(getApplicationContext(), "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(username, Password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Sign In failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        else {
                            FirebaseUser currentuser = mAuth.getCurrentUser();
                            RegisteredIdUser = currentuser.getUid();
//                            user.getDisplayName();

//                            FirebaseUser currentUser = mAuth.getCurrentUser();

                            loadingDialog.startLoadingDialog();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loadingDialog.dismissDialog();
                                }
                            }, 3000);

                            dbr.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String userType = dataSnapshot.child("role").getValue().toString();
                                    if (userType.equals("Owner")){

                                        Toast.makeText(MainActivity.this, userType, Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), kasir_home.class));
                                    }
                                    else if (userType.equals("Admin")){
                                        Toast.makeText(MainActivity.this, userType, Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), home.class));
                                    }

                                    else if (userType.equals("Kasir")){
                                        Toast.makeText(MainActivity.this, userType, Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), kasir_home.class));
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
//
//                            Toast.makeText(MainActivity.this, "test", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(getApplicationContext(), home.class));

                        }
                    }
                });
            }
        });
    }


}
