package cn.ucai.superwechatui.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.easeui.domain.User;
import cn.ucai.superwechatui.R;
import cn.ucai.superwechatui.SuperWeChatHelper;
import cn.ucai.superwechatui.widget.I;

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
    User user=null;
    @Override
    protected void onCreate(Bundle arg0) {
        setContentView(R.layout.activity_profiles);
        super.onCreate(arg0);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        user = (User) getIntent().getSerializableExtra(I.User.TABLE_NAME);
        if(user!=null){
            showInfo();
        }else{
            finish();
        }
    }

    private void showInfo() {
        tvUserinfoName.setText(user.getMUserName());
        tvUserinfoNick.setText(user.getMUserNick()==null?user.getMUserName():user.getMUserNick());
        if(user != null && user.getAvatar() != null){
            try {
                int avatarResId = Integer.parseInt(user.getAvatar());
                Glide.with(ProfilesActivity.this).load(avatarResId).into(profileImage);
            } catch (Exception e) {
                //use default avatar
                Glide.with(ProfilesActivity.this).load(user.getAvatar())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ease_default_avatar).into(profileImage);
            }
        }else{
            Glide.with(ProfilesActivity.this).load(R.drawable.ease_default_avatar).into(profileImage);
        }
        showButton(SuperWeChatHelper.getInstance().getContactList().containsKey(user.getMUserName()));
    }

    private void showButton(boolean isContact) {
        btnAddContact.setVisibility(isContact? View.GONE:View.VISIBLE);
        btnSendMsg.setVisibility(isContact?View.VISIBLE:View.GONE);
        btnSendVideo.setVisibility(isContact?View.VISIBLE:View.GONE);
    }

}
