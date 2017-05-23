package cn.ucai.superwechatui.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.easeui.domain.User;
import cn.ucai.easeui.ui.EaseBaseFragment;
import cn.ucai.superwechatui.R;
import cn.ucai.superwechatui.SuperWeChatHelper;
import cn.ucai.superwechatui.utils.MFGT;

/**
 * Created by Administrator on 2017/5/23 0023.
 */

public class ContactFragment extends EaseBaseFragment {
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_wexinhao)
    TextView tvWexinhao;
    Unbinder bind;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.contact_fragment, null);
        bind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setUpView() {
        titleBar.setRightImageResource(R.drawable.em_add);
        titleBar.setTitle(getString(R.string.me));
        User user = SuperWeChatHelper.getInstance().getUserProfileManager().getCurrentAppUserInfo();
        if(user!=null){
            tvNickname.setText(user.getMUserNick());
            tvWexinhao.setText("微信号："+user.getMUserName());
            if(!TextUtils.isEmpty(user.getAvatar())){
                Glide.with(getContext()).load(user.getAvatar()).placeholder(R.drawable.em_default_avatar).
                        into(ivAvatar);
            }else{
                Glide.with(getContext()).load(R.drawable.em_default_avatar).into(ivAvatar);
            }
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }

    @OnClick({R.id.txt_money, R.id.txt_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_money:
                break;
            case R.id.txt_setting:
                MFGT.gotoSetting((MainActivity)getContext());
                break;
        }
    }
}
