package Sub_Admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.system_stats.LoginActivity;
import com.example.system_stats.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import PC_info.PC_info;

public class Sub_Admin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private DocumentReference lab = db.collection("LAB").document("LAB");

    private CollectionReference Sub_Admins = db.collection("Sub_Admins");

    private String UId, labNo;

    private ArrayList<sub_admin_Lab_model_class> model_classList = new ArrayList<>();

    private EditText search;

    private LabItemAdapter labItemAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        if (mUser == null) {
            Intent intent = new Intent(Sub_Admin.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        setContentView(R.layout.activity_sub__admin);
        Toast.makeText(this, "Logged in as " + mUser.getEmail(), Toast.LENGTH_SHORT).show();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        UId = mUser.getUid();

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

        Sub_Admins
                .document(UId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshot != null) {
                            labNo = (String) documentSnapshot.get("labNo");
                        }
                        if (labNo != null) {
                            lab.collection(labNo)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                            if (e != null)
                                                Toast.makeText(Sub_Admin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            else {
                                                model_classList.clear();
                                                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                                                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                                            model_classList.add(new sub_admin_Lab_model_class(queryDocumentSnapshot.getId().toUpperCase()));
                                                        }
                                                    } else {
                                                        showMessage("Desktop Application not installed...", "Please install the desktop application in your lab PC's to view their status!");
                                                    }
                                                    RecyclerView recyclerView = findViewById(R.id.rV_subAdmin);
                                                    recyclerView.setHasFixedSize(true);
                                                    LinearLayoutManager linearLayout = new LinearLayoutManager(Sub_Admin.this);
                                                    linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
                                                    recyclerView.setLayoutManager(linearLayout);

                                                    labItemAdapter = new LabItemAdapter(model_classList);
                                                    labItemAdapter.notifyDataSetChanged();

                                                    recyclerView.setAdapter(labItemAdapter);


                                                    labItemAdapter.setOnItemClickLIstener(new LabItemAdapter.OnItemClickListener() {
                                                        @Override
                                                        public void onClick(String PCno, int position) {
                                                            Intent intent = new Intent(Sub_Admin.this, PC_info.class);
                                                            intent.putExtra("Labno", labNo);
                                                            intent.putExtra("PCno", PCno);
                                                            startActivity(intent);
                                                        }
                                                    });
                                            }

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
        labItemAdapter.filterList(fileredList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        Intent exit = new Intent(Intent.ACTION_MAIN);
        exit.addCategory(Intent.CATEGORY_HOME);
        exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(exit);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                mAuth.signOut();
                Intent intent = new Intent(Sub_Admin.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

}
