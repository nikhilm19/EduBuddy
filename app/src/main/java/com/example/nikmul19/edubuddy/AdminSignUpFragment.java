package com.example.nikmul19.edubuddy;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminSignUpFragment extends Fragment {


    public AdminSignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_sign_up,container,false);




        //todo :: implement sign up and sign in
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
