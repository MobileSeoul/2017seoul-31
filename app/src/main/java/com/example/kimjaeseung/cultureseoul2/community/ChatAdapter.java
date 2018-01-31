package com.example.kimjaeseung.cultureseoul2.community;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kimjaeseung.cultureseoul2.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by kimjaeseung on 2017. 7. 22..
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("a h:mm", Locale.getDefault());
    private static final int CHAT_MY = 0;
    private static final int CHAT_OTHER = 1;
    private Context mContext;
    private List<ChatData> chatDataList = new ArrayList<>();
    private ChatAdapterOnClickHandler chatAdapterOnClickHandler;
    private ChatData chatData;
    private int mPosition;
    private String mEmail;

    public interface ChatAdapterOnClickHandler {
        void onClick(ChatData chatData);
    }

    public ChatAdapter(Context context, ChatAdapterOnClickHandler handler) {
        mContext = context;
        chatAdapterOnClickHandler = handler;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case CHAT_MY:
                Context context1 = parent.getContext();
                int layoutIdForListItem1 = R.layout.community_listitem_chat_my;
                LayoutInflater inflater1 = LayoutInflater.from(context1);

                boolean shouldAttachToParentImmediately1 = false;

                View view1 = inflater1.inflate(layoutIdForListItem1, parent, shouldAttachToParentImmediately1);
                MyViewHolder viewHolder1 = new MyViewHolder(view1);

                return viewHolder1;

            case CHAT_OTHER:
                Context context = parent.getContext();
                int layoutIdForListItem = R.layout.community_listitem_chat;
                LayoutInflater inflater = LayoutInflater.from(context);

                boolean shouldAttachToParentImmediately = false;

                View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
                OtherViewHolder viewHolder = new OtherViewHolder(view);

                return viewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case CHAT_MY:
                MyViewHolder myViewHolder = (MyViewHolder) holder;
                myViewHolder.bind(chatDataList.get(position));
                break;
            case CHAT_OTHER:
                OtherViewHolder otherViewHolder = (OtherViewHolder) holder;
                otherViewHolder.bind(chatDataList.get(position));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        ChatData chatData = chatDataList.get(position);
        if (chatData.email.equals(mEmail)) return CHAT_MY;
        else return CHAT_OTHER;
    }

    @Override
    public int getItemCount() {
        return chatDataList.size();
    }

    public void setItemList(List<ChatData> itemList) {
        chatDataList.clear();
        chatDataList.addAll(itemList);
    }

    public void addItem(ChatData chatData) {
        chatDataList.add(chatData);
    }

    public void removeItem(int position) {
        chatDataList.remove(position);
    }


    public class OtherViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView userPhoto;
        private TextView userName;
        private TextView userMessage;
        private TextView userTime;

        public OtherViewHolder(View itemView) {
            super(itemView);

            userPhoto = (ImageView) itemView.findViewById(R.id.community_iv_userphoto);
            userName = (TextView) itemView.findViewById(R.id.community_tv_username);
            userMessage = (TextView) itemView.findViewById(R.id.community_tv_message);
            userTime = (TextView) itemView.findViewById(R.id.community_tv_time);

            itemView.setOnClickListener(this);
        }

        public void bind(ChatData chatData) {
            Picasso.with(itemView.getContext())
                    .load(chatData.userPhoto)
                    .error(R.drawable.error_image)
                    .fit()
                    .into(userPhoto);
            userName.setText(chatData.userName);
            userMessage.setText(chatData.message);
            userTime.setText(mSimpleDateFormat.format(chatData.time));
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            chatData = chatDataList.get(position);
            chatAdapterOnClickHandler.onClick(chatData);
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView myMessage;
        private TextView myTime;

        public MyViewHolder(View itemView) {
            super(itemView);

            myMessage = (TextView) itemView.findViewById(R.id.community_tv_message_my);
            myTime = (TextView) itemView.findViewById(R.id.community_tv_time_my);

            itemView.setOnClickListener(this);
        }

        public void bind(ChatData chatData) {
            myMessage.setText(chatData.message);
            myTime.setText(mSimpleDateFormat.format(chatData.time));
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            chatData = chatDataList.get(position);
            chatAdapterOnClickHandler.onClick(chatData);
        }
    }
}

