package com.example.kimjaeseung.cultureseoul2.performance;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
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

import com.example.kimjaeseung.cultureseoul2.GlobalApp;
import com.example.kimjaeseung.cultureseoul2.R;
import com.example.kimjaeseung.cultureseoul2.community.AddChatRoomActivity;
import com.example.kimjaeseung.cultureseoul2.domain.CultureEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by heo04 on 2017-08-10.
 */

public class PerformanceDateFragment extends Fragment implements PerformanceAdapter.PerformanceAdapterOnClickHandler, SearchView.OnQueryTextListener {
    private final static String TAG = "PDF";
    private PerformanceAdapter mAdapter;

    @Bind(R.id.performance_rv_date)
    RecyclerView mPerformanceList;
    @Bind(R.id.performance_btn_date)
    Button mButton;
    private int mYear = 0, mMonth = 0, mDay = 0;
    private String mCurDate;

    List<CultureEvent> mCultureEventLIst = new ArrayList<>();
    GlobalApp mGlobalApp;
    MenuItem mMenuItem;
    SearchView mSearchView;

    public PerformanceDateFragment() {

    }

    public static Fragment getInstance() {
        PerformanceDateFragment performanceDateFragment = new PerformanceDateFragment();
        return performanceDateFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_performance_date, container, false);

        ButterKnife.bind(this, view);

        setHasOptionsMenu(true);

        mGlobalApp = GlobalApp.getGlobalApplicationContext();

        Calendar calendar = new GregorianCalendar();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        mButton.setOnClickListener(new View.OnClickListener()   // 기간 선택
        {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), R.style.DatePicker, mDateSetListener, mYear, mMonth - 1, mDay).show();

            }
        });

        mCurDate = String.valueOf(mYear + "-" + mMonth + "-" + mDay);
        mButton.setText(mCurDate);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mPerformanceList.setLayoutManager(layoutManager);
        mPerformanceList.setHasFixedSize(true);

        mAdapter = new PerformanceAdapter(this.getContext(), this, mCultureEventLIst);
        mPerformanceList.setAdapter(mAdapter);

        setData();

    }


    @Override
    public void onClick(CultureEvent cultureEvent) {
        //채팅방추가를 위해 intent 넘어옴
        String choose = getActivity().getIntent().getStringExtra("choose");
        if (choose != null && choose.equals(AddChatRoomActivity.class.getSimpleName())) {
            Intent intent = new Intent(getActivity(), AddChatRoomActivity.class);
            intent.putExtra("key", cultureEvent);
            startActivity(intent);

        } else {
            Intent startToDetailActivity = new Intent(getActivity(), DetailActivity.class);
            startToDetailActivity.putExtra("key", cultureEvent);
            startActivity(startToDetailActivity);
        }
        getActivity().getIntent().putExtra("choose", "");
    }


    DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mYear = year;
            mMonth = month + 1;
            mDay = dayOfMonth;

            mCurDate = String.valueOf(mYear + "-" + mMonth + "-" + mDay);
            mButton.setText(mCurDate);

            if (mMenuItem != null)
                mMenuItem.collapseActionView(); // 클릭하면 검색창 없어지게

            divisionDate();
        }
    };


    private void setData() {
        mAdapter.setItemList(mGlobalApp.getmList());
        mAdapter.notifyAdapter();
        divisionDate();
    }

    public void divisionDate() {
        List<CultureEvent> newList = new ArrayList<>();

        for (CultureEvent cultureEvent : mCultureEventLIst) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

            Date startDate = cultureEvent.getStartDate();
            Date endDate = cultureEvent.getEndDate();
            Date selectedDate = null;
            try {
                selectedDate = formatter.parse(mCurDate);
            } catch (ParseException e) {
                Log.e(TAG, "Date parsing error", e);
            }

            if (selectedDate.compareTo(startDate) >= 0 && selectedDate.compareTo(endDate) <= 0)   // 날짜 비교
                newList.add(cultureEvent);
        }

        mAdapter.setFilter(newList);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_performance, menu);
        mMenuItem = menu.findItem(R.id.item_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(mMenuItem); // 액션바에 searchview 추가
        mSearchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        List<CultureEvent> newList = new ArrayList<>();

        for (CultureEvent cultureEvent : mCultureEventLIst) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

            Date startDate = cultureEvent.getStartDate();
            Date endDate = cultureEvent.getEndDate();
            Date selectedDate = null;
            try {
                selectedDate = formatter.parse(mCurDate);
            } catch (ParseException e) {
                Log.e(TAG, "Date parsing error", e);
            }

            String name = cultureEvent.getTitle().toLowerCase();
            if (name.contains(newText) && selectedDate.compareTo(startDate) >= 0 && selectedDate.compareTo(endDate) <= 0)   // 날짜 비교
                newList.add(cultureEvent);
        }

        mAdapter.setFilter(newList);
        return false;
    }
}
