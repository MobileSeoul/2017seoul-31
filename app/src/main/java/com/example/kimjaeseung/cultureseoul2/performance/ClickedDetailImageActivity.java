package com.example.kimjaeseung.cultureseoul2.performance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.kimjaeseung.cultureseoul2.R;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by heo04 on 2017-10-23.
 */

public class ClickedDetailImageActivity extends AppCompatActivity {
    @Bind(R.id.iv_detail_image_clicked)
    ImageView mClickedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_image_clicked);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String message = (String) intent.getSerializableExtra("img");

        Picasso.with(this) // 공연 이미지
                .load(message)
                .error(R.drawable.error_image)
                .into(mClickedImage);
    }
}
