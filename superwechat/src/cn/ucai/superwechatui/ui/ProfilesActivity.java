package cn.ucai.superwechatui.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.easeui.domain.User;
import cn.ucai.easeui.utils.EaseUserUtils;
import cn.ucai.easeui.widget.EaseTitleBar;
import cn.ucai.superwechatui.R;
import cn.ucai.superwechatui.SuperWeChatHelper;
import cn.ucai.superwechatui.utils.MFGT;
import cn.ucai.superwechatui.widget.I;

import static cn.ucai.superwechatui.R.id.username;

/**
 * Created by Administrator on 2017/5/25 0025.
 */

public class ProfilesActivity extends BaseActivity {
    @BindView(R.id.profile_image)
    ImageView profileImage;
    @BindView(R.id.tv_userinfo_nick)
    TextView tvUserinfoNick;
    @BindView(R.id.tv_userinfo_name)
    TextView tvUserinfoName;
    @BindView(R.id.btn_add_contact)
    Button btnAddContact;
    @BindView(R.id.btn_send_msg)
    Button btnSendMsg;
    @BindView(R.id.btn_send_video)
    Button btnSendVideo;
    User user = null;
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.view_user)
    RelativeLayout viewUser;
    @BindView(R.id.txt_note_mark)
    TextView txtNoteMark;

    @Override
    protected void onCreate(Bundle arg0) {
        setContentView(R.layout.activity_profiles);
        super.onCreate(arg0);
        ButterKnife.bind(this);
        initData();
        showLeftBack();
    }

    private void initData() {
        String username = getIntent().getStringExtra(I.User.USER_NAME);
        if (username != null) {
            user = SuperWeChatHelper.getInstance().getAppContactList().get(username);
        }
        if (user == null) {
            user = (User) getIntent().getSerializableExtra(I.User.TABLE_NAME);
        }
        if(user==null&&username.equals(EMClient.getInstance().getCurrentUser())){
            user=SuperWeChatHelper.getInstance().getUserProfileManager().getCurrentAppUserInfo();
        }
        if (user != null) {
            showInfo();
        } else {
            finish();
        }
    }

    private void showInfo() {
        tvUserinfoName.setText(user.getMUserName());
        EaseUserUtils.setAppUserAvatar(ProfilesActivity.this, user, profileImage);
        EaseUserUtils.setAppUserNick(user, tvUserinfoNick);
        showButton(SuperWeChatHelper.getInstance().getAppContactList().containsKey(user.getMUserName()));
    }

    private void showButton(boolean isContact) {
        if(!user.getMUserName().equals(EMClient.getInstance().getCurrentUser())) {
            btnAddContact.setVisibility(isContact ? View.GONE : View.VISIBLE);
            btnSendMsg.setVisibility(isContact ? View.VISIBLE : View.GONE);
            btnSendVideo.setVisibility(isContact ? View.VISIBLE : View.GONE);
        }
    }

    @OnClick(R.id.btn_add_contact)
    public void sendAddContactMsg() {
        MFGT.gotoSendMsg(ProfilesActivity.this, user.getMUserName());
    }

    @OnClick({R.id.btn_send_msg, R.id.btn_send_video})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_send_msg:
                MFGT.gotoChat(ProfilesActivity.this,user.getMUserName());
                break;
            case R.id.btn_send_video:
                MFGT.gotoVideo(ProfilesActivity.this,user.getMUserName(),false);
               /* startActivity(new Intent(ProfilesActivity.this, VideoCallActivity.class).putExtra("username", user.getMUserName())
                        .putExtra("isComingCall", false));*/
                break;
        }
    }
}
