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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText email,username, password;
    private TextView alreadyreg;
    private FirebaseAuth auth;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email=(EditText)findViewById(R.id.etmail);
        username=(EditText)findViewById(R.id.etusrname);
        password=(EditText)findViewById(R.id.etpassw);
        alreadyreg=(TextView)findViewById(R.id.tvalready);

        auth= FirebaseAuth.getInstance();
        dbRef= FirebaseDatabase.getInstance().getReference().child("Users");

        alreadyreg.setOnClickListener(this);
    }
    public void registerUser(View view){
        final String username_content, password_content, email_content;
        username_content= username.getText().toString().trim();
        password_content=password.getText().toString().trim();
        email_content=email.getText().toString().trim();

        if (!TextUtils.isEmpty(email_content)&& !TextUtils.isEmpty(username_content)&& !TextUtils.isEmpty(email_content)){
            auth.createUserWithEmailAndPassword(email_content,password_content).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        final String user_id= auth.getCurrentUser().getUid();
                        DatabaseReference current_user_db = dbRef.child(user_id);
                        current_user_db.child("Name").setValue(username_content);
                        startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                        Toast.makeText(getApplicationContext(),"Congrats..!!! \n Registration Successfull",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }
}
