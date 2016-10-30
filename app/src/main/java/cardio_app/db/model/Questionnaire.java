package cardio_app.db.model;


import com.j256.ormlite.field.DatabaseField;

public class Questionnaire extends BaseModel {

    // TODO change this class into singleton (if possible)

    @DatabaseField
    public static boolean isMale; // = true; // just because

    public Questionnaire() {

    }
}
