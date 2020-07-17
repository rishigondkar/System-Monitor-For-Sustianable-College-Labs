package Admin.pending;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.system_stats.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class pending_fragment extends Fragment {
    private RecyclerView recyclerView;
    private itemAdapter itemAdapter;


    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference Admins = db.collection("Admins");
    private CollectionReference Sub_Admins = db.collection("Sub_Admins");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pending_fragment,null,true);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        recyclerView=view.findViewById(R.id.rV);
        CollectionReference requests = Admins.document("Sub-Admins").collection("Sub-Admins");
            Query query = requests
//                .whereEqualTo("access_given", "pending".toString())
                    .orderBy("labNo", Query.Direction.ASCENDING);
//                .whereEqualTo("access_given","pending".toString());

            FirestoreRecyclerOptions<model_class> firebaseRecyclerAdapter = new FirestoreRecyclerOptions.Builder<model_class>()
                    .setQuery(query, model_class.class)
                    .build();

            itemAdapter = new itemAdapter(firebaseRecyclerAdapter);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            itemAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(itemAdapter);

            itemAdapter.setOnItemClickLIstener(new itemAdapter.OnItemClickListener() {
                @Override
                public void onYesClick(final DocumentSnapshot documentSnapshot, int position) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Do you confirm to grant access?")
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("name", documentSnapshot.get("name"));
                                    data.put("email", documentSnapshot.get("email"));
                                    data.put("labNo", documentSnapshot.get("labNo"));
                                    Sub_Admins.document(documentSnapshot.getId()).set(data);
                                    Admins.document("Sub-Admins").collection("Sub-Admins").document(documentSnapshot.getId())
                                            .delete();
                                    Toast.makeText(view.getContext(), "User added as sub-admin.", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setCancelable(false)
                            .show();
                }

                @Override
                public void onNoClick(final DocumentSnapshot documentSnapshot, int position) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Do you confirm to deny access?")
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("name", documentSnapshot.get("name"));
                                    data.put("email", documentSnapshot.get("email"));
                                    data.put("labNo", documentSnapshot.get("labNo"));
                                    db.collection("Spam").document(documentSnapshot.getId()).set(data);
                                    Admins.document("Sub-Admins").collection("Sub-Admins").document(documentSnapshot.getId())
                                            .delete();
//                                        .update("access_given", "no");
                                    Toast.makeText(view.getContext(), "User was reported as spam.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
            });
    }
    @Override
    public void onStart() {
        super.onStart();
        itemAdapter.startListening();
    }

    @Override
    public void onStop() {
        itemAdapter.stopListening();
        super.onStop();
    }
}
