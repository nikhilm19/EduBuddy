package com.example.nikmul19.edubuddy;

import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{


    public LoginFragment() {
        // Required empty public constructor
    }



        public View view;

        private FirebaseAuth cAuth;
        private FirebaseUser cUser;
        private DatabaseReference db;
        public EditText email,password,enroll;
        private TextView signUp,errorText;
        private ProgressBar progressBar;
        private Button signInButton,signOutButton;
        private String uId;




        public void showProgressBar(){
            ProgressBar progressBar=view.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            Log.i("bar","yes");

        }

        public void hideProgressBar() {
            ProgressBar progressBar=view.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.INVISIBLE);



        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            view=inflater.inflate(R.layout.fragment_login, container, false);
            findViews();
        /*if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Toast toast = Toast.makeText(this, "Permission not granted", Toast.LENGTH_LONG);
            toast.show();
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 200);

        }*/
            return  view;
        }
        /*@Override
        public void onCreate(Bundle savedInstanceState) {
            Intent intent = getIntent();
            int status_value = intent.getIntExtra("signed_out", -1);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login2);

            if (status_value != -1) {
                signInButton.setEnabled(true);
            }



        }*/
        public void findViews(){
            db= FirebaseDatabase.getInstance().getReference();
            errorText=view.findViewById(R.id.errorText);
            signInButton=view.findViewById(R.id.sign_in_button);
            // emailVerifyButton=view.findViewById(R.id.verify_button);
            cAuth=FirebaseAuth.getInstance();
            email=((EditText)view.findViewById(R.id.email_id_1));
            password=((EditText)view.findViewById(R.id.password));
            //createButton=view.findViewById(R.id.create_account);
            progressBar=view.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.INVISIBLE);
            signInButton.setOnClickListener(this);


        }



        @Override
        public void onRequestPermissionsResult ( int requestCode,
                                                 String permissions[], int[] grantResults){
            if (requestCode == 200) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast toast = Toast.makeText(getActivity(), "Permission granted. Thanks", Toast.LENGTH_LONG);
                    toast.show();


                }
            }


        }
        @Override
        public void onStart(){
            super.onStart();
            cUser=cAuth.getCurrentUser();
            if(cUser!=null){
                MainIntent(cUser.getEmail());
            }
            //todo
            // if cUser is not null update UI
        }


        public void signIn(final String email, final String password){
            cUser=cAuth.getCurrentUser();
            String domain= email.substring(email.indexOf("@")+1);

            if(!domain.contains(".")){
                Toast toast = Toast.makeText(getActivity(), "Fill proper Credentials", Toast.LENGTH_LONG);
                toast.show();
            }



            //showProgressBar();
            cAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override

                public void onComplete(@NonNull Task<AuthResult> task) {
                    final String error;
                    if (task.isSuccessful()){
                        MainIntent(email);
                        System.out.print("succesful");
                    }
                    else{

                        try {
                            errorText.setVisibility(View.VISIBLE);
                            throw task.getException();

                        } catch(FirebaseAuthWeakPasswordException e) {

                            errorText.setText("Weak Password");
                           LoginFragment.this.password.requestFocus();
                            Log.e("errror", e.getMessage());

                        } catch(FirebaseAuthInvalidCredentialsException e) {
                            LoginFragment.this.password.requestFocus();
                            errorText.setText("Invalid Credentials");
                            Log.e("errror", e.getMessage());

                        } catch(FirebaseAuthUserCollisionException e) {
                            LoginFragment.this.password.requestFocus();
                            errorText.setText("User Already Exists");
                            Log.e("errror", e.getMessage());
                        } catch(Exception e) {
                            Log.e("errror", e.getMessage());
                        }
                        Log.e("eror", "onComplete: Failed=" + task.getException().getMessage());
                        System.out.print("UNsuccesful");


                    }

                }
            });

            //hideProgressBar();

        }

        public void signOut(){
            Log.i("yest","out");
            showProgressBar();
            cAuth.signOut();
            hideProgressBar();

            signInButton.setVisibility(View.VISIBLE);
            //emailVerifyButton.setVisibility(View.VISIBLE);
            signInButton.setEnabled(true);
            signOutButton.setEnabled(false);
            //emailVerifyButton.setEnabled(true);

        }

        @Override
        public void onClick(View v)
        {

            int id=v.getId();
            switch (id) {


                case R.id.sign_in_button:

                    showProgressBar();
                    //Log.i("test",email+" "+password);

                    signIn(email.getText().toString(),password.getText().toString());

                    break;


                case R.id.sign_up:
                    System.out.println("clicked");
                    SignUpFragment fragment= new SignUpFragment();
                    FragmentManager fragmentManager= getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.Constraint_Layout,fragment,null).addToBackStack(null).commit();

            }
            hideProgressBar();

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



