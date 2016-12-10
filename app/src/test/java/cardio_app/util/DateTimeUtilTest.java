package cardio_app.util;

import org.joda.time.LocalDateTime;
import org.junit.Test;

import cardio_app.db.model.TimeUnit;

import static org.junit.Assert.assertEquals;

public class DateTimeUtilTest {

    @Test
    public void shouldIncreaseDate() {
        LocalDateTime actual = LocalDateTime.now();
        LocalDateTime expected = actual.plusWeeks(3);

        actual = DateTimeUtil.increaseDate(actual, TimeUnit.WEEK, 3);
        assertEquals(expected, actual);
    }
}
