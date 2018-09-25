package com.example.nikmul19.edubuddy;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class DrawerActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FragmentManager fragmentManager;
    private TextView headerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        fragmentManager=getFragmentManager();
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
       // actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        if (headerTitle==null){
            Log.i("test","null");
        }

        View header=navigationView.getHeaderView(0);
        headerTitle=(TextView) (header.findViewById(R.id.nav_header_title));
        headerTitle.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());





        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                System.out.println("id"+id);
                switch (id)
                {
                    case R.id.book_sell:

                       // CheckAttendanceFragment fragment= new CheckAttendanceFragment();
                       // fragmentManager.beginTransaction().add(R.id.fragment_container,fragment).commit();
                        drawerLayout.closeDrawers();
                        break;


                    case R.id.book_buy:
                        drawerLayout.closeDrawers();
                        break;



                    case R.id.events:
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.logout_menu:
                        FirebaseAuth.getInstance().signOut();
                        drawerLayout.closeDrawers();
                        DrawerActivity.this.finish();
                        Intent intent= new Intent(DrawerActivity.this,LoginSignUpContainer.class);
                        startActivity(intent);

                        break;

                    case R.id.feedback_provide:
                        drawerLayout.closeDrawers();
                        break;

                }


                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;


        }
        return super.onOptionsItemSelected(item);

    }

}

