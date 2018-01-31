package com.example.kimjaeseung.cultureseoul2.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kimjaeseung.cultureseoul2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by kimjaeseung on 2017. 9. 24..
 */

public class ChatListViewAdapter extends BaseAdapter {
    private ArrayList<ChatListItem> chatListItems = new ArrayList<>();

    @Override
    public int getCount() {
        return chatListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return chatListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.community_listview_item, parent, false);
        }

        ImageView photoImageView = (ImageView) convertView.findViewById(R.id.iv_community_photo);
        TextView nameTextView = (TextView) convertView.findViewById(R.id.tv_community_name);

        ChatListItem chatListViewItem = chatListItems.get(position);

        Picasso.with(convertView.getContext()) // 공연 이미지
                .load(chatListViewItem.getPhoto())
                .error(R.drawable.smile_50dp)
                .fit()
                .into(photoImageView);
        nameTextView.setText(chatListViewItem.getName());

        return convertView;
    }

    public void addItem(String name, String photo) {
        ChatListItem item = new ChatListItem();
        item.setName(name);
        item.setPhoto(photo);
        chatListItems.add(item);
    }
}
