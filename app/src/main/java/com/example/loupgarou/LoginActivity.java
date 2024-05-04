package com.example.loupgarou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailfield,passwordfield;
    private Button loginbutton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        TextView textView = findViewById(R.id.textView8);
        String text = "vous n'avez pas encore de compte? créez-en un !";
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        textView.setText(content);

        mAuth = FirebaseAuth.getInstance();
        emailfield = findViewById(R.id.editTextText2);
        passwordfield = findViewById(R.id.editTextText3);
        loginbutton = findViewById(R.id.loginbutton);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailfield.getText().toString().trim();
                String password = passwordfield.getText().toString().trim();
                if(email.isEmpty()){
                    emailfield.setError("email field cant be empty!");
                }else if(password.isEmpty()){
                    passwordfield.setError("password cant be empty");
                }else{
                    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        emailfield.setError("format of email not convenient!");
                    }else{
                        mAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

    public void openCreateAccountActivity(View view) {
        Intent intent = new Intent(this,createAccountActivity.class);
        startActivity(intent);
    }
}