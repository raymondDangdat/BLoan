package comq.example.raymond.bloan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import comq.example.raymond.bloan.Model.BVNModel;

public class BVNs extends AppCompatActivity {
    private Toolbar bvnToolBar;
    private FloatingActionButton fab;

    private DatabaseReference database;

    private RecyclerView recycler_bvns;
    RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerAdapter<BVNModel, BVNViewHolder>adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bvns);

        database = FirebaseDatabase.getInstance().getReference().child("bLoan").child("BVN");


        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BVNs.this, GenerateBVN.class));
            }
        });
        //toolbar
        //initialize our toolBar
        bvnToolBar = findViewById(R.id.bvnstoolbar);
        setSupportActionBar(bvnToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("BVNs");


        recycler_bvns = findViewById(R.id.recycler_bvns);
        recycler_bvns.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_bvns.setLayoutManager(layoutManager);


        loadBVNs();
    }

    private void loadBVNs() {
        FirebaseRecyclerOptions<BVNModel>options = new FirebaseRecyclerOptions.Builder<BVNModel>()
                .setQuery(database, BVNModel.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<BVNModel, BVNViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BVNViewHolder holder, int position, @NonNull BVNModel model) {
                holder.txtPhone.setText(" Phone: " + model.getPhone());
                holder.txtOtherName.setText(" Other Name: "+ model.getOtherName());
                holder.txtFirstName.setText(" First Name:  " + model.getFirstName());
                holder.txtBVN.setText(" BVN: " + model.getBvn());
                holder.txtAccount.setText(" Account No. : " + model.getAccount());
                holder.txtSurname.setText("Surname: " + model.getSurname());

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });

            }

            @NonNull
            @Override
            public BVNViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bvn_layout, viewGroup,false);
                BVNViewHolder viewHolder = new BVNViewHolder(view);
                return viewHolder;
            }
        };
        recycler_bvns.setAdapter(adapter);
        adapter.startListening();
    }


    public static class BVNViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtAccount, txtBVN, txtSurname, txtFirstName, txtOtherName, txtPhone;
        private ItemClickListener itemClickListener;
        public BVNViewHolder(@NonNull View itemView) {
            super(itemView);

            txtAccount = itemView.findViewById(R.id.account);
            txtBVN = itemView.findViewById(R.id.bvn);
            txtSurname = itemView.findViewById(R.id.surname);
            txtFirstName = itemView.findViewById(R.id.firstName);
            txtOtherName = itemView.findViewById(R.id.otherName);
            txtPhone = itemView.findViewById(R.id.phone);

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
