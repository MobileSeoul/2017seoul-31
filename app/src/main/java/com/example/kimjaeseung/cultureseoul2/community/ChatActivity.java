package com.example.kimjaeseung.cultureseoul2.community;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kimjaeseung.cultureseoul2.R;
import com.example.kimjaeseung.cultureseoul2.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kimjaeseung on 2017. 7. 22..
 */

public class ChatActivity extends AppCompatActivity implements ChatAdapter.ChatAdapterOnClickHandler {
    private static final String TAG = ChatActivity.class.getSimpleName();

    @Bind(R.id.community_chat_recyclerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.community_chat_edittext)
    EditText chatEditText;
    @Bind(R.id.community_chat_button)
    Button chatButton;

    private List<ChatData> chatDataList = new ArrayList<>();
    private List<ChatPeople> chatPeoples = new ArrayList<>();
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference reference;
    private DatabaseReference peopleReference;
    private ChatAdapter mAdapter;

    ChatRoomData chatRoomData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_chat);
        ButterKnife.bind(this);

        initView();
        initFirebaseDatabase();
        initToolbar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (chatEditText != null && textWatcher != null)
            chatEditText.removeTextChangedListener(textWatcher);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.community_chat__item_roominfo:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        ChatActivity.this);
                alertDialogBuilder.setTitle("방정보");
                alertDialogBuilder
                        .setIcon(R.drawable.send_button)
                        .setMessage(
                                "- 방이름: " + chatRoomData.getRoomName() + "\n"
                                        + "- 공연이름: " + chatRoomData.getPerformanceName() + "\n"
                                        + "- 모임장소: " + chatRoomData.getRoomLocationName() + "(" + chatRoomData.getRoomLocation() + ")\n"
                                        + "- 모임날짜: " + chatRoomData.getRoomDay() + "\n"
                                        + "- 모임시간: " + chatRoomData.getRoomTime() + "\n"
                                        + "- 모임인원: " + chatRoomData.getRoomPeople())
                        .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
                return true;
            case R.id.community_chat_item_location:
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + chatRoomData.getRoomLocationName());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
                return true;
            case R.id.community_chat_item_people:
                View view = getLayoutInflater().inflate(R.layout.community_listview, null);
                ListView listView = (ListView) view.findViewById(R.id.lv_community);
                ChatListViewAdapter chatListViewAdapter = new ChatListViewAdapter();
                listView.setAdapter(chatListViewAdapter);
                for (ChatPeople c : chatPeoples) {
                    chatListViewAdapter.addItem(c.getName(), c.getPhoto());
                }

                AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(
                        ChatActivity.this);
                alertDialogBuilder2
                        .setIcon(R.drawable.send_button)
                        .setTitle("대화상대")
                        .setView(view)
                        .create().show();
                return true;
            case R.id.community_chat_item_exit:
                if (chatPeoples.size() == 1) {
                    peopleReference.getParent().removeValue();
                    FirebaseDatabase.getInstance().getReference(peopleReference.getParent().getKey()).removeValue();
                } else {
                    for (ChatPeople c : chatPeoples) {
                        if (c.getUid().equals(mUser.getUid())) {
                            peopleReference.child(c.getFirebaseKey()).removeValue();
                        }
                    }
                }
                gotoCommunity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initView() {
        Intent intent = getIntent();
        chatRoomData = (ChatRoomData) intent.getSerializableExtra("room_information");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new ChatAdapter(this, this);
        mAdapter.setEmail(mUser.getEmail());
        mRecyclerView.setAdapter(mAdapter);

        chatEditText.addTextChangedListener(textWatcher);
    }

    private void initFirebaseDatabase() {
        reference = FirebaseDatabase.getInstance().getReference(chatRoomData.getFirebaseKey());
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatData chatData = dataSnapshot.getValue(ChatData.class);
                chatData.firebaseKey = dataSnapshot.getKey();
                chatDataList.add(chatData);
                mAdapter.addItem(chatData);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String firebaseKey = dataSnapshot.getKey();
                int count = chatDataList.size();
                for (int i = 0; i < count; i++) {
                    if (chatDataList.get(i).firebaseKey.equals(firebaseKey)) {
                        chatDataList.remove(i);
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
        });
        peopleReference = FirebaseDatabase.getInstance().getReference("room").child(chatRoomData.getFirebaseKey()).child("people");
        peopleReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatPeople chatPeople = dataSnapshot.getValue(ChatPeople.class);
                chatPeople.setFirebaseKey(dataSnapshot.getKey());
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
    }

    private void initToolbar() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.community_chat_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setBackgroundResource(R.color.titlebackground);
        getSupportActionBar().setTitle(chatRoomData.getRoomName() + "채팅방");
        myToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        myToolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_more_vert_white_24dp));
    }


    @OnClick({R.id.community_chat_button})
    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.community_chat_button:
                if (!chatEditText.getText().toString().isEmpty()) {
                    Calendar calendar = Calendar.getInstance();
                    long now = calendar.getTimeInMillis();

                    ChatData chatData = new ChatData();
                    chatData.userPhoto = mUser.getPhotoUrl().toString();
                    chatData.message = chatEditText.getText().toString();
                    chatData.email = mUser.getEmail();
                    chatData.userName = mUser.getDisplayName();
                    chatData.time = now;

                    reference.push().setValue(chatData);

                    chatEditText.setText("");
                } else {
                    Toast.makeText(ChatActivity.this, "채팅을 입력해 주세요", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    public void onClick(ChatData chatData) {

    }

    @Override
    public void onBackPressed() {
        gotoCommunity();
    }

    private void gotoCommunity() {
        Intent intent = new Intent(ChatActivity.this, MainActivity.class);
        intent.putExtra("select_page", CommunityFragment.class.getSimpleName());
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private TextWatcher textWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(chatEditText.getText()))
                chatButton.setBackgroundColor(Color.YELLOW);
            else chatButton.setBackgroundColor(Color.LTGRAY);
        }
    };


}
