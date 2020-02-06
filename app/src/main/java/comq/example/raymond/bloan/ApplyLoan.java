package comq.example.raymond.bloan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import comq.example.raymond.bloan.Model.LoanHistoryModel;

public class ApplyLoan extends AppCompatActivity {
    private Toolbar applyLoanToolBar;

    private DatabaseReference loan, customer;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;

    private String uId = "";
    private String uName = "";
    private String accountNumber = "";
    private String bvn = "";

    private Button btn_10k, btn_5k, btn_2k;

    private LoanHistoryModel newLoan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_loan);

        customer = FirebaseDatabase.getInstance().getReference().child("bLoan").child("users");
        mAuth = FirebaseAuth.getInstance();
        uId = mAuth.getCurrentUser().getUid();
        loan = FirebaseDatabase.getInstance().getReference().child("bLoan").child("loans");

        //toolbar
        //initialize our toolBar
        applyLoanToolBar = findViewById(R.id.applyLoanToolbar);
        setSupportActionBar(applyLoanToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Apply Loan");

        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);

        getUserDetails();

        btn_10k = findViewById(R.id.btn_10k);
        btn_5k = findViewById(R.id.btn_5k);
        btn_2k = findViewById(R.id.btn_2k);

        btn_10k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apply_10k_dialog();

            }
        });

        btn_5k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                apply_5k_dialog();
            }
        });

        btn_2k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                apply_2k_dialog();
            }
        });



    }

    private void apply_5k_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ApplyLoan.this);
        View view = getLayoutInflater().inflate(R.layout.apply_loan_dialog, null);

        final TextView txt_account_name = view.findViewById(R.id.account_name);
        final TextView txt_account_number = view.findViewById(R.id.account_number);
        final TextView txt_loan_amount = view.findViewById(R.id.amount);
        //final Spinner spinnerAccount = view.findViewById(R.id.spinner_account_number);
        Button btn_change_account = view.findViewById(R.id.btn_chane_account);
        Button btn_apply = view.findViewById(R.id.btn_apply);

        txt_account_name.setText("Acc. Name: " +uName);
        txt_account_number.setText("Acc. No:" + accountNumber);

        final double amount = 5000.0;
        final double interest = amount * 0.15;
        final double totalDue = amount + interest;
        txt_loan_amount.setText("Amount: " + amount + " With 15% Interest of: " + interest);

        final String amountString = String.valueOf(amount);
        final String interestString = String.valueOf(interest);

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("Requesting...");
                dialog.show();

                final long dateSent = new Date().getTime();

                newLoan = new LoanHistoryModel(amount, interest, totalDue, "Unpaid", uId, uName, accountNumber, bvn, dateSent);
                loan.child(uId).setValue(newLoan).addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    dialog.dismiss();
                                    Toast.makeText(ApplyLoan.this, "Loan Sent Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ApplyLoan.this, MyAccountActivity.class));
                                }
                            }
                        }
                ).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(ApplyLoan.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


                //Toast.makeText(ApplyLoan.this, ""+amount +" " + interest, Toast.LENGTH_SHORT).show();
            }
        });


//
//        //set onclick listener on login button
//        btn_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!editTextEmail.getText().toString().trim().isEmpty() || !password.getText().toString().trim().isEmpty()){
//                    progressDialog.setTitle("Login");
//                    progressDialog.setMessage("Logging in...");
//                    progressDialog.show();
//                    mAuth.signInWithEmailAndPassword(editTextEmail.getText().toString().trim(), password.getText().toString().trim())
//                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                                @Override
//                                public void onSuccess(AuthResult authResult) {
//                                    if (editTextEmail.getText().toString().equals("churchadmin@gmail.com")){
//                                        progressDialog.dismiss();
//                                        Intent profileIntent = new Intent(MainActivity.this, AdminHome.class);
//                                        profileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                        startActivity(profileIntent);
//                                        finish();
//                                    }else{
//                                        progressDialog.dismiss();
//                                        Intent profileIntent = new Intent(MainActivity.this, MemberHome.class);
//                                        profileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                        startActivity(profileIntent);
//                                        finish();
//                                    }
//
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            progressDialog.dismiss();
//                            Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                }else {
//                    Toast.makeText(MainActivity.this, "Sorry, you can't login with empty fields", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void apply_2k_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ApplyLoan.this);
        View view = getLayoutInflater().inflate(R.layout.apply_loan_dialog, null);

        final TextView txt_account_name = view.findViewById(R.id.account_name);
        final TextView txt_account_number = view.findViewById(R.id.account_number);
        final TextView txt_loan_amount = view.findViewById(R.id.amount);
        //final Spinner spinnerAccount = view.findViewById(R.id.spinner_account_number);
        Button btn_change_account = view.findViewById(R.id.btn_chane_account);
        Button btn_apply = view.findViewById(R.id.btn_apply);

        txt_account_name.setText("Acc. Name: " +uName);
        txt_account_number.setText("Acc. No:" + accountNumber);

        final double amount = 2000.0;
        final double interest = amount * 0.15;
        final double totalDue = amount + interest;
        txt_loan_amount.setText("Amount: " + amount + " With 15% Interest of: " + interest);

        final String amountString = String.valueOf(amount);
        final String interestString = String.valueOf(interest);


        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("Requesting...");
                dialog.show();

                final long dateSent = new Date().getTime();


                newLoan = new LoanHistoryModel(amount, interest, totalDue, "Unpaid", uId, uName, accountNumber, bvn, dateSent);
                loan.child(uId).setValue(newLoan).addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    dialog.dismiss();
                                    Toast.makeText(ApplyLoan.this, "Loan Sent Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ApplyLoan.this, MyAccountActivity.class));
                                }
                            }
                        }
                ).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(ApplyLoan.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


                //Toast.makeText(ApplyLoan.this, ""+amount +" " + interest, Toast.LENGTH_SHORT).show();
            }
        });


//
//        //set onclick listener on login button
//        btn_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!editTextEmail.getText().toString().trim().isEmpty() || !password.getText().toString().trim().isEmpty()){
//                    progressDialog.setTitle("Login");
//                    progressDialog.setMessage("Logging in...");
//                    progressDialog.show();
//                    mAuth.signInWithEmailAndPassword(editTextEmail.getText().toString().trim(), password.getText().toString().trim())
//                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                                @Override
//                                public void onSuccess(AuthResult authResult) {
//                                    if (editTextEmail.getText().toString().equals("churchadmin@gmail.com")){
//                                        progressDialog.dismiss();
//                                        Intent profileIntent = new Intent(MainActivity.this, AdminHome.class);
//                                        profileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                        startActivity(profileIntent);
//                                        finish();
//                                    }else{
//                                        progressDialog.dismiss();
//                                        Intent profileIntent = new Intent(MainActivity.this, MemberHome.class);
//                                        profileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                        startActivity(profileIntent);
//                                        finish();
//                                    }
//
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            progressDialog.dismiss();
//                            Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                }else {
//                    Toast.makeText(MainActivity.this, "Sorry, you can't login with empty fields", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getUserDetails() {
        customer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uName = dataSnapshot.child(uId).child("fullName").getValue(String.class);
                accountNumber = dataSnapshot.child(uId).child("account").getValue(String.class);
                bvn = dataSnapshot.child(uId).child("bvn").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void apply_10k_dialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ApplyLoan.this);
        View view = getLayoutInflater().inflate(R.layout.apply_loan_dialog, null);

        final TextView txt_account_name = view.findViewById(R.id.account_name);
        final TextView txt_account_number = view.findViewById(R.id.account_number);
        final TextView txt_loan_amount = view.findViewById(R.id.amount);
        //final Spinner spinnerAccount = view.findViewById(R.id.spinner_account_number);
        Button btn_change_account = view.findViewById(R.id.btn_chane_account);
        Button btn_apply = view.findViewById(R.id.btn_apply);

        txt_account_name.setText("Acc. Name: " +uName);
        txt_account_number.setText("Acc. No:" + accountNumber);

        final double amount = 10000.0;
        final double interest = amount * 0.15;
        final double totalDue = amount + interest;
        txt_loan_amount.setText("Amount: " + amount + " With 15% Interest of: " + interest);

        final String amountString = String.valueOf(amount);
        final String interestString = String.valueOf(interest);


        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("Requesting...");
                dialog.show();

                final long dateSent = new Date().getTime();


                newLoan = new LoanHistoryModel(amount, interest, totalDue, "Unpaid", uId, uName, accountNumber, bvn, dateSent);
                loan.child(uId).setValue(newLoan).addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    dialog.dismiss();
                                    Toast.makeText(ApplyLoan.this, "Loan Sent Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ApplyLoan.this, MyAccountActivity.class));
                                }
                            }
                        }
                ).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(ApplyLoan.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


                //Toast.makeText(ApplyLoan.this, ""+amount +" " + interest, Toast.LENGTH_SHORT).show();
            }
        });


//
//        //set onclick listener on login button
//        btn_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!editTextEmail.getText().toString().trim().isEmpty() || !password.getText().toString().trim().isEmpty()){
//                    progressDialog.setTitle("Login");
//                    progressDialog.setMessage("Logging in...");
//                    progressDialog.show();
//                    mAuth.signInWithEmailAndPassword(editTextEmail.getText().toString().trim(), password.getText().toString().trim())
//                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                                @Override
//                                public void onSuccess(AuthResult authResult) {
//                                    if (editTextEmail.getText().toString().equals("churchadmin@gmail.com")){
//                                        progressDialog.dismiss();
//                                        Intent profileIntent = new Intent(MainActivity.this, AdminHome.class);
//                                        profileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                        startActivity(profileIntent);
//                                        finish();
//                                    }else{
//                                        progressDialog.dismiss();
//                                        Intent profileIntent = new Intent(MainActivity.this, MemberHome.class);
//                                        profileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                        startActivity(profileIntent);
//                                        finish();
//                                    }
//
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            progressDialog.dismiss();
//                            Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                }else {
//                    Toast.makeText(MainActivity.this, "Sorry, you can't login with empty fields", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
