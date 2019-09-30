package com.example.nikmul19.edubuddy;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener {


    public SignUpFragment() {
        // Required empty public constructor
    }

    private FirebaseAuth cAuth;
    private FirebaseUser cUser;
    private DatabaseReference db;
    public EditText email,password,enroll,name,phoneNo;

    private TextView status, isAdmin, errorText;
    private ProgressBar signProgress;
    private Button createButton,signInButton;
    private String uId,sYear;
    private View view;
    private RadioGroup year;
    private CheckBox shift;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_student_sign_up, container, false);
        findViews();
        /*if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Toast toast = Toast.makeText(this, "Permission not granted", Toast.LENGTH_LONG);
            toast.show();
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 200);

        }*/
        return  view;
    }

    public void showProgress() {

        Log.i("test1", "shown");
        signProgress.setVisibility(View.VISIBLE);
        signProgress.setIndeterminate(true);

    }

    public void hideProgress() {
        Log.i("test1", "hidden");
        signProgress.setVisibility(View.INVISIBLE);
        signProgress.setIndeterminate(false);
    }

    public void findViews(){

        db= FirebaseDatabase.getInstance().getReference();
        isAdmin=view.findViewById(R.id.if_admin_account);
        isAdmin.setOnClickListener(this);
        enroll=(EditText) view.findViewById(R.id.enrollment_no_1);
        status=view.findViewById(R.id.sign_in_status);
        cAuth=FirebaseAuth.getInstance();
        shift=view.findViewById(R.id.shift);
        email=((EditText)view.findViewById(R.id.email_id_1));
        enroll=view.findViewById(R.id.enrollment_no_1);
        name=view.findViewById(R.id.student_name);
        password=((EditText)view.findViewById(R.id.password_1));
        createButton=view.findViewById(R.id.create_account);
        signInButton=view.findViewById(R.id.Sign_in);
        signInButton.setOnClickListener(this);
        phoneNo=view.findViewById(R.id.phone_no);
        signProgress = view.findViewById(R.id.progressBar);
        errorText = view.findViewById(R.id.error_text);

        signProgress.setVisibility(View.INVISIBLE);
        createButton.setOnClickListener(this);
        year=view.findViewById(R.id.year_radio_grp);
        year.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.year1_radio_btn:
                        sYear="1";

                        break;
                    case R.id.year2_radio_btn:
                        sYear="2";
                        break;

                    case R.id.year3_radio_btn:

                        sYear="3";
                        break;

                    case R.id.year4_radio_btn:
                        sYear="4";
                        break;
                }
            }
        });



    }


    public void createAccount(final String email, String password, final String enroll){


        final String name = this.name.getText().toString();
        final String phone = this.phoneNo.getText().toString();
        errorText.setVisibility(View.INVISIBLE);
        boolean error = false;

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getActivity(), "Enter email address!", Toast.LENGTH_SHORT).show();
            error = true;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getActivity(), "Enter valid email address!", Toast.LENGTH_SHORT).show();
            error = true;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Enter password!", Toast.LENGTH_SHORT).show();
            error = true;
        }


        if (password.length() < 6) {
            Toast.makeText(getActivity(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            error = true;
        }
        if (!Patterns.PHONE.matcher(phone).matches()) {
            Toast.makeText(getActivity(), "Enter valid Phone number!", Toast.LENGTH_SHORT).show();
            error = true;
        }
        if (error) hideProgress();

        if (!error) {
            final String enrollment = this.enroll.getText().toString();
            final String shift = (this.shift.isChecked()) ? "Evening" : "Morning";
            final StudentSignUpData student = new StudentSignUpData(email, enrollment, sYear, name, shift, phone);


            cAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    View view1 = view.findViewById(R.id.Constraint_layout);
                    if (task.isSuccessful()) {
                        System.out.print("created");
                        Snackbar snackbar = Snackbar.make(view, "Successful Sign in", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        cUser = cAuth.getCurrentUser();
                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                        cUser.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.i("test", "Set display name successfully");
                                }
                            }
                        });
                        createButton.setVisibility(View.GONE);
                        signInButton.setVisibility(View.VISIBLE);
                        uId = cUser.getUid();


                        writeNewUser(uId, student);
                        verifyEmail();
                    } else {

                        try {
                            errorText.setVisibility(View.VISIBLE);
                            throw task.getException();

                        } catch (FirebaseAuthWeakPasswordException e) {

                            errorText.setText("Weak Password");
                            Log.e("errror", e.getMessage());

                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            errorText.setText("Invalid Credentials");
                            Log.e("errror", e.getMessage());

                        } catch (FirebaseAuthUserCollisionException e) {
                            errorText.setText("User Already Exists");
                            Log.e("errror", e.getMessage());
                        } catch (Exception e) {
                            Log.e("errror", e.getMessage());
                        }
                        Log.e("eror", "onComplete: Failed=" + task.getException().getMessage());
                        System.out.print("UNsuccesful");
                    }


                }

            });
        }


        hideProgress();

    }
    public void writeNewUser(String UserId, StudentSignUpData student){

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()) {
                    String token = task.getResult().getToken();
                    db.child("/users/Students/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/Fcm_Token").setValue(token);
                }

            }
        });

        System.out.print("created new user");
        db.child("/users/Students/" + UserId).setValue(student);
    }
    public void signIn(final String email, final String password){

        cUser = cAuth.getCurrentUser();

        if (cUser != null && !cUser.isEmailVerified()) {
            Toast toast = Toast.makeText(getActivity(), "Please verify email " + cUser.getEmail()
                    , Toast.LENGTH_LONG);
            toast.show();
            cUser.reload();
        } else {
            cAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                View view1 = view.findViewById(R.id.Constraint_layout);

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        signInButton.setEnabled(false);

                        //status.setText("Signed in");
                        Log.i("test", "signed in");

                        MainIntent(email);

                    } else {
                        //status.setText("Signed out");
                        Log.i("test", "not signed in");
                    }
                }
            });
            //hideProgressBar();
        }

    }
    public void verifyEmail(){

        cUser.sendEmailVerification().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override

            public void onComplete(@NonNull Task<Void> task) {
                View view1=view.findViewById(R.id.Constraint_layout);
                if(task.isSuccessful()){
                    Snackbar snackbar= Snackbar.make(view1,"email sent",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else {
                    Snackbar snackbar= Snackbar.make(view1,"No mail sent",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

    }
    @Override
    public void onClick(View v)
    {


        int id=v.getId();
        final Handler handler = new Handler();
        switch (id) {
            case R.id.create_account:

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                showProgress();
                            }
                        });
                    }
                });
                showProgress();
                Log.i("test",email.getText().toString()+" "+password.getText().toString());
                createAccount(email.getText().toString(),password.getText().toString(),enroll.getText().toString());
                break;

            case R.id.Sign_in:
                signIn(email.getText().toString(),password.getText().toString());

                break;

            case R.id.if_admin_account:
                AdminSignUpFragment fragment= new AdminSignUpFragment();
                FragmentManager fragmentManager= getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.Constraint_Layout,fragment,"null").addToBackStack(null).commit();
                break;
        }
        hideProgress();

    }

    public boolean isValidAdminEmail(String email){

        String domain= email.substring(email.indexOf("@")+1);
        if(domain.compareTo("scet.ac.in")==0)return true;
        else return false;

    }

    public void MainIntent(String email){
        Bundle bundle= new Bundle();
        Intent intent= new Intent(getActivity(),DrawerActivity.class);
        bundle.putString("user_id",email);
        intent.putExtras(bundle);
        getActivity().finish();

        startActivity(intent);

    }

}


class StudentSignUpData{

    String email_id,enroll,name,phone,year,shift;

    StudentSignUpData(String email_id,String enroll,String year,String name,String shift,String phone ){
        this.shift=shift;
        this.email_id=email_id;
        this.enroll=enroll;
        this.name=name;
        this.year=year;
        this.phone=phone;

    }


}


