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


public class ServiceSmallAdapter extends RecyclerView.Adapter<ServiceSmallAdapter.ViewHolder> {

    private List<Service> services;
    private LayoutInflater layoutInflater;

    public ServiceSmallAdapter(Context context, List<Service> services) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.services = services;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate (R.layout.item_service_small, parent, false);
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
    }

    @Override
    public int getItemCount() {
        return services.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvService, tvAdditional, tvValue;

        ViewHolder(View itemView) {
            super(itemView);
            tvService = itemView.findViewById(R.id.tvServiceName);
            tvAdditional = itemView.findViewById(R.id.tvServiceAdditional);
            tvValue = itemView.findViewById(R.id.tvServiceCount);
        }
    }
}

