package com.example.loupgarou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class createAccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailfield,passwordfield,passwordconfirmedfield;
    private Button signinbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        TextView textView = findViewById(R.id.textView1);
        String text = "vous avez déjà un compte? connectez-vous !";
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        textView.setText(content);


        mAuth = FirebaseAuth.getInstance();
        emailfield = findViewById(R.id.editTextText2);
        passwordfield = findViewById(R.id.editTextText3);
        signinbutton = findViewById(R.id.editTextText5);

        signinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailfield.getText().toString().trim();
                String password = passwordfield.getText().toString().trim();
                if (email.isEmpty()){
                    emailfield.setError("Email cannot be empty");
                }
                if (password.isEmpty()){
                    passwordfield.setError("Password cannot be empty");
                } else{
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(createAccountActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(createAccountActivity.this, LoginActivity.class));
                            } else {
                                Toast.makeText(createAccountActivity.this, "SignUp Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });





    }


    public void openLoginActivity(View view) {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}