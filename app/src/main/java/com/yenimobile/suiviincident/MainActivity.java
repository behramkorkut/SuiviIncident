package com.yenimobile.suiviincident;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yenimobile.suiviincident.baseDeDonne.DatabaseHandler;
import com.yenimobile.suiviincident.model.Customer;
import com.yenimobile.suiviincident.model.Incident;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseHandler db;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        /* for testing purpose we populate the database with dummy data */

        db = new DatabaseHandler(this);

        //we create the customers for testing
        Customer cust1 = new Customer("Dupont", "Jean");
        Customer cust2 = new Customer("Martin", "Jean");
        Customer cust3 = new Customer("ETS Billou", "SARL");
        Customer cust4 = new Customer("ETS Vieux", "SA");

        //we insert customers into the database
        long cust1_id = db.createCustomer(cust1);
        long cust2_id = db.createCustomer(cust2);
        long cust3_id = db.createCustomer(cust3);
        long cust4_id = db.createCustomer(cust4);

        Log.d("customers Count", "customers Count is:::: " + db.getAllCustomers().size());

        //we create the incidents
        Incident inc1 = new Incident("facture impayée", "05/09/17", "05/09/17");
        Incident inc2 = new Incident("contrat non reçu", "12/07/17", "15/07/17");
        Incident inc3 = new Incident("paiement non reçu", "11/07/17", "11/07/17");
        Incident inc4 = new Incident("facture impayée", "01/02/17", "09/03/17");


        //inserting incidents into db
        //with the customer reliated to it
        long inc1_id = db.createIncident(inc1, new long[] { cust1_id });
        long inc2_id = db.createIncident(inc2, new long[] { cust2_id });
        long inc3_id = db.createIncident(inc3, new long[] { cust3_id });
        long inc4_id = db.createIncident(inc4, new long[] { cust4_id });

        //we list all the customers
        List<Customer> allCustomers = db.getAllCustomers();
        for (Customer customer : allCustomers) {
            Log.d("Customer Name", customer.getName());
        }


        //we list all the incidents
        List<Incident> allIncidents = db.getAllIncidents();
        for(Incident incident : allIncidents){
            Log.d("Incident Name", incident.getName());
        }




        /* initiate and populate the listview */
        MyAdapter adapter = new MyAdapter();
        ListView listView = (ListView) findViewById(R.id.listView);


        //we add incidents to the list vew
        for(Incident incident : allIncidents){
            adapter.addIncident(incident);
        }

        listView.setAdapter(adapter);



        //closing db connexion
        db.closeDB();




    }


    public class MyAdapter extends BaseAdapter{

        ArrayList<Incident> incidentArrayList = new ArrayList<Incident>();

        public void addIncident(Incident incident){
            incidentArrayList.add(incident);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return incidentArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return incidentArrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view == null){
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.list_item, viewGroup, false);
            }

            Incident tempIncident = incidentArrayList.get(i);

            TextView textName = (TextView) view.findViewById(R.id.text_name);
            TextView textCustomer = (TextView) view.findViewById(R.id.text_client);
            TextView textCreatedAt = (TextView) view.findViewById(R.id.text_createdAt);
            TextView textModifiedAt = (TextView) view.findViewById(R.id.text_modifiedAt);
            Button btnOpen = (Button) view.findViewById(R.id.btnOpen);
            btnOpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), "action available soon", Toast.LENGTH_SHORT).show();
                }
            });

            textName.setText(tempIncident.getName());
            textCreatedAt.setText(tempIncident.getCreatedAt());
            textModifiedAt.setText(tempIncident.getModifiedAt());

            return view;
        }
    }


}
