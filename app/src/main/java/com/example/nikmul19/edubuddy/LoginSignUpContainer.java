package com.example.nikmul19.edubuddy;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class LoginSignUpContainer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up_container);
        FragmentManager fragmentManager= getSupportFragmentManager();
        LoginFragment fragment= new LoginFragment();
        fragmentManager.beginTransaction().add(R.id.Constraint_Layout,fragment,null).addToBackStack(null).commit();
    }
}
