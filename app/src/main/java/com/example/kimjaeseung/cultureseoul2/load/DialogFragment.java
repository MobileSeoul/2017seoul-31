package com.example.kimjaeseung.cultureseoul2.load;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

/**
 * Created by heo04 on 2017-10-23.
 */

public class DialogFragment extends android.app.DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("네트워크 연결 상태를 확인한 후 다시 실행해주세요.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ActivityCompat.finishAffinity(getActivity()); // 클릭했을시 앱 종료
                    }
                });

        return builder.create();
    }
}

