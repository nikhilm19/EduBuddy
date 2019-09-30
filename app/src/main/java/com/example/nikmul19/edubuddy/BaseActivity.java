package com.example.nikmul19.edubuddy;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class BaseActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            Intent intent;
            Bundle bundle;
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_base);
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser != null) {
                bundle = new Bundle();
                bundle.putString("user_id", firebaseUser.getEmail());
                intent = new Intent(this, DrawerActivity.class);
                intent.putExtras(bundle);
            } else {
                intent = new Intent(this, LoginSignUpContainer.class);

            }
            startActivity(intent);
            this.finish();

        }
    }

