package cardio_app.activity.drug;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import cardio_app.R;
import cardio_app.databinding.DrugListItemBinding;
import cardio_app.db.DbHelper;
import cardio_app.db.model.Drug;
import cardio_app.viewmodel.DrugViewModel;

public class DrugsActivity extends AppCompatActivity {
    private static final String TAG = DrugsActivity.class.getName();

    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        ListView listView = (ListView) findViewById(R.id.item_list);
        listView.setOnItemClickListener(((adapterView, view, i, l) -> {
            Drug drug = (Drug) adapterView.getItemAtPosition(i);
            Intent intent = new Intent(DrugsActivity.this, AddDrugActivity.class);
            intent.putExtra("drug", drug);
            startActivity(intent);
        }));

        assignDataToListView();
    }

    private void assignDataToListView() {
        try {
            ListView listView = (ListView) findViewById(R.id.item_list);
            Dao<Drug, Integer> dao = getHelper().getDao(Drug.class);
            List<Drug> data = dao.queryForAll();
            listView.setAdapter(new DrugAdapter(data));
            listView.invalidateViews();
        } catch (SQLException e) {
            Log.e(TAG, "Can't get drugs", e);
            throw new RuntimeException(e);
        }
    }

    private void refreshListView() {
        assignDataToListView();
    }

    @Override
    protected void onResume() {
        refreshListView();
        super.onResume();
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

    public void addItem(View view) {
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
            DrugListItemBinding binding;
            if (convertView == null) {
                binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.drug_list_item, parent, false);

                convertView = binding.getRoot();
            } else {
                binding = DataBindingUtil.findBinding(convertView);
            }

            Drug drug = getItem(position);
            binding.setDrug(new DrugViewModel(drug));

            return convertView;
        }
    }
}
