package cn.ucai.superwechatui.data.net;


import android.content.Context;
import android.view.GestureDetector;

import java.io.File;

import cn.ucai.easeui.domain.User;
import cn.ucai.superwechatui.data.OnCompleteListener;
import cn.ucai.superwechatui.data.Result;

/**
 * Created by Administrator on 2017/5/19 0019.
 */

public interface IUserModel {
    void register(Context context, String username, String nickname, String password,
                  OnCompleteListener<String> listener);

    void unRegister(Context context, String username, OnCompleteListener<String> listener);
    void loadUserInfo(Context context,String username,OnCompleteListener<String> listener);
    void updateUserNick(Context context , String username, String newnick, OnCompleteListener<String> listener);
    void updateAvatar(Context context, String name, String avatarType, File file,OnCompleteListener<String> listener);
    void addConact(Context context,String name,String contactname, OnCompleteListener<String> listener);
    void deleteContact(Context context, String name, String contactname, OnCompleteListener<String> listener);
    void downloadContactAllList(Context context ,String name, OnCompleteListener<String> listener);
    void createGroup(Context context,String hxid,String name,String des,String owner,
                     boolean isPublic,boolean isInvites,File file, OnCompleteListener<String> listener);
    void findAllGroup(Context context,String name,OnCompleteListener<String> listener);
    void addGroupMembers(Context context,String usernames,String hxid, OnCompleteListener<String> listener);
    void updateGroupNameByHxid(Context context,String hxid,String newGroupName,OnCompleteListener<String> listener);
}
