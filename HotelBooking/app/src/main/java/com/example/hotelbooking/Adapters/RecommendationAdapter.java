package com.example.hotelbooking.Adapters;

import android.content.Context;
import android.content.Intent;
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
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.RoomViewHolder> implements Serializable {

    private final Context context;
    private final LayoutInflater inflater;
    private View view;
    private RoomViewHolder roomViewHolder;
    private List<Room> rooms;

    public RecommendationAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setRooms(Set<Room> lists) {
        this.rooms = new ArrayList<>(lists);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = inflater.inflate(R.layout.room_card, parent, false);

        roomViewHolder = new RoomViewHolder(view);

        return roomViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RoomViewHolder holder, final int position) {
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
//                int vis = Integer.valueOf(rooms.get(position).getVisits());
//                rooms.get(position).setVisits(String.valueOf(++vis));
//                setRooms(new HashSet<>(rooms));
//                Intent i = new Intent(context, RoomInfoActivity.class);
//                i.putExtra("data", rooms.get(position));
//                context.startActivity(i);

                int vis = Integer.valueOf(rooms.get(position).getVisits());
                rooms.get(position).setVisits(String.valueOf(++vis));
                setRooms(new HashSet<>(rooms));
                Intent i = new Intent(context, RoomInfoActivity.class);
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


    public class RoomViewHolder extends RecyclerView.ViewHolder implements UpdateListener, Serializable {

        TextView roomViews;
        ImageView roomImage;
        TextView roomRatings, roomName;
        TextView tags;
        Button bookButton;

        public RoomViewHolder(@NonNull final View itemView) {
            super(itemView);
            roomImage = itemView.findViewById(R.id.roomImage);
            roomRatings = itemView.findViewById(R.id.ratings);
            bookButton = itemView.findViewById(R.id.roomBookButton);
            tags = itemView.findViewById(R.id.tagsList);
            roomName = itemView.findViewById(R.id.roomName);
            roomViews = itemView.findViewById(R.id.roomCardViews);

        }

        @Override
        public void update() {
            notifyItemChanged(getAdapterPosition());
        }
    }

    public interface UpdateListener extends Serializable {
        void update();
    }


}
