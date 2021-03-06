package com.yenimobile.suiviincident;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yenimobile.suiviincident.adapters.IncidentListAdapter;
import com.yenimobile.suiviincident.adapters.SpinnerOptionsAdapter;
import com.yenimobile.suiviincident.baseDeDonne.CustomerDAO;
import com.yenimobile.suiviincident.baseDeDonne.IncidentDAO;
import com.yenimobile.suiviincident.model.Customer;
import com.yenimobile.suiviincident.model.Incident;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView headerText;
    private FloatingActionButton fabAddincident;
    public static final String ARG_ITEM_ID = "incident_list";
    private ListView incidentListView;
    private ArrayList<Incident> incidentArrayList;
    private IncidentListAdapter adapter;
    private IncidentDAO incidentDAO;
    private CustomerDAO customerDAO;
    private Button btnDisplay;
    private Spinner optionSpinner;
    private SpinnerOptionsAdapter spinnerAdapter;
    private String mSelectedOption;
    private GetIncidentTask task;
    private FirstLaunchManager firstLaunchManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        incidentDAO = new IncidentDAO(this);
        customerDAO = new CustomerDAO(this);
        firstLaunchManager = new FirstLaunchManager(this);
        if(firstLaunchManager.isFirstTimeLaunch()){
            Log.e("Mainnnnnnn", "this is the first launch ");
            //we insert some dummy data
            insertDummyData();
            firstLaunchManager.setFirstTimeLaunch(false);
        }

        List<String> displayOptions = insertListOfDisplayOptions();
        if (displayOptions != null){
            optionSpinner = (Spinner) findViewById(R.id.spinnerDisplayOptions);
            spinnerAdapter = new SpinnerOptionsAdapter(this, displayOptions);
            optionSpinner.setAdapter(spinnerAdapter);
            optionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    mSelectedOption = (String) spinnerAdapter.getItem(i);

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            btnDisplay = (Button) findViewById(R.id.btnDisplay);
            btnDisplay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mSelectedOption == (String) spinnerAdapter.getItem(0)){
                        incidentArrayList = (ArrayList<Incident>) incidentDAO.getAllInProgressIncidents();
                        adapter = new IncidentListAdapter(MainActivity.this, incidentArrayList);
                        adapter.notifyDataSetChanged();
                        incidentListView.setAdapter(adapter);
                    }
                }
            });
        }



        incidentArrayList = (ArrayList<Incident>) incidentDAO.getAllIncidents();
        adapter = new IncidentListAdapter(this, incidentArrayList);
        incidentListView = (ListView) findViewById(R.id.listview);
        incidentListView.setClickable(true);
        incidentListView.setAdapter(adapter);

        incidentListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Incident itemIncident = adapter.getItem(i);
                adapter.remove(itemIncident);
                incidentDAO.deleteIncident(itemIncident);
                return false;
            }
        });

        fabAddincident = (FloatingActionButton) findViewById(R.id.fab);
        //task = new GetIncidentTask(this);
        //task.execute((Void) null);




    }


    public class GetIncidentTask extends AsyncTask<Void, Void, ArrayList<Incident>> {

        private final WeakReference<Activity> activityWeakRef;

        public GetIncidentTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected ArrayList<Incident> doInBackground(Void... arg0) {
            ArrayList<Incident> incidentArrayListBackground = (ArrayList<Incident>) incidentDAO.getAllIncidents();
            return incidentArrayListBackground;
        }

        @Override
        protected void onPostExecute(ArrayList<Incident> incidentArrayList) {
            if (activityWeakRef.get() != null && !activityWeakRef.get().isFinishing()) {

                MainActivity.this.incidentArrayList = incidentArrayList;
                if (incidentArrayList != null) {
                    if (incidentArrayList.size() != 0) {
                        adapter = new IncidentListAdapter(MainActivity.this, incidentArrayList);
                        incidentListView.setAdapter(adapter);
                        incidentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                adapter.remove(adapter.getItem(i));
                            }
                        });
                    } else {
                        Toast.makeText(MainActivity.this, "No Incident Records", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }




    private void insertDummyData(){

        String[] inC = {"facture impayée", "contrat non reçu", "paiment non reçu"};
        Date currentDate = new Date();

        Customer customer0 = customerDAO.createCustomer("Dupont", "Jean");
        Customer customer1 = customerDAO.createCustomer("Martin", "jean");
        Customer customer2 = customerDAO.createCustomer("Billou", "ETS");
        Customer customer3 = customerDAO.createCustomer("Vieux", "ETS");

        Incident incident0 = incidentDAO.createIncident(inC[0], currentDate, currentDate, true, customer0.getId());
        Incident incident1 = incidentDAO.createIncident(inC[1], currentDate, currentDate, true, customer1.getId());
        Incident incident2 = incidentDAO.createIncident(inC[2], currentDate, currentDate, false, customer2.getId());
        Incident incident3 = incidentDAO.createIncident(inC[0], currentDate, currentDate, true, customer3.getId());

    }

    private List<String> insertListOfDisplayOptions(){
        List<String> options = new ArrayList<String>();
        options.add("display inProgress incidents only");
        options.add("display all incidents");
        options.add("erase all incidents");

        return options;
    }






}
