package cn.ucai.superwechatui.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import cn.ucai.easeui.domain.User;
import cn.ucai.superwechatui.Manifest;
import cn.ucai.superwechatui.R;
import cn.ucai.superwechatui.ui.AddContactActivity;
import cn.ucai.superwechatui.ui.ChatActivity;
import cn.ucai.superwechatui.ui.GuideActivity;
import cn.ucai.superwechatui.ui.LoginActivity;
import cn.ucai.superwechatui.ui.MainActivity;
import cn.ucai.superwechatui.ui.ProfilesActivity;
import cn.ucai.superwechatui.ui.RecorderVideoActivity;
import cn.ucai.superwechatui.ui.RegisterActivity;
import cn.ucai.superwechatui.ui.SendAddContactActivity;
import cn.ucai.superwechatui.ui.SettingsActivity;
import cn.ucai.superwechatui.ui.SplashActivity;
import cn.ucai.superwechatui.ui.UserProfileActivity;
import cn.ucai.superwechatui.ui.VideoCallActivity;
import cn.ucai.superwechatui.widget.I;

/**
 * Created by Administrator on 2017/5/19 0019.
 */

public class MFGT  {
    public static void finish(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    public static void startActivity(Context context,Intent intent){
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
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
    public static void gotoMain(Activity activity,boolean isChat) {
        startActivity(activity,new Intent(activity, MainActivity.class).putExtra(I.RESULT_IS_CHAT,isChat));
    }

    public static void gotoLogin(Activity activity) {
        startActivity(activity,LoginActivity.class);
    }

    public static void gotoRegister(Activity activity) {
        startActivity(activity, RegisterActivity.class);
    }

    public static void gotoSettings(Activity activity) {
        startActivity(activity, SettingsActivity.class);
    }

    public static void logout(Activity activity){
        startActivity(activity,new Intent(activity,LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_SINGLE_TOP));
    }

    public static void gotoProfiles(Activity activity,User user) {
        startActivity(activity,new Intent(activity,ProfilesActivity.class).putExtra(I.User.TABLE_NAME,user));
    }
    public static void gotoProfiles(Activity activity,String  username) {
        startActivity(activity,new Intent(activity,ProfilesActivity.class).putExtra(I.User.USER_NAME,username));
    }

    public static void gotoSendMsg(ProfilesActivity activity, String username) {
        startActivity(activity,new Intent(activity,SendAddContactActivity.class)
        .putExtra(I.User.USER_NAME,username));
    }

    public static void gotoChat(ProfilesActivity activity, String username) {
        startActivity(activity,new Intent(activity,ChatActivity.class)
                .putExtra("userId",username));
    }

    public static void gotoVideo(ProfilesActivity activity, String username, boolean b) {
        startActivity(activity,new Intent(activity, VideoCallActivity.class).putExtra("username",username)
                .putExtra("isComingCall", b));
    }
}
