package com.yenimobile.suiviincident.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yenimobile.suiviincident.R;
import com.yenimobile.suiviincident.model.Incident;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by bebeNokiaX6 on 08/09/2017.
 */

public class IncidentListAdapter extends ArrayAdapter<Incident> {





    private Context context;
    List<Incident> incidentList;

    private static final SimpleDateFormat formatter = new SimpleDateFormat(
            "dd-MM-yyyy", Locale.getDefault());

    public IncidentListAdapter(Context context, List<Incident> incidentList) {
        super(context, R.layout.list_item, incidentList);
        this.context = context;
        this.incidentList = incidentList;
    }

    /**
     * View holder class just in case of Fragment use
     *
     */
    private class ViewHolder {
        TextView incidentIdTxt;
        TextView incidentNameTxt;
        TextView incidentCreateAtTxt;
        TextView incidentModifiedAtTxt;
        TextView incidentCustomerNameTxt;
        TextView incidentIsInProgressTxt;

        Button btnOpen;
    }

    @Override
    public int getCount() {
        return incidentList.size();
    }

    @Override
    public Incident getItem(int position) {
        return incidentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();

            holder.incidentIdTxt = (TextView) convertView
                    .findViewById(R.id.txt_incident_id);
            holder.incidentNameTxt = (TextView) convertView
                    .findViewById(R.id.txt_incident_name);
            holder.incidentCreateAtTxt = (TextView) convertView
                    .findViewById(R.id.txt_incident_createdAt);
            holder.incidentModifiedAtTxt = (TextView) convertView
                    .findViewById(R.id.txt_incident_modifiedAt);
            holder.incidentCustomerNameTxt = (TextView) convertView
                    .findViewById(R.id.txt_incident_customer_name);
            holder.incidentIsInProgressTxt = (TextView) convertView
                    .findViewById(R.id.txt_incident_inProgress);

            holder.btnOpen = (Button) convertView.findViewById(R.id.btn_open);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Incident incident = (Incident) getItem(position);
        holder.incidentIdTxt.setText(String.valueOf(incident.getId()));
        holder.incidentNameTxt.setText(incident.getName());
        holder.incidentCreateAtTxt.setText(formatter.format(incident.getCreatedAt()));
        holder.incidentModifiedAtTxt.setText(formatter.format(incident.getModifiedAt()));
        holder.incidentIsInProgressTxt.setText(String.valueOf(incident.isInProgress()));
        holder.incidentCustomerNameTxt.setText(incident.getCustomer().getName());

        holder.btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "The details fragment is about to be opened",
                        Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    @Override
    public void add(Incident incident) {
        incidentList.add(incident);
        notifyDataSetChanged();
        super.add(incident);
    }

    @Override
    public void remove(Incident incident) {
        incidentList.remove(incident);
        notifyDataSetChanged();
        super.remove(incident);
    }
}
