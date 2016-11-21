package cardio_app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cardio_app.db.model.Alarm;
import cardio_app.db.model.AlarmDrug;
import cardio_app.db.model.DoctorsAppointment;
import cardio_app.db.model.Drug;
import cardio_app.db.model.Emotion;
import cardio_app.db.model.Event;
import cardio_app.db.model.OtherSymptomsRecord;
import cardio_app.db.model.PressureData;
import cardio_app.db.model.TimeUnit;
import temporary_package.InitialPressureData;

public class DbHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = DbHelper.class.getName();
    private static final String DB_NAME = "CardioApp.db";
    private static final int VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    private void initAlarms() throws SQLException {
        Dao<Alarm, Integer> dao = getDao(Alarm.class);

        Alarm morning = new Alarm(8, 0, "MORNING", null);
        Alarm midday = new Alarm(12, 0, "MIDDAY", null);
        Alarm evening = new Alarm(18, 0, "EVENING", null);

        dao.create(morning);
        dao.create(midday);
        dao.create(evening);
    }

    private void initPressureDataTable() throws SQLException {
        // TODO -> to remove, it's just to fill layout with records
        Dao<PressureData, Integer> daoHp = getDao(PressureData.class);
        List<PressureData> hpdatList = InitialPressureData.makePressureDataList();
        for (PressureData hpdat : hpdatList) {
            daoHp.create(hpdat);
        }
    }

    private Date getDate(int day, int month, int year) {
        Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, day);
        return myCalendar.getTime();
    }

    private void initEventData() throws SQLException {
        Dao<OtherSymptomsRecord, Integer> daoOtherS = getDao(OtherSymptomsRecord.class);
        OtherSymptomsRecord osr = new OtherSymptomsRecord(true, true, true, false, false);
        daoOtherS.create(osr);
        Dao<DoctorsAppointment, Integer> daoAppointment = getDao(DoctorsAppointment.class);
        DoctorsAppointment da = new DoctorsAppointment(true, true, true, false, false);
        daoAppointment.create(da);
        Dao<Event, Integer> daoHp = getDao(Event.class);
        Event e1 = new Event(getDate(19, 10, 2016), getDate(19, 11, 2016), false, 0, "description1", null, Emotion.HAPPY, osr, da);
        Event e2 = new Event(getDate(19, 10, 2016), getDate(20, 11, 2016), false,  0, "description2", null, Emotion.SAD, osr, da);
        Event e3 = new Event(getDate(19, 10, 2016), getDate(19, 12, 2016), true,  2, "description3", TimeUnit.WEEK, Emotion.ANGRY, osr, da);
        Event e4 = new Event(new Date(2016, 10, 20, 16, 0), new Date(2016, 10, 30), true, 1, "description4", TimeUnit.DAY, Emotion.CRYING, osr, da);
        daoHp.create(e1);
        daoHp.create(e2);
        daoHp.create(e3);
        daoHp.create(e4);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Alarm.class);
            TableUtils.createTable(connectionSource, Drug.class);
            TableUtils.createTable(connectionSource, AlarmDrug.class);
            TableUtils.createTable(connectionSource, PressureData.class);
            TableUtils.createTable(connectionSource, OtherSymptomsRecord.class);
            TableUtils.createTable(connectionSource, DoctorsAppointment.class);
            TableUtils.createTable(connectionSource, Event.class);
        } catch (SQLException e) {
            Log.e(TAG, "Can't create database", e);
            throw new RuntimeException(e);
        }

        try {
            initAlarms();
            initPressureDataTable();
            initEventData();
        } catch (SQLException e) {
            Log.e(TAG, "Can't insert initial data", e);
            throw new RuntimeException(e);
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }


    public List<PressureData> getAllOrderedPressureData() throws SQLException {
        return getFilteredAndOrderedByDatePressureData(null, null);
    }

    public List<PressureData> getFilteredAndOrderedByDatePressureData(Date dateFrom, Date dateTo) throws SQLException {
        Dao<PressureData, Integer> dao = getDao(PressureData.class);
        return getFilteredByDate(dao.queryBuilder(), "dateTime", false, false, dateFrom, dateTo).query();
    }

    private static Where getFilteredByDate(QueryBuilder queryBuilder, String columnName, boolean isOrderedNow, boolean isAscending, Date dateFrom, Date dateTo) throws SQLException {

        QueryBuilder qb = isOrderedNow ? queryBuilder : queryBuilder.orderBy(columnName, isAscending);

        if (dateFrom != null && dateTo != null) {
            return qb.where().between(columnName, dateFrom, dateTo);
        } else if (dateFrom != null) {
            return qb.where().ge(columnName, dateFrom);
        } else if (dateTo != null) {
            return qb.where().le(columnName, dateTo);
        } else {
            return qb.where().isNotNull(columnName);
        }
    }

    public Date getFirstDateFromPressureDataTable() throws Exception{
        Dao<PressureData, Integer> dao = getDao(PressureData.class);
        // descending date
        PressureData pressureData = (PressureData) getFilteredByDate(dao.queryBuilder(), "dateTime", false, true, null, null).queryForFirst();
        return pressureData.getDateTime();
    }

    public Date getLastDateFromPressureDataTable() throws Exception {
        Dao<PressureData, Integer> dao = getDao(PressureData.class);
        // ascending date
        PressureData pressureData = (PressureData) getFilteredByDate(dao.queryBuilder(), "dateTime", false, false, null, null).queryForFirst();
        return pressureData.getDateTime();
    }

    public JSONObject exportToJson() throws SQLException, JSONException {
        JSONArray drugs = new JSONArray();
        List<Drug> drugList = getDao(Drug.class).queryForAll();
        for (Drug drug : drugList) {
            drugs.put(drug.convertToJson());
        }

        JSONArray pressures = new JSONArray();
        List<PressureData> pressureDataList = getDao(PressureData.class).queryForAll();
        for (PressureData pressure : pressureDataList) {
            pressures.put(pressure.convertToJson());
        }

        JSONObject result = new JSONObject();

        result.put("drugs", drugs);
        result.put("pressures", pressures);

        return result;
    }
}
