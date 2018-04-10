package com.cienciacomputacao.osqr.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cienciacomputacao.osqr.R;
import com.cienciacomputacao.osqr.model.Register;

import java.util.List;

public class RegisterAdapter extends RecyclerView.Adapter<RegisterAdapter.ViewHolder> {

    private RegisterClickListener registerClickListener;
    private List<Register> registers;
    private LayoutInflater inflater;

    public RegisterAdapter(Context context, List<Register> registers, RegisterClickListener registerClickListene) {
        this.registers = registers;
        this.registerClickListener = registerClickListene;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_register, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Register register = registers.get(position);
        holder.tvDate.setText(register.getDateTime());
        holder.tvClient.setText(register.getClient());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (registerClickListener != null){
                    registerClickListener.onClick(register);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return registers.size();
    }

    public interface RegisterClickListener{
        void onClick(Register register);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate;
        TextView tvClient;

        ViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvClient = itemView.findViewById(R.id.tvClient);
        }
    }
}
