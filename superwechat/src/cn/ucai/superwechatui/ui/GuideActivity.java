package cn.ucai.superwechatui.ui;

import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechatui.R;
import cn.ucai.superwechatui.utils.MFGT;

/**
 * Created by Administrator on 2017/5/19 0019.
 */

public class GuideActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.btn_login, R.id.splash_root})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                MFGT.gotoLogin(GuideActivity.this);
                break;
            case R.id.splash_root:
                MFGT.gotoRegister(GuideActivity.this);
                break;
        }
    }
}
