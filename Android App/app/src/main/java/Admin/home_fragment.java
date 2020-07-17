package Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.system_stats.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;


public class home_fragment extends Fragment {

    private ArrayList<admin_lab_model_class> model_classList = new ArrayList<>();

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference lab = db.collection("LAB2");

    private CollectionReference Admins = db.collection("Admins");
    private CollectionReference Sub_Admins = db.collection("Sub_Admins");

    private String labNo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_fragment, null, true);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        lab.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null)
                    Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                else {
                    model_classList.clear();
                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                            model_classList.add(new admin_lab_model_class(queryDocumentSnapshot.getId().toUpperCase()));
                            RecyclerView recyclerView = view.findViewById(R.id.rV_Admin);
                            recyclerView.setHasFixedSize(true);
                            LinearLayoutManager linearLayout = new LinearLayoutManager(view.getContext());
                            linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerView.setLayoutManager(linearLayout);

                            adminLabAdapter labItemAdapter = new adminLabAdapter(model_classList);
                            labItemAdapter.notifyDataSetChanged();

                            recyclerView.setAdapter(labItemAdapter);

                            labItemAdapter.setOnItemClickListener(new adminLabAdapter.OnItemClickListener() {
                                @Override
                                public void onClick(String LabNo, int position) {
                                    Intent intent = new Intent(view.getContext(), Admin_Lab.class);
                                    intent.putExtra("Labno", LabNo);
                                    startActivity(intent);
                                }
                            });
                        }
                    } else {
                        showMessage("Desktop Application not installed...", "Please install the desktop application in your college labs to view PC status!");
                    }
                }
            }
        });
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
