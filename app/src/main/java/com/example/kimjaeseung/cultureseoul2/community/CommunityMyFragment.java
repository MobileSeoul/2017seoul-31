package com.example.kimjaeseung.cultureseoul2.community;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kimjaeseung on 2017. 10. 4..
 */

public class CommunityMyFragment extends Fragment implements ChatRoomAdapter.ChatRoomAdapterOnClickHandler {
    private static final String TAG = CommunityMyFragment.class.getSimpleName();
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("room");
    private ChildEventListener mChildEventListener;
    private ValueEventListener mValueEventListener;
    private ChatRoomAdapter mAdapter;
    private List<ChatRoomData> chatRoomDataList = new ArrayList<>();

    @Bind(R.id.community_my_recyclerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.pb_community_my)
    ProgressBar progressBar;

    public CommunityMyFragment() {
    }

    public static Fragment getInstance() {
        return new CommunityMyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community_my, container, false);
        ButterKnife.bind(this, view);
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

    private void initFirebase() {
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final ChatRoomData chatRoomData = dataSnapshot.getValue(ChatRoomData.class);
                chatRoomData.setFirebaseKey(dataSnapshot.getKey());
                chatRoomData.setRoomPeople((int) dataSnapshot.child("people").getChildrenCount());
                DatabaseReference databaseReference = mDatabaseReference.child(chatRoomData.getFirebaseKey()).child("people");
                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        ChatPeople chatPeople = dataSnapshot.getValue(ChatPeople.class);
                        if (chatPeople.getUid().equals(mUser.getUid())) {
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

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new ChatRoomAdapter(this.getContext(), this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(ChatRoomData chatRoomData) {
        gotoChatActivity(chatRoomData);
    }

    private void gotoChatActivity(ChatRoomData chatRoomData) {
        Intent intent = new Intent(getContext(), ChatActivity.class);
        intent.putExtra("room_information", chatRoomData);
        startActivity(intent);
    }
}
