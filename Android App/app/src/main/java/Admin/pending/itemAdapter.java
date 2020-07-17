package Admin.pending;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.system_stats.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class itemAdapter extends FirestoreRecyclerAdapter<model_class, itemAdapter.itemHolder> {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference Admins = db.collection("Admins");
    private OnItemClickListener listener;

    public itemAdapter(@NonNull FirestoreRecyclerOptions<model_class> options) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new itemHolder(view);
    }


    class itemHolder extends RecyclerView.ViewHolder {

        private TextView name, mail, labNo;
        private RadioButton yes, no;

        public itemHolder(@NonNull final View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            mail = itemView.findViewById(R.id.mail);
            labNo = itemView.findViewById(R.id.lab_no);
            yes = itemView.findViewById(R.id.yes);
            no = itemView.findViewById(R.id.no);

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && listener!=null){
                        listener.onYesClick(getSnapshots().getSnapshot(position), position);
                        yes.setChecked(false);
                    }
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && listener!=null){
                        listener.onNoClick(getSnapshots().getSnapshot(position), position);
                        no.setChecked(false);
                    }
                }
            });
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int position=getAdapterPosition();
//                    if(position!=RecyclerView.NO_POSITION && listener!=null){
//                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
//                    }
//                }
//            });
        }
    }

    public interface OnItemClickListener{
        void onYesClick(DocumentSnapshot documentSnapshot, int position);

        void onNoClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickLIstener(OnItemClickListener listener){
        this.listener=listener;
    }
}
