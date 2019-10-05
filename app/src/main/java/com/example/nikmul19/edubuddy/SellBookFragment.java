package com.example.nikmul19.edubuddy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static androidx.appcompat.app.AppCompatActivity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class SellBookFragment extends Fragment implements View.OnClickListener {

    final static String GALLERY_DIRECTORY_NAME = "EDUBUDDY CAMERA";
    public static final int MEDIA_TYPE_IMAGE = 1;
    Spinner spinner;
    ImageView bookImage;
    Button sellBook, captureBookImage;
    String fileStoragePath;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private static String imageStoragePath;
    public static final int BITMAP_SAMPLE_SIZE = 1;
    public DatabaseReference db, usersDb;
    public FirebaseAuth cAuth;
    EditText price, title;
    Uri fileUri;
    ProgressBar uploadProgress;


    public static final String KEY_IMAGE_STORAGE_PATH = "image_path";
    View view;


    public SellBookFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sell_book, container, false);
        findViews();
        return view;

    }

    public void findViews() {

        cAuth = FirebaseAuth.getInstance();
        spinner = view.findViewById(R.id.book_subject);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.subjects, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        captureBookImage = view.findViewById(R.id.book_picture_btn);
        sellBook = view.findViewById(R.id.submit_book_btn);
        captureBookImage.setOnClickListener(this);
        sellBook.setOnClickListener(this);
        bookImage = view.findViewById(R.id.book_image);
        db = FirebaseDatabase.getInstance().getReference();
        price = view.findViewById(R.id.book_price);
        title = view.findViewById(R.id.book_title);
        uploadProgress = view.findViewById(R.id.upload_progress);
        usersDb = FirebaseDatabase.getInstance().getReference().child("users/");


    }

    public void showProgress() {

        uploadProgress.setVisibility(View.VISIBLE);
        uploadProgress.setIndeterminate(true);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        // view.setBackgroundColor(Color.RED);
        //view.setBackground(new ColorDrawable(Color.GRAY));


    }

    public void hideProgress() {
        uploadProgress.setVisibility(View.INVISIBLE);
        uploadProgress.setIndeterminate(false);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (checkPermissions()) {

            showProgress();
            switch (id) {

                case R.id.book_picture_btn:
                    System.out.print("cliclked book");
                    dispatchTakePictureIntent();
                    hideProgress();

                    break;

                case R.id.submit_book_btn:
                    uploadBook();
                    break;


            }
        }
    }

    public boolean checkPermissions() {
        if ((ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            Toast toast = Toast.makeText(getActivity(), "Please provide call,camera and storage permissions in settings of this app", Toast.LENGTH_LONG);
            toast.show();
            return false;
        }
        return true;
    }


    public void uploadBook() {
        Book book = new Book();
        book.price = this.price.getText().toString();
        String subject = this.spinner.getSelectedItem().toString();
        String title = this.title.getText().toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm a");
        simpleTimeFormat.setTimeZone(TimeZone.getTimeZone("GMT + 5:30"));
        Date curr = new Date();
        String currDate = simpleDateFormat.format(curr);
        book.date = currDate;
        String currTime = simpleTimeFormat.format(Calendar.getInstance().getTime());
        book.time = currTime;
        book.uploadedBy = FirebaseAuth.getInstance().getCurrentUser().getUid();
        book.subject = subject;
        book.title = title;

        DatabaseReference bookDbLocation = db.child("books/");

        String key = bookDbLocation.push().getKey();


        bookDbLocation.child(key).setValue(book);

        db.child("subjects/" + subject + "/" + title + "/" + key).setValue(true);

        Log.i("test", "got key = " + key);
        usersDb.child("Students").child(cAuth.getCurrentUser().getUid()).child("books-uploaded").child(key).setValue(true);

        System.out.print("clciked me");

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("photos/" + key);
        storageReference.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i("upload", "yes");

            }

        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                Log.i("upload", "completed");
                Snackbar snackbar = Snackbar.make(view, "Uploaded to sell Complete", Snackbar.LENGTH_SHORT);
                snackbar.show();
                hideProgress();
                sellBook.setVisibility(View.VISIBLE);
            }
        });


    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (file != null) {
            imageStoragePath = file.getAbsolutePath();
        }

        fileUri = CameraUtils.getOutputMediaFileUri(getActivity(), file);

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        CameraUtils.refreshGallery(getActivity(), imageStoragePath);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            previewCapturedImage();
            System.out.println("onactivityresult");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("test", "saved");
        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putString(KEY_IMAGE_STORAGE_PATH, imageStoragePath);

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.i("test", "saved1");
        if (savedInstanceState != null) {
            imageStoragePath = savedInstanceState.getString(KEY_IMAGE_STORAGE_PATH);


        }
    }

    public void previewCapturedImage() {

        Log.i("test", "saved4");
        try {
            CameraUtils.refreshGallery(getActivity(), imageStoragePath);
            System.out.print(imageStoragePath);
            Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);
            bookImage.setImageBitmap(bitmap);

            sellBook.setVisibility(View.VISIBLE);
            captureBookImage.setVisibility(View.GONE);
        } catch (NullPointerException e) {
            Log.i("test", "nullpi");
            e.printStackTrace();
        }
    }


}

class Book {

    String price, date, time, uploadedBy, subject, title, photoLocation;
    String phoneNo;

    public String getTitle() {
        return title;
    }


    public String getTime() {
        return time;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public String getPrice() {
        return price;
    }

    public String getPhotoLocation() {
        return photoLocation;
    }
}
