package com.example.nikmul19.edubuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.BitmapTransformation;

public class BookDetailsPage extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    String uploadedBy ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details_page);

        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();

        TextView buyBookSeller =  findViewById(R.id.buy_book_seller);
        TextView buyBookPrice =  findViewById(R.id.buy_book_price);
        TextView buyBookTitle =  findViewById(R.id.buy_book_title);
        bottomNavigation = findViewById(R.id.bottom_navigation);



        buyBookTitle.setText(bundle.getString("title"));
        buyBookSeller.setText(bundle.getString("seller"));
        buyBookPrice.setText("Rs:"+bundle.getString("price"));

        Log.i("test-url",bundle.getString("image-url"));
        Log.i("photo-url",bundle.getString("photo-location"));

        TextView bookName = findViewById(R.id.book_name);
        bookName.setText(bundle.getString("title"));

        TextView bookEmail = findViewById(R.id.buy_book_email);
        bookEmail.setText(bundle.getString("email"));

        uploadedBy = bundle.getString("uploaded-by");









        ImageView bookImage = findViewById(R.id.bookDetailImage);
        ImageView bookPrevImage = findViewById(R.id.buy_book_image);

        final CardView bookCard = findViewById(R.id.book_card);

        final TextView bookDesc = findViewById(R.id.book_description);
        bookDesc.setMovementMethod(new ScrollingMovementMethod());

        final Button readMore= findViewById(R.id.read_more);
        final Button readLess= findViewById(R.id.read_less);
        readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookDesc.setMaxLines(10);
                readLess.setVisibility(View.VISIBLE);
                readMore.setVisibility(View.GONE);

            }
        });


        readLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookDesc.setMaxLines(3);
                readLess.setVisibility(View.GONE);
                readMore.setVisibility(View.VISIBLE);

            }
        });



        Glide.with(getApplicationContext())
                //.apply(new RequestOptions().override(100, 100))
                .load("https://d3i6fh83elv35t.cloudfront.net/static/2017/10/jimkay-1024x576.jpg")
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(10, 10)))

                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            bookCard.setBackground(resource);
                        }
                    }
                });

       // StorageReference storageReference = new StorageReference(bundle.getString("imag-url"));

        final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("photos/" +bundle.getString("photo-location"));



        Glide.with(getApplicationContext())
                //.apply(new RequestOptions().override(100, 100))
                .load(storageReference)
                .into(bookPrevImage);




        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.notify_seller:
                        testNoti(" wants to buy your book " + bundle.getString("title"), uploadedBy);



                        // fragmentManager.beginTransaction().replace(R.id.drawer_fragments_container, new HomeFragment(), null).commit();



                        return true;

                    case R.id.call_seller:
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users/Students").child(uploadedBy).child("phone");
                        db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                String phoneNo = dataSnapshot.getValue(String.class);
                                Log.i("test", "phone" + phoneNo);


                                //Intent intent1 = getApplicationContext().getPackageManager().getLaunchIntentForPackage("com.google.android.apps.tachyon");

                                Intent intent= new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + phoneNo));
                                getApplicationContext().startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        return true;


                    case R.id.bidding_seller:
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());
                        alertDialog.setTitle("Bid");
                        alertDialog.setMessage("Enter your bid amount");

                        final EditText input = new EditText(getApplicationContext());
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,

                                LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(lp);
                        alertDialog.setView(input);

                        alertDialog.setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        String bidding_count = input.getText().toString();
                                        testNoti(" wants to buy your book for Rs " + bidding_count,uploadedBy );
                                        //Toast.makeText(c, "Make bid of " + bidding_count, Toast.LENGTH_SHORT).show();

                                    }
                                });

                        alertDialog.setNegativeButton("NO",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                        alertDialog.show();
                        return true;

                }

                return true;
            }
        });




    }
    private void testNoti(final String str, String uploadedBy) {

       final DatabaseReference db;



        db = FirebaseDatabase.getInstance().getReference().child("users/Students").child(uploadedBy).child("Fcm_Token");


        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {

                if (task.isSuccessful()) {
                    String token = task.getResult().getToken();
                    Log.i("test", token);

                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String receiver = dataSnapshot.getValue(String.class);

                            ServerTest test = new ServerTest();

                            try {


                                new ServerTest().execute(receiver, "Buyer Alert", FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + str);
                            } catch (Exception e) {

                                Log.i("exception", e.toString());

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }


                    });


                }

            }
        });
    }
}
