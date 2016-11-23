package cardio_app.activity.pdf_creation;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import cardio_app.R;
import cardio_app.activity.statistics.ChartActivity;
import cardio_app.pdf_creation.param_models.BitmapFromChart;
import cardio_app.util.FileWalkerUtil;
import cardio_app.util.PermissionUtil;

public class CollectedChartsActivity extends AppCompatActivity {

    private static final String TAG = CollectedChartsActivity.class.toString();
    private BitmapFromChartDataAdapter bitmapFromChartDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collected_charts);

        ListView listView = (ListView) findViewById(R.id.collected_charts_list_view);

        listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            BitmapFromChart bitmapFromChart = (BitmapFromChart) adapterView.getItemAtPosition(i);
                deleteOnLongClick(bitmapFromChart);
            return true;
        });

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            BitmapFromChart bitmapFromChart = (BitmapFromChart) adapterView.getItemAtPosition(i);
            if (bitmapFromChart.hasFilePathExt()) {
                File file = new File(bitmapFromChart.getFilePathWithExt());
                if (file.exists()){
                    Uri path = Uri.fromFile(file);
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(path, "image/*");
                    startActivity(intent);
                }
            }
        });

        assignDataToListView();
    }

    public void refreshListView() {
        // TODO maybe we could do the same simpler
        assignDataToListView();
    }


    private void deleteOnLongClick(BitmapFromChart chartToDelete) {
        View contextView = findViewById(R.id.activity_collected_charts);
        Snackbar
                .make(contextView, R.string.are_you_sure_to_delete_item, Snackbar.LENGTH_LONG)
                .setAction(R.string.delete, view -> {
                    if (PermissionUtil.isStoragePermissionGranted(this)) {
                        deleteBitmapFromChart(chartToDelete);
                    }
                })
                .setActionTextColor(ContextCompat.getColor(this, R.color.brightOnDarkBg))
                .show();
    }

    private void deleteBitmapFromChart(BitmapFromChart pressureData) {
        if(PermissionUtil.isStoragePermissionGranted(this)) {
            File file = new File(pressureData.getFilePathWithExt());
            if (file.exists()) {
                boolean isDeleted = file.delete();
                if (isDeleted) {
                    Toast.makeText(this, R.string.after_chart_delete, Toast.LENGTH_SHORT).show();
                    refreshListView();
                } else {
                    Log.e(TAG, "deleteBitmapFromChart: file still exists... :" + file.getAbsolutePath());
                }
            }
        }
    }

    private void assignDataToListView(){
        List<BitmapFromChart> dataList = FileWalkerUtil.getBitmapFromChartList_fromSavedDir();
        ListView listView = (ListView) findViewById(R.id.collected_charts_list_view);
        bitmapFromChartDataAdapter = new BitmapFromChartDataAdapter(CollectedChartsActivity.this, dataList);
        listView.setAdapter(bitmapFromChartDataAdapter);
        listView.invalidateViews();
    }

    @Override
    protected void onResume() {
        refreshListView();
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_collected_charts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.collected_charts_add_new: {
                Intent intent = new Intent(this, ChartActivity.class);
                intent.putExtra("collectedChartsInvoked", true);
                startActivity(intent);
                return true;
            }
            case R.id.collected_charts_clear: {
                FileWalkerUtil.deleteAllCollectedCharts();
                refreshListView();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }
}
