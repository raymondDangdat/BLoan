package comq.example.raymond.bloan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import comq.example.raymond.bloan.Interface.ItemClickListener;
import comq.example.raymond.bloan.Model.MessageModel;
import comq.example.raymond.bloan.Utils.BLoansUtils;

public class AdminCustomerCareActivity extends AppCompatActivity {
    private Toolbar adminCustomerCareToolBar;

    private RecyclerView recycler_messages;
    RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerAdapter<MessageModel, AdminCustomerCareViewHolder>adapter;

    private DatabaseReference messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_customer_care);

        messages = FirebaseDatabase.getInstance().getReference().child("bLoan").child("messages");

        //toolbar
        //initialize our toolBar
        adminCustomerCareToolBar = findViewById(R.id.adminCustomerCaretoolbar);
        setSupportActionBar(adminCustomerCareToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Customer's Complaints");

        recycler_messages = findViewById(R.id.recycler_messages);
        recycler_messages.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_messages.setLayoutManager(layoutManager);

        loadMessages();


    }

    private void loadMessages() {
        FirebaseRecyclerOptions<MessageModel> options = new FirebaseRecyclerOptions.Builder<MessageModel>()
                .setQuery(messages, MessageModel.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<MessageModel, AdminCustomerCareViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminCustomerCareViewHolder holder, int position, @NonNull MessageModel model) {
                holder.txtMessage.setText(model.getMessage());
                String name = model.getUserName().toString();
                if (name.equals("admin")){
                    holder.txtUserName.setText("You replied this:");
                }else {
                    holder.txtUserName.setText(model.getUserName());
                }


                holder.txtDate.setText(BLoansUtils.dateFromLong(model.getDateSent()));

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //get message id to new activity
                        Intent replyComplaint = new Intent(AdminCustomerCareActivity.this, AdminReplyComplaintActivity.class);
                        replyComplaint.putExtra("messageId", adapter.getRef(position).getKey());
                        startActivity(replyComplaint);
                    }
                });
            }

            @NonNull
            @Override
            public AdminCustomerCareViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customer_complaint_layout, viewGroup,false);
                AdminCustomerCareViewHolder viewHolder = new AdminCustomerCareViewHolder(view);
                return viewHolder;
            }
        };
        recycler_messages.setAdapter(adapter);
        adapter.startListening();
    }


    public static class AdminCustomerCareViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtUserName, txtDate, txtMessage;
        private ItemClickListener itemClickListener;
        public AdminCustomerCareViewHolder(@NonNull View itemView) {
            super(itemView);

            txtUserName = itemView.findViewById(R.id.txt_user_name);
            txtDate = itemView.findViewById(R.id.txt_time);
            txtMessage= itemView.findViewById(R.id.txt_message);

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
