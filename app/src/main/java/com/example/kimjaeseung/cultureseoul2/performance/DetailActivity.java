package com.example.kimjaeseung.cultureseoul2.performance;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kimjaeseung.cultureseoul2.R;
import com.example.kimjaeseung.cultureseoul2.domain.CultureEvent;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.example.kimjaeseung.cultureseoul2.utils.DateUtils.dateToString;

/**
 * Created by heo04 on 2017-07-22.
 */

public class DetailActivity extends AppCompatActivity {
    @Bind(R.id.btn_detail_back)
    Button mButtonBack;
    @Bind(R.id.tv_detail_title)
    TextView mDetailTitle;
    @Bind(R.id.iv_detail_image)
    ImageView mDetailImage;
    @Bind(R.id.lv_detail)
    ListView mDetailLIst;
    @Bind(R.id.btn_detail_reserve)
    Button mButtonReserve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        final CultureEvent cultureEvent = (CultureEvent) intent.getSerializableExtra("key");

        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDetailTitle.setText(cultureEvent.getTitle());
        mDetailTitle.setSingleLine(true); //한줄로 나오게 하기.
        mDetailTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        mDetailTitle.setSelected(true);

        Picasso.with(this) // 공연 이미지
                .load(cultureEvent.getMainImg().toLowerCase())
                .error(R.drawable.error_image)
                .into(mDetailImage);

        mDetailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // 클릭하면 원본 크기 이미지로
                Intent intent = new Intent(DetailActivity.this, ClickedDetailImageActivity.class);
                String message = cultureEvent.getMainImg().toLowerCase();
                intent.putExtra("img", message);
                startActivity(intent);
            }
        });

        DetailAdapter mDetailAdapter = new DetailAdapter();
        mDetailLIst.setAdapter(mDetailAdapter);
        mDetailLIst.setDivider(null);

        mDetailAdapter.addItem("기간 : ", dateToString(cultureEvent.getStartDate()) + " ~ " + dateToString(cultureEvent.getEndDate()));
        mDetailAdapter.addItem("시간 : ", cultureEvent.getTime());
        mDetailAdapter.addItem("장소 : ", cultureEvent.getPlace());
        mDetailAdapter.addItem("이용대상 : ", cultureEvent.getUseTrgt());
        mDetailAdapter.addItem("이용요금 : ", cultureEvent.getUseFee());
        mDetailAdapter.addItem("주최 : ", cultureEvent.getSponsor());
        mDetailAdapter.addItem("문의 : ", cultureEvent.getInquiry());

        mButtonReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String homeURL = cultureEvent.getOrgLink();
                if (homeURL.charAt(0) == ' ')
                    homeURL = homeURL.substring(1);
                intent.setData(Uri.parse(homeURL));
                startActivity(intent);
            }
        });

    }

}
