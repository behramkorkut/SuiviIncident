package com.yenimobile.suiviincident;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.yenimobile.suiviincident.baseDeDonne.CustomerDAO;
import com.yenimobile.suiviincident.baseDeDonne.IncidentDAO;
import com.yenimobile.suiviincident.model.Customer;
import com.yenimobile.suiviincident.model.Incident;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by bebeNokiaX6 on 08/09/2017.
 */

public class CustomDialogFragment extends DialogFragment {

    private EditText incidentNameEtxt;
    private EditText incidentModifiedAtEtxt;
    private EditText incidentCreatedAtEtxt;
    private Spinner customerSpinner;
    private LinearLayout submitLayout;

    private Incident incident;

    IncidentDAO incidentDAO;
    ArrayAdapter<Customer> adapter;

    public static final String ARG_ITEM_ID = "emp_dialog_fragment";

    private static final SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.ENGLISH);

    /*
     * Callback used to communicate with EmpListFragment to notify the list adapter.
     * MainActivity implements this interface and communicates with EmpListFragment.
     */
    public interface CustomDialogFragmentListener {
        void onFinishDialog();
    }

    public CustomDialogFragment() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        incidentDAO = new IncidentDAO(getActivity());

        Bundle bundle = this.getArguments();
        incident = bundle.getParcelable("selectedIncident");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

       /* View customDialogView = inflater.inflate(R.layout.fragment_add_incident, null);
        builder.setView(customDialogView);

        incidentNameEtxt = (EditText) customDialogView.findViewById(R.id.etxt_name);
        incidentModifiedAtEtxt = (EditText) customDialogView
                .findViewById(R.id.etxt_salary);
        incidentCreatedAtEtxt = (EditText) customDialogView.findViewById(R.id.etxt_dob);
        customerSpinner = (Spinner) customDialogView
                .findViewById(R.id.spinner_dept);
        submitLayout = (LinearLayout) customDialogView
                .findViewById(R.id.layout_submit);
        submitLayout.setVisibility(View.GONE);
        setValue();

        builder.setTitle(R.string.update_emp);*/
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.update,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            incident.setCreatedAt(formatter.parse(incidentCreatedAtEtxt.getText().toString()));
                            incident.setModifiedAt(formatter.parse(incidentModifiedAtEtxt.getText().toString()));
                        } catch (ParseException e) {
                            Toast.makeText(getActivity(),
                                    "Invalid date format!",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        incident.setName(incidentNameEtxt.getText().toString());
                        Customer customer = (Customer) adapter.getItem(customerSpinner.getSelectedItemPosition());
                        incident.setCustomer(customer);
                        /*long result = incidentDAO.updateIncident(incident);
                        if (result > 0) {
                            MainActivity activity = (MainActivity) getActivity();
                            //activity.onFinishDialog();
                            //dismiss();
                        } else {
                            Toast.makeText(getActivity(),
                                    "Unable to update incident",
                                    Toast.LENGTH_SHORT).show();
                        }*/
                    }
                });
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

        AlertDialog alertDialog = builder.create();

        return alertDialog;
    }

    private void setValue() {
        CustomerDAO customerDAO = new CustomerDAO(getActivity());

        List<Customer> customerList = customerDAO.getAllCustomers();
        adapter = new ArrayAdapter<Customer>(getActivity(),
                android.R.layout.simple_list_item_1, customerList);
        customerSpinner.setAdapter(adapter);
        int pos = adapter.getPosition(incident.getCustomer());

        if (incident != null) {
            incidentNameEtxt.setText(incident.getName());
            incidentModifiedAtEtxt.setText(formatter.format(incident.getModifiedAt()));
            incidentCreatedAtEtxt.setText(formatter.format(incident.getCreatedAt()));
            customerSpinner.setSelection(pos);
        }
    }
}
