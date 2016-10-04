package cardio_app.factories;


import android.support.v7.app.AppCompatActivity;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cardio_app.structures.HealthRecord;

public class TableRowFactory {
    private static TableRowFactory ourInstance = new TableRowFactory();

    public static TableRowFactory getInstance() {
        return ourInstance;
    }

    private TableRowFactory() {
    }


    public List<TableRow> makeTableRowListFromTableRecordList(List<HealthRecord> healthList, AppCompatActivity activity) {
        List<TableRow> list = new ArrayList<>();
        for (HealthRecord healthRecord : healthList) {
            TableRow row = makeRowFromRecord(healthRecord, activity);
            list.add(row);
        }
        // TODO sort list by date
        return list;
    }

    private TableRow makeRowFromRecord(HealthRecord record, AppCompatActivity activity) {

        // create map with all columns (from TableRowColumnEnum)
        Map<TableRowColumnEnum, TextView> map = new HashMap<>();
        for (TableRowColumnEnum c : TableRowColumnEnum.values()) {
            map.put(c, new TextView(activity));
        }

        // use map to add all fields
        map.get(TableRowColumnEnum.SYSTOL).setText(record.getSystolStr());
        map.get(TableRowColumnEnum.DIASTOL).setText(record.getDiastolStr());
        map.get(TableRowColumnEnum.CONDITION).setText(record.getConditionStr());
        map.get(TableRowColumnEnum.PULSE).setText(record.getPulseStr());
        map.get(TableRowColumnEnum.ARYTHMIA).setText(record.getArrhythmiaStr());
        map.get(TableRowColumnEnum.DATE).setText(record.getDateStr());

        // create row
        TableRow row = new TableRow(activity);
        row.addView(map.get(TableRowColumnEnum.SYSTOL));
        row.addView(map.get(TableRowColumnEnum.DIASTOL));
        row.addView(map.get(TableRowColumnEnum.CONDITION));
        row.addView(map.get(TableRowColumnEnum.PULSE));
        row.addView(map.get(TableRowColumnEnum.ARYTHMIA));
        row.addView(map.get(TableRowColumnEnum.DATE));

        return row;
    }
}
