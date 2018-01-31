package com.example.kimjaeseung.cultureseoul2.performance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kimjaeseung.cultureseoul2.R;

import java.util.ArrayList;

/**
 * Created by heo04 on 2017-07-23.
 */

public class DetailAdapter extends BaseAdapter {
    private ArrayList<DetailViewItem> mDetailItems = new ArrayList<>();

    @Override
    public int getCount() {
        return mDetailItems.size();
    }

    @Override
    public DetailViewItem getItem(int position) {
        return mDetailItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.detail_list_item, parent, false);
        }

        TextView tv_type = (TextView) convertView.findViewById(R.id.tv_detail_type);
        TextView tv_contents = (TextView) convertView.findViewById(R.id.tv_detail_content);

        DetailViewItem mDetailItem = getItem(position);

        tv_type.setText(mDetailItem.getViewType());
        tv_contents.setText(mDetailItem.getViewContent());

        return convertView;
    }

    public void addItem(String type, String content) {

        DetailViewItem mDetailItem = new DetailViewItem();

        mDetailItem.setViewType(type);
        mDetailItem.setViewContent(content);

        mDetailItems.add(mDetailItem);

    }

}
