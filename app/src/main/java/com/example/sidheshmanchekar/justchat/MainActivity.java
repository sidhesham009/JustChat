package com.example.sidheshmanchekar.justchat;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private EditText editmessage;
    private DatabaseReference db;
    private RecyclerView messageList;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private DatabaseReference dbUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editmessage = (EditText) findViewById(R.id.editmsg);
        db = FirebaseDatabase.getInstance().getReference().child("Message");
        messageList = (RecyclerView) findViewById(R.id.msglist);
        messageList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageList.setLayoutManager(linearLayoutManager);
        auth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                }
            }
        };

    }

    public void sendButtonClicked(View v) {
        currentUser = auth.getCurrentUser();
        dbUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());

        final String msgValue = editmessage.getText().toString().trim();
        if (!TextUtils.isEmpty(msgValue)) {
            final DatabaseReference newPost = db.push();
            dbUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    newPost.child("Content").setValue(msgValue);
                    newPost.child("Username").setValue(dataSnapshot.child("Name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            messageList.scrollToPosition(messageList.getAdapter().getItemCount());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Message, MessageViewHolder> FBRA = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(

                Message.class,
                R.layout.singlemessage,
                MessageViewHolder.class,
                db
        ) {
            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, Message model, int position) {
                viewHolder.setContent(model.getContent());
                viewHolder.setContent(model.getUsername());

            }
        };
        messageList.setAdapter(FBRA);
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        View view;

        public MessageViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setContent(String content) {
            TextView messageContent = (TextView) view.findViewById(R.id.textmsg);
            messageContent.setText(content);
        }

        public void username(String username) {
            TextView username_content = (TextView) view.findViewById(R.id.etusrname);
            username_content.setText(username);
        }
    }
}
