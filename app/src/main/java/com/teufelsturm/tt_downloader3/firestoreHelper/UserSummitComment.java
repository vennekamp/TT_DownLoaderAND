package com.teufelsturm.tt_downloader3.firestoreHelper;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.teufelsturm.tt_downloader3.model.TT_Summit_AND;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Martin
 */
public class UserSummitComment {
    private static String TAG = UserSummitComment.class.getSimpleName();

    public static void storeUserSummitComment(@NotNull final FirebaseUser firebaseUser, final int intTTGipfelNr,
                                              Boolean isAscendedSummit, Long myLongDateOfAscend,
                                              String strMySummitComment, Boolean append) {


        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("uid", firebaseUser.getUid());
        // Add a new document with a generated ID
        db.collection("users")
                .document(firebaseUser.getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "user added with ID: " + firebaseUser.getUid());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        Map<String, Object> newSummitComment = new HashMap<>();
        newSummitComment.put("IsAscendedSummit", isAscendedSummit);
        newSummitComment.put("DateAsscended", myLongDateOfAscend);
        newSummitComment.put("UserSummitComment", stripTrailingLineFeed(strMySummitComment));
        DocumentReference documentReference = db.collection("users").document(firebaseUser.getUid())
                .collection("Summit")
                .document(String.valueOf(intTTGipfelNr));

        if (append) {
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.e(TAG, "Existing DocumentSnapshot data: " + document.getData());
                            newSummitComment.put("UserSummitComment", stripTrailingLineFeed(
                                    document.getString("UserSummitComment")
                                            + "\n" + strMySummitComment));

                        } else {
                            // Log.d(TAG, "Not appending, no data in document!");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                    documentReference
                            .set(newSummitComment)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Summit Comment added for route with ID: " + intTTGipfelNr);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });
                }
            });
        }
        else { /* Do not append */
            documentReference
                    .set(newSummitComment)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Summit Comment added for route with ID: " + intTTGipfelNr);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        }
    }

    private static String stripTrailingLineFeed(String inStr) {
        while (inStr.startsWith("\n")) {
            inStr = inStr.substring(1);
        }
        return inStr;
    }

    public DocumentReference receiveUserSummitComment(final TT_Summit_AND tt_summit_and) {
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            final DocumentReference docRef = db.collection("users").document(firebaseUser.getUid())
                    .collection("Summit").document(String.valueOf(tt_summit_and.getIntTT_IDOrdinal()));
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.e(TAG, "DocumentSnapshot data: " + document.getData());
                            tt_summit_and.setBln_Asscended(document.getBoolean("IsAscendedSummit"));
                            tt_summit_and.setDatumBestiegen(document.getLong("DateAsscended"));
                            tt_summit_and.setStr_MyComment(document.getString("UserSummitComment"));
                        } else {
                            // Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
            return docRef;
        }
        return null;
    }
}
