package com.example.kimjaeseung.cultureseoul2.community;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.kimjaeseung.cultureseoul2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kimjaeseung on 2017. 9. 6..
 */

public class CommunityRealTimeFragment extends Fragment implements ChatRoomAdapter.ChatRoomAdapterOnClickHandler, SearchView.OnQueryTextListener {
    private static final String TAG = CommunityRealTimeFragment.class.getSimpleName();
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("room");
    private ChildEventListener mChildEventListener;
    private ValueEventListener mValueEventListener;
    private ChatRoomAdapter mAdapter;
    private List<ChatRoomData> chatRoomDataList = new ArrayList<>();
    private String mCurSearch;

    @Bind(R.id.community_realtime_recyclerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.community_realtime_fab)
    FloatingActionButton floatingActionButton;
    @Bind(R.id.pb_community_realtime)
    ProgressBar progressBar;

    public CommunityRealTimeFragment() {
    }

    public static Fragment getInstance() {
        CommunityRealTimeFragment communityRealTimeFragment = new CommunityRealTimeFragment();
        return communityRealTimeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community_reatltime, container, false);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_performance, menu);
        MenuItem menuItem = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
    }

    @OnClick({R.id.community_realtime_fab})
    public void mOnClick(View view) {
        switch (view.getId()) {
            case R.id.community_realtime_fab:
                Intent intent = new Intent(getContext(), AddChatRoomActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new ChatRoomAdapter(this.getContext(), this);
        mRecyclerView.setAdapter(mAdapter);
        floatingActionButton.attachToRecyclerView(mRecyclerView);
    }

    private void initFirebase() {
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatRoomData chatRoomData = dataSnapshot.getValue(ChatRoomData.class);

                if (mCurSearch == null) {
                    childAdd(chatRoomData, dataSnapshot);
                } else {
                    if (chatRoomData.getRoomName().toLowerCase().contains(mCurSearch.toLowerCase()) || chatRoomData.getPerformanceName().toLowerCase().contains(mCurSearch)) {
                        childAdd(chatRoomData, dataSnapshot);
                    }
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

    private void childAdd(ChatRoomData chatRoomData, DataSnapshot dataSnapshot) {
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


    @Override
    public void onClick(final ChatRoomData chatRoomData) {
        if (isUserInChatRoom(chatRoomData, mUser)) gotoChatActivity(chatRoomData);
        else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    getContext());
            alertDialogBuilder.setIcon(R.drawable.send_button)
                    .setTitle("채팅방 입장")
                    .setMessage(chatRoomData.getRoomName()+" 채팅방에 입장하시겠습니까?")
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mCurSearch = newText;
        chatRoomDataList.clear();
        mAdapter.removeItemList();
        mAdapter.notifyDataSetChanged();
        initFirebase();
        return true;
    }
}
