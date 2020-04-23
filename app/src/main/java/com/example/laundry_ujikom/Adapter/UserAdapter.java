package com.example.laundry_ujikom.Adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laundry_ujikom.Model.Outlet;
import com.example.laundry_ujikom.Model.PaketLaundry;
import com.example.laundry_ujikom.Model.User;
import com.example.laundry_ujikom.R;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context mcontext;
    private List<User> mUser;
    private UserAdapter.OnItemClickListener mListener;

    public UserAdapter(Context context, List<User> users) {
        mcontext = context;
        mUser = users;

    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row, parent,false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User UserCurrent = mUser.get(position);
        holder.UserName.setText(UserCurrent.getNama());
        holder.UserRole.setText(UserCurrent.getRole());

    }

    public void fiterList(ArrayList<User> filterList) {
        mUser = filterList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener
            , MenuItem.OnMenuItemClickListener {
        CardView card_user;
        View mView;
        TextView UserName, UserRole;
        ImageView user_img;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            card_user = (CardView) itemView.findViewById(R.id.card_User);
            UserName = (TextView) itemView.findViewById(R.id.User_name);
            UserRole = (TextView) itemView.findViewById(R.id.User_role);
            user_img = (ImageView) itemView.findViewById(R.id.icn_outlet);

//
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    switch (menuItem.getItemId()){
                        case 1:
                            mListener.onEditClick(mUser.get(position));
                            return true;
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }

        @Override
        public void onClick(View view) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    mListener.onItemClick(mUser.get(position));
                }
            }

        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select Action");
            MenuItem Edit = contextMenu.add(Menu.NONE, 1, 1, "Edit Item");
            MenuItem Delete = contextMenu.add(Menu.NONE, 2, 2, "Delete Item");
            Edit.setOnMenuItemClickListener(this);
            Delete.setOnMenuItemClickListener(this);

        }
    }

    public interface OnItemClickListener{
        void onItemClick(User user);

        void onDeleteClick(int position);

        void onEditClick(User user);
    }

    public void setOnItemClickListener(UserAdapter.OnItemClickListener listener){
        mListener = listener;
    }
}
