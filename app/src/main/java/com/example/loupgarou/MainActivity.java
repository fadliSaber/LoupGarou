package com.example.loupgarou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText roomCode;
    private Button joinButton;
    private DatabaseReference roomRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        joinButton = findViewById(R.id.button1);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateRoomActivity(v);
            }
        });

    }

    public void openCreateRoomActivity(View view) {
        mAuth = FirebaseAuth.getInstance();
        roomRef = FirebaseDatabase.getInstance().getReference("rooms");
        String code = generateUniqueCode(5);
        User user = new User(mAuth.getCurrentUser().getUid(),"actif","");
        List<User> users = new ArrayList<>();
        users.add(user);
        Room room = new Room(code,0,users,0);
        roomRef.child(code).setValue(room);
        Intent intent = new Intent(this, waitRoomActivity.class);
        intent.putExtra("ROOM_CODE",code);
        startActivity(intent);
    }

    public void openpopUp(View view){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.popup,null);
        int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;

        boolean focusable = true;
        PopupWindow popupWindow = new PopupWindow(popUpView,width,height,focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER,0,0);

        ImageView closeBtn = popUpView.findViewById(R.id.ic_close);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the popup when the close button is clicked
                popupWindow.dismiss();
            }
        });

        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Detect touch outside the PopupWindow's bounds
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    // Dismiss the PopupWindow
                    popupWindow.dismiss();
                    return true; // Consume the touch event
                }
                return false; // Continue dispatching touch event
            }
        });

        roomCode = findViewById(R.id.editTextText3);
        joinButton = findViewById(R.id.button9);
        roomRef = FirebaseDatabase.getInstance().getReference("rooms");
        mAuth = FirebaseAuth.getInstance();

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = roomCode.getText().toString().trim();
                roomRef.child(code).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.exists()){
                            roomCode.setError("Le code de partie n'est pas disponible");
                            Toast.makeText(MainActivity.this,"Partie introuvable",Toast.LENGTH_SHORT).show();
                        }else {
                            Room room = snapshot.getValue(Room.class);
                            String userId = mAuth.getCurrentUser().getUid();
                            User user = new User(userId, "actif", "");
                            List<User> joinedUsers = snapshot.child("users")
                                    .getValue(new GenericTypeIndicator<List<User>>() {
                                    });
                            joinedUsers.add(user);
                            roomRef.child(code).child("users").setValue(joinedUsers);
                            Intent intent = new Intent(MainActivity.this,waitRoomActivity.class);
                            intent.putExtra("ROOM_CODE", code);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("User adding", "addUser:onCancelled", error.toException());
                    }
                });

            }
        });

    }
    public String generateUniqueCode(int length){
        boolean codeAlreadyInUse;
        int leftLetter = 97; // letter 'a'
        int rightLetter = 122; // letter 'z'
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(length);
        String code = "";
        do{
            for (int i = 0; i < length; i++) {
                int randomChoosedNumber = leftLetter + (int)
                        (random.nextFloat() * (rightLetter - leftLetter + 1));
                buffer.append((char) randomChoosedNumber);
            }
            code = buffer.toString();
            /*check if generatedCode is used in another Room, break if not: clear the buffer and try
            again*/
            codeAlreadyInUse = roomCodeExists(code);
        }while(codeAlreadyInUse);

        return code;
    }

    private boolean roomCodeExists(String code) {
        final boolean[] exists = {false};
        roomRef.child(code).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    exists[0] =  false;
                    return;
                }else{
                    exists[0] = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Room fetching", "fetchRoom:onCancelled", error.toException());
            }
        });

        return exists[0];

    }

}