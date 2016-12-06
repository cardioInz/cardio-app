package cardio_app.util;

import android.content.res.Resources;
import android.graphics.Color;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cardio_app.R;
import cardio_app.activity.statistics.ChartActivity;
import cardio_app.db.model.Event;
import cardio_app.db.model.PressureData;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;

public class ChartBuilder {
    private static final long MILLIS_IN_DAY = 24 * 3600 * 1000;
    private List<PressureData> data;
    private List<Event> eventData;
    private ChartMode chartMode = ChartMode.DISCRETE;

    private Resources resources;
    private long days;
    private long startTime;
    private long endTIme;
    private boolean hasLabels;
    private boolean pressureHasPoints = true;
    private boolean eventsHasPoints = true;

    public ChartBuilder(List<PressureData> data, Resources resources) {
        this.data = data;
        this.resources = resources;
    }

    public ChartBuilder(List<PressureData> pressureDataList, List<Event> eventList, ChartMode chartMode, Resources resources) {
        this.data = pressureDataList;
        this.eventData = eventList;
        this.chartMode = chartMode;
        this.resources = resources;
    }

    public ChartBuilder setData(List<PressureData> data) {
        this.data = data;

        return this;
    }

    public ChartMode getMode() {
        return chartMode;
    }

    public ChartBuilder setMode(ChartMode chartMode) {
        this.chartMode = chartMode;

        return this;
    }

    public ChartBuilder setStartTime(long startTime) {
        this.startTime = startTime;

        return this;
    }

    public ChartBuilder setEndTIme(long endTIme) {
        this.endTIme = endTIme;

        return this;
    }

    public ChartBuilder setEvents(List<Event> events) {
        this.eventData = events;

        return this;
    }

    public boolean isHasLabels() {
        return hasLabels;
    }

    public ChartBuilder setHasLabels(boolean hasLabels) {
        this.hasLabels = hasLabels;

        return this;
    }

    public long getDays() {
        return days;
    }

    public ChartBuilder setPressureHasPoints(boolean pressureHasPoints) {
        this.pressureHasPoints = pressureHasPoints;

        return this;
    }

    public ChartBuilder setEventsHasPoints(boolean eventsHasPoints) {
        this.eventsHasPoints = eventsHasPoints;

        return this;
    }

    public LineChartData build() {
        List<Line> lines = new ArrayList<>();
        long min = 0, max = 0, diff = 0;

        if (data.size() > 0 && startTime == 0 && endTIme == 0) {
            min = data.get(data.size() - 1).getDateTime().getTime();
            max = data.get(0).getDateTime().getTime();
        } else {
            min = startTime;
            max = endTIme;
        }
        if (eventData != null && eventData.size() > 0) {
            long tempMin = eventData.get(eventData.size() - 1).getStartDate().getTime();
            long tempMax = eventData.get(0).getEndDate().getTime();

            if (tempMin < min) {
                min = tempMin;
            }
            if (tempMax > max) {
                max = tempMax;
            }
        }
        diff = max - min;
        switch (chartMode) {
            case DISCRETE: {
                List<Line> systoleToDiastoleList = new ArrayList<>();
                for (PressureData entity : data) {
                    List<PointValue> once = new ArrayList<>();
                    once.add(new ChartActivity.CustomPointValue(entity.getDateTime().getTime(), entity.getSystole(), entity));
                    once.add(new ChartActivity.CustomPointValue(entity.getDateTime().getTime(), entity.getDiastole(), entity));
                    systoleToDiastoleList.add(new Line(once).setColor(Color.RED).setHasLabels(hasLabels).setHasPoints(pressureHasPoints));
                }

                lines.addAll(systoleToDiastoleList);

                break;
            }
            case CONTINUOUS: {
                List<PointValue> diastoles = new ArrayList<>();
                List<PointValue> systoles = new ArrayList<>();

                for (PressureData entity : data) {
                    diastoles.add(new ChartActivity.CustomPointValue(entity.getDateTime().getTime(), entity.getDiastole(), entity));
                    systoles.add(new ChartActivity.CustomPointValue(entity.getDateTime().getTime(), entity.getSystole(), entity));
                }

                lines.add(new Line(diastoles).setColor(Color.GREEN).setHasLabels(hasLabels).setHasPoints(pressureHasPoints));
                lines.add(new Line(systoles).setColor(Color.RED).setHasLabels(hasLabels).setHasPoints(pressureHasPoints));

                break;
            }
        }

        if (eventData != null) {
            List<PointValue> pointEvent = new ArrayList<>();
            int positionY = 10;
            int offset = 0;
            for (Event event : eventData) {
                if (event.getStartDate().equals(event.getEndDate())) {
                    PointValue point = new ChartActivity.CustomPointValue(event.getStartDate().getTime(), positionY, event);
                    pointEvent.add(point);
                } else {
                    if (event.isRepeatable()) {
                        for (long i = event.getStartDate().getTime(); i < event.getEndDate().getTime(); i = Event.appendTime(i, event)) {
                            PointValue point = new ChartActivity.CustomPointValue(i, positionY, event);
                            pointEvent.add(point);
                        }
                    } else {
                        List<PointValue> continous = new ArrayList<>();
                        continous.add(new ChartActivity.CustomPointValue(event.getStartDate().getTime(), positionY, event));
                        if (event.getEndDate().getTime() <= max) {
                            continous.add(new ChartActivity.CustomPointValue(event.getEndDate().getTime(), positionY, event));
                        } else {
                            continous.add(new ChartActivity.CustomPointValue(max, positionY, event));
                        }
                        lines.add(new Line(continous).setColor(Color.GRAY).setHasPoints(eventsHasPoints));
                    }
                }
                if (positionY >= 50) {
                    offset++;
                    positionY = 10 + (offset % 5);
                } else {
                    positionY += 5;
                }
            }

            lines.add(new Line(pointEvent).setColor(Color.GRAY).setHasLines(false).setHasPoints(eventsHasPoints));
        }

        days = diff / MILLIS_IN_DAY;

        //x axis with formatted date
        DateFormat simpleDateFormat = SimpleDateFormat.getDateInstance();
        List<AxisValue> aList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (long a = min; a <= max; a += MILLIS_IN_DAY) {
            calendar.setTimeInMillis(a);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            String value = simpleDateFormat.format(new Date(calendar.getTimeInMillis()));
            aList.add(new AxisValue(calendar.getTimeInMillis(), value.toCharArray()));
        }

        Axis axisY = new Axis()
                .setName(resources.getString(R.string.label_axis_y))
                .setTextColor(Color.BLACK)
                .setHasLines(true)
                .setAutoGenerated(true);

        Axis axisX = new Axis()
                .setName(resources.getString(R.string.label_axis_x))
                .setTextColor(Color.BLACK)
                .setHasLines(true)
                .setMaxLabelChars(12)
                .setValues(aList);

        LineChartData chartData = new LineChartData(lines);
        chartData.setAxisYLeft(axisY);
        chartData.setAxisXBottom(axisX);

        return chartData;
    }

    public enum ChartMode {
        DISCRETE, CONTINUOUS
    }
}
