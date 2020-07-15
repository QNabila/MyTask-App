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

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {
    //ekhon casting kortesi email k editText and so on
  private EditText email;
    private EditText pass;
    private Button btnReg;
    private TextView login_txt;
    private FirebaseAuth mAuth;

    private ProgressDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth=FirebaseAuth.getInstance();
        mDialog=new ProgressDialog(this);

        email=findViewById(R.id.email_reg);
        pass=findViewById(R.id.password_reg);

        btnReg=findViewById(R.id.reg_btn);

        login_txt=findViewById(R.id.login_txt);


        //FOR "Already have an account?sign in here"(first page e nie jabe)
        login_txt.setOnClickListener(new View.OnClickListener() {
            @Override


            //login click korle activity pass
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

       //FOR BUTTON
        btnReg.setOnClickListener(new View.OnClickListener() {
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

                //processing show korbe
                mDialog.setMessage("Processing..");
                mDialog.show();


                mAuth.createUserWithEmailAndPassword(mEmail,mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){

                            Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));//it will take you to home Activity
                            mDialog.dismiss();
                            finish();

                        }else {
                            Toast.makeText(getApplicationContext(),"There is a problem", Toast.LENGTH_LONG).show();
                            mDialog.dismiss();
                            finish();
                        }

                    }
                });

            }
        });
    }
}




