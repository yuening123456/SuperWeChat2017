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
    public void downloadContactAllList(Context context, String name, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils =new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_DOWNLOAD_CONTACT_ALL_LIST)
                .addParam(I.Contact.USER_NAME,name)
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void createGroup(Context context, String hxid, String name, String des, String owner, boolean isPublic, boolean isInvites, File file, OnCompleteListener<String> listener) {
        OkHttpUtils<String > utils =new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_CREATE_GROUP)
                .addParam(I.Group.HX_ID,hxid)
                .addParam(I.Group.NAME,name)
                .addParam(I.Group.DESCRIPTION,des)
                .addParam(I.Group.OWNER,owner)
                .addParam(I.Group.IS_PUBLIC,String.valueOf(isPublic))
                .addParam(I.Group.ALLOW_INVITES,String.valueOf(isInvites))
                .addFile2(file)
                .targetClass(String.class)
                .post()
                .execute(listener);
    }

    @Override
    public void findAllGroup(Context context, String name, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils =new OkHttpUtils<>(context);
        utils.setRequestUrl("findAllGroupByUserName")
                .addParam("m_user_name",name)
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void addGroupMembers(Context context, String usernames, String hxid, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils =new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_ADD_GROUP_MEMBERS)
                .addParam(I.Member.GROUP_HX_ID,hxid)
                .addParam(I.Member.USER_NAME,usernames)
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void updateGroupNameByHxid(Context context, String hxid, String newGroupName, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils =new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_GROUP_NAME_BYHXID)
                .addParam(I.Group.HX_ID,hxid)
                .addParam(I.Group.NAME,newGroupName)
                .targetClass(String.class)
                .execute(listener);
    }

 /*   @Override
    public void downLoadAvatar(Context context, String hxid, String avatarType, String avatarSuffix, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils =new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_DOWNLOAD_AVATAR)
                .addParam(I.Group.HX_ID,hxid)
                .addParam(I.AVATAR_TYPE,avatarType)
                .addParam("m_avatar_suffix",avatarSuffix)
                .targetClass(String.class)
                .execute(listener);
    }*/


    @Override
    public void unRegister(Context context, String username, OnCompleteListener listener) {
        OkHttpUtils<String > utils =new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_REGISTER)
                .addParam(I.User.USER_NAME,username)
                .targetClass(String.class)
                .execute(listener);
    }
}
