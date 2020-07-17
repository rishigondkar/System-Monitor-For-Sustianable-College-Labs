package Admin.accepted;

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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

import Admin.pending.model_class;


public class accepted_fragment extends Fragment {

    private RecyclerView recyclerView;
    private accepteditemAdapter accepteditemAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference Admins = db.collection("Admins");
    private CollectionReference Sub_Admins = db.collection("Sub_Admins");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accepted_fragment,null,true);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        recyclerView=view.findViewById(R.id.rV2);

        Query query = Sub_Admins
                .orderBy("labNo", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<model_class> firebaseRecyclerAdapter = new FirestoreRecyclerOptions.Builder<model_class>()
                .setQuery(query, model_class.class)
                .build();

        accepteditemAdapter = new accepteditemAdapter(firebaseRecyclerAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        accepteditemAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(accepteditemAdapter);

        accepteditemAdapter.setOnItemClickLIstener(new accepteditemAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(final DocumentSnapshot documentSnapshot, int position) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Do you confirm to revoke the access given to this sub-admin?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Sub_Admins.document(documentSnapshot.getId()).delete();
                                Toast.makeText(view.getContext(), "Sub-Admin deleted!", Toast.LENGTH_SHORT).show();
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
        accepteditemAdapter.startListening();
    }

    @Override
    public void onStop() {
        accepteditemAdapter.stopListening();
        super.onStop();
    }
}
