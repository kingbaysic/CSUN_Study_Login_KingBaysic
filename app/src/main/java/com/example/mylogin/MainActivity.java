package com.example.mylogin;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mylogin.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public Button LoginButton;
    public TextView RegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoginButton = (Button) findViewById(R.id.btn);
        RegisterButton =(TextView) findViewById(R.id.need_new_account_link);

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Registration.class);
                startActivity(intent);
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomePage.class);
                startActivity(intent);
            }
        });

        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.fragment_container,new MathGroupFragment());
        fragmentTransaction.add(R.id.fragment_container,new HistoryGroupFragment());
        //fragmentTransaction.commit();
    }
}