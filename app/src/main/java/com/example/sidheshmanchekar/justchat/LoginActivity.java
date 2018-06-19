package com.example.sidheshmanchekar.justchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText loginmail, loginpass;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private TextView newUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginmail=(EditText)findViewById(R.id.etlogmail);
        loginpass=(EditText)findViewById(R.id.etlogpass);
        newUser=(TextView)findViewById(R.id.tvreg);

        mAuth= FirebaseAuth.getInstance();
        reference= FirebaseDatabase.getInstance().getReference().child("Users");

        newUser.setOnClickListener(this);

    }

    public void loginUser(View view){
        final String email=loginmail.getText().toString().trim();
        final String passw=loginpass.getText().toString().trim();

        if (!TextUtils.isEmpty(email)&& !TextUtils.isEmpty(passw)){
            mAuth.signInWithEmailAndPassword(email,passw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        checkUserExists();
                    }
                }

                private void checkUserExists() {
                    final String user_id = mAuth.getCurrentUser().getUid();
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(user_id)){
                               startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                Toast.makeText(getApplicationContext(),"Log in Successfully...",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(),"Error!!! Please try again later...",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });

        }

    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }
}
