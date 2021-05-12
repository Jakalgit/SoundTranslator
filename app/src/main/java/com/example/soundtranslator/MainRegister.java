package com.example.soundtranslator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainRegister extends AppCompatActivity {

    private MotionLayout motionLayout;

    private Button signIn, signUp;

    private EditText login, password;

    private TextView error;

    private View view;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        motionLayout = findViewById(R.id.motion1);

        mAuth = FirebaseAuth.getInstance();

        login = findViewById(R.id.email1);
        password = findViewById(R.id.password);
        error = findViewById(R.id.error);

        signUp = findViewById(R.id.signUp);
        signIn = findViewById(R.id.signIn);

        view = this.getCurrentFocus();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        String email = login.getText().toString();
                        String pass = password.getText().toString();
                        boolean flag = true;

                        if (email.isEmpty() || pass.isEmpty()){
                            error.setText("Поля не могут быть пустыми");
                            flag = false;
                        } else {
                            error.setText("");
                            flag = true;
                        }

                        //аутентификация пользователя
                        if (flag) {
                            signIn(email, pass);
                        }
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();

                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentRegister register = new FragmentRegister();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.homeLayout, register).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        });
        motionLayout.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int i) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {

                }
                FragmentHome home = new FragmentHome();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.homeLayout, home).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {

            }
        });
    }

    /*@Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            FragmentHome home = new FragmentHome();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.homeLayout, home);
            ft.commit();
        }
    }*/

    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    motionLayout.transitionToEnd();
                    error.setText("");
                } else {
                    error.setText("Ошибка входа");
                }
            }
        });
    }
}