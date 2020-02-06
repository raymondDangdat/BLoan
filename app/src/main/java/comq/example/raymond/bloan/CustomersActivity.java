package comq.example.raymond.bloan;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import comq.example.raymond.bloan.Interface.ItemClickListener;
import comq.example.raymond.bloan.Model.CustomersModel;

public class CustomersActivity extends AppCompatActivity {
    private Toolbar customersToolBar;

    private DatabaseReference database;

    private RecyclerView recycler_customers;
    RecyclerView.LayoutManager layoutManager;

    private FirebaseRecyclerAdapter<CustomersModel, CustomerViewHolder>adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);



        database = FirebaseDatabase.getInstance().getReference().child("bLoan").child("users");

        //toolbar
        //initialize our toolBar
        customersToolBar = findViewById(R.id.customerstoolbar);
        setSupportActionBar(customersToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Customers ");

        recycler_customers = findViewById(R.id.recycler_customers);
        recycler_customers.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_customers.setLayoutManager(layoutManager);



        //load customers
        loadCustmers();
    }

    private void loadCustmers() {
        FirebaseRecyclerOptions<CustomersModel> options = new FirebaseRecyclerOptions.Builder<CustomersModel>()
                .setQuery(database, CustomersModel.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<CustomersModel, CustomerViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CustomerViewHolder holder, int position, @NonNull CustomersModel model) {
                holder.txtBVN.setText(" BVN: " + model.getBvn());
                holder.txtAccount.setText("Account No: " + model.getAccount());
                holder.txtFullName.setText("Name: " + model.getFullName());
                holder.txtPhone.setText("Tel: " + model.getPhone());

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }

            @NonNull
            @Override
            public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customer_layout, viewGroup,false);
                CustomerViewHolder viewHolder = new CustomerViewHolder(view);
                return viewHolder;
            }
        };
        recycler_customers.setAdapter(adapter);
        adapter.startListening();

    }

    public static class CustomerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtFullName, txtAccount, txtBVN, txtPhone;
        private ItemClickListener itemClickListener;
        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);

            txtFullName = itemView.findViewById(R.id.txtFullName);
            txtAccount = itemView.findViewById(R.id.txtAccount);
            txtBVN = itemView.findViewById(R.id.txtBvn);
            txtPhone = itemView.findViewById(R.id.txtPhone);
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
