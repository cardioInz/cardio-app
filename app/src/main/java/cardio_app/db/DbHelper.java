package cardio_app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import cardio_app.db.model.Alarm;
import cardio_app.db.model.AlarmDrug;
import cardio_app.db.model.Drug;
import cardio_app.db.model.PressureData;
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


    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Alarm.class);
            TableUtils.createTable(connectionSource, Drug.class);
            TableUtils.createTable(connectionSource, AlarmDrug.class);
            TableUtils.createTable(connectionSource, PressureData.class);
        } catch (SQLException e) {
            Log.e(TAG, "Can't create database", e);
            throw new RuntimeException(e);
        }

        try {
            initAlarms();
            initPressureDataTable();
        } catch (SQLException e) {
            Log.e(TAG, "Can't insert initial data", e);
            throw new RuntimeException(e);
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }


    public List<PressureData> getOrderedPressureData() throws SQLException {
        Dao<PressureData, Integer> dao = getDao(PressureData.class);
        return dao.queryBuilder().orderByRaw("dateTime desc").query();
    }

    public List<PressureData> getFilteredAndOrderedByDatePressureData(Date dateFrom, Date dateTo) throws SQLException {
        Dao<PressureData, Integer> dao = getDao(PressureData.class);
        return getFilteredAndOrderedByDate(dao.queryBuilder(), "dateTime", dateFrom, dateTo);
    }

    private static List getFilteredAndOrderedByDate(QueryBuilder queryBuilder, String columnName, Date dateFrom, Date dateTo) throws SQLException {
        return queryBuilder.orderByRaw(columnName + " desc").where().between(columnName, dateFrom, dateTo).query();
    }
}
