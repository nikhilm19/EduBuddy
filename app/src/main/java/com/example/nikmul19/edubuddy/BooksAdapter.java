package com.example.nikmul19.edubuddy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.FragmentManager;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.util.ArrayList;
import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.MyViewHolder> {
    private List<Book> bookList, filteredBookList;
    Context context;
    DatabaseReference db;

    public BooksAdapter(List<Book> list, Context context) {
        this.bookList = list;
        this.context = context;
        this.filteredBookList = list;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder  implements  View.OnClickListener{

        public TextView title, price, seller, email;
        public ImageView bookImage, popupDots;

        public MyViewHolder(View view) {

            super(view);
            context = view.getContext();
            title = view.findViewById(R.id.buy_book_title);
            price = view.findViewById(R.id.buy_book_price);
            seller = view.findViewById(R.id.buy_book_seller);
            bookImage = view.findViewById(R.id.buy_book_image);
            email = view.findViewById(R.id.buy_book_email);
            email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("text/plain");
                    context.startActivity(Intent.createChooser(emailIntent, "Send Email"));
                }
            });
            popupDots = view.findViewById(R.id.pop_up_dots);


        }
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(context,BookDetailsPage.class);
            context.startActivity(intent);
            Toast.makeText(context,"Hello",Toast.LENGTH_LONG).show();

           }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_book, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Book book = filteredBookList.get(position);
        holder.title.setText(book.getTitle());
        holder.price.setText("Rs:" + book.getPrice());
        holder.popupDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpMenu(holder.popupDots, book, BooksAdapter.this.context);
            }
        });


        db = FirebaseDatabase.getInstance().getReference().child("users/Students").child(book.getUploadedBy());

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                holder.seller.setText("Seller: " + dataSnapshot.child("name").getValue(String.class));
                holder.email.setText(dataSnapshot.child("email_id").getValue(String.class));


                Log.i("Holder", book.getUploadedBy());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final int SIZE = 20 * 1024 * 1024;

        final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("photos/" + book.getPhotoLocation());


        Glide.with(context).load(storageReference).into(holder.bookImage);




        Log.d("imageUrl", "photos/" + book.getPhotoLocation());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(view.getContext(), BookDetailsPage.class);

                final Bundle bundle =  new Bundle();
                bundle.putString("title",book.getTitle());
                bundle.putString("price",book.getPrice());
                bundle.putString("seller",holder.seller.getText().toString());
                bundle.putString("email",holder.email.getText().toString());
                //String url = storageReference.getDownloadUrl().toString();
                bundle.putString("image-url",storageReference.toString());
                bundle.putString("photo-location",book.getPhotoLocation());

                bundle.putString("uploaded-by",book.getUploadedBy());
                intent.putExtras(bundle);
                Log.i("photo-location",book.getPhotoLocation());




                context.startActivity(intent);

               /* storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                {
                    @Override
                    public void onSuccess(Uri downloadUrl)
                    {

                        Log.i("image-url",downloadUrl.toString());
                        bundle.putString("image-url",downloadUrl.toString());


                        // bundle.putString("image-url",url);
                        intent.putExtras(bundle);


                        context.startActivity(intent);
                    }
                });*/


            }
        });



    }

    public void showPopUpMenu(View view, final Book book, final Context c) {
        final String receiver1 = new String();
        db = FirebaseDatabase.getInstance().getReference().child("users/Students").child(book.getUploadedBy()).child("Fcm_Token");
        FragmentManager fragmentManager;

        PopupMenu popupMenu = new PopupMenu(context, view);
        MenuInflater inflater = new MenuInflater(context);
        inflater.inflate(R.menu.card_popup_menu, popupMenu.getMenu());
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.notify_seller:
                        testNoti(" wants to buy your book " + book.getTitle(), book);



                       // fragmentManager.beginTransaction().replace(R.id.drawer_fragments_container, new HomeFragment(), null).commit();



                        return true;

                    case R.id.call_seller:
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users/Students").child(book.getUploadedBy()).child("phone");
                        db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                String phoneNo = dataSnapshot.getValue(String.class);
                                Log.i("test", "phone" + phoneNo);

                                Intent intent1 = context.getPackageManager().getLaunchIntentForPackage("com.google.android.apps.tachyon");
                                //Intent intent= new Intent(Intent.ACTION_CALL);
                                intent1.setData(Uri.parse("tel:" + phoneNo));
                                context.startActivity(intent1);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        return true;


                    case R.id.bidding_seller:
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(c);
                        alertDialog.setTitle("Bid");
                        alertDialog.setMessage("Enter your bid amount");

                        final EditText input = new EditText(c);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,

                                LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(lp);
                        alertDialog.setView(input);

                        alertDialog.setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        String bidding_count = input.getText().toString();
                                        testNoti(" wants to buy your book for Rs " + bidding_count, book);
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
                return false;
            }

        });
    }

    private void testNoti(final String str, Book book) {


        db = FirebaseDatabase.getInstance().getReference().child("users/Students").child(book.getUploadedBy()).child("Fcm_Token");


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

    public Filter getFilter() {

        Log.i("test1", "in filter");
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Log.i("test1", constraint.toString());
                if (constraint.toString() == "") {
                    filteredBookList = bookList;
                }


                List<Book> filteredList = new ArrayList<>();
                for (Book book : bookList) {
                    // Log.i("test1",book.getTitle());
                    if (book.getTitle().toLowerCase().contains(constraint)) {

                        filteredList.add(book);
                        Log.i("test1", book.getTitle());


                    }

                }
                filteredBookList = filteredList;

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredBookList;
                return filterResults;
            }


            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                filteredBookList = (ArrayList<Book>) results.values;
                notifyDataSetChanged();


            }

        };
    }

    @Override
    public int getItemCount() {


        Log.e("error", "item-count:" + filteredBookList.size());
        return filteredBookList.size();
    }

}
