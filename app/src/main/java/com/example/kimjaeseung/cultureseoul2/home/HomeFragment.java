package com.example.kimjaeseung.cultureseoul2.home;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kimjaeseung.cultureseoul2.GlobalApp;
import com.example.kimjaeseung.cultureseoul2.R;
import com.example.kimjaeseung.cultureseoul2.community.ChatRoomData;
import com.example.kimjaeseung.cultureseoul2.domain.CultureEvent;
import com.example.kimjaeseung.cultureseoul2.main.MainActivity;
import com.example.kimjaeseung.cultureseoul2.performance.DetailActivity;
import com.example.kimjaeseung.cultureseoul2.performance.PerformanceFragment;
import com.example.kimjaeseung.cultureseoul2.performance.PerformanceGenreFragment;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.go.seoul.culturalevents.CulturalEventTypeMini;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by kimjaeseung on 2017. 7. 11..
 */

public class HomeFragment extends Fragment {
    private final static String TAG = "HomeFragment";
    private String openApiKey = "74776b4f6873696c34364a6368704d";

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("room");
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private ChildEventListener mChildEventListener;
    private ValueEventListener mValueEventListener;
    private List<ChatRoomData> chatRoomDataList = new ArrayList<>();
    private CultureEvent cultureEvent;
    private List<CultureEvent> cultureEventList = new ArrayList<>();
    GlobalApp mGlobalApp;

    @Bind(R.id.home_button_culturalevent)
    CulturalEventTypeMini culturalEventTypeMini;
    @Bind(R.id.ll_home_rank)
    LinearLayout linearLayout;

    public HomeFragment() {
    }

    public static Fragment getInstance() {
        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        culturalEventTypeMini.setOpenAPIKey(openApiKey);

        mGlobalApp = (GlobalApp) getApplicationContext();
        cultureEventList.clear();

        final Handler handler = new Handler();

        final Runnable runnable = new Runnable() {  // 0.5초마다 데이터 불러졌는지 확인
            public void run() {
                if (mGlobalApp.getmList() == null)
                    handler.postDelayed(this, 300);
                else
                    cultureEventList.addAll(mGlobalApp.getmList());
            }
        };

        handler.post(runnable);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
                ChatRoomData chatRoomData = dataSnapshot.getValue(ChatRoomData.class);
                chatRoomDataList.add(chatRoomData);
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

        };
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                initView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabaseReference.addChildEventListener(mChildEventListener);
        mDatabaseReference.addValueEventListener(mValueEventListener);
    }

    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        HashMap<String, Integer> map = new HashMap<>();

        for (ChatRoomData c : chatRoomDataList) {
            Integer count = map.get(c.getPerformanceCode());
            map.put(c.getPerformanceCode(), (count == null) ? 1 : count + 1);
        }
        Object[] a = map.entrySet().toArray();

        Arrays.sort(a, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Map.Entry<String, Integer>) o2).getValue()
                        .compareTo(((Map.Entry<String, Integer>) o1).getValue());
            }
        });
        int rank = 0;
        int sizeOfRank = a.length > 5 ? 5 : a.length;
        for (int i = 0; i < sizeOfRank; i++) {
            ChatRoomData mCrd = null;
            for (ChatRoomData crd : chatRoomDataList) {
                if (crd.getPerformanceCode().equals(((Map.Entry<String, Integer>) a[i]).getKey())) {
                    mCrd = crd;
                    break;
                }
            }
            View view = layoutInflater.inflate(R.layout.home_rank, null, false);
            TextView rankNum = (TextView) view.findViewById(R.id.tv_home_rank_num);
            ImageView iv = (ImageView) view.findViewById(R.id.iv_home_rank_image);
            TextView rankName = (TextView) view.findViewById(R.id.tv_home_rank_perform_name);
            TextView rankContext = (TextView) view.findViewById(R.id.tv_home_rank_perform_context);

            if (mCrd != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                String nowDate = dateFormat.format(date);
                int compare = nowDate.compareTo(mCrd.getPerformanceEndDate());

                if (compare <= 0) {
                    rankNum.setText(String.valueOf(rank + 1));
                    Picasso.with(view.getContext())
                            .load(mCrd.getPerformanceImage())
                            .error(R.drawable.error_image)
                            .fit()
                            .into(iv);
                    rankContext.setText(mCrd.getPerformanceStartDate() + " ~ " + mCrd.getPerformanceEndDate() + "\n\n" + mCrd.getPerformanceGenre() + " / " + mCrd.getPerformanceLocation());
                    rankName.setText(mCrd.getPerformanceName());
                    rankName.setSelected(true);

                    linearLayout.addView(view);

                    final ChatRoomData finalMCrd = mCrd;
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cultureEvent = findCulture(finalMCrd.getPerformanceCode());
                            Intent startToDetailActivity = new Intent(getActivity(), DetailActivity.class);
                            startToDetailActivity.putExtra("key", cultureEvent);
                            startActivity(startToDetailActivity);

                            getActivity().getIntent().putExtra("choose", "");
                        }
                    });
                    rank++;
                }
            }
        }
    }

    CultureEvent findCulture(String code) {
        Iterator<CultureEvent> cit = cultureEventList.iterator();

        while (cit.hasNext()) {
            cultureEvent = cit.next();
            if (cultureEvent.getCultCode() == Integer.parseInt(code)) {
                return cultureEvent;
            }
        }

        return cultureEvent;
    }
}
