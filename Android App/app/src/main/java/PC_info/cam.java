package PC_info;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.system_stats.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;

public class cam extends AppCompatActivity {

    private ImageView cam_full;

    private PhotoViewAttacher photoViewAttacher;

    private ProgressDialog mProgress;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private DocumentReference lab = db.collection("LAB").document("LAB");
    private DocumentReference pc;

    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);

        cam_full=findViewById(R.id.cam_full);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Processing...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();


        Intent intentThatStartedThisActivity = getIntent();
        String PC = intentThatStartedThisActivity.getStringExtra("pc");
        String Lab = intentThatStartedThisActivity.getStringExtra("lab");

        if (PC != null && Lab != null) {
            pc = lab.collection(Lab.toLowerCase()).document(PC.toLowerCase());
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pc.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                path = (String) documentSnapshot.get("pathCam");
                                if (path != null && !path.equals("")) {
                                    Picasso.get().load(path).into(cam_full);
                                    photoViewAttacher = new PhotoViewAttacher(cam_full);
                                    photoViewAttacher.setZoomable(true);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(cam.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                if(mProgress.isShowing())
                    mProgress.dismiss();
            }
        }, 10000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mProgress.dismiss();
    }
}
