package com.example.loupgarou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        TextView textView = findViewById(R.id.textView8);
        String text = "vous n'avez pas encore de compte? créez-en un !";
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        textView.setText(content);
    }

    public void connectAccount(View view) {
        //connect Account
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openCreateAccountActivity(View view) {
        Intent intent = new Intent(this,createAccountActivity.class);
        startActivity(intent);
    }
}