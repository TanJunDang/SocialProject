package io.tanjungang.github.social.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.umeng.analytics.MobclickAgent;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import io.tanjundang.github.projectutils.base.Constant;
import io.tanjundang.github.projectutils.utils.Functions;
import io.tanjungang.github.social.MainActivity;
import io.tanjungang.github.social.R;
import io.tanjungang.github.social.base.BaseActivity;


/**
 * Developer: TanJunDang
 * Date: 2016/1/23
 * Email: TanJunDang324@126.com
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private Button btnRegister;
    private EditText etAccount;
    private EditText etPassword;
    private EditText etPasswordConfirm;
    public static final int REQ = 100;

    public static void SkipToRegisterActivity(Context context, int req) {
        Intent intent = new Intent();
        intent.setClass(context, RegisterActivity.class);
        intent.putExtra(Constant.REQ_CODE, req);
        ((Activity) context).startActivityForResult(intent, req);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        etAccount = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPasswordConfirm = (EditText) findViewById(R.id.etPasswordConfirm);
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
        if (v.equals(btnRegister)) {
            final String account = etAccount.getText().toString().trim();
            final String password = etPassword.getText().toString().trim();
            String passwordAgain = etPasswordConfirm.getText().toString().trim();

            if (TextUtils.isEmpty(account)) {
                Functions.toast("用户名不能为空");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Functions.toast("密码不能为空");
                return;
            }

            if (!password.equals(passwordAgain)) {
                Functions.toast("两次输入密码不一致");
                return;
            }

            User user = new User();
            user.setUsername(account);
            user.setPassword(password);
            user.signUp(new SaveListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    if (e == null) {
                        Functions.toast("注册成功");
                        Intent intent = new Intent();
                        intent.putExtra(Constant.ACCOUNT, account);
                        intent.putExtra(Constant.PASSWORD, password);
                        setResult(REQ, intent);
                        finish();
                    } else {
                        Functions.toast(e.getMessage());
                    }
                }
            });

        }

    }
}
