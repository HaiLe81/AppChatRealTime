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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {


    MaterialEditText username, email, password;
    Button btn_register;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Đăng Ký");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_register = findViewById(R.id.btn_register);

        auth = FirebaseAuth.getInstance();
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
//                char firstCharacterUserName = txt_username.charAt(0);

                //validate sign up
                if(txt_username.length() == 0){
                    username.requestFocus();
                    username.setError("Tên người dùng không được để trống");
                    username.setErrorColor(Color.YELLOW);

                }
                if(!txt_username.matches("[a-zA-Z0-9]+") || txt_username.charAt(0) >= 97){
                    username.requestFocus();
                    username.setError("Chữ cái đầu in hoa và không chứa ký tự đặc biệt");
                    username.setErrorColor(Color.YELLOW);

                }
                if(txt_email.length() == 0){
                    email.requestFocus();
                    email.setError("Địa chỉ email không được để trống");
                    email.setErrorColor(Color.YELLOW);

                }
                if(!txt_email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")){
                    email.requestFocus();
                    email.setError("Địa chỉ email không hợp lệ");
                    email.setErrorColor(Color.YELLOW);
                }
                if(txt_password.length() == 0){
                    password.requestFocus();
                    password.setError("Mật khẩu không được để trống");
                    password.setErrorColor(Color.YELLOW);

                }
                if (txt_password.length() < 6 ) {
                    password.requestFocus();
                    password.setError("Mật khẩu có độ dài ít nhất 6 ký tự");
                }
                if(TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(RegisterActivity.this, "Hãy nhập đầy đủ thông tin để đăng ký!", Toast.LENGTH_SHORT).show();
                }
                else {
                    register(txt_username, txt_email, txt_password);

                }
            }
        });
    }

    private void register(final String username, String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);


                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "default");
                            hashMap.put("search", username.toLowerCase());
                            hashMap.put("status", "offline");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            boolean isExited = task.getException().toString().contains("Địa chỉ email đã được sử dụng bởi 1 tài khoản khác");
                            if(isExited){
                                Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, "Bạn không thể đăng ký với email hoặc mật khẩu này", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
