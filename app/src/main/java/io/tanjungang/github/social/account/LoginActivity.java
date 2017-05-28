package io.tanjungang.github.social.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.umeng.analytics.MobclickAgent;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import de.hdodenhof.circleimageview.CircleImageView;
import io.tanjundang.github.projectutils.base.Constant;
import io.tanjundang.github.projectutils.utils.Functions;
import io.tanjungang.github.social.MainActivity;
import io.tanjungang.github.social.R;
import io.tanjungang.github.social.base.BaseActivity;


/**
 * Developer: TanJunDang
 * Date: 2016/1/23
 * Email: TanJunDang@126.com
 * 登录模块
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private Button btnLogin;
    private Button btnRegister;
    private EditText etUserName;
    private EditText etPassword;
    private CircleImageView ivHead;
    private String account;
    private String password;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        ivHead = (CircleImageView) findViewById(R.id.ivHead);

        etPassword = (EditText) findViewById(R.id.etPassword);
        etUserName = (EditText) findViewById(R.id.etUserName);
    }

    @Override
    protected void intData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onClick(View v) {
        if (v.equals(btnLogin)) {
            account = etUserName.getText().toString();
            password = etPassword.getText().toString();

            User user = new User();
            user.setUsername(account);
            user.setPassword(password);
            user.login(new SaveListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    if (e == null) {
                        MobclickAgent.onProfileSignIn(account); //统计
//                        LoginModel.saveAccount(LoginActivity.this, user);//将数据保存在文件里
//                        Global.getInstance().setUser(user);//设置Global的User
//                        LoginModel.saveLastAccount(LoginActivity.this, account);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Functions.toast(e.getMessage());
                    }
                }
            });


        } else if (v.equals(btnRegister)) {
            RegisterActivity.SkipToRegisterActivity(this, RegisterActivity.REQ);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (requestCode == RegisterActivity.REQ) {
            account = data.getStringExtra(Constant.ACCOUNT);
            password = data.getStringExtra(Constant.PASSWORD);
            etUserName.setText(account);
            etPassword.setText(password);
            etPassword.requestFocus();
            etPassword.setSelection(etPassword.length());
        }
    }

}
