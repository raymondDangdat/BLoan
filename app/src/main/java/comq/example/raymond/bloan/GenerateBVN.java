package comq.example.raymond.bloan;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class GenerateBVN extends AppCompatActivity {
    private Toolbar generateBvnToolBar;
    private DatabaseReference database;
    private ProgressDialog dialog;

    private EditText editTextSurname, editTextFirstName, editTextOtherName, editTextPhone;
    private Button btnSubmit;



    private String accountNo = "";
    private String bvn = "";
    private String DoB = "";
    private TextView displayDate;
    private TextView txtAccountNo, txtBvn;
    private Button btnGenerateAccount,btnGenerateBvn;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_bvn);

        //toolbar
        //initialize our toolBar
        generateBvnToolBar = findViewById(R.id.generate_bvn_toolbar);
        setSupportActionBar(generateBvnToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Generate BVN");

        dialog = new ProgressDialog(this);
        btnGenerateAccount = findViewById(R.id.btnGenerateAccountNumber);
        btnGenerateBvn = findViewById(R.id.btnGenerateBvn);

        editTextFirstName = findViewById(R.id.firstName);
        editTextSurname = findViewById(R.id.surname);
        editTextOtherName = findViewById(R.id.otherName);
        editTextPhone = findViewById(R.id.phone);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBvn();
            }
        });

        database = FirebaseDatabase.getInstance().getReference().child("bLoan").child("BVN");

        txtAccountNo = findViewById(R.id.txtAccountNumber);

        btnGenerateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date myDate = new Date();
                SimpleDateFormat format1 = new SimpleDateFormat("yyy-MM-dd hh:mm:ss a", Locale.getDefault());
                String date = format1.format(myDate);
                Random r = new Random();
                int n = 1000000000 + r.nextInt(900010101);
                accountNo = Long.toString(n);

                txtAccountNo.setText(accountNo);
                btnGenerateAccount.setText("Re-Generate Account No.");
            }
        });

        txtBvn = findViewById(R.id.txtBvn);
        btnGenerateBvn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Date myDate = new Date();
                SimpleDateFormat format1 = new SimpleDateFormat("yyy-MM-dd hh:mm:ss a", Locale.getDefault());
                String date = format1.format(myDate);
                Random r = new Random();
                int n = 1000000000 + r.nextInt(900010101);
                bvn = String.valueOf(n);

                txtBvn.setText(bvn);
                btnGenerateBvn.setText("Re-Generate BVN");
            }
        });

        displayDate = findViewById(R.id.txtDoB);
        displayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        GenerateBVN.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener =  new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                DoB = dayOfMonth + "/" + month + "/" + year;

                displayDate.setText(DoB);
            }
        };
    }

    private void saveBvn() {
        dialog.setTitle("Generate BVN");
        dialog.setMessage("Saving BVN info...");

        String surname = editTextSurname.getText().toString().trim();
        String firstName = editTextFirstName.getText().toString();
        String otherName = editTextOtherName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();

        if (DoB.equals("")){
            Toast.makeText(this, "Date of birth is required", Toast.LENGTH_SHORT).show();
        }else if (accountNo.equals("")){
            Toast.makeText(this, "Please generate the account number to link with BVN", Toast.LENGTH_SHORT).show();
        }else if (bvn.equals("")){
            Toast.makeText(this, "Please generate the BVN", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(surname)){
            Toast.makeText(this, "Surname is required", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(firstName)){
            Toast.makeText(this, "First name cannot be empty", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Phone number is required", Toast.LENGTH_SHORT).show();
        }else {
            dialog.show();
            database.child(bvn).child("account").setValue(accountNo);
            database.child(bvn).child("bvn").setValue(bvn);
            database.child(bvn).child("surname").setValue(surname);
            database.child(bvn).child("firstName").setValue(firstName);
            database.child(bvn).child("phone").setValue(phone);
            database.child(bvn).child("dob").setValue(DoB);
            database.child(bvn).child("otherName").setValue(otherName).addOnCompleteListener(
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dialog.dismiss();
                            Toast.makeText(GenerateBVN.this, "BVN Generated successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(GenerateBVN.this, BVNs.class));
                        }
                    }
            ).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(GenerateBVN.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
