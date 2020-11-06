package com.example.sns_project.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sns_project.R;
import com.example.sns_project.Tradeinfo;
import com.example.sns_project.activity.WriteTradePostActivity;
import com.example.sns_project.adapter.TradeAdapter;
import com.example.sns_project.listener.OnTradeListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class TradeFragment extends Fragment {
    private static final String TAG = "TradeFragment";
    private FirebaseFirestore firebaseFirestore;
    private TradeAdapter mainAdapter;
    private ArrayList<Tradeinfo> TradeList;
    private boolean updating;
    private boolean topScrolled;

    public TradeFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      View view = inflater.inflate(R.layout.fragment_trade, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        TradeList = new ArrayList<>();
        mainAdapter = new TradeAdapter(getActivity(), TradeList);
        mainAdapter.setOnTradeListener(onTradeListener);

        RecyclerView recyclerView2 = view.findViewById(R.id.recyclerView2);
        RecyclerView commentRecyclerView = view.findViewById(R.id.comment_recycler_view);
        view.findViewById(R.id.floatingActionButton2).setOnClickListener(onClickListener);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView2.setAdapter(mainAdapter);
        recyclerView2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView2, int newState) {
                super.onScrollStateChanged(recyclerView2, newState);

                RecyclerView.LayoutManager layoutManager = recyclerView2.getLayoutManager();
                int firstVisibleItemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();

                if(newState == 1 && firstVisibleItemPosition == 0) {
                    onPause();
                    topScrolled = true;
                }
                if(newState == 0 && topScrolled){
                    tradeUpdate(true);
                    onPause();
                    topScrolled = false;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView2, int dx, int dy){
                super.onScrolled(recyclerView2, dx, dy);

                RecyclerView.LayoutManager layoutManager = recyclerView2.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();
                int lastVisibleItemPosition = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();

                if(totalItemCount - 3 <= lastVisibleItemPosition && !updating){
                    onPause();
                    tradeUpdate(false);
                }

                if(0 < firstVisibleItemPosition){

                    onPause();
                    topScrolled = false;
                }
            }
        });

        onPause();
        tradeUpdate(false);
        return view;
    }

    @Override
    public  void onPause(){
        super.onPause();
        mainAdapter.playerStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                                /*
                                case R.id.logoutButton:
                                        FirebaseAuth.getInstance().signOut();
                                        myStartActivity(SignUpActivity.class);
                                        break;
                                 */
                case R.id.floatingActionButton2:
                    myStartActivity(WriteTradePostActivity.class);
                    break;
            }
        }
    };

    OnTradeListener onTradeListener = new OnTradeListener() {
        @Override
        public void onDelete2(Tradeinfo tradeinfo) {
            TradeList.remove(tradeinfo);
            mainAdapter.notifyDataSetChanged();

        }

        @Override
        public void onModify2() {

        }
    };

    private void tradeUpdate(final boolean clear) {
        updating = true;
        Date date = TradeList.size() == 0 ||clear ? new Date() : TradeList.get(TradeList.size() - 1).getCreatedAt();
        CollectionReference collectionReference = firebaseFirestore.collection("trades");
        collectionReference.orderBy("createdAt", Query.Direction.DESCENDING).whereLessThan("createdAt", date).limit(10).get() // 최신글부터 출력하며 limit으로 10개만 보이게 제한
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(clear){
                                TradeList.clear();
                            }

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                TradeList.add(new Tradeinfo(
                                        document.getData().get("title").toString(),
                                        (ArrayList<String>) document.getData().get("contents"),
                                        (ArrayList<String>) document.getData().get("formats"),
                                        document.getData().get("publisher").toString(),
                                        new Date(document.getDate("createdAt").getTime()),
                                        document.getId()));
                            }
                            mainAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        updating = false;
                    }
                });
    }


    private void myStartActivity(Class c) {
        Intent intent = new Intent(getActivity(), c);
        startActivityForResult(intent, 0);
    }
}