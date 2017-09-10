package com.yenimobile.suiviincident.fragmenties;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.yenimobile.suiviincident.CustomDialogFragment;
import com.yenimobile.suiviincident.R;
import com.yenimobile.suiviincident.adapters.IncidentListAdapter;
import com.yenimobile.suiviincident.baseDeDonne.IncidentDAO;
import com.yenimobile.suiviincident.model.Incident;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by bebeNokiaX6 on 08/09/2017.
 */

public class IncidentListFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public static final String ARG_ITEM_ID = "incident_list";

    Activity activity;
    ListView incidentListView;
    ArrayList<Incident> incidentArrayList;

    IncidentListAdapter incidentListAdapter;
    IncidentDAO incidentDAO;

    private GetEmpTask task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        incidentDAO = new IncidentDAO(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_item, container,
                false);
        findViewsById(view);

        task = new GetEmpTask(activity);
        task.execute((Void) null);

        incidentListView.setOnItemClickListener(this);
        incidentListView.setOnItemLongClickListener(this);
        return view;
    }

    private void findViewsById(View view) {
        incidentListView = (ListView) view.findViewById(R.id.listview);
    }

    @Override
    public void onItemClick(AdapterView<?> list, View view, int position,
                            long id) {
        Incident incident = (Incident) list.getItemAtPosition(position);

        if (incident != null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable("selectedIncident", incident);
            CustomDialogFragment customDialogFragment = new CustomDialogFragment();
            customDialogFragment.setArguments(arguments);
            customDialogFragment.show(getFragmentManager(),
                    CustomDialogFragment.ARG_ITEM_ID);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                   int position, long id) {
        Incident incident = (Incident) parent.getItemAtPosition(position);
        // Use AsyncTask to delete from database
        incidentDAO.deleteIncident(incident);
        incidentListAdapter.remove(incident);

        return true;
    }

    public class GetEmpTask extends AsyncTask<Void, Void, ArrayList<Incident>> {

        private final WeakReference<Activity> activityWeakRef;

        public GetEmpTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected ArrayList<Incident> doInBackground(Void... arg0) {
            ArrayList<Incident> incidentArrayList = (ArrayList<Incident>) incidentDAO.getAllIncidents();
            return incidentArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<Incident> incidentArrayList) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
                IncidentListFragment.this.incidentArrayList = incidentArrayList;
                if (incidentArrayList != null) {
                    if (incidentArrayList.size() != 0) {
                        incidentListAdapter = new IncidentListAdapter(activity,
                                incidentArrayList);
                        incidentListView.setAdapter(incidentListAdapter);
                    } else {
                        Toast.makeText(activity, "No Incident Records",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    /*
     * This method is invoked from MainActivity onFinishDialog() method. It is
     * called from CustomEmpDialogFragment when an incident record is updated.
     * This is used for communicating between fragments.
     */
    public void updateView() {
        task = new GetEmpTask(activity);
        task.execute((Void) null);
    }

    @Override
    public void onResume() {
        //getActivity().setTitle(R.string.app_name);
        //getActivity().getActionBar().setTitle(R.string.app_name);
        super.onResume();
    }
}
