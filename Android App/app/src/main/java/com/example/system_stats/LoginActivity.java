package com.example.system_stats;


import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import Admin.Admin;
import Sub_Admin.Sub_Admin;

public class LoginActivity extends Activity {

    private TextInputEditText mail, passwd;
    private TextInputLayout layout_mail,layout_password;
    private FloatingActionButton submit;
    private Button signup;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference Admins = db.collection("Admins");
    private CollectionReference Sub_Admins = db.collection("Sub_Admins");
    private CollectionReference requests = Admins.document("Sub-Admins").collection("Sub-Admins");

    private ProgressDialog loginProgress, mProgress;

    private String loginmail, loginpass, User_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intentThatStartedThisActivity = getIntent();
        String mail_intent = intentThatStartedThisActivity.getStringExtra("email");
        String password_intent = intentThatStartedThisActivity.getStringExtra("password");
        String toast_intent = intentThatStartedThisActivity.getStringExtra("toast");

        mProgress = new ProgressDialog(this);
        loginProgress = new ProgressDialog(this);

        mProgress.setMessage("Please Wait!");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        if (mUser != null) {
            User_Id = mUser.getUid();

            Admins.document(User_Id).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                mProgress.dismiss();
                                Intent intent = new Intent(LoginActivity.this, Admin.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } else {
                                Sub_Admins.document(User_Id).get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if (documentSnapshot.exists()) {
                                                    mProgress.dismiss();
                                                    Intent intent = new Intent(LoginActivity.this, Sub_Admin.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                } else {
                                                    mAuth.signOut();
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                mProgress.dismiss();
                                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mProgress.dismiss();
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Admins.get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots.isEmpty()) {
                                mProgress.dismiss();
                                Intent intent = new Intent(LoginActivity.this, Admin_signup.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } else {
                                mProgress.dismiss();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mProgress.dismiss();
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            mail = findViewById(R.id.eT_mail);
            passwd = findViewById(R.id.eT_passwd);
            submit = findViewById(R.id.btn_submit);
            signup=findViewById(R.id.btn_submit2);
            layout_mail=findViewById(R.id.layout_mail);
            layout_password=findViewById(R.id.layout_password);

            if (mail_intent != null && password_intent != null && toast_intent != null) {
                mail.setText(mail_intent);
                passwd.setText(password_intent);
                Toast.makeText(LoginActivity.this, toast_intent, Toast.LENGTH_SHORT).show();
                mProgress.dismiss();
            }


            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    layout_mail.setErrorEnabled(false);
                    layout_password.setErrorEnabled(false);
                    loginmail = mail.getText().toString().trim();
                    loginpass = passwd.getText().toString().trim();
                    if (loginmail.isEmpty())
                        layout_mail.setError("This field cannot be empty!");
                    if (loginpass.isEmpty())
                        layout_password.setError("Mandatory field!");
                    else if (!loginmail.isEmpty() && !loginpass.isEmpty()) {
                        loginProgress.setMessage("Logging in...");
                        loginProgress.setCanceledOnTouchOutside(false);
                        loginProgress.show();

                        mAuth.signInWithEmailAndPassword(loginmail, loginpass)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            String errorMessage = task.getException().getMessage();
                                            Toast.makeText(LoginActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                                            passwd.getText().clear();
                                            loginProgress.dismiss();
                                        } else {
                                            mUser = mAuth.getCurrentUser();
                                            if (mUser != null && mUser.isEmailVerified()) {
                                                User_Id = mUser.getUid();
                                                Admins.document(mUser.getUid()).get()
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                if (documentSnapshot.exists()) {
                                                                    loginProgress.dismiss();
                                                                    Intent intent = new Intent(LoginActivity.this, Admin.class);
                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                    startActivity(intent);
                                                                } else {
                                                                    Sub_Admins.document(mUser.getUid()).get()
                                                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                @Override
                                                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                    if (documentSnapshot.exists()) {
                                                                                        loginProgress.dismiss();
                                                                                        Intent intent = new Intent(LoginActivity.this, Sub_Admin.class);
                                                                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                        startActivity(intent);
                                                                                    } else {
                                                                                        requests.document(mUser.getUid())
                                                                                                .get()
                                                                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                                    @Override
                                                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                                        if (documentSnapshot.exists()) {
                                                                                                            loginProgress.dismiss();
                                                                                                            Toast.makeText(LoginActivity.this, "Please wait for the admin to give you access.", Toast.LENGTH_SHORT).show();
                                                                                                            mAuth.signOut();
                                                                                                        } else {
                                                                                                            loginProgress.dismiss();
                                                                                                            Toast.makeText(LoginActivity.this, "Admin has denied to give you access.", Toast.LENGTH_SHORT).show();
                                                                                                            mUser.delete();
                                                                                                            mAuth.signOut();
                                                                                                        }
                                                                                                    }
                                                                                                })
                                                                                                .addOnFailureListener(new OnFailureListener() {
                                                                                                    @Override
                                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                                    }
                                                                                                });

                                                                                    }
                                                                                }
                                                                            })
                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                loginProgress.dismiss();
                                                            }
                                                        });
                                            } else {
                                                loginProgress.dismiss();
                                                Toast.makeText(LoginActivity.this, "Please verify your e-mail.", Toast.LENGTH_SHORT).show();
                                                mAuth.signOut();
                                            }
                                        }
                                    }
                                });
                    }
                }
            });
            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(LoginActivity.this,signup_sub_admin.class);
                    startActivity(intent);
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        Intent exit = new Intent(Intent.ACTION_MAIN);
        exit.addCategory(Intent.CATEGORY_HOME);
        exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(exit);
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }
}
