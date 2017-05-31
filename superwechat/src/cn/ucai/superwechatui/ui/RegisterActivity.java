/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ucai.superwechatui.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.superwechatui.R;
import cn.ucai.superwechatui.SuperWeChatHelper;
import cn.ucai.superwechatui.data.OnCompleteListener;
import cn.ucai.superwechatui.data.Result;
import cn.ucai.superwechatui.data.net.IUserModel;
import cn.ucai.superwechatui.data.net.UserModel;
import cn.ucai.superwechatui.utils.CommonUtils;
import cn.ucai.superwechatui.utils.L;
import cn.ucai.superwechatui.utils.MD5;
import cn.ucai.superwechatui.utils.MFGT;
import cn.ucai.superwechatui.utils.ResultUtils;
import cn.ucai.superwechatui.widget.I;

/**
 * register screen
 */
public class RegisterActivity extends BaseActivity {

    @BindView(R.id.username)
    EditText userNameEditText;
    @BindView(R.id.user_nickname)
    EditText userNickname;
    @BindView(R.id.password)
    EditText passwordEditText;
    @BindView(R.id.confirm_password)
    EditText confirmPwdEditText;
    Unbinder bind;
    String username, nickname, password;
    ProgressDialog pd;
    IUserModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.em_activity_register);
        bind = ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        model =new UserModel();
        setListener();
    }

    private void setListener() {
        titleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initDialog() {
        pd = new ProgressDialog(this);
        pd.setMessage(getResources().getString(R.string.Is_the_registered));
        pd.show();
    }

    @OnClick(R.id.btn_register)
    public void register(View view) {
        if (checkInput()) {
            initDialog();
            registerApp();
        }else{
            disMissDialog();
        }

    }

    private void disMissDialog() {
        if(pd!=null&&pd.isShowing()){
            pd.dismiss();
        }
    }

    private void registerApp() {
        model.register(RegisterActivity.this, username, nickname, MD5.getMessageDigest(password), new OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                boolean isSuccess=true;
                if (s != null) {
                    L.e("main","registerApp()");
                    Result result = ResultUtils.getResultFromJson(s, Result.class);
                    if(result!=null){
                        if(result.getRetCode()== I.MSG_REGISTER_USERNAME_EXISTS){
                            CommonUtils.showLongToast(R.string.User_already_exists);
                        }else if(result.getRetCode()==I.MSG_REGISTER_FAIL){
                            CommonUtils.showLongToast(R.string.Registration_failed);
                        }else{
                            isSuccess=false;
                            registerEMApp();
                        }
                    }
                    if(isSuccess){
                        disMissDialog();
                    }

                }
            }

            @Override
            public void onError(String error) {
                disMissDialog();
              CommonUtils.showLongToast(R.string.Registration_failed);

            }
        });
    }

    private void registerEMApp() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    // call method in SDK
                    EMClient.getInstance().createAccount(username, MD5.getMessageDigest(password));
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (!RegisterActivity.this.isFinishing())
                               disMissDialog();
                            // save current user
                            SuperWeChatHelper.getInstance().setCurrentUserName(username);
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registered_successfully), Toast.LENGTH_SHORT).show();
                            L.e("main","registerEMApp: "+0);
                            MFGT.gotoLogin(RegisterActivity.this);
                            MFGT.finish(RegisterActivity.this);

                        }
                    });
                } catch (final HyphenateException e) {
                    unRegisterApp();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (!RegisterActivity.this.isFinishing())
                                disMissDialog();
                            int errorCode = e.getErrorCode();
                            if (errorCode == EMError.NETWORK_ERROR) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.USER_ALREADY_EXIST) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.USER_AUTHENTICATION_FAILED) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.USER_ILLEGAL_ARGUMENT) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }).start();

    }

    private void unRegisterApp() {
        model.unRegister(RegisterActivity.this, username, new OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                if(s!=null){
                    Result result = ResultUtils.getResultFromJson(s, Result.class);
                    if(result!=null){
                        L.e("main,","unRegisterApp: "+result.toString());
                    }
                }

            }

            @Override
            public void onError(String error) {
                L.e("main,","unRegisterApp: error:"+error);
            }
        });
    }

    private boolean checkInput() {
        username = userNameEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();
        nickname = userNickname.getText().toString().trim();
        String confirm_pwd = confirmPwdEditText.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, getResources().getString(R.string.User_name_cannot_be_empty), Toast.LENGTH_SHORT).show();
            userNameEditText.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, getResources().getString(R.string.Password_cannot_be_empty), Toast.LENGTH_SHORT).show();
            passwordEditText.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(nickname)) {
            Toast.makeText(this, getResources().getString(R.string.toast_nick_not_isnull), Toast.LENGTH_SHORT).show();
            userNickname.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(confirm_pwd)) {
            Toast.makeText(this, getResources().getString(R.string.Confirm_password_cannot_be_empty), Toast.LENGTH_SHORT).show();
            confirmPwdEditText.requestFocus();
            return false;
        } else if (!password.equals(confirm_pwd)) {
            Toast.makeText(this, getResources().getString(R.string.Two_input_password), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
