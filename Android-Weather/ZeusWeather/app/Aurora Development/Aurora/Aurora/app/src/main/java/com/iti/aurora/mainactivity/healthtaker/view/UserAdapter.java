package com.iti.aurora.mainactivity.healthtaker.view;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.iti.aurora.R;
import com.iti.aurora.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    List<User> userList;
    UserClickHandler userClickHandler;

    public UserAdapter(List<User> userList, UserClickHandler userClickHandler) {
        this.userList = userList;
        this.userClickHandler = userClickHandler;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_health_taker_fragment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.userEmailTextView.setText(userList.get(position).getEmail());
        holder.userNameTextView.setText(userList.get(position).getUsername());
        holder.userImageView.setImageResource(R.drawable.ic_user_svgrepo_com);
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userClickHandler.userItemClickHandler(userList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout constraintLayout;
        ImageView userImageView;
        TextView userNameTextView;
        TextView userEmailTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userImageView = itemView.findViewById(R.id.userImageView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            userEmailTextView = itemView.findViewById(R.id.userEmailTextView);
            constraintLayout = itemView.findViewById(R.id.containerConstraintLayout);
        }
    }
}
