package com.example.laundry_ujikom;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class home extends AppCompatActivity implements View.OnClickListener {
    ConstraintLayout lp,outlet,users,transaction;
    public FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private TextView test_logout;
    private DatabaseReference dbr;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        lp = (ConstraintLayout) findViewById(R.id.feature_1);
        outlet = (ConstraintLayout) findViewById(R.id.feature_2);
        users = (ConstraintLayout) findViewById(R.id.feature_3);
        transaction = (ConstraintLayout) findViewById(R.id.feature_4);
        test_logout = (TextView) findViewById(R.id.textView2);
        loadingDialog = new LoadingDialog(home.this);

        lp.setOnClickListener(this);
        outlet.setOnClickListener(this);
        users.setOnClickListener(this);
        transaction.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        dbr = FirebaseDatabase.getInstance().getReference("user");

        logout();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

//                if (firebaseAuth.getCurrentUser() == null){
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                }
//                else {
//                    dbr.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            String userType = dataSnapshot.child("role").getValue().toString();
//                            if (userType.equals("Owner")){
//                                Toast.makeText(home.this, userType, Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(getApplicationContext(), kasir_home.class));
//                            }
//                            else if (userType.equals("Admin")){
//                                Toast.makeText(home.this, userType, Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(getApplicationContext(), home.class));
//                            }
//
//                            else if (userType.equals("Kasir")){
//                                Toast.makeText(home.this, userType, Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(getApplicationContext(), kasir_home.class));
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//                }
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (null != authStateListener) {
            mAuth.addAuthStateListener(authStateListener);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (null != authStateListener) {
            mAuth.removeAuthStateListener(authStateListener);
//        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.feature_1:
//                overridePendingTransition(0, 0);
                startActivity(new Intent(getApplicationContext(), Laundry_package.class));
//                overridePendingTransition(0, 0);
                break;
            case R.id.feature_2:
                startActivity(new Intent(getApplicationContext(), Laundry_Outlet.class));
                break;
            case R.id.feature_3:
                startActivity(new Intent(getApplicationContext(), Laundry_user.class));
                break;
            case R.id.feature_4:
                startActivity(new Intent(getApplicationContext(), kasir_home.class));
        }
    }

    public void logout(){
        test_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismissDialog();
                        Toast.makeText(home.this, "Berhasil Logout", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }, 2000);



            }
        });
    }
}
