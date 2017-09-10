package com.yenimobile.suiviincident.fragmenties;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.yenimobile.suiviincident.R;
import com.yenimobile.suiviincident.baseDeDonne.CustomerDAO;
import com.yenimobile.suiviincident.baseDeDonne.IncidentDAO;
import com.yenimobile.suiviincident.model.Customer;
import com.yenimobile.suiviincident.model.Incident;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by bebeNokiaX6 on 08/09/2017.
 */

public class IncidentAddFragment extends Fragment implements View.OnClickListener {

    private EditText incidentNameEtxt;
    private EditText incidentModifiedEtxt;
    private EditText incidentCreatedEtxt;
    private Spinner customerSpinner;
    private Button addButton;
    private Button resetButton;

    private static final SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.ENGLISH);

    DatePickerDialog datePickerDialog;
    Calendar dateCalendar;

    Incident incident = null;
    private IncidentDAO incidentDAO;
    private CustomerDAO customerDAO;
    private GetDeptTask task;
    private AddEmpTask addEmpTask;

    public static final String ARG_ITEM_ID = "incident_add_fragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        incidentDAO = new IncidentDAO(getActivity());
        customerDAO = new CustomerDAO(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_item, container,
                false);

        findViewsById(rootView);

        setListeners();

        // Used for orientation change
        /*
         * After entering the fields, change the orientation.
         * NullPointerException occurs for date. This piece of code avoids it.
         */
        if (savedInstanceState != null) {
            dateCalendar = Calendar.getInstance();
            if (savedInstanceState.getLong("dateCalendar") != 0)
                dateCalendar.setTime(new Date(savedInstanceState
                        .getLong("dateCalendar")));
        }

        // asynchronously retrieves department from table and sets it in Spinner
        task = new GetDeptTask(getActivity());
        task.execute((Void) null);

        return rootView;
    }

    private void setListeners() {
        incidentCreatedEtxt.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        dateCalendar = Calendar.getInstance();
                        dateCalendar.set(year, monthOfYear, dayOfMonth);
                        incidentCreatedEtxt.setText(formatter.format(dateCalendar
                                .getTime()));
                    }

                }, newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));

        addButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
    }

    protected void resetAllFields() {
        incidentNameEtxt.setText("");
        incidentModifiedEtxt.setText("");
        incidentCreatedEtxt.setText("");
        if (customerSpinner.getAdapter().getCount() > 0)
            customerSpinner.setSelection(0);
    }

    private void setEmployee() {
        incident = new Incident();
        incident.setName(incidentNameEtxt.getText().toString());
        if (dateCalendar != null) {
            incident.setCreatedAt(dateCalendar.getTime());
            incident.setModifiedAt(dateCalendar.getTime());
        }
        Customer selectedCust = (Customer) customerSpinner.getSelectedItem();
        incident.setCustomer(selectedCust);
    }

    @Override
    public void onResume() {
        //getActivity().setTitle(R.string.add_emp);
        //getActivity().getActionBar().setTitle(R.string.add_emp);
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (dateCalendar != null)
            outState.putLong("dateCalendar", dateCalendar.getTime().getTime());
    }

    private void findViewsById(View rootView) {
        /*incidentNameEtxt = (EditText) rootView.findViewById(R.id.etxt_salary);
        incidentModifiedEtxt = (EditText) rootView.findViewById(R.id.etxt_salary);
        incidentCreatedEtxt = (EditText) rootView.findViewById(R.id.etxt_dob);
        incidentCreatedEtxt.setInputType(InputType.TYPE_NULL);

        customerSpinner = (Spinner) rootView.findViewById(R.id.spinner_dept);
        addButton = (Button) rootView.findViewById(R.id.button_add);
        resetButton = (Button) rootView.findViewById(R.id.button_reset);*/
    }

    @Override
    public void onClick(View view) {
        if (view == incidentCreatedEtxt) {
            datePickerDialog.show();
        } else if (view == addButton) {
            setEmployee();
            addEmpTask = new AddEmpTask(getActivity());
            addEmpTask.execute((Void) null);
        } else if (view == resetButton) {
            resetAllFields();
        }
    }

    public class GetDeptTask extends AsyncTask<Void, Void, Void> {

        private final WeakReference<Activity> activityWeakRef;
        private List<Customer> customerList;

        public GetDeptTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            customerList = customerDAO.getAllCustomers();
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {

                ArrayAdapter<Customer> adapter = new ArrayAdapter<Customer>(
                        activityWeakRef.get(),
                        android.R.layout.simple_list_item_1, customerList);
                customerSpinner.setAdapter(adapter);

                addButton.setEnabled(true);
            }
        }
    }

    public class AddEmpTask extends AsyncTask<Void, Void, Long> {

        private final WeakReference<Activity> activityWeakRef;

        public AddEmpTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected Long doInBackground(Void... arg0) {
            long result = 2017; //incidentDAO.save(incident);
            return result;
        }

        @Override
        protected void onPostExecute(Long result) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
                if (result != -1)
                    Toast.makeText(activityWeakRef.get(), "Incident  Saved",
                            Toast.LENGTH_LONG).show();
            }
        }
    }





}
