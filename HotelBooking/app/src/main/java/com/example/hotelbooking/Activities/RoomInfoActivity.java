package com.example.hotelbooking.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.hotelbooking.Fragments.Recommendation;
import com.example.hotelbooking.R;
import com.example.hotelbooking.model.Booking;
import com.example.hotelbooking.model.Room;
import com.example.hotelbooking.model.RoomResult;
import com.example.hotelbooking.Adapters.RecommendationAdapter;
import com.example.hotelbooking.model.UserRoom;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoomInfoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView roomImage;
    private TextView roomDesc, views, drafts, completed;
    private Button book, draftBook;
    private RecommendationAdapter.RoomViewHolder roomViewHolder;
    Room room;
    int pos;
    RoomResult roomResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_info);

        toolbar = findViewById(R.id.toolbarInfo);
        roomImage = findViewById(R.id.roomImage);
        roomDesc = findViewById(R.id.roomDesc);
        book = findViewById(R.id.confirmBooking);
        draftBook = findViewById(R.id.draftBooking);
        views = findViewById(R.id.views);
        drafts = findViewById(R.id.draftText);
        completed = findViewById(R.id.completedText);


    }

    @Override
    protected void onResume() {
        super.onResume();

        room = (Room) getIntent().getExtras().getSerializable("data");


        roomResult = new Gson().fromJson(getrooms(), RoomResult.class);
        pos = getIntent().getExtras().getInt("pos");

        toolbar.setTitle(room.getName());

        setSupportActionBar(toolbar);
        Picasso
                .with(RoomInfoActivity.this)
                .load(Uri.parse(room.getImageUrl()))
                .into(roomImage);
        roomDesc.setText(room.getDescription());

        views.setText(roomResult.getRooms().get(pos).getVisits() + " views");
        drafts.setText(roomResult.getRooms().get(pos).getDraftBookings() + " drafts");
        completed.setText(roomResult.getRooms().get(pos).getCompletedBookings() + " booked");


        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setBooking(true);
                finish();

            }
        });
        draftBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setBooking(false);
                MainActivity.updatec(1);
                Log.d("ononon", "onClick: ");
                finish();

            }
        });
    }

    public String getrooms() {
        SharedPreferences sp = getSharedPreferences("room", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        if (sp.contains("data")) {
            return sp.getString("data", null);
        } else {
            return null;
        }

    }

    private void setBooking(Boolean complete) {


        UserRoom room1 = new UserRoom();
        room1.setName(room.getName());
        room1.setCompleted(complete);
        room1.setTags(room.getTags());

        Booking booking = new Booking();

        List<UserRoom> userrooms = MainActivity.bookings.getUserRooms();
        userrooms.add(room1);

        MainActivity.bookings.setUserRooms(userrooms);

        Set<String> s = new HashSet<>();
        for (UserRoom userroom : MainActivity.bookings.getUserRooms()) {
            if (userroom.getCompleted()) {
                for (String ss : userroom.getTags().split("\n")) {
                    ss = ss.replace("null", "");
                    s.add(ss);
                }

            }
        }
        String sa = "";

        for (String sss : s) {
            sa += sss;
            Recommendation.tagSet.add(sss);
        }

        if (complete) {
            int c = Integer.valueOf(roomResult.getRooms().get(pos).getCompletedBookings());
            roomResult.getRooms().get(pos).setCompletedBookings(String.valueOf(c + 1));
            storeUpdates(roomResult);
        } else {
            int c = Integer.valueOf(roomResult.getRooms().get(pos).getDraftBookings());
            roomResult.getRooms().get(pos).setDraftBookings(String.valueOf(c + 1));
            storeUpdates(roomResult);
        }

    }

    public void storeUpdates(RoomResult roomResult) {
        SharedPreferences.Editor spe = getSharedPreferences("room", Context.MODE_PRIVATE).edit();
        String save = new Gson().toJson(roomResult);
        spe.putString("data", save);
        spe.apply();


    }
}
