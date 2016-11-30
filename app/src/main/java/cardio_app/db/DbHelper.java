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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import cardio_app.db.model.BaseModel;
import cardio_app.db.model.DailyActivitiesRecord;
import cardio_app.db.model.DoctorsAppointment;
import cardio_app.db.model.Drug;
import cardio_app.db.model.Emotion;
import cardio_app.db.model.Event;
import cardio_app.db.model.OtherSymptomsRecord;
import cardio_app.db.model.PressureData;
import cardio_app.db.model.TimeUnit;
import cardio_app.db.model.UserProfile;
import temporary_package.InitialPressureData;

public class DbHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = DbHelper.class.getName();
    private static final String DB_NAME = "CardioApp.db";
    private static final int VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
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
        Event e1 = new Event(getDate(19, 10, 2016), getDate(19, 11, 2016), false, TimeUnit.NONE, 0, "description1", osr, Emotion.HAPPY, da, DailyActivitiesRecord.DRIVING_CAR, false);
        Event e2 = new Event(getDate(19, 10, 2016), getDate(20, 11, 2016), false, TimeUnit.NONE, 0, "description2", osr, Emotion.SAD, da, DailyActivitiesRecord.ARGUE, false);
        Event e3 = new Event(getDate(19, 10, 2016), getDate(19, 12, 2016), true, TimeUnit.WEEK, 2, "description3", osr, Emotion.ANGRY, da, DailyActivitiesRecord.HOUSE_DUTIES, false);
        Event e4 = new Event(new Date(2016, 10, 20, 16, 0), new Date(2016, 10, 30), true, TimeUnit.DAY, 1, "description4", osr, Emotion.CRYING, da, DailyActivitiesRecord.PARTY, false);
        daoHp.create(e1);
        daoHp.create(e2);
        daoHp.create(e3);
        daoHp.create(e4);
    }

    private String getStringFromClassArray(ArrayList<Class> array) {
        StringBuilder sb = new StringBuilder();
        for (Class aClass : array) {
            sb.append(aClass.getName()).append(" ");
        }
        return sb.toString().trim().replaceAll(" ", ", ");
    }

    private void createTables() {
        final Class[] tablesToCreateInOrder = {
                Drug.class,
                PressureData.class,
                OtherSymptomsRecord.class,
                DoctorsAppointment.class,
                Event.class,
                UserProfile.class
        };

        ArrayList<Class> notCreatedTables = new ArrayList<>();

        for (Class table : tablesToCreateInOrder) {
            try {
                TableUtils.createTable(connectionSource, table);
                Log.i(TAG, String.format("Table created successfully: %s", table.getName()));
            } catch (SQLException e) {
                Log.e(TAG, String.format("Can't create table: %s", table.getName()), e);
                notCreatedTables.add(table);
            }
        }

        if (notCreatedTables.size() != 0)
            throw new RuntimeException(
                    String.format("Cant create database, because of error while creating tables: %s",
                            getStringFromClassArray(notCreatedTables))
            );
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        createTables();

        // TODO remove init before release if should not be hardcoded
        try {
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

    public List<Event> getAllOrderedEventData() throws SQLException {
        return getFilteredAndOrderedByDateEvents(null, null);
    }

    public List<PressureData> getFilteredAndOrderedByDatePressureData(Date dateFrom, Date dateTo) throws SQLException {
        Dao<PressureData, Integer> dao = getDao(PressureData.class);
        return getFilteredByDate(dao.queryBuilder(), "dateTime", false, false, dateFrom, dateTo).query();
    }

    public List<Event> getFilteredAndOrderedByDateEvents(Date dateFrom, Date dateTo) throws SQLException {
        Dao<Event, Integer> dao = getDao(Event.class);
        return getFilteredByDate(dao.queryBuilder(), "startDate", false, false, dateFrom, dateTo).query();
    }

    public Date getFirstDateFromPressureDataTable() throws Exception {
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

    public UserProfile getUserProfile() throws SQLException {
        // TODO does not work -> no user profile
        Dao<UserProfile, Integer> dao = getDao(UserProfile.class);
        return dao.queryBuilder().queryForFirst();
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

        JSONArray events = new JSONArray();
        List<Event> eventsList = getDao(Event.class).queryForAll();
        Comparator<BaseModel> comparator = (m1, m2) -> m1.getId() - m2.getId();
        List<OtherSymptomsRecord> otherSymptomsRecordList = getDao(OtherSymptomsRecord.class).queryForAll();
        Collections.sort(otherSymptomsRecordList, comparator);
        List<DoctorsAppointment> doctorsAppointmentList = getDao(DoctorsAppointment.class).queryForAll();
        Collections.sort(doctorsAppointmentList, comparator);
        for (Event event : eventsList) {
            int symptomId = Collections.binarySearch(otherSymptomsRecordList, event.getOtherSymptomsRecord(), comparator);
            int doctorId = Collections.binarySearch(doctorsAppointmentList, event.getOtherSymptomsRecord(), comparator);

            if (symptomId >= 0) {
                event.setOtherSymptomsRecord(otherSymptomsRecordList.get(symptomId));
            }
            if (doctorId >= 0) {
                event.setDoctorsAppointment(doctorsAppointmentList.get(doctorId));
            }

            events.put(event.convertToJson());
        }

        JSONArray profiles = new JSONArray();
        List<UserProfile> profileList = getDao(UserProfile.class).queryForAll();
        for (UserProfile userProfile : profileList) {
            profiles.put(userProfile.convertToJson());
        }

        JSONObject result = new JSONObject();

        result.put("drugs", drugs);
        result.put("pressures", pressures);
        result.put("events", events);
        result.put("userProfiles", profiles);

        return result;
    }

    public void importFromJson(JSONObject data) throws JSONException, SQLException, ParseException {
        Dao<Drug, Integer> drugDao = getDao(Drug.class);
        int drugsDeleted = drugDao.deleteBuilder().delete();
        Log.d(TAG, "Delete " + drugsDeleted + " drugs");

        Dao<PressureData, Integer> pressureDao = getDao(PressureData.class);
        int pressureDeleted = pressureDao.deleteBuilder().delete();
        Log.d(TAG, "Delete " + pressureDeleted + " pressures");

        Dao<OtherSymptomsRecord, Integer> symptomsDao = getDao(OtherSymptomsRecord.class);
        int symptomsDeleted = symptomsDao.deleteBuilder().delete();
        Log.d(TAG, "Delete " + symptomsDeleted + " symptoms");

        Dao<DoctorsAppointment, Integer> appointmentDao = getDao(DoctorsAppointment.class);
        int appointmentsDeleted = appointmentDao.deleteBuilder().delete();
        Log.d(TAG, "Delete " + appointmentsDeleted + " appointments");

        Dao<Event, Integer> eventsDao = getDao(Event.class);
        int eventsDeleted = eventsDao.deleteBuilder().delete();
        Log.d(TAG, "Delete " + eventsDeleted + " events");

        Dao<UserProfile, Integer> profilesDao = getDao(UserProfile.class);
        int profilesDeleted = profilesDao.deleteBuilder().delete();
        Log.d(TAG, "Delete " + profilesDeleted + " profiles");

        JSONArray drugs = data.getJSONArray("drugs");
        JSONArray pressures = data.getJSONArray("pressures");
        JSONArray events = data.getJSONArray("events");
        JSONArray profiles = data.getJSONArray("userProfiles");

        for (int i = 0; i < drugs.length(); i++) {
            JSONObject drugObject = drugs.getJSONObject(i);
            Drug drug = Drug.convert(drugObject);
            drugDao.create(drug);
        }

        for (int i = 0; i < pressures.length(); i++) {
            JSONObject pressureObject = pressures.getJSONObject(i);
            PressureData pressureData = PressureData.convert(pressureObject);
            pressureDao.create(pressureData);
        }

        for (int i = 0; i < events.length(); i++) {
            JSONObject eventObject = events.getJSONObject(i);
            Event event = Event.convert(eventObject);

            symptomsDao.create(event.getOtherSymptomsRecord());
            appointmentDao.create(event.getDoctorsAppointment());
            eventsDao.create(event);
        }

        for (int i = 0; i < profiles.length(); i++) {
            JSONObject profileObject = profiles.getJSONObject(i);
            UserProfile userProfile = UserProfile.convert(profileObject);
            profilesDao.create(userProfile);
        }
    }
}
