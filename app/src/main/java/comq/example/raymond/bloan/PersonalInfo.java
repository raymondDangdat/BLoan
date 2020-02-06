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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PersonalInfo extends AppCompatActivity {
    private Toolbar personalInfoToolBar;
    private TextView txtFullName;
    private TextView txtAccountName;

    private String accountName = "";
    String fullName = "";

    private DatabaseReference bvnRef, users;
    private FirebaseAuth mAuth;
    private String uId = "";

    private ProgressDialog dialog;

    private Button btnBvn;
    private Button btnAccount;
    private EditText editTextAccount;
    private EditText editTextBvn;

    private String bvn = "";
    private String account = "";
    private String phone = "";
    private String dob = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        dialog = new ProgressDialog(this);

        bvnRef = FirebaseDatabase.getInstance().getReference().child("bLoan").child("BVN");
        users = FirebaseDatabase.getInstance().getReference().child("bLoan").child("users");
        mAuth = FirebaseAuth.getInstance();
        uId = mAuth.getCurrentUser().getUid();

        //toolbar
        //initialize our toolBar
        personalInfoToolBar = findViewById(R.id.personalInfotoolbar);
        setSupportActionBar(personalInfoToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Personal Information");

        editTextBvn = findViewById(R.id.editBVN);
        editTextAccount = findViewById(R.id.editAccount);
        btnAccount = findViewById(R.id.buttonAccount);
        btnBvn = findViewById(R.id.buttonBvn);

        txtFullName = findViewById(R.id.txtFullName);
        txtAccountName = findViewById(R.id.txtAccountNumber);

        btnBvn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextBvn.setVisibility(View.VISIBLE);
                btnBvn.setText("Submit");

                btnBvn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        verifyBvn();
                    }
                });
            }
        });

        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextAccount.setVisibility(View.VISIBLE);
                btnAccount.setText("Submit");

                btnAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        verifyAccount();
                    }
                });
            }
        });
    }

    private void verifyAccount() {
        account = editTextAccount.getText().toString().trim();

        if (TextUtils.isEmpty(account)){
            Toast.makeText(this, "Please enter a valid account number", Toast.LENGTH_SHORT).show();
        }else if (account.length()< 10){
            Toast.makeText(this, "Please enter a valid account number", Toast.LENGTH_SHORT).show();
        }else {
            dialog.setMessage("Verifying Account...");
            dialog.show();
            bvnRef.child(bvn).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final String accountNo = dataSnapshot.child("account").getValue(String.class);
                    if (accountNo.equals(account)){
                        dialog.dismiss();
                        editTextAccount.setVisibility(View.GONE);
                        //Toast.makeText(PersonalInfo.this, "good!", Toast.LENGTH_SHORT).show();
                        btnAccount.setText("Save Details");
                        btnAccount.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                saveDetails();
                            }
                        });
                    }else {
                        dialog.dismiss();
                        Toast.makeText(PersonalInfo.this, "Account number does not exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void saveAccountNumber() {
        dialog.setMessage("Saving details...");
        dialog.show();
        users.child(uId).child("account").setValue(account).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            dialog.dismiss();
                            Toast.makeText(PersonalInfo.this, "Details Saved", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PersonalInfo.this, CustomerHome.class));
                        }
                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(PersonalInfo.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verifyBvn() {
        bvn = editTextBvn.getText().toString().trim();

        if (TextUtils.isEmpty(bvn)){
            Toast.makeText(this, "Enter a valid BVN", Toast.LENGTH_SHORT).show();
        }else if (bvn.length() < 10){
            Toast.makeText(this, "Please enter a valid BVN", Toast.LENGTH_SHORT).show();
        }else {
            dialog.setMessage("Verifying BVN...");
            dialog.show();
            bvnRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(bvn).exists()){
                        dialog.dismiss();
                        editTextBvn.setVisibility(View.GONE);
                        String surname = dataSnapshot.child(bvn).child("surname").getValue(String.class);
                        String firstName  = dataSnapshot.child(bvn).child("firstName").getValue(String.class);
                        String otherName = dataSnapshot.child(bvn).child("otherName").getValue(String.class);
                        dob = dataSnapshot.child(bvn).child("dob").getValue().toString();
                        phone = dataSnapshot.child(bvn).child("phone").getValue(String.class);

                        fullName = surname + " " + firstName + " " + otherName;

                        txtFullName.setVisibility(View.VISIBLE);
                        txtFullName.setText(fullName);

                        btnBvn.setVisibility(View.GONE);

                        btnAccount.setVisibility(View.VISIBLE);
                        //editTextAccount.setVisibility(View.VISIBLE);

                    }else {
                        dialog.dismiss();
                        Toast.makeText(PersonalInfo.this, "BVN does not exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void saveDetails() {
        dialog.setMessage("Saving details...");
        dialog.show();
        users.child(uId).child("fullName").setValue(fullName);
        users.child(uId).child("phone").setValue(phone);
        users.child(uId).child("dob").setValue(dob);
        users.child(uId).child("account").setValue(account);
        users.child(uId).child("bvn").setValue(bvn).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            dialog.dismiss();
                            Toast.makeText(PersonalInfo.this, "Details Saved", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PersonalInfo.this, CustomerHome.class));
                        }
                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(PersonalInfo.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
