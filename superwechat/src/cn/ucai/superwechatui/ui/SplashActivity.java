package cn.ucai.superwechatui.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;

import cn.ucai.superwechatui.R;
import cn.ucai.superwechatui.SuperWeChatHelper;
import cn.ucai.superwechatui.utils.MFGT;

import com.hyphenate.util.EasyUtils;

/**
 * 开屏页
 *
 */
public class SplashActivity extends BaseActivity {

	private static final int sleepTime = 2000;

	@Override
	protected void onCreate(Bundle arg0) {
		setContentView(R.layout.em_activity_splash);
		super.onCreate(arg0);

	}

	@Override
	protected void onStart() {
		super.onStart();

		new Thread(new Runnable() {
			public void run() {
				if (SuperWeChatHelper.getInstance().isLoggedIn()) {
					// auto login mode, make sure all group and conversation is loaed before enter the main screen
					long start = System.currentTimeMillis();
					EMClient.getInstance().chatManager().loadAllConversations();
					EMClient.getInstance().groupManager().loadAllGroups();
					long costTime = System.currentTimeMillis() - start;
					//wait
					if (sleepTime - costTime > 0) {
						try {
							Thread.sleep(sleepTime - costTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					String topActivityName = EasyUtils.getTopActivityName(EMClient.getInstance().getContext());
					if (topActivityName != null && (topActivityName.equals(VideoCallActivity.class.getName()) || topActivityName.equals(VoiceCallActivity.class.getName()))) {
						// nop
						// avoid main screen overlap Calling Activity
					} else {
						//enter main screen
						MFGT.gotoMain(SplashActivity.this);

					}
					finish();
				}else {
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
					}
					MFGT.gotoGuide(SplashActivity.this);
					finish();
				}
			}
		}).start();

	}
	
	/**
	 * get sdk version
	 */
	private String getVersion() {
	    return EMClient.getInstance().VERSION;
	}
}
