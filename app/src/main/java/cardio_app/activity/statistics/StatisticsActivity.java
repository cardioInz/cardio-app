package cardio_app.activity.statistics;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import cardio_app.R;
import cardio_app.activity.filter.FilterActivity;
import cardio_app.filtering.DataFilter;
import cardio_app.filtering.DataFilterModeEnum;

public class StatisticsActivity extends AppCompatActivity {

    private static final DataFilterModeEnum DEFAULT_DATA_FILTER = DataFilterModeEnum.NO_FILTER;
    private DataFilter dataFilter = new DataFilter(DEFAULT_DATA_FILTER);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);


    }

    public void moveToActivity(View view) {
        int id = view.getId();

        switch (id){
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
                Intent intentP = new Intent(this, CreatePdfReportActivity.class);
                intentP.putExtra("filterdata", dataFilter);
                startActivity(intentP);
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_data: {
                onFilterDataClick();
                return true;
            }
            case R.id.chart: {
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
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                DataFilter dataFilter = data.getParcelableExtra("filterdata");
                if (dataFilter != null)
                    this.dataFilter = dataFilter;
                Toast.makeText(this, this.dataFilter.getFilterMsg(), Toast.LENGTH_LONG).show(); // TODO remove it
                refreshStatisticsView();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
//                dataFilter.setMode(DEFAULT_DATA_FILTER);
                refreshStatisticsView();
            }
        }
    }
}
