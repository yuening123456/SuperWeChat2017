package cn.ucai.superwechatui.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import cn.ucai.superwechatui.R;
import cn.ucai.superwechatui.ui.GuideActivity;
import cn.ucai.superwechatui.ui.LoginActivity;
import cn.ucai.superwechatui.ui.MainActivity;
import cn.ucai.superwechatui.ui.RecorderVideoActivity;
import cn.ucai.superwechatui.ui.RegisterActivity;
import cn.ucai.superwechatui.ui.SplashActivity;

/**
 * Created by Administrator on 2017/5/19 0019.
 */

public class MFGT  {

    public static void finish(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);

    }
    private static void startActivity(Context context,Class clazz){
        context.startActivity(new Intent(context,clazz));
        ((Activity)context).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    public static void gotoGuide(Activity activity) {
        startActivity(activity, GuideActivity.class);
    }

    public static void gotoMain(Activity activity) {
        startActivity(activity, MainActivity.class);
    }

    public static void gotoLogin(Activity activity) {
        startActivity(activity,LoginActivity.class);
    }

    public static void gotoRegister(Activity activity) {
        startActivity(activity, RegisterActivity.class);
    }


}
