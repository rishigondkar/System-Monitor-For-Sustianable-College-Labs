package Admin.accepted;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.system_stats.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import Admin.pending.model_class;

public class accepteditemAdapter extends FirestoreRecyclerAdapter<model_class, accepteditemAdapter.itemHolder> {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference Admins = db.collection("Admins");
    private OnItemClickListener listener;

    public accepteditemAdapter(@NonNull FirestoreRecyclerOptions<model_class> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull itemHolder holder, int position, @NonNull model_class model) {
        holder.name.setText(model.getName());
        holder.mail.setText(model.getEmail());
        holder.labNo.setText(model.getLabNo());
    }

    @NonNull
    @Override
    public itemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_accepted_layout, parent, false);
        return new itemHolder(view);
    }


    class itemHolder extends RecyclerView.ViewHolder {

        private TextView name, mail, labNo;
        ImageButton delete;


        public itemHolder(@NonNull final View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            mail = itemView.findViewById(R.id.mail);
            labNo = itemView.findViewById(R.id.lab_no);
            delete = itemView.findViewById(R.id.delete);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onDeleteClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onDeleteClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickLIstener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
