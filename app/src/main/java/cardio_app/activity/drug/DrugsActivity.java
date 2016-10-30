package cardio_app.activity.drug;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import cardio_app.R;
import cardio_app.db.DbHelper;
import cardio_app.db.model.Drug;

public class DrugsActivity extends AppCompatActivity {
    private static final String TAG = DrugsActivity.class.getName();

    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drugs);

        ListView listView = (ListView) findViewById(R.id.drug_list);
        listView.setOnItemClickListener(((adapterView, view, i, l) -> {
            Drug drug = (Drug) adapterView.getItemAtPosition(i);

            Intent intent = new Intent(DrugsActivity.this, AddDrugActivity.class);
            intent.putExtra("drug", drug);
            startActivity(intent);
        }));

        try {
            Dao<Drug, Integer> dao = getHelper().getDao(Drug.class);

            List<Drug> data = dao.queryForAll();
            listView.setAdapter(new DrugAdapter(data));
        } catch (SQLException e) {
            Log.e(TAG, "Can't get drugs", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (dbHelper != null) {
            OpenHelperManager.releaseHelper();
            dbHelper = null;
        }
    }

    private DbHelper getHelper() {
        if (dbHelper == null) {
            dbHelper = OpenHelperManager.getHelper(this, DbHelper.class);
        }

        return dbHelper;
    }

    public void addDrug(View view) {
        Intent intent = new Intent(this, AddDrugActivity.class);

        startActivity(intent);
    }

    private class DrugAdapter extends ArrayAdapter<Drug> {

        public DrugAdapter(List<Drug> data) {
            super(DrugsActivity.this, R.layout.drug_list_item, data);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

                convertView = inflater.inflate(R.layout.drug_list_item, parent, false);
            }

            TextView nameText = (TextView) convertView.findViewById(R.id.drug_name);
            TextView descriptionText = (TextView) convertView.findViewById(R.id.drug_description);
            TextView timeText = (TextView) convertView.findViewById(R.id.drug_time);

            Drug drug = getItem(position);

            nameText.setText(drug.getName());
            descriptionText.setText(drug.getDescription());
            timeText.setText("MORNING");

            return convertView;
        }
    }
}
