package cn.ucai.easeui.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import cn.ucai.easeui.R;
import cn.ucai.easeui.controller.EaseUI;
import cn.ucai.easeui.controller.EaseUI.EaseUserProfileProvider;
import cn.ucai.easeui.domain.EaseUser;
import cn.ucai.easeui.domain.User;

public class EaseUserUtils {
    
    static EaseUserProfileProvider userProvider;
    //static  EaseUserProfileProvider appUserProvider;
    
    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();

    }
    
    /**
     * get EaseUser according username
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username){
        if(userProvider != null)
            return userProvider.getUser(username);
        
        return null;
    }
    public static User getAppUserInfo(String username){
        if(userProvider != null)
            return userProvider.getAppUser(username);

        return null;
    }

    
    /**
     * set user avatar
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView){
    	EaseUser user = getUserInfo(username);
        if(user != null && user.getAvatar() != null){
            try {
                int avatarResId = Integer.parseInt(user.getAvatar());
                Glide.with(context).load(avatarResId).into(imageView);
            } catch (Exception e) {
                //use default avatar
                Glide.with(context).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ease_default_avatar).into(imageView);
            }
        }else{
            Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
        }
    }
    
    /**
     * set user's nickname
     */
    public static void setUserNick(String username,TextView textView){
        if(textView != null){
        	EaseUser user = getUserInfo(username);
        	if(user != null && user.getNick() != null){
        		textView.setText(user.getNick());
        	}else{
        		textView.setText(username);
        	}
        }
    }
    public static void setAppUserNick(String username,TextView textView){
        if(textView != null){
            User user = getAppUserInfo(username);
            setAppUserNick(user,textView);
        }
    }
    public static void setAppUserNick(User user,TextView textView){
            if(user != null && user.getMUserNick()!= null){
                textView.setText(user.getMUserNick());
            }else{
                textView.setText(user.getMUserNick());
            }

    }
    public static void setNick(String nickname,TextView textView){
        if(textView!=null){
            textView.setText(nickname);
        }
    }
    public static void setAppUserAvatar(Context context,String username, ImageView imageView){
        User user = getAppUserInfo(username);
        setAppUserAvatar(context,user,imageView);
    }
    public static void setAppUserAvatar(Context context,User user, ImageView imageView){
        if(user != null && user.getAvatar() != null){
           setAvatar(context,user.getAvatar(),imageView);
        }else{
            Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
        }
    }
    public static void setAvatar(Context context,String avatarPath,ImageView imageView){
        if(avatarPath!=null){
            try{
                int avatarResId=Integer.parseInt(avatarPath);
                Glide.with(context).load(avatarResId).into(imageView);
            }catch (Exception e){
                Glide.with(context).load(avatarPath).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ease_default_avatar).into(imageView);
            }
        }
    }


}
