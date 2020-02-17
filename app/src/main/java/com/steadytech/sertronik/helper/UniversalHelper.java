package com.steadytech.sertronik.helper;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;


import com.steadytech.sertronik.service.ApiServices;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.steadytech.sertronik.helper.UniversalKey.BASE_URL;


public class UniversalHelper {

    public static Retrofit setInit(){
        return new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
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
