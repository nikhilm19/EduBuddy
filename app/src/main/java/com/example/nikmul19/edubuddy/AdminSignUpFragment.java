package com.example.nikmul19.edubuddy;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminSignUpFragment extends Fragment implements View.OnClickListener {

    public AdminSignUpFragment() {
        // Required empty public constructor
    }


    private FirebaseAuth cAuth;
    private FirebaseUser cUser;
    private DatabaseReference db;
    public EditText email, password, name, phoneNo;

    private TextView status,adminErrorText;
    private ProgressBar progressBar;
    private Button createButton, signInButton;
    private String uId;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_admin_sign_up, container, false);
        findViews();
        return view;
    }

    public void findViews() {

        db = FirebaseDatabase.getInstance().getReference();
        status = view.findViewById(R.id.sign_in_status);
        cAuth = FirebaseAuth.getInstance();
        email = ((EditText) view.findViewById(R.id.email_id_1));
        name = view.findViewById(R.id.student_name);
        password = ((EditText) view.findViewById(R.id.password_1));
        createButton = view.findViewById(R.id.create_account);
        signInButton = view.findViewById(R.id.Sign_in);
        signInButton.setOnClickListener(this);
        phoneNo = view.findViewById(R.id.phone_no);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        createButton.setOnClickListener(this);
        adminErrorText=view.findViewById(R.id.admin_errorText);
        adminErrorText.setVisibility(View.GONE);
    }


    public void createAccount(final String email, String password) {
        if (isValidAdminEmail(email)) {

            cAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    View view1 = view.findViewById(R.id.Constraint_layout);
                    if (task.isSuccessful()) {
                        //todo add student
                        System.out.print("created");
                        Snackbar snackbar = Snackbar.make(view, "Successful Sign in", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        cUser = cAuth.getCurrentUser();
                        createButton.setVisibility(View.GONE);
                        signInButton.setVisibility(View.VISIBLE);
                        verifyEmail();
                    } else {
                        try {
                            adminErrorText.setVisibility(View.VISIBLE);
                            throw task.getException();

                        } catch (FirebaseAuthWeakPasswordException e) {

                            adminErrorText.setText("Weak Password");
                            AdminSignUpFragment.this.password.requestFocus();
                            Log.e("errror", e.getMessage());

                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            AdminSignUpFragment.this.password.requestFocus();
                            adminErrorText.setText("Invalid Credentials");
                            Log.e("errror", e.getMessage());

                        } catch (FirebaseAuthUserCollisionException e) {
                            AdminSignUpFragment.this.password.requestFocus();
                            adminErrorText.setText("User Already Exists");
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



    }
        public void writeNewUser (String UserId, AdminSignUpData admin){
            db.child("users/Admins/" + UserId).setValue(admin);
        }
        public void signIn ( final String email, final String password){

            final String name = this.name.getText().toString();
            final String phone = this.phoneNo.getText().toString();
            final AdminSignUpData admin = new AdminSignUpData(email, phone, name);

            cUser = cAuth.getCurrentUser();

            if (cUser != null && !cUser.isEmailVerified()) {
                Toast toast = Toast.makeText(getActivity(), "Please verify email" + cUser.getEmail()
                        , Toast.LENGTH_LONG);
                toast.show();
                cUser.reload();
            } else {
                //showProgressBar();

                cAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    View view1 = view.findViewById(R.id.Constraint_layout);

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            signInButton.setEnabled(false);

                            //status.setText("Signed in");
                            Log.i("test", "signed in");
                            uId = cUser.getUid();
                            writeNewUser(uId, admin);
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
        public void verifyEmail () {

            cUser.sendEmailVerification().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                @Override

                public void onComplete(@NonNull Task<Void> task) {
                    View view1 = view.findViewById(R.id.Constraint_layout);
                    if (task.isSuccessful()) {
                        Snackbar snackbar = Snackbar.make(view1, "Verification Email sent", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    } else {
                        Snackbar snackbar = Snackbar.make(view1, "Coudn't send Email", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }
            });

        }
        @Override
        public void onClick (View v)
        {

            int id = v.getId();
            switch (id) {
                case R.id.create_account:
                    Log.i("test", email + " " + password);
                    createAccount(email.getText().toString(), password.getText().toString());

                    break;

                case R.id.Sign_in:
                    signIn(email.getText().toString(), password.getText().toString());

                    break;
            }
        }

        public boolean isValidAdminEmail (String email){
            String domain = email.substring(email.indexOf("@") + 1);
            if (domain.compareTo("scet.ac.in") == 0) return true;
            else return false;
        }
        public void MainIntent (String email){
        //todo AdminActivity
            Bundle bundle = new Bundle();
            Intent intent = new Intent(getActivity(), DrawerActivity.class);

            bundle.putString("user_id", email);
            intent.putExtras(bundle);
            getActivity().finish();
            startActivity(intent);

        }



}

class AdminSignUpData{

    String email_id,mobile,name;

    AdminSignUpData( String email_id,String mobile , String name){
        this.email_id=email_id;
        this.mobile=mobile;
        this.name=name;

    }


}
