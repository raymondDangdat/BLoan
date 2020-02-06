package comq.example.raymond.bloan;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import comq.example.raymond.bloan.Interface.ItemClickListener;
import comq.example.raymond.bloan.Model.LoanHistoryModel;

public class AdminViewLoans extends AppCompatActivity {
    private Toolbar loanToolBar;

    private RecyclerView recycler_loans;
    RecyclerView.LayoutManager layoutManager;

    private FirebaseRecyclerAdapter<LoanHistoryModel, LoanViewHolder>adapter;

    private DatabaseReference loans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_loans);

        loans = FirebaseDatabase.getInstance().getReference().child("bLoan").child("loans");

        //toolbar
        //initialize our toolBar
        loanToolBar = findViewById(R.id.loanstoolbar);
        setSupportActionBar(loanToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Loans");

        recycler_loans = findViewById(R.id.recycler_loans);
        recycler_loans.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_loans.setLayoutManager(layoutManager);


        loadLoans();


    }

    private void loadLoans() {
        FirebaseRecyclerOptions<LoanHistoryModel>options = new FirebaseRecyclerOptions.Builder<LoanHistoryModel>()
                .setQuery(loans, LoanHistoryModel.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<LoanHistoryModel, LoanViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull LoanViewHolder holder, int position, @NonNull LoanHistoryModel model) {
                holder.txtUserName.setText(model.getuName());
                Double interest = model.getInterest();
                Double amount = model.getAmount();
                final String amountString = String.valueOf(amount);
                final String interestString = String.valueOf(interest);
                holder.txtAmount.setText(amountString);
                holder.txtInterest.setText(interestString);
                holder.txtStatus.setText(model.getStatus());
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }

            @NonNull
            @Override
            public LoanViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.a_view_loans_layout, viewGroup,false);
                LoanViewHolder viewHolder = new LoanViewHolder(view);
                return viewHolder;
            }
        };
        recycler_loans.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sort_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort) {
            //display alert to choose sort type
            showSortDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showSortDialog() {
        //Options to display
        String[] sortOptions = {"Paid", "Unpaid", "All"};
        //create alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort by payment status:")
                .setIcon(R.drawable.ic_sort)
                .setItems(sortOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //the which contains the index position of the selected item
                        if (which==0){
                            //Male selected
                            loadPaid();
                        }else if (which==1){
                            loadUnpaid();
                        }else if (which==2){
                            loadLoans();
                        }
                    }
                });
        builder.show();
    }

    private void loadPaid() {

        FirebaseRecyclerOptions<LoanHistoryModel>options = new FirebaseRecyclerOptions.Builder<LoanHistoryModel>()
                .setQuery(loans.orderByChild("status").equalTo("Paid"), LoanHistoryModel.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<LoanHistoryModel, LoanViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull LoanViewHolder holder, int position, @NonNull LoanHistoryModel model) {
               Double amount = model.getAmount();
               Double interest = model.getInterest();

                final String amountString = String.valueOf(amount);
                final String interestString = String.valueOf(interest);

                holder.txtUserName.setText(model.getuName());
                holder.txtAmount.setText(amountString);
                holder.txtInterest.setText(interestString);
                holder.txtStatus.setText(model.getStatus());

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }

            @NonNull
            @Override
            public LoanViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.a_view_loans_layout, viewGroup,false);
                LoanViewHolder viewHolder = new LoanViewHolder(view);
                return viewHolder;
            }
        };
        recycler_loans.setAdapter(adapter);
        adapter.startListening();
    }

    private void loadUnpaid() {

        FirebaseRecyclerOptions<LoanHistoryModel>options = new FirebaseRecyclerOptions.Builder<LoanHistoryModel>()
                .setQuery(loans.orderByChild("status").equalTo("Unpaid"), LoanHistoryModel.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<LoanHistoryModel, LoanViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull LoanViewHolder holder, int position, @NonNull LoanHistoryModel model) {
               Double amount = model.getAmount();
               Double interest = model.getInterest();

                final String amountString = String.valueOf(amount);
                final String interestString = String.valueOf(interest);

                holder.txtUserName.setText(model.getuName());
                holder.txtAmount.setText(amountString);
                holder.txtInterest.setText(interestString);
                holder.txtStatus.setText(model.getStatus());

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }

            @NonNull
            @Override
            public LoanViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.a_view_loans_layout, viewGroup,false);
                LoanViewHolder viewHolder = new LoanViewHolder(view);
                return viewHolder;
            }
        };
        recycler_loans.setAdapter(adapter);
        adapter.startListening();
    }


    public static class LoanViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtAmount, txtInterest, txtStatus, txtUserName;
        private ItemClickListener itemClickListener;
        public LoanViewHolder(@NonNull View itemView) {
            super(itemView);

            txtAmount = itemView.findViewById(R.id.txt_amount);
            txtInterest = itemView.findViewById(R.id.txt_interest);
            txtStatus = itemView.findViewById(R.id.txt_status);
            txtUserName = itemView.findViewById(R.id.txt_user_name);

            itemView.setOnClickListener(this);
        }


        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }


        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }
}
