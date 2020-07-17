package Admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.system_stats.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import PC_info.PC_info;
import Sub_Admin.LabItemAdapter;
import Sub_Admin.sub_admin_Lab_model_class;

public class Admin_Lab extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private DocumentReference lab = db.collection("LAB").document("LAB");

    private ArrayList<sub_admin_Lab_model_class> model_classList = new ArrayList<>();

    private EditText search;

    private LabItemAdapter labItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__lab);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intentThatStartedThisActivity=getIntent();
        final String LabNo = intentThatStartedThisActivity.getStringExtra("Labno");
        Toast.makeText(this, LabNo, Toast.LENGTH_SHORT).show();

        search = findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
        assert LabNo != null;
        lab.collection(LabNo.toLowerCase())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null)
                            Toast.makeText(Admin_Lab.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        else {
                            model_classList.clear();
                            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                    model_classList.add(new sub_admin_Lab_model_class(queryDocumentSnapshot.getId().toUpperCase()));
                                }
                            } else {
                                showMessage("Desktop Application not installed...", "Please install the desktop application in your lab PC's to view their status!");
                            }
                            RecyclerView recyclerView = findViewById(R.id.rV_AdminPC);
                            recyclerView.setHasFixedSize(true);
                            LinearLayoutManager linearLayout = new LinearLayoutManager(Admin_Lab.this);
                            linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerView.setLayoutManager(linearLayout);

                            labItemAdapter = new LabItemAdapter(model_classList);
                            labItemAdapter.notifyDataSetChanged();

                            recyclerView.setAdapter(labItemAdapter);


                            labItemAdapter.setOnItemClickLIstener(new LabItemAdapter.OnItemClickListener() {
                                @Override
                                public void onClick(String PCno, int position) {
                                    Intent intent = new Intent(Admin_Lab.this, PC_info.class);
                                    intent.putExtra("Labno", LabNo);
                                    intent.putExtra("PCno", PCno);
                                    startActivity(intent);
                                }
                            });
                        }

                    }
                });
    }

    private void filter(String text) {
        ArrayList<sub_admin_Lab_model_class> fileredList = new ArrayList<>();
        for (sub_admin_Lab_model_class item : model_classList) {
            if (item.getPCno().toLowerCase().contains(text.toLowerCase())) {
                fileredList.add(item);
            }
        }
        if (!fileredList.isEmpty())
            labItemAdapter.filterList(fileredList);
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
