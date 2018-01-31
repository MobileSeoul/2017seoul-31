package com.example.kimjaeseung.cultureseoul2.performance;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.kimjaeseung.cultureseoul2.GlobalApp;
import com.example.kimjaeseung.cultureseoul2.R;
import com.example.kimjaeseung.cultureseoul2.community.AddChatRoomActivity;
import com.example.kimjaeseung.cultureseoul2.domain.CultureEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by heo04 on 2017-08-09.
 */

public class PerformanceGenreFragment extends Fragment implements PerformanceAdapter.PerformanceAdapterOnClickHandler, SearchView.OnQueryTextListener, AdapterView.OnItemSelectedListener {
    private final static String TAG = "PGF";
    private PerformanceAdapter mAdapter;

    @Bind(R.id.performance_rv_genre)
    RecyclerView mPerformanceList;
    @Bind(R.id.performance_spn_genre)
    Spinner mSpinner;

    List<CultureEvent> mCultureEventLIst = new ArrayList<>();
    String[] mGenreStr;
    static String mGenreTitle = "";
    static int mGenreCount = 0;
    static int GenreCategory = 999;
    GlobalApp mGlobalApp;
    MenuItem mMenuItem;
    SearchView mSearchView;

    public PerformanceGenreFragment() {

    }

    public static Fragment getInstance() {
        PerformanceGenreFragment performanceGenreFragment = new PerformanceGenreFragment();
        return performanceGenreFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_performance_genre, container, false);

        ButterKnife.bind(this, view);

        setHasOptionsMenu(true);

        mGlobalApp = GlobalApp.getGlobalApplicationContext();

        mSpinner.setOnItemSelectedListener(this);

        // 스피너 항목 담을 array 생성
        mGenreStr = getResources().getStringArray(R.array.array_genre);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, mGenreStr);

        mSpinner.setAdapter(arrayAdapter);

        if (GenreCategory != 999) {
            mSpinner.setSelection(GenreCategory);
            GenreCategory = 999;
        }


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

        //mProgressBar.setVisibility(View.VISIBLE);

        setData();

    }

    @Override
    public void onClick(CultureEvent cultureEvent) {
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

    private void setData() {
        mAdapter.setItemList(mGlobalApp.getmList());
        mAdapter.notifyAdapter();
        divisionGenre();
    }


    /* 장르 구분 */
    public void divisionGenre() {
        List<CultureEvent> newList = new ArrayList<>();

        for (CultureEvent cultureEvent : mCultureEventLIst) {
            String genre = cultureEvent.getCodeName();
            if (genre.contains(mGenreTitle))
                newList.add(cultureEvent);
        }

        mAdapter.setFilter(newList);

    }


    /* 스피너 선택 이벤트*/
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (mGenreCount == 0) {
            if (mGenreTitle == "")
                mGenreTitle = "콘서트";    // default 장르 값
            else
                mGenreTitle = mGenreStr[position];
        }

        mGenreCount = 0;
        divisionGenre();

        if (mMenuItem != null)
            mMenuItem.collapseActionView(); // 클릭하면 검색창 없어지게

    }

    /* 스피너 선택 이벤트 */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static void setmGenreTitle(int TagID, String setGenre) {
        mGenreTitle = setGenre;
        GenreCategory = TagID;
        mGenreCount = 1;
    }

    /* Searchview 생성 */
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

    /* 필터에서 텍스트 검색 처리 */
    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        List<CultureEvent> newList = new ArrayList<>();
        for (CultureEvent cultureEvent : mCultureEventLIst) {
            String name = cultureEvent.getTitle().toLowerCase();
            String genre = cultureEvent.getCodeName();
            if (name.contains(newText) && genre.contains(mGenreTitle))
                newList.add(cultureEvent);
        }

        mAdapter.setFilter(newList);

        return true;
    }


}