package comq.example.raymond.bloan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MyLoanActivity extends AppCompatActivity {
    private Toolbar myLoanToolBar;
    private TextView txt_principal, txt_interest, txt_total_due;
    private Button btn_tap_to_pay;

    private ProgressDialog dialog;

    private FirebaseAuth mAuth;
    private DatabaseReference loans;

    private String uId = "";
    private String principal, interest, status, totalDue;

    private MaterialEditText editTextCardName,editTextTotal, editTextCadCCV, editTextExpiry, editTextCardNumber;
    private Button btnSUbmit;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_loan);

        mAuth = FirebaseAuth.getInstance();
        loans = FirebaseDatabase.getInstance().getReference().child("bLoan").child("loans");
        uId = mAuth.getCurrentUser().getUid();

        dialog = new ProgressDialog(this);


        //toolbar
        //initialize our toolBar
        myLoanToolBar = findViewById(R.id.myLoanToolbar);
        setSupportActionBar(myLoanToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My Loan");

        txt_principal = findViewById(R.id.txt_principal);
        txt_interest = findViewById(R.id.txt_interest);
        txt_total_due = findViewById(R.id.total_due);
        btn_tap_to_pay = findViewById(R.id.tap_to_pay);

        editTextCadCCV = findViewById(R.id.edit_ccv);
        editTextCardName = findViewById(R.id.edit_card_name);
        editTextCardNumber = findViewById(R.id.edit_card_number);
        editTextExpiry = findViewById(R.id.edit_card_expiry);
        editTextTotal = findViewById(R.id.edit_total_amount);
        btnSUbmit = findViewById(R.id.btn_submit);


        loadLoan();
    }

    private void loadLoan() {
        loans.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(uId).exists()){
                    double retrievedInterest = dataSnapshot.child(uId).child("interest").getValue(Double.class);
                    double retrievedPrincipal = dataSnapshot.child(uId).child("amount").getValue(Double.class);
                    double retrievedTotalDue = dataSnapshot.child(uId).child("totalDue").getValue(Double.class);
                    status = dataSnapshot.child(uId).child("status").getValue(String.class);

                    interest = String.valueOf(retrievedInterest);
                    principal = String.valueOf(retrievedPrincipal);
                    totalDue = String.valueOf(retrievedTotalDue);

                    txt_interest.setText(interest);
                    txt_principal.setText(principal);
                    txt_total_due.setText(totalDue);


                    if (status.equals("Unpaid")){
                        btn_tap_to_pay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                btn_tap_to_pay.setVisibility(View.GONE);
                                btnSUbmit.setVisibility(View.VISIBLE);
                                editTextCadCCV.setVisibility(View.VISIBLE);
                                editTextCardName.setVisibility(View.VISIBLE);
                                editTextCardNumber.setVisibility(View.VISIBLE);
                                editTextExpiry.setVisibility(View.VISIBLE);
                                editTextTotal.setVisibility(View.VISIBLE);
                                editTextTotal.setEnabled(false);

                                editTextTotal.setText("Total Amount: N" + totalDue);

                                //when submit button is clicked
                                btnSUbmit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //get the values in the editTexts

                                        String cardName = editTextCardName.getText().toString().trim();
                                        String cardNumber = editTextCardNumber.getText().toString().trim();
                                        String cardExpiry = editTextExpiry.getText().toString().trim();

                                        if (cardName.length() < 5 ){
                                            Toast.makeText(MyLoanActivity.this, "Enter a valid card name", Toast.LENGTH_SHORT).show();
                                        } else if (cardNumber.length() < 16){
                                            Toast.makeText(MyLoanActivity.this, "Enter a valid card number to complete loan paymnet", Toast.LENGTH_SHORT).show();
                                        }else if (cardExpiry.length() < 4 ){
                                            Toast.makeText(MyLoanActivity.this, "Enter a valid card expiry date", Toast.LENGTH_SHORT).show();
                                        }else {
                                            dialog.setTitle("Loan Payment");
                                            dialog.setMessage("Paying...");
                                            dialog.show();
                                            loans.child(uId).child("status").setValue("Paid").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    dialog.dismiss();
                                                    Toast.makeText(MyLoanActivity.this, "Loan settled", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(MyLoanActivity.this, ApplyLoan.class));
                                                }
                                            });
                                        }

                                    }
                                });


                            }
                        });

                    }else {
                        btn_tap_to_pay.setText("Paid");
                        btn_tap_to_pay.setEnabled(false);
                    }
                }else {
                    Toast.makeText(MyLoanActivity.this, "You don't have any loan", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MyLoanActivity.this, ApplyLoan.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
