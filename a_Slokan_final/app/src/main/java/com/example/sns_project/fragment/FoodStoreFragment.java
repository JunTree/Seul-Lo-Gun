package com.example.sns_project.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sns_project.R;
import com.example.sns_project.Storeinfo;
import com.example.sns_project.adapter.StoreAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import javax.annotation.Nonnull;


public class FoodStoreFragment extends Fragment {
    private static final String TAG = "FoodStoreFragment";
    private FirebaseFirestore firebaseFirestore;
    private StoreAdapter mainAdapter;
    private ArrayList<Storeinfo> storeList;

    public FoodStoreFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Nonnull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_foodstore, container, false);

        RecyclerView recyclerView3 = view.findViewById(R.id.recyclerView3);
        recyclerView3.setHasFixedSize(true);
        recyclerView3.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseFirestore = FirebaseFirestore.getInstance();
        storeList = new ArrayList<>();
        mainAdapter = new StoreAdapter(storeList,getActivity());
        //mainAdapter.setOnPostListener(onPostListener);
        recyclerView3.setHasFixedSize(true);
        recyclerView3.setAdapter(mainAdapter);
        storeupdate();
        return view;
    }


    private void storeupdate() {
        CollectionReference collectionReference = firebaseFirestore.collection("stores");
        collectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                storeList.add(new Storeinfo(
                                        document.getData().get("storeName").toString(),
                                        document.getData().get("Tag").toString(),
                                        document.getData().get("storetel").toString(),
                                        document.getData().get("photoUrl").toString()));
                            }
                            mainAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    private void myStartActivity(Class c) {
        Intent intent = new Intent(getActivity(), c);
        startActivityForResult(intent, 0);
    }

}