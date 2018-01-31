package com.example.kimjaeseung.cultureseoul2.community;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;

import com.example.kimjaeseung.cultureseoul2.R;
import com.example.kimjaeseung.cultureseoul2.utils.DateUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kimjaeseung on 2017. 9. 6..
 */

public class CommunityDateFragment extends Fragment implements ChatRoomAdapter.ChatRoomAdapterOnClickHandler {
    private static final String TAG = CommunityDateFragment.class.getSimpleName();
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    ;
    private DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("room");
    ;
    private ChildEventListener mChildEventListener;
    private ValueEventListener mValueEventListener;
    private ChatRoomAdapter mAdapter;
    private List<ChatRoomData> chatRoomDataList = new ArrayList<>();
    private int mYear = 0, mMonth = 0, mDay = 0;
    private static String mCurDate;

    @Bind(R.id.community_date_recyclerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.community_date_button)
    Button dateButton;
    @Bind(R.id.pb_community_date)
    ProgressBar progressBar;

    public CommunityDateFragment() {
    }

    public static Fragment getInstance() {
        CommunityDateFragment communityDateFragment = new CommunityDateFragment();
        return communityDateFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community_date, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#3666A5"), android.graphics.PorterDuff.Mode.MULTIPLY);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initButton();
        initFirebase();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChildEventListener != null)
            mDatabaseReference.removeEventListener(mChildEventListener);
        if (mValueEventListener != null)
            mDatabaseReference.removeEventListener(mValueEventListener);
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new ChatRoomAdapter(this.getContext(), this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initButton() {
        Calendar calendar = new GregorianCalendar();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), R.style.DatePicker, mDateSetListener, mYear, mMonth - 1, mDay).show();

            }
        });
        if (mCurDate == null) {
            mCurDate = String.valueOf(mYear + "-" + formatDay(mMonth) + "-" + formatDay(mDay));
        }

        dateButton.setText(mCurDate);
    }

    private void initFirebase() {
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatRoomData chatRoomData = dataSnapshot.getValue(ChatRoomData.class);
                if (chatRoomData.getRoomDay().equals(mCurDate)) {
                    chatRoomData.setFirebaseKey(dataSnapshot.getKey());
                    chatRoomData.setRoomPeople((int) dataSnapshot.child("people").getChildrenCount());
                    final List<ChatPeople> chatPeoples = new ArrayList<>();
                    DatabaseReference databaseReference = mDatabaseReference.child(chatRoomData.getFirebaseKey()).child("people");
                    databaseReference.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            ChatPeople chatPeople = dataSnapshot.getValue(ChatPeople.class);
                            chatPeoples.add(chatPeople);
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    chatRoomData.setChatPeoples(chatPeoples);
                    chatRoomDataList.add(chatRoomData);
                    mAdapter.addItem(chatRoomData);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String firebaseKey = dataSnapshot.getKey();
                int count = chatRoomDataList.size();
                for (int i = 0; i < count; i++) {
                    if (chatRoomDataList.get(i).getFirebaseKey().equals(firebaseKey)) {
                        chatRoomDataList.remove(i);
                        mAdapter.removeItem(i);
                        mAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabaseReference.addChildEventListener(mChildEventListener);
        mDatabaseReference.addValueEventListener(mValueEventListener);
    }

    DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mYear = year;
            mMonth = month + 1;
            mDay = dayOfMonth;

            mCurDate = String.valueOf(mYear + "-" + formatDay(mMonth) + "-" + formatDay(mDay));
            dateButton.setText(mCurDate);

            chatRoomDataList.clear();
            mAdapter.removeItemList();
            mAdapter.notifyDataSetChanged();

            initFirebase();
        }
    };

    @Override
    public void onClick(final ChatRoomData chatRoomData) {
        if (isUserInChatRoom(chatRoomData, mUser)) gotoChatActivity(chatRoomData);
        else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    getContext());
            alertDialogBuilder.setIcon(R.drawable.send_button)
                    .setTitle(chatRoomData.getRoomName() + "채팅방에 입장하시겠습니까?")
                    .setPositiveButton("입장", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ChatPeople chatPeople = new ChatPeople();
                            chatPeople.setUid(mUser.getUid());
                            chatPeople.setName(mUser.getDisplayName());
                            chatPeople.setEmail(mUser.getEmail());
                            chatPeople.setPhoto(mUser.getPhotoUrl().toString());
                            mDatabaseReference.child(chatRoomData.getFirebaseKey()).child("people").push().setValue(chatPeople);
                            gotoChatActivity(chatRoomData);
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).create().show();
        }
    }

    private boolean isUserInChatRoom(ChatRoomData chatRoomdData, FirebaseUser mUser) {
        for (ChatPeople c : chatRoomdData.getChatPeoples()) {
            if (c.getUid().equals(mUser.getUid())) return true;
        }
        return false;
    }

    private void gotoChatActivity(ChatRoomData chatRoomData) {
        Intent intent = new Intent(getContext(), ChatActivity.class);
        intent.putExtra("room_information", chatRoomData);
        startActivity(intent);
    }

    private String formatDay(int day) {
        String dayString;
        if (day < 10) dayString = "0" + day;
        else dayString = Integer.toString(day);
        return dayString;
    }
}
