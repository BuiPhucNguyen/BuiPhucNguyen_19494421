package com.example.buiphucnguyen_19494421;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {
    EditText txtEmailSI,txtPassSI;
    TextView lblSU;
    Button btnSignInSI;
    ImageButton imgBackSI;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        txtEmailSI = findViewById(R.id.txtEmailSI);
        txtPassSI = findViewById(R.id.txtPassSI);
        btnSignInSI = findViewById(R.id.btnSignInSI);
        imgBackSI = findViewById(R.id.imgBackSI);
        mAuth = FirebaseAuth.getInstance();


        imgBackSI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this,MainActivity.class));
            }
        });
        btnSignInSI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtEmailSI.getText().length()==0){
                    txtEmailSI.setError("Email không được để trồng!");
                }else if(txtPassSI.getText().length()==0){
                    txtPassSI.setError("Mật khẩu không được để trống!");
                }else{
                    LogIn();
                }
            }
        });
        lblSU = findViewById(R.id.lblSU);
        lblSU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this,Rigister.class));
            }
        });
    }

    private void LogIn() {
        String password = txtPassSI.getText().toString();
        final String email = txtEmailSI.getText().toString();

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(SignIn.this,HomeActivity.class));
                            Toast.makeText(SignIn.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(SignIn.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}