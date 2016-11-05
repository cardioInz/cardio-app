package cardio_app.db.model;

import com.j256.ormlite.field.DatabaseField;

public class BaseModel {

    @DatabaseField(generatedId = true)
    private int id;

    public BaseModel() {
    }

    public BaseModel(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
