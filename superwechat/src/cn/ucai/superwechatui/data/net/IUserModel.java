package cn.ucai.superwechatui.data.net;


import android.content.Context;

import cn.ucai.superwechatui.data.OnCompleteListener;

/**
 * Created by Administrator on 2017/5/19 0019.
 */

public interface IUserModel {
 void register(Context context, String username, String nickname, String password,
                  OnCompleteListener listener);

    void unRegister(Context context, String username, OnCompleteListener listener);

}
