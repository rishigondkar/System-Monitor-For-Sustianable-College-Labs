package PC_info;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.system_stats.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.spark.submitbutton.SubmitButton;

import java.util.Calendar;
import java.util.Objects;

public class PC_info extends AppCompatActivity {
    private TextView total, used, available, cpu_brand, cpu_mnf, cpu_spd, cpu_usage, mnf_brand, mnf_model, platorm, uptime, status,idle;
    private Button sleep, shutdown, hibernate, req_ss,req_cam;
    private FirebaseAuth mAuth;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private DocumentReference lab = db.collection("LAB").document("LAB");
    private DocumentReference pc;

    private Handler handler = new Handler();

    private SubmitButton shutdown2;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pc_info);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Info");

        total = findViewById(R.id.tV2);
        available = findViewById(R.id.tV4);
        used = findViewById(R.id.tV6);
        cpu_brand = findViewById(R.id.tV8);
        cpu_spd = findViewById(R.id.tV10);
        cpu_usage = findViewById(R.id.tV12);
        cpu_mnf = findViewById(R.id.tV14);
        mnf_brand = findViewById(R.id.tV16);
//        mnf_model = findViewById(R.id.tV18);
//        platorm = findViewById(R.id.tV20);
        sleep = findViewById(R.id.sleep);
        uptime = findViewById(R.id.tV22);
        status = findViewById(R.id.tV24);
        idle=findViewById(R.id.tV26);
        shutdown=findViewById(R.id.register);
        shutdown2 = findViewById(R.id.register1);
        hibernate=findViewById(R.id.hibernate);
        req_ss = findViewById(R.id.req_ss);
        req_cam=findViewById(R.id.req_cam);

        Intent intentThatStartedThisActivity = getIntent();
        final String PC = intentThatStartedThisActivity.getStringExtra("PCno");
        final String Lab = intentThatStartedThisActivity.getStringExtra("Labno");
        assert Lab != null;
        Toast.makeText(this, Lab.toUpperCase() + ":  " + PC, Toast.LENGTH_SHORT).show();

        status.setText("Processing...");

        assert PC != null;
        pc = lab.collection(Lab.toLowerCase()).document(PC.toLowerCase());

        runnable.run();

        pc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null)
                    Toast.makeText(PC_info.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                else {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        total.setText((String) documentSnapshot.get("Total_memory"));
                        available.setText((String) documentSnapshot.get("Available_memory"));
                        used.setText((String) documentSnapshot.get("Used_memory"));
                        cpu_brand.setText((String) documentSnapshot.get("cpu_brand"));
                        cpu_spd.setText(documentSnapshot.get("cpu_speed")+" GHz");
                        cpu_usage.setText((documentSnapshot.get("cpu_usage"))+"%");
                        cpu_mnf.setText((String) documentSnapshot.get("cpu_mnf"));
                        mnf_brand.setText((String) documentSnapshot.get("cpu_brand"));
//                        mnf_model.setText((String) documentSnapshot.get("mnf_model"));
//                        platorm.setText((String) documentSnapshot.get("platform"));
                        int hours = (Integer.valueOf((String) Objects.requireNonNull(documentSnapshot.get("upTime")))) / 3600;
                        int minutes = (Integer.valueOf((String) Objects.requireNonNull(documentSnapshot.get("upTime"))) % 3600) / 60;
                        int seconds = Integer.valueOf((String) Objects.requireNonNull(documentSnapshot.get("upTime"))) % 60;
//                        uptime.setText(String.valueOf(documentSnapshot.get("upTime"))+" seconds");
                        uptime.setText(hours+"Hr "+minutes+"min "+seconds+"sec");
                        String s1= (String) documentSnapshot.get("mouseMovement");
                        if (s1 != null) {
                            if(s1.equals("idle"))
                                idle.setText("True");
                            else
                                idle.setText("False");
                        }
                    }
                }
            }
        });
        shutdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String active = (String) status.getText();
                if (active.equals("PC is active!")) {
                    shutdown.setVisibility(View.GONE);
                    shutdown2.setVisibility(View.VISIBLE);
                    shutdown2.startAnimation();
                    pc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Boolean status1 = (Boolean) documentSnapshot.get("shutDown");
                            if (status1 == Boolean.FALSE) {
                                pc.update("shutDown", Boolean.TRUE);
                                Toast.makeText(PC_info.this, PC + " is being shutting down.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PC_info.this, "This operation can't take place!", Toast.LENGTH_SHORT).show();
                                shutdown.setVisibility(View.VISIBLE);
                                shutdown2.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                else
                    Toast.makeText(PC_info.this, "PC is inactive!", Toast.LENGTH_SHORT).show();
            }
        });
        sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String active = (String) status.getText();
                if (active.equals("PC is active!")) {
                pc.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Boolean status2=documentSnapshot.getBoolean("logOff");
                                if(status2==Boolean.FALSE){
                                    pc.update("logOff",Boolean.TRUE);
                                    Toast.makeText(PC_info.this, PC + " is going in sleep mode!", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(PC_info.this, "This operation can't take place!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PC_info.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                }
                else
                    Toast.makeText(PC_info.this, "PC is inactive!", Toast.LENGTH_SHORT).show();
            }
        });

        hibernate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String active = (String) status.getText();
                if (active.equals("PC is active!")) {
                pc.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Boolean status3=documentSnapshot.getBoolean("hibernate");
                                if(status3==Boolean.FALSE){
                                    pc.update("logOff",Boolean.TRUE);
                                    Toast.makeText(PC_info.this, PC + " is going in hibernation mode!", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(PC_info.this, "This operation can't take place!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PC_info.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
                else
                        Toast.makeText(PC_info.this, "PC is inactive!", Toast.LENGTH_SHORT).show();
            }
        });

        req_ss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String active = (String) status.getText();
                if (active.equals("PC is active!")) {
                    pc.get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(final DocumentSnapshot documentSnapshot) {
                                    Boolean req = documentSnapshot.getBoolean("screenshot");
                                    if (req == Boolean.FALSE) {
                                        pc.update("screenshot", Boolean.TRUE).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                            Intent intent = new Intent(PC_info.this, ss.class);
                                                            intent.putExtra("pc", PC);
                                                            intent.putExtra("lab",Lab);
                                                            startActivity(intent);
                                                } else {
                                                    Toast.makeText(PC_info.this, (CharSequence) task.getException(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(PC_info.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else
                    Toast.makeText(PC_info.this, "PC in inactive. Cant capture screenshot.", Toast.LENGTH_SHORT).show();
            }
        });

        req_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String active = (String) status.getText();
                if (active.equals("PC is active!")) {
                    pc.get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(final DocumentSnapshot documentSnapshot) {
                                    Boolean req = documentSnapshot.getBoolean("cam");
                                    if (req == Boolean.FALSE) {
                                        pc.update("cam", Boolean.TRUE).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Intent intent = new Intent(PC_info.this, cam.class);
                                                    intent.putExtra("pc", PC);
                                                    intent.putExtra("lab",Lab);
                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(PC_info.this, (CharSequence) task.getException(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(PC_info.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else
                    Toast.makeText(PC_info.this, "PC in inactive. Cant capture screenshot.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            Calendar cal = Calendar.getInstance();
            final String crntHr = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
            final String crntMin = String.valueOf(cal.get(Calendar.MINUTE));
//            String crntSec = String.valueOf(cal.get(Calendar.SECOND));
            pc.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String sysTime = (String) documentSnapshot.get("sysTime");
                            String[] Time;
                            if (sysTime != null) {
                                Time = sysTime.split(":");
                                if (crntHr.equals(Time[0])) {
                                    if (crntMin.equals(Time[1])) {
                                        status.setText("PC is active!");
                                        pc.update("Status", "PC is active!");
                                    } else {
                                        status.setText("PC is inactive!");
                                        pc.update("Status", "PC is inactive!");
                                    }
                                    if (Math.abs(Integer.parseInt(crntMin) - Integer.parseInt(Time[1])) <= 1) {
                                        status.setText("PC is active!");
                                    } else {
                                        status.setText("PC is inactive!");
                                    }
                                } else if (Math.abs(Integer.valueOf(crntHr) - Integer.valueOf(Time[0])) == 1) {
                                    if (Math.abs(Integer.parseInt(crntMin) - Integer.parseInt(Time[1])) <= 60 && Math.abs(Integer.parseInt(crntMin) + Integer.parseInt(Time[1])) >= 59) {
                                        status.setText("PC is active!");
                                    } else if (Math.abs(Integer.parseInt(crntMin) - Integer.parseInt(Time[1])) >= 0 && Math.abs(Integer.parseInt(crntMin) - Integer.parseInt(Time[1])) <= 1) {
                                        status.setText("PC is active!");
                                    } else {
                                        status.setText("PC is inactive!");
                                    }
                                } else {
                                    status.setText("PC is inactive!");
                                }

                                } else {
                                    status.setText("PC is inactive!");
                                    pc.update("Status", "PC is active!");
                                }
                            }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PC_info.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            handler.postDelayed(this, 5000);
        }
    };

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }


    @Override
    public void onBackPressed() {
        handler.removeCallbacks(runnable);
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStop() {
        handler.removeCallbacks(runnable);
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        runnable.run();
    }
}

