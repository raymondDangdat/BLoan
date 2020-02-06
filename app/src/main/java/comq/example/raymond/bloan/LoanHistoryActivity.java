package comq.example.raymond.bloan;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import comq.example.raymond.bloan.Interface.ItemClickListener;
import comq.example.raymond.bloan.Model.LoanHistoryModel;

public class LoanHistoryActivity extends AppCompatActivity {
    private Toolbar loanHistoryToolBar;

    private RecyclerView recycler_loans;
    RecyclerView.LayoutManager layoutManager;

    private FirebaseRecyclerAdapter<LoanHistoryModel, LoanHistoryViewHolder>adapter;

    private FirebaseAuth mAuth;
    private DatabaseReference loans;

    private String uId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_history);

        mAuth = FirebaseAuth.getInstance();
        loans = FirebaseDatabase.getInstance().getReference().child("bLoan").child("loans");
        uId = mAuth.getCurrentUser().getUid();


        //toolbar
        //initialize our toolBar
        loanHistoryToolBar = findViewById(R.id.loanHistorytoolbar);
        setSupportActionBar(loanHistoryToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Loan History");

        recycler_loans = findViewById(R.id.recycler_loans);
        recycler_loans.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_loans.setLayoutManager(layoutManager);

        getLoanHistory();

    }

    private void getLoanHistory() {
        FirebaseRecyclerOptions<LoanHistoryModel> options = new FirebaseRecyclerOptions.Builder<LoanHistoryModel>()
                .setQuery(loans.orderByChild("uId").equalTo(uId), LoanHistoryModel.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<LoanHistoryModel, LoanHistoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull LoanHistoryViewHolder holder, int position, @NonNull LoanHistoryModel model) {
                Double amount = model.getAmount();
                Double interest = model.getInterest();

                final String amountString = String.valueOf(amount);
                final String interestString = String.valueOf(interest);

                holder.txtInterest.setText(interestString);
                holder.txtStatus.setText(model.getStatus());
                holder.txtAmount.setText(amountString);

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }

            @NonNull
            @Override
            public LoanHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.loan_history_layout, viewGroup,false);
                LoanHistoryViewHolder viewHolder = new LoanHistoryViewHolder(view);
                return viewHolder;
            }
        };
        recycler_loans.setAdapter(adapter);
        adapter.startListening();
    }

    public static class LoanHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtAmount, txtInterest, txtStatus;
        private ItemClickListener itemClickListener;
        public LoanHistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            txtAmount = itemView.findViewById(R.id.txt_amount);
            txtInterest = itemView.findViewById(R.id.txt_interest);
            txtStatus = itemView.findViewById(R.id.txt_status);

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
