package comq.example.raymond.bloan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomerHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    private String uId = "";

    private ProgressDialog dialog;

    private DatabaseReference loan, database;
    private TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference().child("bLoan").child("users");
        loan = FirebaseDatabase.getInstance().getReference().child("bLoan").child("loans");
        uId = mAuth.getCurrentUser().getUid();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                View headerView = navigationView.getHeaderView(0);
                username = headerView.findViewById(R.id.user_name);

                String retrieveUsername = dataSnapshot.child(uId).child("fullName").getValue(String.class);

                username.setText(retrieveUsername);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.customer_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_request_loan) {
            // Handle the camera action
            dialog.setMessage("Checking loan eligibility...");
            dialog.show();
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(uId).exists()){
                        loan.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //if the user has collected loan before, get his payment status
                                String status = dataSnapshot.child(uId).child("status").getValue(String.class);
                                if (status.equals("Unpaid")){
                                    dialog.dismiss();
                                    Toast.makeText(CustomerHome.this, "You have an unpaid loan, check your loan history", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(CustomerHome.this, LoanHistoryActivity.class));

                                }else if (status.equals("Paid")){
                                    dialog.dismiss();
                                    startActivity(new Intent(CustomerHome.this, ApplyLoan.class));

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }else{
                        startActivity(new Intent(CustomerHome.this, PersonalInfo.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else if (id == R.id.nav_loan_history) {
            startActivity(new Intent(CustomerHome.this, LoanHistoryActivity.class));

        } else if (id == R.id.nav_my_account) {
            startActivity(new Intent(CustomerHome.this, MyAccountActivity.class));

        } else if (id == R.id.nav_customer_care) {
            startActivity(new Intent(CustomerHome.this, ContactCustomerCareActivity.class));

        }else if (id == R.id.nav_personal_info){
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(uId).exists()){
                        startActivity(new Intent(CustomerHome.this, DisplayPersolInfo.class));
                    }else{
                        startActivity(new Intent(CustomerHome.this, PersonalInfo.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }else if (id == R.id.nav_exit) {
            //logout
            mAuth.getCurrentUser();
            mAuth.signOut();
            finish();
            Intent signoutIntent = new Intent(CustomerHome.this, MainActivity.class);
            signoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signoutIntent);
            finish();


        }else if (id == R.id.nav_my_loan){
            startActivity(new Intent(CustomerHome.this, MyLoanActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
