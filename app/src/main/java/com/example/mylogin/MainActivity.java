package com.example.mylogin;

import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class MainActivity extends AppCompatActivity {
    public Button LoginButton;
    public TextView RegisterButton;
    public Connection db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoginButton = (Button) findViewById(R.id.btn);
        RegisterButton = (TextView) findViewById(R.id.need_new_account_link);

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
                EditText login_email = findViewById(R.id.login_email);
                EditText login_password = findViewById(R.id.login_password);

                final String email = login_email.getText().toString();
                final String password = login_password.getText().toString();

                // Start an AsyncTask to handle the database connection
                new DatabaseConnectionTask().execute(email, password);
            }
        });
    }

    private class DatabaseConnectionTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... credentials) {
            String email = credentials[0];
            String password = credentials[1];

            try {
                db = DatabaseConnection.connect();

                // SQL query to check if the user exists with the provided email and password
                String query = "SELECT * FROM [user] WHERE email = ? AND password = ?";
                PreparedStatement statement = db.prepareStatement(query);
                statement.setString(1, email);
                statement.setString(2, password);

                ResultSet result = statement.executeQuery();

                if (result.next()) {
                    // User exists and the credentials are valid
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DatabaseConnection.disconnect(db);
            }

            // User doesn't exist or credentials are invalid
            return false;
        }

        @Override
        protected void onPostExecute(Boolean isValidUser) {
            if (isValidUser) {
                System.out.println("Successfully logged in!");
                Intent intent = new Intent(MainActivity.this, HomePage.class);
                startActivity(intent);
            } else {
                System.out.println("Invalid log in credentials");
                // Show an error message
                // Display a toast message or change a TextView to display an error.
            }
        }
    }
}

/*
* package com.example.mylogin;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.sql.Connection;

import com.example.mylogin.databinding.ActivityMainBinding;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {
    public Button LoginButton;
    public TextView RegisterButton;
    public Connection db;






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
                EditText login_email = findViewById(R.id.login_email);
                EditText login_password = findViewById(R.id.login_password);

                String email = login_email.getText().toString();
                String password = login_password.getText().toString();

                System.out.println("hello hello.");

                if (isValidUser(email, password)) {
                    Intent intent = new Intent(MainActivity.this, HomePage.class);
                    startActivity(intent);
                } else {
                    System.out.println("error hello.");
                    // Show an error message
                    // Display a toast message or change a TextView to display an error.
                }
            }
        });

    }

    private boolean isValidUser(String email, String password) {
        try {
           // DatabaseConnection databaseConnection = new DatabaseConnection();
            System.out.println("made it here");
            db = DatabaseConnection.connect();

            System.out.println("made it here 4");
            // SQL query to check if the user exists with the provided email and password
            String query = "SELECT * FROM user WHERE email = ? AND password = ?";
            PreparedStatement statement = db.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                // User exists and the credentials are valid
                System.out.println("It matches");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Woah error");
            e.printStackTrace();
        } finally {
            System.out.println("disconnected");
            DatabaseConnection.disconnect(db);
        }

        // User doesn't exist or credentials are invalid
        return false;
    }
}*/