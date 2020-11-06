package com.example.sns_project;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.example.sns_project.listener.OnPostListener;
import com.example.sns_project.listener.OnTradeListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static com.example.sns_project.Util.isStorageUrlpost;
import static com.example.sns_project.Util.isStorageUrltrade;
import static com.example.sns_project.Util.showToast;
import static com.example.sns_project.Util.storageUrlToName;

public class FirebaseHelper {
    private Activity activity;
    private OnPostListener onPostListener;
    private OnTradeListener onTradeListener;
    private int successCount;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    public FirebaseHelper(Activity activity) {
        this.activity = activity;
    }



    public void setOnPostListener(OnPostListener onPostListener) {
    this.onPostListener = onPostListener;
    }

    public void setOnTradeListener(OnTradeListener onTradeListener) {
        this.onTradeListener = onTradeListener;
    }



    public void storageDelete(final Postinfo postinfo){
         storage = FirebaseStorage.getInstance();
         storageRef = storage.getReference();

        final String id = postinfo.getId();
        ArrayList<String> contentsList = postinfo.getContents();

        for (int i = 0; i < contentsList.size(); i++) {
            String contents = contentsList.get(i);
            if (isStorageUrlpost(contents)) {
                successCount++;
                StorageReference desertRef = storageRef.child("posts/" + id + "/" + storageUrlToName(contents));
                desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        successCount--;
                        storeDelete(id, postinfo);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        showToast(activity, " ERROR");

                    }
                });
            }

        }
        storeDelete(id, postinfo);

    }

    public void storageDelete2(Tradeinfo tradeinfo) {
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        ArrayList<String> contentsList = tradeinfo.getContents();
        final String id = tradeinfo.getId();

        for (int i = 0; i < contentsList.size(); i++) {
            String contents = contentsList.get(i);
            if (isStorageUrltrade(contents)) {
                successCount++;
                StorageReference desertRef = storageRef.child("trades/" + id + "/" + storageUrlToName(contents));
                desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        successCount--;
                        storeDelete2(id, tradeinfo);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        showToast(activity, " ERROR");

                    }
                });
            }

        }
        storeDelete2(id, tradeinfo);
    }



    private void storeDelete(String id, final Postinfo postinfo) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        if (successCount == 0 ) {
            firebaseFirestore.collection("posts").document(id)
                    .delete()
                    .addOnSuccessListener(aVoid1 -> {
                        showToast(activity, "게시글 삭제를 완료하였습니다.");
                        onPostListener.onDelete(postinfo);
                    })
                    .addOnFailureListener((e) -> {
                        showToast(activity, "게시글 삭제를 실패하였습니다.");
                    });
        }
    }

    private void storeDelete2(String id, final Tradeinfo tradeinfo) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        if (successCount == 0 ) {
            firebaseFirestore.collection("trades").document(id)
                    .delete()
                    .addOnSuccessListener(aVoid1 -> {
                        showToast(activity, "게시글 삭제를 완료하였습니다.");
                        onTradeListener.onDelete2(tradeinfo);
                    })
                    .addOnFailureListener((e) -> {
                        showToast(activity, "게시글 삭제를 실패하였습니다.");
                    });
        }
    }
}
