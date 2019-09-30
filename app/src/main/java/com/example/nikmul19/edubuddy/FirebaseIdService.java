package com.example.nikmul19.edubuddy;

import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;

public class FirebaseIdService extends FirebaseMessagingService{
    
    @Override
    public void onNewToken(String token ){
        Log.d("MESSAGE", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.

        DatabaseReference db= FirebaseDatabase.getInstance().getReference().child("users/Students").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        db.child("Fcm_Token").setValue(token);

    }
}