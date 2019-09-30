package com.example.nikmul19.edubuddy;


import android.app.SearchManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecyclerBuyBookFragment extends Fragment {

    View view;
    RecyclerView recyclerView;

    ActionBar actionBar;

    RecyclerView.LayoutManager layoutManager;
    DatabaseReference db;
    ProgressBar bookProgress;
    TextView notConnectedError;
    ImageView notConnectedImage;
    Button tryAgain;

    BooksAdapter adapter;
    List<Book> bookList = new ArrayList<>();

    List<Book> filteredBookList = new ArrayList<>();


    public RecyclerBuyBookFragment() {
        // Required empty public constructor
    }

    public void showProgress() {

        Log.i("test1", "shown");
        bookProgress.setVisibility(View.VISIBLE);
        bookProgress.setIndeterminate(true);

    }

    public void hideProgress() {
        Log.i("test1", "hidden");
        bookProgress.setVisibility(View.INVISIBLE);
        bookProgress.setIndeterminate(false);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recycler_buy_book, container, false);
        notConnectedError = view.findViewById(R.id.not_connected_error);


        notConnectedImage = view.findViewById(R.id.not_connected_image);
        bookProgress = view.findViewById(R.id.loading_books_progress);
        hideProgress();

        tryAgain = view.findViewById(R.id.try_again);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();

                notConnectedError.setVisibility(View.INVISIBLE);
                notConnectedImage.setVisibility(View.INVISIBLE);
                if (isConnected()) {
                    setHasOptionsMenu(true);
                    createSearchBar();
                    tryAgain.setVisibility(View.GONE);

                    notConnectedImage.setVisibility(View.GONE);
                    notConnectedError.setVisibility(View.GONE);
                    listBooks();

                } else {
                    notConnectedImage.setVisibility(View.VISIBLE);
                    notConnectedError.setVisibility(View.VISIBLE);
                    hideProgress();
                }


            }
        });
        recyclerView = view.findViewById(R.id.buy_recycler);
        layoutManager = new LinearLayoutManager(this.getActivity());
        adapter = new BooksAdapter(bookList, getContext());
        recyclerView.setLayoutManager(layoutManager);
        db = FirebaseDatabase.getInstance().getReference();

        recyclerView.setAdapter(adapter);
        showProgress();

        if (isConnected()) {
            setHasOptionsMenu(true);
            createSearchBar();

            notConnectedImage.setVisibility(View.GONE);
            notConnectedError.setVisibility(View.GONE);
            listBooks();
        } else {
            hideProgress();

            tryAgain.setVisibility(View.VISIBLE);

            notConnectedImage.setVisibility(View.VISIBLE);
            notConnectedError.setVisibility(View.VISIBLE);
        }

        return view;
    }

    public boolean isConnected() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void createSearchBar() {


        final SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        final SearchView searchView;

        searchView = getActivity().findViewById(R.id.search_toolbar);
        searchView.setVisibility(View.VISIBLE);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("test", "clcilked search");
                searchView.onActionViewExpanded();
            }
        });


        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (query == "") {


                }

                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return false;
            }
        });


    }

    @Override
    public void onDetach() {
        super.onDetach();
        final SearchView searchView;

        searchView = getActivity().findViewById(R.id.search_toolbar);
        searchView.setVisibility(View.GONE);
    }

    /*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search_toolbar);
    }
  */


    public void listBooks() {

        db.child("books").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Book book = snapshot.getValue(Book.class);
                    book.photoLocation = snapshot.getKey();
                    Log.i("test", "location : " + book.photoLocation);
                    Log.i("test", snapshot.getValue().toString());
                    bookList.add(book);


                    adapter.notifyDataSetChanged();
                }

                hideProgress();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

