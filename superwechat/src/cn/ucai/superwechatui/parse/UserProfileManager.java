package cn.ucai.superwechatui.parse;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;

import cn.ucai.easeui.domain.User;
import cn.ucai.superwechatui.SuperWeChatHelper;
import cn.ucai.superwechatui.data.OnCompleteListener;
import cn.ucai.superwechatui.data.Result;
import cn.ucai.superwechatui.data.net.IUserModel;
import cn.ucai.superwechatui.data.net.UserModel;
import cn.ucai.superwechatui.utils.L;
import cn.ucai.superwechatui.utils.PreferenceManager;
import cn.ucai.easeui.domain.EaseUser;
import cn.ucai.superwechatui.utils.ResultUtils;
import cn.ucai.superwechatui.widget.I;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UserProfileManager {

	/**
	 * application context
	 */
	protected Context appContext = null;

	/**
	 * init flag: test if the sdk has been inited before, we don't need to init
	 * again
	 */
	private boolean sdkInited = false;

	/**
	 * HuanXin sync contact nick and avatar listener
	 */
	private List<SuperWeChatHelper.DataSyncListener> syncContactInfosListeners;

	private boolean isSyncingContactInfosWithServer = false;

	private EaseUser currentUser;
	private User currentAppUser;
	IUserModel model;

	public UserProfileManager() {
	}

	public synchronized boolean init(Context context) {
		if (sdkInited) {
			return true;
		}
		ParseManager.getInstance().onInit(context);
		appContext=context;
		syncContactInfosListeners = new ArrayList<SuperWeChatHelper.DataSyncListener>();
		sdkInited = true;
		model=new UserModel();
		return true;
	}

	public void addSyncContactInfoListener(SuperWeChatHelper.DataSyncListener listener) {
		if (listener == null) {
			return;
		}
		if (!syncContactInfosListeners.contains(listener)) {
			syncContactInfosListeners.add(listener);
		}
	}

	public void removeSyncContactInfoListener(SuperWeChatHelper.DataSyncListener listener) {
		if (listener == null) {
			return;
		}
		if (syncContactInfosListeners.contains(listener)) {
			syncContactInfosListeners.remove(listener);
		}
	}

	public void asyncFetchContactInfosFromServer(List<String> usernames, final EMValueCallBack<List<EaseUser>> callback) {
		if (isSyncingContactInfosWithServer) {
			return;
		}
		isSyncingContactInfosWithServer = true;
		ParseManager.getInstance().getContactInfos(usernames, new EMValueCallBack<List<EaseUser>>() {

			@Override
			public void onSuccess(List<EaseUser> value) {
				isSyncingContactInfosWithServer = false;
				// in case that logout already before server returns,we should
				// return immediately
				if (!SuperWeChatHelper.getInstance().isLoggedIn()) {
					return;
				}
				if (callback != null) {
					callback.onSuccess(value);
				}
			}

			@Override
			public void onError(int error, String errorMsg) {
				isSyncingContactInfosWithServer = false;
				if (callback != null) {
					callback.onError(error, errorMsg);
				}
			}

		});

	}

	public void notifyContactInfosSyncListener(boolean success) {
		for (SuperWeChatHelper.DataSyncListener listener : syncContactInfosListeners) {
			listener.onSyncComplete(success);
		}
	}

	public boolean isSyncingContactInfoWithServer() {
		return isSyncingContactInfosWithServer;
	}

	public synchronized void reset() {
		isSyncingContactInfosWithServer = false;
		currentUser = null;
		PreferenceManager.getInstance().removeCurrentUserInfo();
	}

	public synchronized EaseUser getCurrentUserInfo() {
		if (currentUser == null) {
			String username = EMClient.getInstance().getCurrentUser();
			currentUser = new EaseUser(username);
			String nick = getCurrentUserNick();
			currentUser.setNick((nick != null) ? nick : username);
			currentUser.setAvatar(getCurrentUserAvatar());
		}
		return currentUser;
	}
	public synchronized User getCurrentAppUserInfo() {
		if (currentAppUser == null) {
			String username = EMClient.getInstance().getCurrentUser();
			currentAppUser = new User(username);
			String nick = getCurrentUserNick();
			currentAppUser.setMUserNick((nick != null) ? nick : username);
			currentAppUser.setAvatar(getCurrentUserAvatar());
		}
		return currentAppUser;
	}

	public boolean updateCurrentUserNickName(final String nickname) {
		boolean isSuccess = ParseManager.getInstance().updateParseNickName(nickname);
		if (isSuccess) {
			setCurrentUserNick(nickname);
		}
		return isSuccess;
	}
	public void updateCurrentAppUserNickName( String nickname){
		setCurrentAppUserNick(nickname);
	}


	public String uploadUserAvatar(byte[] data) {
		String avatarUrl = ParseManager.getInstance().uploadParseAvatar(data);
		if (avatarUrl != null) {
			setCurrentUserAvatar(avatarUrl);
		}
		return avatarUrl;
	}

	public void uploadAppUserAvatar(File file) {

	}

	public void asyncGetCurrentUserInfo() {
		ParseManager.getInstance().asyncGetCurrentUserInfo(new EMValueCallBack<EaseUser>() {

			@Override
			public void onSuccess(EaseUser value) {
			    if(value != null){
    				setCurrentUserNick(value.getNick());
    				setCurrentUserAvatar(value.getAvatar());
			    }
			}

			@Override
			public void onError(int error, String errorMsg) {

			}
		});

	}
	public void asyncGetCurrentAppUserInfo(){
		model.loadUserInfo(appContext, EMClient.getInstance().getCurrentUser(), new OnCompleteListener<String>() {
			@Override
			public void onSuccess(String s) {
				if(s!=null){

					Result<User> result= ResultUtils.getResultFromJson(s,User.class);
					if(result!=null&&result.isRetMsg()){
						User user=result.getRetData();

						if(user!=null){
							setCurrentAppUserNick(user.getMUserNick());
							setCurrentAppUserAvatar(user.getAvatar());
						}
					}
				}
			}

			@Override
			public void onError(String error) {

			}
		});

	}

	public void asyncGetUserInfo(final String username,final EMValueCallBack<EaseUser> callback){
		ParseManager.getInstance().asyncGetUserInfo(username, callback);
	}

	private void setCurrentUserNick(String nickname) {
		getCurrentUserInfo().setNick(nickname);
		PreferenceManager.getInstance().setCurrentUserNick(nickname);
	}

	private void setCurrentUserAvatar(String avatar) {
		getCurrentUserInfo().setAvatar(avatar);
		PreferenceManager.getInstance().setCurrentUserAvatar(avatar);
	}

	private String getCurrentUserNick() {
		return PreferenceManager.getInstance().getCurrentUserNick();
	}

	private String getCurrentUserAvatar() {
		return PreferenceManager.getInstance().getCurrentUserAvatar();
	}

	public void setCurrentAppUserAvatar(String avatar) {
		getCurrentAppUserInfo().setAvatar(avatar);
		PreferenceManager.getInstance().setCurrentUserAvatar(avatar);
	}

	public void setCurrentAppUserNick(String nickname) {
		getCurrentAppUserInfo().setMUserNick(nickname);
		PreferenceManager.getInstance().setCurrentUserNick(nickname);

	}


}
