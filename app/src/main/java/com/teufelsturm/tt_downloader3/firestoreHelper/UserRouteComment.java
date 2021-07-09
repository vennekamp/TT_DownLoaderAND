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
import com.teufelsturm.tt_downloader3.model.TT_Route_AND;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Martin
 */
public class UserRouteComment {
    private static String TAG = UserRouteComment.class.getSimpleName();


	public static void storeUserRouteComment(@NotNull final FirebaseUser firebaseUser, final int intIntWegNr,
                                             int typeRouteAscend, Long long_DateAsscended,
                                             String strMyRouteComment)  {
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
        Map<String, Object>newRouteComment = new HashMap<>();
        newRouteComment.put("TypeRouteAscend", typeRouteAscend);
        newRouteComment.put("DateAsscended", long_DateAsscended);
        newRouteComment.put("UserRouteComment", strMyRouteComment);
        db.collection("users").document(firebaseUser.getUid())
                .collection("Route")
                .document(String.valueOf(intIntWegNr))
                .set(newRouteComment)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Route Comment added for route with ID: " + intIntWegNr);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

	}

    public DocumentReference receiveUserRouteComment(final TT_Route_AND tt_route_and)  {
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if ( firebaseUser != null ) {
            final DocumentReference docRef = db.collection("users").document(firebaseUser.getUid())
                    .collection("Route").document(String.valueOf(tt_route_and.getIntTT_IDOrdinal()));
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            tt_route_and.setBegehungsStil(document.getLong("TypeRouteAscend").intValue());
                            tt_route_and.setDatumBestiegen(document.getLong("DateAsscended"));
                            tt_route_and.setStrKommentar(document.getString("UserRouteComment"));
                        } else {
//                        Log.v(TAG, "No such document");
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
