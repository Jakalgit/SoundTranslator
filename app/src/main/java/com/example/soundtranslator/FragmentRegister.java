package com.example.soundtranslator;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class FragmentRegister extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String email, username, passwordd;

    private Button regButt;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;

    private EditText Email, userName, password;

    private MotionLayout register;

    private TextView emailErr, nameErr, passErr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_register, container, false);

        regButt = rootView.findViewById(R.id.regButt);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Users");

        Email = rootView.findViewById(R.id.email);
        userName = rootView.findViewById(R.id.username);
        password = rootView.findViewById(R.id.password1);

        emailErr = rootView.findViewById(R.id.emailErr);
        nameErr = rootView.findViewById(R.id.nameErr);
        passErr = rootView.findViewById(R.id.passErr);

        register = rootView.findViewById(R.id.register);

        regButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ;
                boolean flag = false;
                email = Email.getText().toString();
                if (email.isEmpty() /*здесь будет код который проверяет есть ли на сервере такая почта(например с помощью метод)*/) {
                    emailErr.setText("Адресс не может быть пустым");
                    emailErr.setVisibility(View.VISIBLE);
                    flag = false;
                } else {
                    emailErr.setVisibility(View.INVISIBLE);
                    flag = true;
                }

                username = userName.getText().toString();
                if (username.isEmpty() /*здесь будет код котрый проверяет есть ли на сервере такое имя на сервере(например с помощью метода)*/) {
                    nameErr.setText("Имя не может быть путстым");
                    nameErr.setVisibility(View.VISIBLE);
                    flag = false;
                } else {
                    nameErr.setVisibility(View.INVISIBLE);
                    flag = true;
                }

                passwordd = password.getText().toString();
                if (passwordd.length() < 6) {
                    passErr.setVisibility(View.VISIBLE);
                    flag = false;
                } else {
                    passErr.setVisibility(View.INVISIBLE);
                    flag = true;
                }

                //регистрация пользователя
                if (flag) {
                    createAccount(email, passwordd);
                }
            }
        });

        register.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int i) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(FragmentRegister.this);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                ft.commit();
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {

            }
        });

        return rootView;
    }

    private void createAccount(final String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    writeNewUser(userId, username, email, passwordd);
                    register.transitionToEnd();
                } else {

                }
            }
        });
    }

    private void writeNewUser(String userId, String username, String email, String password){
        User user = new User(email, username, password);
        mDatabase.child("Users").child(userId).setValue(user);
    }
}