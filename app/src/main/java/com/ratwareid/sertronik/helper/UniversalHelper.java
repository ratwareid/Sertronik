package com.ratwareid.sertronik.helper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;


import com.ratwareid.sertronik.R;
import com.ratwareid.sertronik.service.ApiServices;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.ratwareid.sertronik.helper.UniversalKey.BASE_URL;


public class UniversalHelper {

    public static Retrofit setInit(){
        return new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradientPrimary (Activity activity) {
        Window window = activity.getWindow();
        Drawable background = activity.getResources().getDrawable(R.drawable.gradient_primary);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
        window.setBackgroundDrawable(background);
    }


    public static ApiServices getApiServiceInstance(){
        return setInit().create(ApiServices.class);
    }


    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
