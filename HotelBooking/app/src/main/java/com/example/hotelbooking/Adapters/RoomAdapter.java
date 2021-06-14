package com.example.hotelbooking.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelbooking.Activities.RoomInfoActivity;
import com.example.hotelbooking.R;
import com.example.hotelbooking.model.Room;
import com.example.hotelbooking.model.RoomResult;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private View view;
    private RoomViewHolder roomViewHolder;
    private List<Room> rooms;
    private RoomResult roomResult = new RoomResult();

    public RoomAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setRooms(List<Room> lists) {
        this.rooms = lists;
        roomResult.setRooms(rooms);
        RoomResult roomResult = new RoomResult();
        roomResult.setRooms(rooms);
        storeUpdates(roomResult);
        notifyDataSetChanged();
    }

    public void storeUpdates(RoomResult roomResult) {
        SharedPreferences.Editor spe = context.getSharedPreferences("room", Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        spe.putString("data", gson.toJson(roomResult));
        spe.apply();

    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = inflater.inflate(R.layout.room_card, parent, false);
        roomViewHolder = new RoomViewHolder(view);

        return roomViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, final int position) {
        Picasso
                .with(context)
                .load(Uri.parse(rooms.get(position).getImageUrl()))
                .into(holder.roomImage);

        holder.roomRatings.setText(rooms.get(position).getRatings());
        holder.tags.setText(rooms.get(position).getTags());
        holder.roomName.setText(rooms.get(position).getName());
        holder.roomViews.setText(rooms.get(position).getVisits() + "\nViews");
        holder.bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int vis = Integer.valueOf(rooms.get(position).getVisits());
                rooms.get(position).setVisits(String.valueOf(++vis));
                setRooms(rooms);
                Intent i = new Intent(context, RoomInfoActivity.class);
                i.putExtra("rooms", roomResult);
                i.putExtra("pos", position);
                i.putExtra("data", rooms.get(position));
                context.startActivity(i);

            }
        });

    }


    @Override
    public int getItemCount() {
        return rooms.size();
    }

    class RoomViewHolder extends RecyclerView.ViewHolder {

        ImageView roomImage;
        TextView roomRatings, roomName, roomViews;

        TextView tags;
        Button bookButton;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            roomImage = itemView.findViewById(R.id.roomImage);
            roomRatings = itemView.findViewById(R.id.ratings);
            bookButton = itemView.findViewById(R.id.roomBookButton);
            tags = itemView.findViewById(R.id.tagsList);
            roomName = itemView.findViewById(R.id.roomName);
            roomViews = itemView.findViewById(R.id.roomCardViews);


        }
    }


}
