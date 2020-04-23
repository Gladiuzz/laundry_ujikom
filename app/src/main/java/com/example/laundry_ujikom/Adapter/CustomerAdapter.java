package com.example.laundry_ujikom.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laundry_ujikom.Model.Customer;
import com.example.laundry_ujikom.Model.Outlet;
import com.example.laundry_ujikom.R;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {
    private Context mcontext;
    private List<Customer> mCustomer;

    public CustomerAdapter(Context context, List<Customer> Customers) {
        mcontext = context;
        mCustomer = Customers;

    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_row, parent,false);
        return new CustomerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer customer = mCustomer.get(position);
        holder.CustomerName.setText(customer.getNama_customer());
    }

    public void fiterList(ArrayList<Customer> filterList) {
        mCustomer = filterList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mCustomer.size();
    }


    public class CustomerViewHolder extends RecyclerView.ViewHolder{
        CardView card_Customer;
        View mView;
        TextView CustomerName;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            card_Customer = (CardView) itemView.findViewById(R.id.card_Customer);
            CustomerName = (TextView) itemView.findViewById(R.id.customer_name);
        }
    }
}
