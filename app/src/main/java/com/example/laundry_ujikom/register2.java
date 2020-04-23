package com.example.laundry_ujikom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class register2 extends AppCompatActivity {
    String id_user, name, username, password;
    RadioGroup rgRole;
    RadioButton rbKasir, rbOwner;
    Button btn_next;
    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        rgRole = (RadioGroup) findViewById(R.id.rg_role);
        btn_next = (Button) findViewById(R.id.btn_register2);
        id_user = getIntent().getStringExtra("id_user");
        name = getIntent().getStringExtra("name");
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");

        AddRole();

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goNext();
            }
        });


    }

    private void goNext() {
        if (value.equals("Kasir")){
            Intent i = new Intent(getApplicationContext(), register3.class);
//            i.putExtra("id_user",id_user);
            i.putExtra("name",name);
            i.putExtra("username",username);
            i.putExtra("password",password);
            i.putExtra("role",value);
            startActivity(i);
        }
        else if (value.equals("Owner")){
            Intent i = new Intent(getApplicationContext(), Outlet_create.class);
//            i.putExtra("id_user",id_user);
            i.putExtra("name",name);
            i.putExtra("username",username);
            i.putExtra("password",password);
            i.putExtra("role",value);
            startActivity(i);
        }

    }

    public void AddRole() {
        rgRole.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_kasir:
                        value = "Kasir";
                        Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rb_owner:
                        value = "Owner";
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}
