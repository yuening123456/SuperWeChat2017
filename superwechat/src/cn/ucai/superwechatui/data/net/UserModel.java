package cn.ucai.superwechatui.data.net;

import android.content.Context;

import cn.ucai.superwechatui.data.OkHttpUtils;
import cn.ucai.superwechatui.data.OnCompleteListener;
import cn.ucai.superwechatui.widget.I;


/**
 * Created by Administrator on 2017/5/19 0019.
 */

public class UserModel implements IUserModel {
    @Override
    public void register(Context context, String username, String nickname,
                         String password, OnCompleteListener listener) {
        OkHttpUtils<String > utils =new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_REGISTER)
                .addParam(I.User.USER_NAME,username)
                .addParam(I.User.NICK,nickname)
                .addParam(I.User.PASSWORD,password)
                .targetClass(String.class)
                .post()
                .execute(listener);

    }

    @Override
    public void unRegister(Context context, String username, OnCompleteListener listener) {
        OkHttpUtils<String > utils =new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_REGISTER)
                .addParam(I.User.USER_NAME,username)
                .targetClass(String.class)
                .execute(listener);
    }
}
