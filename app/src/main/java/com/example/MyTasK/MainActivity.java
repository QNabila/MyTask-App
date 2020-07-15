package com.example.MyTasK;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private TextView signup;

    private EditText email;
    private EditText pass;
    private Button btnLogin;

    //Firebase..

    private FirebaseAuth mAuth; //mAuth basically firebaseAuth e casting korechi

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            mAuth=FirebaseAuth.getInstance();

            if (mAuth.getCurrentUser()!=null){
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                finish();
            }



            mDialog=new ProgressDialog(this);

            signup=findViewById(R.id.signup_txt);

            email=findViewById(R.id.email_login);
            pass=findViewById(R.id.password_login);
            btnLogin=findViewById(R.id.login_btn);

            //LOGIN button
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String mEmail=email.getText().toString().trim();
                    String mPass=pass.getText().toString().trim();

                    if (TextUtils.isEmpty(mEmail)){
                        email.setError("Required Field..");
                        return;
                    }
                    if (TextUtils.isEmpty(mPass)){
                        pass.setError("Required Field..");
                        return;
                    }

                    mDialog.setMessage("Processing..");
                    mDialog.show();

                    //Firebase will take email and pass
                    mAuth.signInWithEmailAndPassword(mEmail,mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){

                                Toast.makeText(getApplicationContext(),"Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),HomeActivity.class)); //Login korte parle home e nie jabe
                                mDialog.dismiss();
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(),"Problem",Toast.LENGTH_SHORT).show();
                                mDialog.dismiss();
                                finish();
                            }

                        }
                    });



                }
            });



          //"Dont have account?" click korle registration page e nie jabe
            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
                    finish();
                }
            });

        }
    }