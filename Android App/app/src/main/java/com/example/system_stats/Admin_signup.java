package com.example.system_stats;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Admin_signup extends AppCompatActivity {

    private TextInputEditText mail, passwd;
    private TextInputLayout lyt_mail,lyt_pass;
    private FloatingActionButton submit;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference Admins = db.collection("Admins");

    private ProgressDialog loginProgress;

    private String loginmail, loginpass, User_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signup);

        mAuth = FirebaseAuth.getInstance();

        loginProgress = new ProgressDialog(this);

        mail = findViewById(R.id.eT_mail);
        passwd = findViewById(R.id.eT_passwd);
        submit = findViewById(R.id.btn_submit);
        lyt_mail=findViewById(R.id.lyt_mail);
        lyt_pass=findViewById(R.id.lyt_pass);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lyt_mail.setErrorEnabled(false);
                lyt_pass.setErrorEnabled(false);
                loginmail = mail.getText().toString().trim();
                loginpass = passwd.getText().toString().trim();
                if (loginmail.isEmpty())
                    lyt_mail.setError("This field cannot be empty!");
                if (loginpass.isEmpty())
                    lyt_pass.setError("This field cannot be empty!");
                else if (!loginmail.isEmpty() && !loginpass.isEmpty()) {
                    loginProgress.setMessage("Signing Up...");
                    loginProgress.setCanceledOnTouchOutside(false);
                    loginProgress.show();
                    mAuth.createUserWithEmailAndPassword(loginmail, loginpass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        String errorMessage = task.getException().getMessage();
                                        Toast.makeText(Admin_signup.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                                        mail.getText().clear();
                                        passwd.getText().clear();
                                        loginProgress.dismiss();
                                    } else {
                                        mUser = mAuth.getCurrentUser();
                                        if (mUser != null) {
                                            User_Id = mUser.getUid();
                                            Map<String, Object> data = new HashMap<>();
                                            data.put("admin", Boolean.TRUE);
//                                            data.put("E-mail verified", Boolean.FALSE);
                                            Admins.document(User_Id).set(data)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            sendMailVerification(mUser);
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(Admin_signup.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                }
                            });
                }
            }
        });
    }

    private void sendMailVerification(FirebaseUser mUser) {
        mUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(Admin_signup.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    loginProgress.dismiss();
                } else {
                    mAuth.signOut();
                    loginProgress.dismiss();
                    Intent intent = new Intent(Admin_signup.this, LoginActivity.class);
                    intent.putExtra("email", loginmail);
                    intent.putExtra("password", loginpass);
                    intent.putExtra("toast", "User added as admin. Please verify your e-mail.");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
    }


}
