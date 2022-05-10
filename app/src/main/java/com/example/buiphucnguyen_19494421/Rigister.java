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

public class Rigister extends AppCompatActivity {
    EditText txtEmailSU,txtPass1SU;
    TextView lblSI;
    Button btnRegisterSU;
    ImageButton imgBackSU;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rigister);


        txtEmailSU = findViewById(R.id.txtEmailSU);
        txtPass1SU = findViewById(R.id.txtPass1SU);

        btnRegisterSU = findViewById(R.id.btnSignInSI);
        mAuth = FirebaseAuth.getInstance();
        imgBackSU = findViewById(R.id.imgBackSU);
        imgBackSU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Rigister.this, MainActivity.class));
            }
        });
        btnRegisterSU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtEmailSU.getText().length()==0){
                    txtEmailSU.setError("Email không được để trống!");
                }else if(txtPass1SU.getText().length()==0){
                    txtPass1SU.setError("Mật khẩu không được để trống!");
                } else{
                    Register();
                }

            }
        });
        lblSI = findViewById(R.id.lblSI);
        lblSI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Rigister.this, SignIn.class));
            }
        });
    }
    private void Register(){

        String password = txtPass1SU.getText().toString();
        final String email = txtEmailSU.getText().toString();

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Rigister.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));

                            
                        }else{
                            Toast.makeText(Rigister.this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}