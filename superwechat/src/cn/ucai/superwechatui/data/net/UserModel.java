package cn.ucai.superwechatui.data.net;

import android.content.Context;

import java.io.File;

import cn.ucai.easeui.domain.User;
import cn.ucai.superwechatui.data.OkHttpUtils;
import cn.ucai.superwechatui.data.OnCompleteListener;
import cn.ucai.superwechatui.data.Result;
import cn.ucai.superwechatui.widget.I;


/**
 * Created by Administrator on 2017/5/19 0019.
 */

public class UserModel implements IUserModel {
    @Override
    public void register(Context context, String username, String nickname,
                         String password, OnCompleteListener<String> listener) {
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
    public void loadUserInfo(Context context, String username,
                             OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils =new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_USER)
                .addParam(I.User.USER_NAME,username)
                .targetClass(String.class)
                .execute(listener);

    }

    @Override
    public void updateUserNick(Context context, String username, String newnick, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils =new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_USER_NICK)
                .addParam(I.User.USER_NAME,username)
                .addParam(I.User.NICK,newnick)
                .targetClass(String.class)
                .execute(listener);

    }

    @Override
    public void updateAvatar(Context context, String name, String avatarType, File file, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils =new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_AVATAR)
                .addParam(I.NAME_OR_HXID,name)
                .addParam(I.AVATAR_TYPE,avatarType)
                .addFile2(file)
                .targetClass(String.class)
                .post()
                .execute(listener);

    }

    @Override
    public void addConact(Context context, String name, String contactname, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils =new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_ADD_CONTACT)
                .addParam(I.Contact.USER_NAME,name)
                .addParam(I.Contact.CU_NAME,contactname)
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void deleteContact(Context context, String name, String contactname, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils =new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_DELETE_CONTACT)
                .addParam(I.Contact.USER_NAME,name)
                .addParam(I.Contact.CU_NAME,contactname)
                .targetClass(String.class)
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
