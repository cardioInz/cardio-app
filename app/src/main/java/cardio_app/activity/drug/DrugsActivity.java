package cardio_app.activity.drug;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import cardio_app.R;
import cardio_app.db.DbHelper;
import cardio_app.db.model.Drug;
import cardio_app.db.model.PressureData;
import cardio_app.util.ChartBuilder;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

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

//        LineChartView view = new LineChartView(this);
//        view.setZoomType(ZoomType.HORIZONTAL);
//
//        try {
//            List<PressureData> pressureDataList = getHelper().getDao(PressureData.class).queryForAll();
//            ChartBuilder builder = new ChartBuilder(pressureDataList, getResources());
//            LineChartData data = builder.setMode(ChartBuilder.ChartMode.DISCRETE).build();
//            view.setLineChartData(data);
//            Viewport viewport = view.getCurrentViewport();
//            view.setZoomLevel(viewport.centerX(), viewport.centerY(), builder.getDays() / 4);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//
//        int sizePixels = 1080;
//        Bitmap result = Bitmap.createBitmap(sizePixels, sizePixels, Bitmap.Config.ARGB_8888);
//        result.eraseColor(Color.WHITE);
//        Canvas c = new Canvas(result);
//        int sizeSpec = View.MeasureSpec.makeMeasureSpec(sizePixels, View.MeasureSpec.EXACTLY);
//        view.measure(sizeSpec, sizeSpec);
//        int width = view.getMeasuredWidth();
//        int height = view.getMeasuredHeight();
//        view.layout(0, 0, width, height);
//        view.setBackgroundColor(Color.YELLOW);
//
//        view.draw(c);
//
//        FileOutputStream out = null;
//        try {
//            out = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/plik.png");
//            result.compress(Bitmap.CompressFormat.PNG, 100, out);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } finally {
//            if (out != null) {
//                try {
//                    out.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
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
