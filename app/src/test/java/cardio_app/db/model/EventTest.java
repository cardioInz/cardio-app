package cardio_app.db.model;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class EventTest {

    @Test
    public void shouldReturnListWithTeSameElements() {
        List<Event> list = new ArrayList<>();
        Event event1 = new Event();
        event1.setStartDate(new Date());
        event1.setEndDate(new Date());
        Event event2 = new Event();
        event2.setStartDate(new Date());
        event2.setEndDate(new Date());

        list.add(event1);
        list.add(event2);

        List<Event> result = Event.multiplyRepeatableEvents(list);

        assertEquals(result, list);
    }

    @Test
    public void shouldReturnMultiplyEvents() {
        Event event = new Event();
        LocalDateTime startDate = LocalDateTime.now();
        LocalDate endDate = LocalDate.now().plusDays(5);

        event.setStartDate(startDate.toDate());
        event.setEndDate(endDate.toDate());
        event.setRepeatable(true);
        event.setTimeUnit(TimeUnit.DAY);
        event.setTimeDelta(3);

        Event resultEvent1 = new Event();
        resultEvent1.setStartDate(startDate.toDate());
        resultEvent1.setEndDate(startDate.toLocalDate().toDate());
        resultEvent1.setRepeatable(true);
        resultEvent1.setTimeUnit(TimeUnit.DAY);
        resultEvent1.setTimeDelta(3);
        Event resultEvent2 = new Event();
        resultEvent2.setStartDate(startDate.plusDays(3).toDate());
        resultEvent2.setEndDate(startDate.plusDays(3).toLocalDate().toDate());
        resultEvent2.setRepeatable(true);
        resultEvent2.setTimeUnit(TimeUnit.DAY);
        resultEvent2.setTimeDelta(3);

        List<Event> list = Collections.singletonList(event);
        List<Event> actual = Event.multiplyRepeatableEvents(list);

        assertEquals(actual.size(), 2);

        assertEquals(actual.get(0), resultEvent1);
        assertEquals(actual.get(1), resultEvent2);
    }
}
