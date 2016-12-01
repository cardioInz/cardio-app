package cardio_app.util;


import android.content.res.Resources;
import android.util.Log;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import cardio_app.R;
import cardio_app.db.DbHelper;
import cardio_app.db.model.PressureData;
import cardio_app.pdf_creation.param_models.BitmapFromChart;
import cardio_app.pdf_creation.param_models.PdfRecordsContainer;
import cardio_app.statistics.Statistics;
import cardio_app.statistics.analyse.StatisticCounter;
import cardio_app.statistics.analyse.StatisticLastMeasure;
import cardio_app.viewmodel.PressureDataViewModel;
import cardio_app.viewmodel.ProfileViewModel;
import cardio_app.viewmodel.statistics.StatisticLastMeasureViewModel;

import static cardio_app.util.DateTimeUtil.DATETIME_FORMATTER;
import static cardio_app.util.DateTimeUtil.DATE_FORMATTER;
import static cardio_app.util.DateTimeUtil.TIME_FORMATTER;

public class PdfCreator {
    private static final String TAG = PdfCreator.class.getName();

    private static Font fontChapter = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font fontSubParagraph = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static Font fontDates = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

    private Document document = null;
    private PdfRecordsContainer recordsContainer;
    private Resources resources;
    private Integer chapterCnt;
    private java.util.List<BitmapFromChart> extraChartBitmapList;
    private boolean isMyProfileAttached = false;

    public PdfCreator(PdfRecordsContainer recordsContainerParam, java.util.List<BitmapFromChart> extraChartBitmapList, Resources res){
        this.resources = res;
        this.recordsContainer = recordsContainerParam;
        this.extraChartBitmapList = extraChartBitmapList;
        this.chapterCnt = 0;
        this.document = new Document();

        whatsDocumentSize();
    }

    private void whatsDocumentSize(){
        float width = document.getPageSize().getWidth();
        float height = document.getPageSize().getHeight();
        String sizeInfo = String.format("\t\tWidth: %s\n\t\tHeight: %s", String.valueOf(width), String.valueOf(height));
        Log.i(TAG, "createAndSavePdf: \n" + sizeInfo);
    }

    private java.util.List<Image> initExtraCharts() {
        cleanBitmapWithoutPaths(extraChartBitmapList);
        for (BitmapFromChart fromChart : extraChartBitmapList) {
            if (!BitmapUtil.loadBitmapFromFile(fromChart))
                continue;
            BitmapUtil.loadBitmapFromFile(fromChart);
        }
        return prepareImagesToPdf(extraChartBitmapList);
    }

    private static void scaleImage(Image image, Document document) {

        float w = image.getWidth(); // pdf 595
        float h = image.getHeight(); // pdf 842
        float docW = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();
        float scale = docW / w;
        int newW = (int) (w * scale);
        int newH = (int) (h * scale);

        image.scaleAbsolute(newW, newH);
    }

    private static void scaleImage(Image image, float scale) {
        int w = (int) (image.getWidth() * scale);
        int h = (int) (image.getHeight() * scale);
        image.scaleAbsolute(w, h);
    }


    public void createAndSavePdf(String fileLocation) {
        try {
            assert resources != null;
            assert recordsContainer != null;
            PdfWriter.getInstance(document, new FileOutputStream(fileLocation));
            document.open();
            document.newPage();
            addMetaData(document);
            addTitleAndDates(document);
            addUserProfileInformation(document);

            // TODO 
            // split recordsContained into "smaller" instances by dates from-to (e.g. month by month)
            // and pass to below method one by one as instance of PdfRecordsContainer producing chapters
            addContent(document, recordsContainer);

            addExtraCharts(document);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "makePdfFile: error while trying to create/save pdf report", e);
        }

        recordsContainer = null;
        resources = null;
    }

    private void addMetaData(Document document) {
        final String APP_NAME = "Cardio-Inz";

        document.addTitle(getResourceString(R.string.pdf_report_generated_by) + " " + APP_NAME + " App");
        document.addSubject(APP_NAME + " " + getResourceString(R.string.report));
        document.addKeywords(APP_NAME + " " + getResourceString(R.string.report));
        document.addAuthor(APP_NAME);
        document.addCreator(APP_NAME);
    }

    private void addTitleAndDates(Document document) throws DocumentException {
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Cardio-Inz Report", fontChapter));

        addEmptyLine(preface, 1);

        preface.add(new Paragraph(
                "Date of report generation: " + DATETIME_FORMATTER.format(new Date()),
                fontDates));

        Date dateFrom = recordsContainer.getDateFrom();
        Date dateTo = recordsContainer.getDateTo();
        String dateFromStr;
        String dateToStr;

        if (dateFrom == null || dateTo == null) {
            try {
                dateFrom = getHelper().getFirstDateFromPressureDataTable();
                dateTo = getHelper().getLastDateFromPressureDataTable();
                dateFromStr = DATE_FORMATTER.format(dateFrom);
                dateToStr = DATE_FORMATTER.format(dateTo);
            } catch (Exception e) {
                dateFromStr = "-";
                dateToStr = "-";
            }
        } else {
            dateFromStr = DATE_FORMATTER.format(dateFrom);
            dateToStr = DATE_FORMATTER.format(dateTo);
        }

        try {
            recordsContainer.setDateFrom(DATE_FORMATTER.parse(dateFromStr));
            recordsContainer.setDateTo(DATE_FORMATTER.parse(dateToStr));
        } catch (Exception e) {
            Log.e(TAG, "addTitleAndDates: ", e);
        }

        addEmptyLine(preface, 1);
        preface.add(new Paragraph(getResourceString(R.string.pdf_generation_interval_dates)
                + " " + getResourceString(R.string.from_date) + " " + dateFromStr
                + " " + getResourceString(R.string.to_date) + " " + dateToStr,
                fontDates));
        addEmptyLine(preface, 2);
        document.add(preface);
    }

    private void addContent(Document document, PdfRecordsContainer recordsContainer) throws DocumentException {
        recordsContainer.initRecordsByHelper();

        String chapterTitle = getResourceString(R.string.results);

        try {
            chapterTitle = String.format("%s %s: %s, %s: %s",
                    getResourceString(R.string.results),
                    getResourceString(R.string.from_date), DateTimeUtil.DATE_FORMATTER.format(recordsContainer.getDateFrom()),
                    getResourceString(R.string.to_date), DateTimeUtil.DATE_FORMATTER.format(recordsContainer.getDateTo())
            );
        } catch (Exception e) {
            Log.e(TAG, "addContent: ", e);
        }

        Anchor anchor = new Anchor(chapterTitle, fontChapter);
        anchor.setName(chapterTitle);
        Chapter chapter = new Chapter(new Paragraph(anchor), ++chapterCnt);
        chapter.setTriggerNewPage(isMyProfileAttached || chapterCnt != 1);
        addEmptyLine(chapter, 2);

        // TODO if "any given chart image generation" will work, push some continuous chart for this interval here
        // TODO if it will work, create some subparagraph that will describe events on the chart

        addPressureData(chapter, recordsContainer.getPressureDataList());
        // addEvents(chapter, recordsContainer.getEventsDataList());
        addStatistics(chapter, recordsContainer);
        document.add(chapter);
    }

    private void addExtraCharts(Document document) throws DocumentException {
        java.util.List<Image> extraChartImageList = initExtraCharts();
        String name = getResourceString(R.string.pdf_content_charts_chosen);
        Anchor anchor = new Anchor(name, fontChapter);
        anchor.setName(name);

        Chapter catPart = new Chapter(new Paragraph(anchor), ++chapterCnt);
        catPart.setTriggerNewPage(false);
        addEmptyLine(catPart, 1);

        for (Image image : extraChartImageList) {
            if (image != null) {
                scaleImage(image, document); // scale to page width
                scaleImage(image, 0.4f); // scale percent
                catPart.add(image);
            } else
                Log.e(TAG, "addChart: image = null");
        }

        document.add(catPart);
    }

    private void addUserProfileInformation(Document document) throws DocumentException {
        String name = getResourceString(R.string.pdf_content_about_patient);
        Anchor anchor = new Anchor(name, fontChapter);
        anchor.setName(name);

        // Second parameter is the number of the chapter
        Chapter catPart = new Chapter(new Paragraph(anchor), ++chapterCnt);
        catPart.setTriggerNewPage(false);
        addEmptyLine(catPart, 1);

        addEmptyLine(catPart, 1);

        try {
            ProfileViewModel profileViewModel = new ProfileViewModel(recordsContainer.getUserProfile());
            HashMap<String, String> map = profileViewModel.getHashMapFields(resources);
            boolean throwSomeEx = true;
            for (String key : map.keySet()) {
                if (!map.get(key).equals("-")) {
                    throwSomeEx = false;
                    catPart.add(new Paragraph(key + "  " + map.get(key)));
                }
            }

            if (throwSomeEx)
                throw new Exception("UserProfile is empty, so I don't attach it to the pdf report");

            addEmptyLine(catPart, 2);
            document.add(catPart);
            isMyProfileAttached = true;
        } catch (Exception e) {
            Log.e(TAG, "addUserProfileInformation: ", e);
            chapterCnt--;
            isMyProfileAttached = false;
        }
    }

    private void addPressureData(Chapter chapter, java.util.List<PressureData> pressureDataList) throws DocumentException {
        String name = getResourceString(R.string.pdf_chapter_blood_pressure);
        Paragraph mainSubPara = new Paragraph(name, fontSubParagraph);

        Section catPart = chapter.addSection(mainSubPara);
        catPart.setTriggerNewPage(false);
        addEmptyLine(catPart, 1);
        createTableOfPressure(catPart, pressureDataList);

        addEmptyLine(catPart, 2);
    }

    private void addStatistics(Chapter chapter, PdfRecordsContainer recordsContainer) throws DocumentException {
        String name = getResourceString(R.string.pdf_chapter_statistics);
        Paragraph mainSubPara = new Paragraph(name, fontSubParagraph);
        Section catPart = chapter.addSection(mainSubPara);
        addEmptyLine(catPart, 1);

        List listCnt = new List(false, false, 10);
        List listLast = new List(false, false, 10);
        createListOfMeasurements(listCnt, listLast, recordsContainer);


        Paragraph subPara = new Paragraph(getResourceString(R.string.title_statistics_countered_measures), fontSubParagraph);
        Section subCatPart = catPart.addSection(subPara);
        addEmptyLine(subCatPart, 1);
        subCatPart.add(listCnt);

        addEmptyLine(catPart, 2);

        subPara = new Paragraph(getResourceString(R.string.title_statistics_last_measures), fontSubParagraph);
        subCatPart = catPart.addSection(subPara);
        addEmptyLine(subCatPart, 1);
        subCatPart.add(listLast);

        addEmptyLine(catPart, 2);
    }

    private void createTableOfPressure(Section subCatPart, java.util.List<PressureData> pressureDataList) throws DocumentException {
        PdfPTable table = new PdfPTable(7);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase(getResourceString(R.string.pdf_systole)));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(getResourceString(R.string.pdf_diastole)));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(getResourceString(R.string.pdf_difference)));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(getResourceString(R.string.pdf_pulse)));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(getResourceString(R.string.pdf_arrhythmia)));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(getResourceString(R.string.pdf_date)));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(getResourceString(R.string.pdf_time)));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        table.setHeaderRows(1);

        PressureDataViewModel pressureDataViewModel = new PressureDataViewModel();
        for (PressureData pressureData : pressureDataList) {
            pressureDataViewModel.setPressureData(pressureData);

            table.addCell(pressureDataViewModel.getSystoleStr());
            table.addCell(pressureDataViewModel.getDiastoleStr());
            table.addCell(String.valueOf(pressureData.getSystole() - pressureData.getDiastole()));
            table.addCell(pressureDataViewModel.getPulseStr());
            table.addCell("  " + pressureDataViewModel.getArrhythmiaStr());
            table.addCell(DATE_FORMATTER.format(pressureData.getDateTime()));
            table.addCell(TIME_FORMATTER.format(pressureData.getDateTime()));
        }

        float[] columnWidths = new float[]{15f, 15f, 17f, 10f, 16f, 17f, 10f};
        table.setWidths(columnWidths);

        subCatPart.add(table);

    }

    private void createListOfMeasurements(List listCnt, List listLast, PdfRecordsContainer recordsContainer) {
        final boolean statCnt = listCnt != null;
        final boolean statLast = listLast != null;
        Statistics statistics = new Statistics(statCnt, statLast);
        statistics.assignValues(recordsContainer.getPressureDataList());

        if (statCnt) {
            final HashMap<StatisticCounter.TypeEnum, Integer> mapCounter = statistics.getStatisticCounter().getMapCounter();
            for (StatisticCounter.TypeEnum key : StatisticCounter.TypeEnum.values()) {
                if (!mapCounter.containsKey(key))
                    continue;

                final int cnt = mapCounter.get(key);
                final String title = getResourceString(key.toTitleId());
                listCnt.add(new ListItem(title + " " + cnt));
            }
        }

        if (statLast) {
            final HashMap<StatisticLastMeasure.TypeEnum, StatisticLastMeasure> mapLastMeasures = statistics.getStatisticMeasuresMap();
            final StatisticLastMeasureViewModel measureViewModel = new StatisticLastMeasureViewModel();
            for (StatisticLastMeasure.TypeEnum key : StatisticLastMeasure.TypeEnum.values()) {
                if (!mapLastMeasures.keySet().contains(key))
                    continue;

                final String title = getResourceString(key.mapToTitleStringId());
                final StatisticLastMeasure statisticLastMeasure = mapLastMeasures.get(key);
                measureViewModel.setStatisticLastMeasure(statisticLastMeasure);

                final String listItemStr = String.format("%s\n\t%s    %s\n\t%s  %s\n\t%s  %s%s\n\n",
                        title,
                        getResourceString(R.string.statistics_title_values).replaceAll("/", " / "), measureViewModel.getValuesStr().replaceAll("/", " / "),
                        getResourceString(R.string.statistics_title_date), measureViewModel.getDateStr(),
                        getResourceString(R.string.statistics_title_time), measureViewModel.getTimeStr(),
                        (
                                !measureViewModel.shouldShowArrhythmia() ? "" :
                                        String.format("\n%s", statisticLastMeasure.getPressureData().isArrhythmia() ?
                                                getResourceString(R.string.statistics_title_arrhythmia) :
                                                getResourceString(R.string.statistics_title_no_arrhythmia)
                                        )
                        )
                );

                listLast.add(new ListItem(listItemStr));
            }
        }
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private static void addEmptyLine(Section section, int number) {
        for (int i = 0; i < number; i++) {
            section.add(new Paragraph(" "));
        }
    }

    private DbHelper getHelper() {
        return recordsContainer.getDbHelper();
    }

    private String getResourceString(int id) {
        return resources.getString(id);
    }

    private static java.util.List<Image> prepareImagesToPdf(java.util.List<BitmapFromChart> list) {
        java.util.List<Image> imageList = new ArrayList<>();
        for (BitmapFromChart bitmapFromChart : list) {
            try {
                Image image = bitmapFromChart.getImage();
                imageList.add(image);
                bitmapFromChart.setBitmap(null);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "doInBackground: ", e);
            }
        }
        return imageList;
    }

    private static void cleanBitmapWithoutPaths(java.util.List<BitmapFromChart> list) {
        java.util.List<BitmapFromChart> newList = new ArrayList<>();

        for (BitmapFromChart fromChart : list) {
            if (fromChart.hasFilePathExt()) {
                if (BitmapUtil.loadBitmapFromFile(fromChart))
                    newList.add(fromChart);
            } else {
                Log.w(TAG, "makeBitmaps: values not completed\n" + fromChart.infoStrForLogger());
            }
        }

        list.clear();
        list.addAll(newList);
    }
}
