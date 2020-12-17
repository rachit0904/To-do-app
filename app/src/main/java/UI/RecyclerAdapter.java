package UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.sample.to_do.R;

import java.util.List;

import Data.DatabaseHandler;
import Modal.TaskItems;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private Context context;
    private List<TaskItems> tasks;
    public RecyclerAdapter(Context context,List tasks){
        this.context=context;
        this.tasks=tasks;
    }
    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.task_row,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        TaskItems items=tasks.get(position);
        holder.taskitem.setText(items.getTask());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public RadioButton taskitem;
        public int id;
        public ImageButton priority,trash;
        public ViewHolder(@NonNull View v,Context ctx) {
            super(v);
            context=ctx;
            taskitem=(RadioButton) v.findViewById(R.id.taskDisp);
            priority=(ImageButton) v.findViewById(R.id.priorityBtn);
            trash=(ImageButton) v.findViewById(R.id.trashBtn);
            priority.setOnClickListener(this);
            trash.setOnClickListener(this);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskDetails();
                }
            });
        }

        private void taskDetails() {

        }

        @Override
        public void onClick(View v) {
                switch (v.getId()){
                    case R.id.priorityBtn:{
                        priority.setImageResource(R.drawable.filled_star);
                        break;
                    }
                    case R.id.trashBtn: {
                        TaskItems tskItem = tasks.get(getAdapterPosition());
                        deleteTask(tskItem.getTask());
                        break;
                    default: {
                        break;
                       }
                    }
                }
        }
        public void deleteTask(String tsk){
            DatabaseHandler db=new DatabaseHandler(context);
            db.deleteTask(tsk);
            tasks.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
        }
    }
}
