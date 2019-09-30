package com.example.nikmul19.edubuddy;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class DrawerActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FragmentManager fragmentManager;
    private TextView headerTitle;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "app_channel";
            String description = "This app's notification channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            String CHANNEL_ID = "1";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);


            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
        createNotificationChannel();
        
        setContentView(R.layout.activity_drawer);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);


        fragmentManager = getSupportFragmentManager();
        Toolbar toolbar=findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_menu_white_18dp);
        if (headerTitle==null){
            Log.i("test","null");
        }

        View header=navigationView.getHeaderView(0);
        headerTitle=(TextView) (header.findViewById(R.id.nav_header_title));
        headerTitle.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        navigationView.setItemIconTintList(null);

        fragmentManager.beginTransaction().replace(R.id.drawer_fragments_container, new HomeFragment(), null).commit();





        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                System.out.println("id"+id);
                switch (id)
                {
                    case R.id.book_sell:

                        SellBookFragment fragment = new SellBookFragment();
                        fragmentManager.beginTransaction().replace(R.id.drawer_fragments_container, fragment, null).commit();
                        drawerLayout.closeDrawers();
                        break;


                    case R.id.book_buy:
                        RecyclerBuyBookFragment fragment1 = new RecyclerBuyBookFragment();
                        fragmentManager.beginTransaction().replace(R.id.drawer_fragments_container, fragment1, null).commit();
                        drawerLayout.closeDrawers();
                        break;


                    case R.id.logout_menu:
                        FirebaseAuth.getInstance().signOut();
                        drawerLayout.closeDrawers();
                        DrawerActivity.this.finish();
                        Intent intent= new Intent(DrawerActivity.this,LoginSignUpContainer.class);
                        startActivity(intent);

                        break;


                }


                return true;
            }
        });

    }

    //FirebaseRemoteConfig



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;


        }
        return super.onOptionsItemSelected(item);

    }

    public void checkPermissions() {
        int reqCode = 200;
        String[] permissions = {Manifest.permission.CALL_PHONE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, permissions, reqCode);
                break;

            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 200:
                if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast toast = Toast.makeText(this, "Can't call seller as permission not granted. Please give permission", Toast.LENGTH_SHORT);
                    toast.show();
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 200);
                }
        }
    }
}

