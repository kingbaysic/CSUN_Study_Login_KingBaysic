


package com.example.mylogin;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;

public class Registration extends AppCompatActivity {
    private TextView RegisterButton;
    private TextView AlreadyHaveAccountButton;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputConfirmPassword;
    private boolean passwordVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        RegisterButton = findViewById(R.id.reg_btn);
        AlreadyHaveAccountButton = findViewById(R.id.already_have_account_link);

        inputEmail = findViewById(R.id.register_email);
        inputPassword = findViewById(R.id.register_password);
        inputConfirmPassword = findViewById(R.id.confirm_password);

        inputPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int Right = 2;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= inputPassword.getRight() - inputPassword.getCompoundDrawables()[Right].getBounds().width()) {
                        int selection = inputPassword.getSelectionEnd();
                        if (passwordVisible) {
                            inputPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_off_24, 0);
                            inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible = false;
                        } else {
                            inputPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.password_icon, 0);
                            inputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible = true;
                        }
                        inputPassword.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = inputEmail.getText().toString();
                String userPassword = inputPassword.getText().toString();
                String userConfirmPassword = inputConfirmPassword.getText().toString();

                new RegistrationTask().execute(userEmail, userPassword, userConfirmPassword);
            }
        });

        AlreadyHaveAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private class RegistrationTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String userEmail = params[0];
            String userPassword = params[1];
            String userConfirmPassword = params[2];

            if (!userPassword.equals(userConfirmPassword)) {
                return false;
            }

            if(!isCsunEmail(userEmail)) {
              return false;
            }

            if (isEmailExists(userEmail)) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                new RegisterUserTask().execute(inputEmail.getText().toString(), inputPassword.getText().toString());
            } else {
                if(!inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString()))
                {
                    Toast.makeText(Registration.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(!isCsunEmail(inputEmail.getText().toString()))
                    {
                        Toast.makeText(Registration.this, "Please enter a valid CSUN email.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(Registration.this, "Email already exists!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private class RegisterUserTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String email = params[0];
            String password = params[1];

            // Additional registration logic
            registerCredentials(email, password);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(Registration.this, "Registration successful", Toast.LENGTH_SHORT).show();
        }
    }


    private void registerCredentials(String email, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String firstName = null;
        String lastName = null;

        // Define a regular expression for the required email format
        String emailPattern = "([a-zA-Z]+)\\.([a-zA-Z]+)\\.\\d{3}@my\\.csun\\.edu";

        // Use the matches method to check if the email matches the pattern
        if (email.matches(emailPattern)) {
            // Use a Matcher to extract the first name and last name
            Pattern pattern = Pattern.compile(emailPattern);
            Matcher matcher = pattern.matcher(email);

            if (matcher.find()) {
                // Extract the first name and last name
                firstName = capitalizeFirstLetter(matcher.group(1));
                lastName = capitalizeFirstLetter(matcher.group(2));
            }
        }

        try {
            connection = DatabaseConnection.connect();

            // Define the SQL query for inserting data into the "user" table
            String insertQuery = "INSERT INTO [user] (email, password, firstName, lastName) VALUES (?, ?, ?, ?);";

            // Prepare the statement with the SQL query
            preparedStatement = connection.prepareStatement(insertQuery);

            // Set values for the parameters in the SQL query
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, firstName);
            preparedStatement.setString(4, lastName);

            // Execute the insert statement
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            // Handle SQL exceptions (e.g., display an error message)
            e.printStackTrace();
        } finally {
            // Close resources in a finally block to ensure they are closed even if an exception occurs
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) DatabaseConnection.disconnect(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //Method to check if its a valid csun email
    private boolean isCsunEmail(String email) {
        // Define a regular expression for the required email format
        //String emailPattern = "[a-zA-Z]+\\.[a-zA-Z]+@my\\.csun\\.edu";
        // Adjust the email pattern to match the desired format
        String emailPattern = "[a-zA-Z]+\\.[a-zA-Z]+\\.\\d{3}@my\\.csun\\.edu";


        // Use the matches method to check if the email matches the pattern
        if (!email.isEmpty() && email.matches(emailPattern)) {
            return true;  // Email format is correct
        } else {
            // Email format is incorrect
            // You might want to display an error message or handle it in some way
            return false;
        }
    }

    // Method to check if the email already exists in the database
    private boolean isEmailExists(String email) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Connect to the database
            connection = DatabaseConnection.connect();

            // Prepare the SQL query
            String query = "SELECT COUNT(*) FROM [user] WHERE email = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);

            // Execute the query
            resultSet = preparedStatement.executeQuery();

            // Check the result
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0; // If count > 0, the email exists; otherwise, it doesn't
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources in a finally block to ensure they are closed even if an exception occurs
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) DatabaseConnection.disconnect(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Default to false in case of an exception
        return false;
    }
    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        } else {
            return Character.toUpperCase(str.charAt(0)) + str.substring(1).toLowerCase();
        }
    }
}
