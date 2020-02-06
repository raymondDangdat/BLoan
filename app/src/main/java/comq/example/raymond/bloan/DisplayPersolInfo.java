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

public class DisplayPersolInfo extends AppCompatActivity {
    private Toolbar displayDisplayToolBar;

    private FirebaseAuth mAuth;
    private DatabaseReference database;
    private TextView txtFullName, accountNumber, bvnNumber, txtPhone, txtDoB;
    private String uId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_persol_info);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference().child("bLoan").child("users");
        uId = mAuth.getCurrentUser().getUid();

        txtFullName = findViewById(R.id.txtFullName);
        accountNumber = findViewById(R.id.txtAccountNumber);
        bvnNumber = findViewById(R.id.txtBvn);
        txtPhone = findViewById(R.id.txtPhone);
        txtDoB = findViewById(R.id.txtDoB);

        //txtFullName.setText("Dangdat D");

        //toolbar
        //initialize our toolBar
        displayDisplayToolBar = findViewById(R.id.displayInfotoolbar);
        setSupportActionBar(displayDisplayToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My Information");

        //getUserDetail
        getUserDetail();
    }

    private void getUserDetail() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String fullName = dataSnapshot.child(uId).child("fullName").getValue(String.class);
                String account = dataSnapshot.child(uId).child("bvn").getValue(String.class);
                String dob = dataSnapshot.child(uId).child("dob").getValue(String.class);
                String phone = dataSnapshot.child(uId).child("phone").getValue(String.class);
                String bvn = dataSnapshot.child(uId).child("bvn").getValue(String.class);

                txtFullName.setText("Name: " + fullName);
                txtPhone.setText("Phone: " + phone);
                accountNumber.setText("Account Number: " +account);
                txtDoB.setText("DoB: " + dob);
                bvnNumber.setText("BVN: " +bvn);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
