package cardio_app.activity.statistics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import cardio_app.R;
import cardio_app.activity.filter.FilterActivity;
import cardio_app.activity.pdf_creation.CreatePdfReportActivity;
import cardio_app.filtering.DataFilter;
import cardio_app.util.Defaults;
import cardio_app.util.PermissionUtil;

public class StatisticsActivity extends AppCompatActivity {

    private DataFilter dataFilter = Defaults.getDefaultDataFilter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
    }

    public void moveToActivity(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.show_counter_stats_btn:
                Intent intentC = new Intent(this, StatisticsCounterActivity.class);
                intentC.putExtra("filterdata", dataFilter);
                startActivity(intentC);
                break;
            case R.id.show_last_measurements_stats_btn:
                Intent intentL = new Intent(this, StatisticsLastMeasurementsActivity.class);
                intentL.putExtra("filterdata", dataFilter);
                startActivity(intentL);
                break;
            case R.id.show_charts_btn:
                onChartClick();
                break;
            case R.id.create_pdf_btn:
                if (PermissionUtil.isStoragePermissionGranted(this)) {
                    Intent intentP = new Intent(this, CreatePdfReportActivity.class);
                    intentP.putExtra("filterdata", dataFilter);
                    startActivityForResult(intentP, 1);
                }
                break;
            default:
                break;
        }
    }


    public void refreshStatisticsView() {
        // if here will be some content that needs refresh
    }

    @Override
    protected void onResume() {
        refreshStatisticsView();
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filter_data, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_item_chart);
        menuItem.setEnabled(false);
        menuItem.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_filter_data: {
                onFilterDataClick();
                return true;
            }
            case R.id.menu_item_chart: {
                onChartClick();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void onFilterDataClick() {
        Intent intent = new Intent(this, FilterActivity.class);
        intent.putExtra("filterdata", dataFilter);
        startActivityForResult(intent, 1);
    }

    private void onChartClick() {
        Intent intent = new Intent(this, ChartActivity.class);
        intent.putExtra("filterdata", dataFilter);
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                DataFilter dataFilter = data.getParcelableExtra("filterdata");
                if (dataFilter != null)
                    this.dataFilter = dataFilter;
                refreshStatisticsView();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                refreshStatisticsView();
            }
        }
    }
}
