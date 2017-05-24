package cn.ucai.superwechatui.ui;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.easeui.domain.EaseUser;
import cn.ucai.easeui.domain.User;
import cn.ucai.easeui.utils.EaseUserUtils;
import cn.ucai.superwechatui.R;
import cn.ucai.superwechatui.SuperWeChatHelper;
import cn.ucai.superwechatui.data.OnCompleteListener;
import cn.ucai.superwechatui.data.Result;
import cn.ucai.superwechatui.data.net.IUserModel;
import cn.ucai.superwechatui.data.net.UserModel;
import cn.ucai.superwechatui.utils.CommonUtils;
import cn.ucai.superwechatui.utils.L;
import cn.ucai.superwechatui.utils.MFGT;
import cn.ucai.superwechatui.utils.ResultUtils;
import cn.ucai.superwechatui.widget.I;

public class UserProfileActivity extends BaseActivity {

    private static final int REQUESTCODE_PICK = 1;
    private static final int REQUESTCODE_CUTTING = 2;
    @BindView(R.id.iv_userinfo_avatar)
    ImageView mIvUserinfoAvatar;
    @BindView(R.id.tv_userinfo_nick)
    TextView mTvUserinfoNick;
    @BindView(R.id.tv_userinfo_name)
    TextView mTvUserinfoName;
    private ProgressDialog dialog;
    User user = null;
    IUserModel model;
    String avatarName;
    UpdateAvatarBroadcastReceiver mReceiver;


    @Override
    protected void onCreate(Bundle arg0) {
        setContentView(R.layout.em_activity_user_profile);
        ButterKnife.bind(this);
        super.onCreate(arg0);
        initListener();
        user = SuperWeChatHelper.getInstance().getUserProfileManager().getCurrentAppUserInfo();
        model = new UserModel();
    }

    private void initListener() {
        String username = EMClient.getInstance().getCurrentUser();
        if (username!=null){
            mTvUserinfoName.setText("微信号: "+username);
            EaseUserUtils.setAppUserNick(username, mTvUserinfoNick);
            EaseUserUtils.setAppUserAvatar(this, username, mIvUserinfoAvatar);
//            asyncFetchUserInfo(user.getMUserName());
        }
        titleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MFGT.finish(UserProfileActivity.this);
            }
        });
        mReceiver = new UpdateAvatarBroadcastReceiver();
        IntentFilter filter = new IntentFilter(I.BROADCAST_UPDATE_AVATAR);
        registerReceiver(mReceiver,filter);
    }

    public void asyncFetchUserInfo(String username) {
        SuperWeChatHelper.getInstance().getUserProfileManager().asyncGetUserInfo(username, new EMValueCallBack<EaseUser>() {

            @Override
            public void onSuccess(EaseUser user) {
                if (user != null) {
                    SuperWeChatHelper.getInstance().saveContact(user);
                    if (isFinishing()) {
                        return;
                    }
                    mTvUserinfoName.setText(user.getNick());
                    if (!TextUtils.isEmpty(user.getAvatar())) {
                        Glide.with(UserProfileActivity.this).load(user.getAvatar()).placeholder(R.drawable.em_default_avatar).into(mIvUserinfoAvatar);
                    } else {
                        Glide.with(UserProfileActivity.this).load(R.drawable.em_default_avatar).into(mIvUserinfoAvatar);
                    }
                }
            }

            @Override
            public void onError(int error, String errorMsg) {
            }
        });
    }


    private void uploadHeadPhoto() {
        Builder builder = new Builder(this);
        builder.setTitle(R.string.dl_title_upload_photo);
        builder.setItems(new String[]{getString(R.string.dl_msg_take_photo), getString(R.string.dl_msg_local_upload)},
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which) {
                            case 0:
                                Toast.makeText(UserProfileActivity.this, getString(R.string.toast_no_support),
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(pickIntent, REQUESTCODE_PICK);
                                break;
                            default:
                                break;
                        }
                    }
                });
        builder.create().show();
    }


    private void updateRemoteNick(final String nickName) {
        dialog = ProgressDialog.show(this, getString(R.string.dl_update_nick), getString(R.string.dl_waiting));

        model.updateUserNick(UserProfileActivity.this, user.getMUserName(), nickName,
                new OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        if (s!=null){
                            Result<User> result = ResultUtils.getResultFromJson(s, User.class);
                            if (result!=null){
                                if (result.getRetCode() == I.MSG_USER_SAME_NICK){
                                    CommonUtils.showLongToast(R.string.toast_nick_not_modfy);
                                }else if (result.getRetCode() == I.MSG_USER_UPDATE_NICK_FAIL){
                                    CommonUtils.showLongToast(R.string.toast_updatenick_fail);
                                }else if (result.isRetMsg()){
                                    CommonUtils.showLongToast(R.string.toast_updatenick_success);
                                    mTvUserinfoNick.setText(nickName);
                                    SuperWeChatHelper.getInstance().getUserProfileManager().updateCurrentAppUserNickName(nickName);
                                }
                            }
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(UserProfileActivity.this, getString(R.string.toast_updatenick_fail), Toast.LENGTH_SHORT)
                                .show();
                        dialog.dismiss();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUESTCODE_PICK:
                if (data == null || data.getData() == null) {
                    return;
                }
                startPhotoZoom(data.getData());
                break;
            case REQUESTCODE_CUTTING:
                if (data != null) {
                    setPicToView(data);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    /**
     * save the picture data
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            L.i("main","setPicToView="+photo);
//            Drawable drawable = new BitmapDrawable(getResources(), photo);
//            mIvUserinfoAvatar.setImageDrawable(drawable);
//            uploadUserAvatar(Bitmap2Bytes(photo));
            L.i("main","setPicToView="+saveBitmapFile(photo));
            SuperWeChatHelper.getInstance().getUserProfileManager()
                    .uploadAppUserAvatar(saveBitmapFile(photo));
        }

    }

    private void uploadUserAvatar(final byte[] data) {
        dialog = ProgressDialog.show(this, getString(R.string.dl_update_photo), getString(R.string.dl_waiting));
        new Thread(new Runnable() {

            @Override
            public void run() {
                final String avatarUrl = SuperWeChatHelper.getInstance().getUserProfileManager().uploadUserAvatar(data);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        if (avatarUrl != null) {
                            Toast.makeText(UserProfileActivity.this, getString(R.string.toast_updatephoto_success),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserProfileActivity.this, getString(R.string.toast_updatephoto_fail),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        }).start();

        dialog.show();
    }


    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    @OnClick({R.id.layout_userinfo_avatar, R.id.layout_userinfo_nick})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_userinfo_avatar:
                uploadHeadPhoto();
                break;
            case R.id.layout_userinfo_nick:
                final EditText editText = new EditText(this);
                editText.setText(user.getMUserNick());
                editText.selectAll();
                new Builder(this).setTitle(R.string.setting_nickname).setIcon(android.R.drawable.ic_dialog_info).setView(editText)
                        .setPositiveButton(R.string.dl_ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String nickString = editText.getText().toString();
                                if (TextUtils.isEmpty(nickString)) {
                                    Toast.makeText(UserProfileActivity.this, getString(R.string.toast_nick_not_isnull), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (nickString.equals(user.getMUserNick())){
                                    CommonUtils.showLongToast(R.string.toast_nick_not_modfy);
                                    return;
                                }
                                updateRemoteNick(nickString);
                            }
                        }).setNegativeButton(R.string.dl_cancel, null).show();
                break;
        }
    }

    private String getAvatarName() {
        avatarName = user.getMUserName()+ System.currentTimeMillis();
        return avatarName;
    }

    /**
     * 返回头像保存在sd卡的位置:
     * Android/data/cn.ucai.superwechat/files/pictures/user_avatar
     * @param context
     * @param path
     * @return
     */
    public static String getAvatarPath(Context context, String path){
        File dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File folder = new File(dir,path);
        if(!folder.exists()){
            folder.mkdir();
        }
        return folder.getAbsolutePath();
    }

    private File saveBitmapFile(Bitmap bitmap) {
        if (bitmap != null) {
            String imagePath = getAvatarPath(UserProfileActivity.this,I.AVATAR_TYPE)+"/"+getAvatarName()+".jpg";
            File file = new File(imagePath);//将要保存图片的路径
            L.e("file path="+file.getAbsolutePath());
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file;
        }
        return null;
    }

    class UpdateAvatarBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            uploadAvatar(intent.getBooleanExtra(I.RESULT_UPDATE_AVATAR,false));
        }
    }

    private void uploadAvatar(boolean isSuccess) {
        if (isSuccess){
            Toast.makeText(UserProfileActivity.this, getString(R.string.toast_updatephoto_success),
                    Toast.LENGTH_SHORT).show();
            EaseUserUtils.setAppUserAvatar(UserProfileActivity.this,user.getMUserName(),mIvUserinfoAvatar);
        }else{
            Toast.makeText(UserProfileActivity.this, getString(R.string.toast_updatephoto_fail),
                    Toast.LENGTH_SHORT).show();
        }
        dismiss();
    }

    private void dismiss(){
        if (dialog!=null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver!=null){
            unregisterReceiver(mReceiver);
        }
    }
}
