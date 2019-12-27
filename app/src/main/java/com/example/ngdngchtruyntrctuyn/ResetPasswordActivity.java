package com.example.ngdngchtruyntrctuyn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

public class ResetPasswordActivity extends AppCompatActivity {

    MaterialEditText send_email;
    Button btn_reset;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Đặt lại mật khẩu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        send_email = findViewById(R.id.send_email);
        btn_reset = findViewById(R.id.btn_reset);

        firebaseAuth = FirebaseAuth.getInstance();

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = send_email.getText().toString();

                // validate

                if(!txt_email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")){
                    send_email.requestFocus();
                    send_email.setError("Địa chỉ email không hợp lệ");
                    send_email.setErrorColor(Color.YELLOW);
                }
                if(txt_email.length() == 0) {
                    send_email.requestFocus();
                    send_email.setError("Địa chỉ email không được để trống");
                    send_email.setErrorColor(Color.YELLOW);
                }
//                if(txt_email.length() == 0){
//                    Toast.makeText(ResetPasswordActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
//                }
                else {
                    firebaseAuth.sendPasswordResetEmail(txt_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ResetPasswordActivity.this, "Vui lòng kiểm tra lại hòm thư!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(ResetPasswordActivity.this, "Thao tác thất bại! Vui Lòng Kiểm Tra Lại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
