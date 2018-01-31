package com.example.kimjaeseung.cultureseoul2.community;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kimjaeseung.cultureseoul2.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kimjaeseung on 2017. 7. 23..
 */

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder> {
    private Context mContext;
    private List<ChatRoomData> chatRoomDataList = new ArrayList<>();
    private ChatRoomAdapterOnClickHandler chatRoomAdapterOnClickHandler;
    private ChatRoomData chatRoomData;
    private int mPosition;

    public interface ChatRoomAdapterOnClickHandler {
        void onClick(ChatRoomData chatRoomData);
    }

    public ChatRoomAdapter(Context context, ChatRoomAdapterOnClickHandler handler) {
        mContext = context;
        chatRoomAdapterOnClickHandler = handler;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.community_listitem_chatroom;
        LayoutInflater inflater = LayoutInflater.from(context);

        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(chatRoomDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return chatRoomDataList.size();
    }

    public void setChatRoomDataList(List<ChatRoomData> chatRoomDataList) {
        this.chatRoomDataList = chatRoomDataList;
    }

    public List<ChatRoomData> getChatRoomDataList() {
        return chatRoomDataList;
    }

    public void setItemList(List<ChatRoomData> itemList) {
        chatRoomDataList.clear();
        chatRoomDataList.addAll(itemList);
    }

    public void addItem(ChatRoomData chatRoomData) {
        chatRoomDataList.add(chatRoomData);
    }

    public void removeItem(int position) {
        chatRoomDataList.remove(position);
    }

    public void removeItemList() {
        chatRoomDataList.clear();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mPerformanceImage;
        private TextView mPerformanceName;
        private TextView mRoomName;
        private TextView mRoomLocation;
        private TextView mRoomDay;
        private TextView mRoomTime;
        private TextView mRoomPeople;
        private TextView mRoomday2;

        public ViewHolder(View itemView) {
            super(itemView);

            mPerformanceImage = (ImageView) itemView.findViewById(R.id.community_iv_chatroom);
            mPerformanceName = (TextView) itemView.findViewById(R.id.community_tv_performancename);
            mRoomName = (TextView) itemView.findViewById(R.id.community_tv_roomname);
            mRoomLocation = (TextView) itemView.findViewById(R.id.community_tv_roomlocation);
            mRoomTime = (TextView) itemView.findViewById(R.id.community_tv_roomtime);
            mRoomDay = (TextView) itemView.findViewById(R.id.community_tv_roomday);
            mRoomday2 = (TextView) itemView.findViewById(R.id.community_tv_roomday2);
            mRoomPeople = (TextView) itemView.findViewById(R.id.community_tv_roompeople);

            itemView.setOnClickListener(this);
        }

        public void bind(ChatRoomData chatRoomData) {
            Picasso.with(itemView.getContext())
                    .load(chatRoomData.getPerformanceImage())
                    .error(R.drawable.error_image)
                    .fit()
                    .into(mPerformanceImage);
            mRoomName.setText(chatRoomData.getRoomName());
            mPerformanceName.setText(chatRoomData.getPerformanceName());
            mRoomPeople.setText("참여인원: " + chatRoomData.getRoomPeople());
            mRoomDay.setText(chatRoomData.getRoomDay());
            mRoomday2.setText(calculateDay(chatRoomData.getRoomDay()));
            mRoomTime.setText(chatRoomData.getRoomTime());
            mRoomLocation.setText(chatRoomData.getRoomLocation());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            chatRoomData = chatRoomDataList.get(position);
            chatRoomAdapterOnClickHandler.onClick(chatRoomData);
        }

        public String calculateDay(String roomDay) {
            String mString = "";
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date performanceDate = formatter.parse(roomDay);
                Date thisDate = formatter.parse(formatter.format(new Date()));

                long diff = performanceDate.getTime() - thisDate.getTime();
                long diffDays = diff / (24 * 60 * 60 * 1000);

                if (diffDays < 0) mString = "이전";
                else if (diffDays == 0) mString = "오늘";
                else if (diffDays == 1) mString = "내일";
                else if (diffDays == 2) mString = "모레";
                else mString = Long.toString(diffDays) + "일 후";

                return mString;

            } catch (ParseException e) {
                e.printStackTrace();
                return mString;
            }
        }
    }

    public void setFilter(List<ChatRoomData> newList) {
        chatRoomDataList = new ArrayList<>();
        chatRoomDataList.addAll(newList);
        notifyDataSetChanged();
    }


}
