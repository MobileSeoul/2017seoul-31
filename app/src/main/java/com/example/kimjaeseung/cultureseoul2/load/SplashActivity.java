package com.example.kimjaeseung.cultureseoul2.load;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.kimjaeseung.cultureseoul2.GlobalApp;
import com.example.kimjaeseung.cultureseoul2.main.MainActivity;
import com.example.kimjaeseung.cultureseoul2.utils.LoadDataCallback;

/**
 * Created by Nick0917 on 2017-08-10.
 */

public class SplashActivity extends Activity {

    private GlobalApp mGlobalApp;
    private DialogFragment mDialogFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGlobalApp = GlobalApp.getGlobalApplicationContext();

        mDialogFragment = new DialogFragment();

        mGlobalApp.loadData(new LoadDataCallback() {
            @Override
            public void onSuccess() {
                // 로그인페이지 이동
                startActivity(new Intent(SplashActivity.this, MainActivity.class));

                finish();
            }

            @Override
            public void onFailure() {
                // 네트워크 연결 안돼있을 시 dialog 띄우고 앱 종료
                mDialogFragment.show(getFragmentManager(), "dialog");
            }
        });

    }

}
