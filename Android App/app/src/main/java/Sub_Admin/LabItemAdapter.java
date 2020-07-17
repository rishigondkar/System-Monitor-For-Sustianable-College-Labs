package Sub_Admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.system_stats.R;

import java.util.ArrayList;
import java.util.List;

public class LabItemAdapter extends RecyclerView.Adapter<LabItemAdapter.ViewHolder> {

    private List<sub_admin_Lab_model_class> model_classList;
    private OnItemClickListener listener;


    public LabItemAdapter(List<sub_admin_Lab_model_class> model_classList) {
        this.model_classList = model_classList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_admin_lab_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LabItemAdapter.ViewHolder holder, int position) {
        holder.pcNo.setText(model_classList.get(position).getPCno());

    }

    @Override
    public int getItemCount() {
        return model_classList.size();
    }

    public void filterList(List<sub_admin_Lab_model_class> filteredList){
        model_classList=filteredList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView pcNo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pcNo=itemView.findViewById(R.id.PCno);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && listener!=null){
                        listener.onClick(model_classList.get(position).getPCno(),position);
                    }
                }
            });
        }
    }
    public interface OnItemClickListener{
        void onClick(String PCno,int position);

    }

    public void setOnItemClickLIstener(OnItemClickListener listener){
        this.listener=listener;
    }
}