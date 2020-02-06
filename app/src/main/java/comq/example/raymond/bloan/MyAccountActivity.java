package comq.example.raymond.bloan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyAccountActivity extends AppCompatActivity {
    private Toolbar myAccountToolBar;
    private DatabaseReference loans;
    private FirebaseAuth mAuth;

    private TextView txtAccountName, txtAccountBalance;

    private String uId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        txtAccountBalance = findViewById(R.id.txt_acount_balance);
        txtAccountName = findViewById(R.id.txt_acount_name);

        mAuth = FirebaseAuth.getInstance();
        uId = mAuth.getCurrentUser().getUid();
        loans = FirebaseDatabase.getInstance().getReference().child("bLoan").child("loans");


        //toolbar
        //initialize our toolBar
        myAccountToolBar = findViewById(R.id.myAccounttoolbar);
        setSupportActionBar(myAccountToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My Account");

        getLoanDetails();

    }

    private void getLoanDetails() {
        loans.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(uId).exists()){
                    String accounntName = dataSnapshot.child(uId).child("uName").getValue(String.class);
                    Double balance = dataSnapshot.child(uId).child("amount").getValue(Double.class);

                    txtAccountName.setText(accounntName);
                    txtAccountBalance.setText("Balance: " + balance);
                }else {
                    String accountName = dataSnapshot.child(uId).child("uName").getValue(String.class);
                    txtAccountBalance.setText("Balance: 0.00");

                    txtAccountName.setText(accountName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
