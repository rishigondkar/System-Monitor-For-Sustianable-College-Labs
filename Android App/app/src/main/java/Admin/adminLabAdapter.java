package Admin;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.system_stats.R;

import java.util.List;

public class adminLabAdapter extends RecyclerView.Adapter<adminLabAdapter.ViewHolder> {

    private List<admin_lab_model_class> model_classList;
    private OnItemClickListener listener;


    public adminLabAdapter(List<admin_lab_model_class> model_classList) {
        this.model_classList = model_classList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_lab_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.labNo.setText(model_classList.get(position).getLabNo());
    }

    @Override
    public int getItemCount() {
        return model_classList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView labNo;
        private LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            labNo=itemView.findViewById(R.id.LabNo);
            linearLayout=itemView.findViewById(R.id.LL);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && listener!=null){
                        listener.onClick(model_classList.get(position).getLabNo(),position);
                    }
                }
            });
        }
    }
    public interface OnItemClickListener{
        void onClick(String LabNo,int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
}