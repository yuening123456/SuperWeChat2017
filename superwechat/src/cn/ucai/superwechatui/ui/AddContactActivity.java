/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
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
import android.widget.RelativeLayout;
import android.widget.TextView;


import cn.ucai.easeui.domain.User;
import cn.ucai.superwechatui.R;

import cn.ucai.easeui.widget.EaseAlertDialog;
import cn.ucai.superwechatui.data.OnCompleteListener;
import cn.ucai.superwechatui.data.Result;
import cn.ucai.superwechatui.data.net.IUserModel;
import cn.ucai.superwechatui.data.net.UserModel;
import cn.ucai.superwechatui.utils.MFGT;
import cn.ucai.superwechatui.utils.ResultUtils;

public class AddContactActivity extends BaseActivity{
	private EditText editText;
	private RelativeLayout searchedUserLayout;
	//private TextView nameText;
	private String toAddUsername;
	private ProgressDialog progressDialog;
	IUserModel model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.em_activity_add_contact);
		super.onCreate(savedInstanceState);
		editText = (EditText) findViewById(R.id.edit_note);
		String strUserName = getResources().getString(R.string.user_name);
		editText.setHint(strUserName);
		searchedUserLayout = (RelativeLayout) findViewById(R.id.ll_user);

        model=new UserModel();
        initView();
       	showLeftBack();

	}
	private void initDialog(){
        progressDialog=new ProgressDialog(AddContactActivity.this);
        progressDialog.setMessage(getString(R.string.searching));
        progressDialog.show();
    }
    private void dissmissDialog(){
        if(progressDialog!=null&&progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

	private void initView() {
		titleBar.setLeftLayoutClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MFGT.finish(AddContactActivity.this);
			}
		});
		titleBar.setRightLayoutClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				searchContact();
			}
		});
	}


	/**
	 * search contact
	 * @param
	 */
	public void searchContact() {
		final String name = editText.getText().toString();
			toAddUsername = name;
			if(TextUtils.isEmpty(name)) {
				new EaseAlertDialog(this, R.string.Please_enter_a_username).show();
				return;
			}
		// TODO you can search the user from your app server here.
        searchContactFromAppServer();
		//show the userame and add button if user exist
		//searchedUserLayout.setVisibility(View.VISIBLE);
		//nameText.setText(toAddUsername);
	}

	private void searchContactFromAppServer() {

        //initDialog();
		model.loadUserInfo(AddContactActivity.this, toAddUsername, new OnCompleteListener<String>() {
			@Override
			public void onSuccess(String s) {
				boolean isSuccess=false;
                User user=null;
				if(s!=null){
					Result<User> result=ResultUtils.getResultFromJson(s,User.class);
                    if(result!=null&&result.isRetMsg()){
                       user=result.getRetData();
                        if(user!=null){
                            isSuccess=true;
                        }
                    }
				}
				showSearchResult(isSuccess,user);
			}

			@Override
			public void onError(String error) {
                showSearchResult(false,null);
			}
		});
	}

    private void showSearchResult(boolean isSuccess,User user) {
        dissmissDialog();
        searchedUserLayout.setVisibility(isSuccess?View.GONE:View.VISIBLE);
        if(isSuccess){
            MFGT.gotoProfiles(AddContactActivity.this,user) ;

        }
    }

    /**
	 *  add contact
	 * @param view
	 */
	public void addContact(View view){/*
		if(EMClient.getInstance().getCurrentUser().equals(nameText.getText().toString())){
			new EaseAlertDialog(this, R.string.not_add_myself).show();
			return;
		}
		
		if(SuperWeChatHelper.getInstance().getContactList().containsKey(nameText.getText().toString())){
		    //let the user know the contact already in your contact list
		    if(EMClient.getInstance().contactManager().getBlackListUsernames().contains(nameText.getText().toString())){
		        new EaseAlertDialog(this, R.string.user_already_in_contactlist).show();
		        return;
		    }
			new EaseAlertDialog(this, R.string.This_user_is_already_your_friend).show();
			return;
		}
		
		progressDialog = new ProgressDialog(this);
		String stri = getResources().getString(R.string.Is_sending_a_request);
		progressDialog.setMessage(stri);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		
		new Thread(new Runnable() {
			public void run() {
				
				try {
					//demo use a hardcode reason here, you need let user to input if you like
					String s = getResources().getString(R.string.Add_a_friend);
					EMClient.getInstance().contactManager().addContact(toAddUsername, s);
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							String s1 = getResources().getString(R.string.send_successful);
							Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_LONG).show();
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							String s2 = getResources().getString(R.string.Request_add_buddy_failure);
							Toast.makeText(getApplicationContext(), s2 + e.getMessage(), Toast.LENGTH_LONG).show();
						}
					});
				}
			}
		}).start();*/
	}
	

}
