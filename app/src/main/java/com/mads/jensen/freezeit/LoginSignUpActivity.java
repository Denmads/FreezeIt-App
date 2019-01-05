package com.mads.jensen.freezeit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class LoginSignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmail, edtPassword;
    private Button btnLoginSignup;

    private enum State {
        LOGIN, SIGNUP
    }
    private State state;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            Intent i = new Intent(this, OverviewActivity.class);
            startActivity(i);
            finish();
        }

        setTitle(R.string.btn_login_text);
        state = State.LOGIN;

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLoginSignup = findViewById(R.id.btnLoginSignup);

        btnLoginSignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (email.length() > 0 && password.length() > 0) {
            switch (state) {
                case LOGIN:
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginSignUpActivity.this, "Signed in successfully!", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(LoginSignUpActivity.this, OverviewActivity.class);
                                startActivity(i);
                                finish();
                            }
                            else {
                                Exception ex = task.getException();
                                if (ex instanceof FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(LoginSignUpActivity.this, "Wrong password/Email combo!", Toast.LENGTH_SHORT).show();
                                }
                                else if (ex instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(LoginSignUpActivity.this, "Wrong password/Email combo!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                    break;

                case SIGNUP:
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginSignUpActivity.this, "Signed up successfully!", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(LoginSignUpActivity.this, OverviewActivity.class);
                                startActivity(i);
                                finish();
                            }
                            else {
                                Exception ex = task.getException();
                                if (ex instanceof FirebaseAuthWeakPasswordException) {
                                    FirebaseAuthWeakPasswordException e = (FirebaseAuthWeakPasswordException)ex;
                                    Toast.makeText(LoginSignUpActivity.this, e.getReason(), Toast.LENGTH_SHORT).show();
                                }
                                else if (ex instanceof FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(LoginSignUpActivity.this, "Invalid email!", Toast.LENGTH_SHORT).show();
                            }
                                else if (ex instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(LoginSignUpActivity.this, "Email already in use!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                    break;
            }
        }
        else {
            Toast.makeText(this, "Empty fields!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (state == State.LOGIN) {
            //Switch to sign up
            state = State.SIGNUP;
            item.setTitle(R.string.btn_login_text);
            btnLoginSignup.setText(R.string.btn_signup_text);
        }
        else {
            //switch to login
            state = State.LOGIN;
            item.setTitle(R.string.btn_signup_text);
            btnLoginSignup.setText(R.string.btn_login_text);
        }

        return super.onOptionsItemSelected(item);
    }
}
