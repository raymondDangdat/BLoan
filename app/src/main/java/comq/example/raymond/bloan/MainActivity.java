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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private Toolbar mainToolBar;
    private Button btnLogin, uLogin , btnloginWithEmail;

    private ProgressDialog dialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar
        //initialize our toolBar
        mainToolBar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("B-LOAN APP");


        //btnLogin = findViewById(R.id.buttonLogin);
        uLogin = findViewById(R.id.userLogin);
        btnloginWithEmail = findViewById(R.id.btn_sign_in_with_email);

        dialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        uLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
            }
        });

//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, AdminHomeActivity.class));
//            }
//        });

        btnloginWithEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAlertDialog();
            }
        });
    }

    private void loginAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_login, null);

        final EditText editTextEmail = view.findViewById(R.id.edit_email);
        final EditText password = view.findViewById(R.id.edit_password);
        Button btn_login = view.findViewById(R.id.btn_login);

        //set onclick listener on login button
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextEmail.getText().toString().trim().isEmpty() || !password.getText().toString().trim().isEmpty()){
                    dialog.setTitle("Login");
                    dialog.setMessage("Logging in...");
                    dialog.show();
                    mAuth.signInWithEmailAndPassword(editTextEmail.getText().toString().trim(), password.getText().toString().trim())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    if (editTextEmail.getText().toString().equals("bloanadmin@gmail.com")){
                                        dialog.dismiss();
                                        Intent profileIntent = new Intent(MainActivity.this, AdminHomeActivity.class);
                                        profileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(profileIntent);
                                        finish();
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }else {
                    Toast.makeText(MainActivity.this, "Sorry, you can't login with empty fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
