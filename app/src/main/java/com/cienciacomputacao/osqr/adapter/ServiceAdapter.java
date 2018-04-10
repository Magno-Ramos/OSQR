package com.cienciacomputacao.osqr.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cienciacomputacao.osqr.R;
import com.cienciacomputacao.osqr.custom.TextView;
import com.cienciacomputacao.osqr.model.Service;

import java.text.NumberFormat;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {

    private List<Service> services;
    private LayoutInflater layoutInflater;
    private OnDeleteItemClickListener onDeleteItemListener;
    private boolean blocked;

    public ServiceAdapter(Context context, List<Service> services, OnDeleteItemClickListener onDeleteItemListener, boolean blocked) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.services = services;
        this.onDeleteItemListener = onDeleteItemListener;
        this.blocked = blocked;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate (blocked ? R.layout.item_service_blocked : R.layout.item_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Service service = services.get(position);

        String value = service.getValue();
        if (!service.getValue().isEmpty()){
            try{
                int v = Integer.parseInt(service.getValue());
                NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
                value = numberFormat.format(v);
            }catch (Exception e){
                value = service.getValue();
            }
        }

        holder.tvService.setText(service.getService());
        holder.tvAdditional.setText(service.getAdditional());
        holder.tvValue.setText(value);
        holder.btDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteItemListener != null) {
                    onDeleteItemListener.onDeleteItem(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return services.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvService, tvAdditional, tvValue;
        View btDeleteItem;

        ViewHolder(View itemView) {
            super(itemView);
            tvService = itemView.findViewById(R.id.tvServiceName);
            tvAdditional = itemView.findViewById(R.id.tvServiceAdditional);
            tvValue = itemView.findViewById(R.id.tvServiceValue);
            btDeleteItem = itemView.findViewById(R.id.btDeleteItem);
        }
    }

    public interface OnDeleteItemClickListener {
        void onDeleteItem(int position);
    }
}
