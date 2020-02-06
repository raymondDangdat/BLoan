package comq.example.raymond.bloan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import comq.example.raymond.bloan.Model.MessageModel;

public class AdminReplyComplaintActivity extends AppCompatActivity {
    private Toolbar adminReplyComplaintToolBar;
    private DatabaseReference messages;

    private String messageId = "";
    private String senderId = "";

    private TextView txt_message;
    private EditText editTextMessage;
    private Button btnSend;
    private MessageModel newMessage;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reply_complaint);

        dialog = new ProgressDialog(this);

        messages = FirebaseDatabase.getInstance().getReference().child("bLoan").child("messages");


        txt_message = findViewById(R.id.txt_message);

        editTextMessage = findViewById(R.id.editTextMessage);
        btnSend = findViewById(R.id.buttonSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });

        //toolbar
        //initialize our toolBar
        adminReplyComplaintToolBar = findViewById(R.id.adminReplyComplaintToolbar);
        setSupportActionBar(adminReplyComplaintToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Reply Complaint");

        if (getIntent() != null){
            messageId =  getIntent().getStringExtra("messageId");
            if (!messageId.isEmpty()){
                getMessageDetail(messageId);
            }
        }


    }

    private void sendFeedback() {

        String message = editTextMessage.getText().toString().trim();

        final long dateSent = new Date().getTime();

        if (TextUtils.isEmpty(message)){
            Toast.makeText(this, "Type your reply ", Toast.LENGTH_SHORT).show();
        }else {
            dialog.setMessage("Sending message...");
            dialog.show();
            newMessage = new MessageModel(senderId, "admin", message, dateSent);

            messages.push().setValue(newMessage).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    dialog.dismiss();
                    Toast.makeText(AdminReplyComplaintActivity.this, "Message sent!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AdminReplyComplaintActivity.this, AdminCustomerCareActivity.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(AdminReplyComplaintActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void getMessageDetail(String messageId) {
        messages.child(messageId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);


                senderId = dataSnapshot.child("uId").getValue(String.class);
                txt_message.setText(messageModel.getMessage());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
