package com.example.ngdngchtruyntrctuyn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity {


    MaterialEditText email, password;
    Button btn_login;

    FirebaseAuth mAuth;
    TextView forgot_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Đăng Nhập");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        forgot_password = findViewById(R.id.forgot_password);

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ResetPasswordActivity.class));
            }
        });


        // validate login

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if(txt_email.length() == 0){
                    email.requestFocus();
                    email.setError("Email không được để trống");
                    email.setErrorColor(Color.YELLOW);
                }
                if(!txt_email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")){
                    email.requestFocus();
                    email.setError("Địa chỉ email không hợp lệ");
                    email.setErrorColor(Color.YELLOW);
                }
                if (txt_password.length() == 0) {
                    password.requestFocus();
                    password.setError("password không được để trống");
                    password.setErrorColor(Color.YELLOW);
                }
                if (txt_password.length() < 6 ) {
                    password.requestFocus();
                    password.setError("Mật khẩu có độ dài ít nhất 6 ký tự");
                    password.setErrorColor(Color.YELLOW);
                }
                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(LoginActivity.this, "Hãy Nhập Đầy Đủ Thông Tin!", Toast.LENGTH_SHORT).show();
                }  else {
                    mAuth.signInWithEmailAndPassword(txt_email, txt_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        Toast.makeText(LoginActivity.this, "Đăng Nhập Thành Công!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Đăng Nhập Thất Bại!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}
