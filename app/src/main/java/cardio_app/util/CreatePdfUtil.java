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
import java.util.Date;
import java.util.HashMap;

import cardio_app.R;
import cardio_app.db.DbHelper;
import cardio_app.db.model.PressureData;
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

public class CreatePdfUtil {
    private static final String TAG = CreatePdfUtil.class.getName();

    private static Font fontChapter = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font fontSubParagraph = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static Font fontDates = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

    private static PdfRecordsContainer recordsContainer;
    private static Resources resources;
    private static Integer chapterCnt = 0;

    private static void scaleImage(Image image, Document document) {

        float w = image.getWidth(); // pdf 595
        float h = image.getHeight(); // pdf 842
        float docW = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();
        float scale = docW / w;
        int newW = (int) (w * scale);
        int newH = (int) (h * scale);

        image.scaleAbsolute(newW, newH);
    }

    private static void scaleImage(Image image, float scale){
        int w = (int) (image.getWidth() * scale);
        int h = (int) (image.getHeight() * scale);
        image.scaleAbsolute(w, h);
    }

// TODO check that we are able to scale bitmap while saving (it could be faster then)
//    public static Bitmap scaleBitmapToPdf(Bitmap bitmap){
//        float w = bitmap.getWidth(); // pdf 595
//        float h = bitmap.getHeight(); // pdf 842
//        float scale = 595f / w;
//        int newW = (int) (w * scale);
//        int newH = (int) (h * scale);
//        return Bitmap.createScaledBitmap(bitmap, newW, newH, false);
//    }

    public static void createAndSavePdf(String fileLocation, PdfRecordsContainer recordsContainerParam,
                                        java.util.List<Image> imageList, Resources res) {
        try {
            resources = res;
            recordsContainer = recordsContainerParam;
            chapterCnt = 0;

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileLocation));
            document.open();
            document.newPage();

            float width = document.getPageSize().getWidth();
            float height = document.getPageSize().getHeight();
            String sizeInfo = String.format("\t\tWidth: %s\n\t\tHeight: %s", String.valueOf(width), String.valueOf(height));
            Log.i(TAG, "createAndSavePdf: \n" + sizeInfo);

            addMetaData(document);
            addTitleAndDates(document);


            addContent(document);
            addExtraCharts(document, imageList);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "makePdfFile: error while trying to create/save pdf report", e);
        }

        recordsContainer = null;
        resources = null;
    }

    private static void addMetaData(Document document) {
        final String APP_NAME = "Cardio-Inz";
        
        document.addTitle(getResourceString(R.string.pdf_report_generated_by) + " " + APP_NAME + " App");
        document.addSubject(APP_NAME + " " + getResourceString(R.string.report));
        document.addKeywords(APP_NAME + " " + getResourceString(R.string.report));
        document.addAuthor(APP_NAME);
        document.addCreator(APP_NAME);
    }

    private static void addTitleAndDates(Document document) throws DocumentException {
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

        addEmptyLine(preface, 1);
        preface.add(new Paragraph( getResourceString(R.string.pdf_generation_interval_dates)
                        + " " + getResourceString(R.string.from_date) + " " + dateFromStr
                + " " + getResourceString(R.string.to_date) + " " + dateToStr,
                fontDates));
        addEmptyLine(preface, 2);
        document.add(preface);
    }

    private static void addContent(Document document) throws DocumentException {
        addUserProfileInformations(document);
        addPressureData(document); // in future with charts
        addStatistics(document);
    }

    private static void addExtraCharts(Document document, java.util.List<Image> imageList) throws DocumentException {
        String name = getResourceString(R.string.pdf_content_charts_chosen);
        Anchor anchor = new Anchor(name, fontChapter);
        anchor.setName(name);

        Chapter catPart = new Chapter(new Paragraph(anchor), ++chapterCnt);
        catPart.setTriggerNewPage(false);
        addEmptyLine(catPart, 1);

        for (Image image : imageList) {
            if (image != null) {
                scaleImage(image, document); // scale to page width
                scaleImage(image, 0.4f); // percent
                catPart.add(image);
            } else
                Log.e(TAG, "addChart: image = null");
        }

        document.add(catPart);
    }

    private static void addUserProfileInformations(Document document) throws DocumentException {
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

            catPart.add(new Paragraph(getResourceString(R.string.name) + ": " + profileViewModel.getNameStr()));
            catPart.add(new Paragraph(getResourceString(R.string.surname) + ": " + profileViewModel.getSurnameStr()));
            catPart.add(new Paragraph(getResourceString(R.string.date_of_birth) + ": " + profileViewModel.getDateOfBirthStr()));
            catPart.add(new Paragraph(getResourceString(R.string.sex) + profileViewModel.getSex()));
            catPart.add(new Paragraph(getResourceString(R.string.height) + " [cm]: " + profileViewModel.getHeightStr()));
            catPart.add(new Paragraph(getResourceString(R.string.weight) + " [kg]: " + profileViewModel.getWeightStr()));
            catPart.add(new Paragraph(getResourceString(R.string.smoker) + ": " + (profileViewModel.getSmoker() ? getResourceString(R.string.yes) : getResourceString(R.string.no))));
            catPart.add(new Paragraph(getResourceString(R.string.glucose) + " [mmol/l]: " + profileViewModel.getGlucoseStr()));
            catPart.add(new Paragraph(getResourceString(R.string.cholesterol) + " [mmol/l]: " + profileViewModel.getCholesterolStr()));

            addEmptyLine(catPart, 2);
            document.add(catPart);
        } catch (Exception e) {
            Log.e(TAG, "addUserProfileInformations: ", e);
            chapterCnt--;
        }
    }

    private static void addPressureData(Document document) throws DocumentException {
        String name = getResourceString(R.string.pdf_chapter_blood_pressure);
        Anchor anchor = new Anchor(name, fontChapter);
        anchor.setName(name);

        Chapter catPart = new Chapter(new Paragraph(anchor), ++chapterCnt);
        catPart.setTriggerNewPage(false);
        addEmptyLine(catPart, 1);
        createTableOfPressure(catPart);

        addEmptyLine(catPart, 2);
        document.add(catPart);
    }

    private static void addStatistics(Document document) throws DocumentException {
        String name = getResourceString(R.string.pdf_chapter_statistics);
        Anchor anchor = new Anchor(name, fontChapter);
        anchor.setName(name);

        Chapter catPart = new Chapter(new Paragraph(anchor), ++chapterCnt);
//        catPart.setTriggerNewPage(false);
        addEmptyLine(catPart, 1);

        List listCnt = new List(false, false, 10);
        List listLast = new List(false, false, 10);
        createListOfMeasurements(listCnt, listLast);


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
        document.add(catPart);
    }


    private static void createTableOfPressure(Section subCatPart) throws DocumentException {
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
        for (PressureData pressureData : recordsContainer.getPressureDataList()) {
            pressureDataViewModel.setPressureData(pressureData);

            table.addCell(pressureDataViewModel.getSystoleStr());
            table.addCell(pressureDataViewModel.getDiastoleStr());
            table.addCell(String.valueOf(pressureData.getSystole() - pressureData.getDiastole()));
            table.addCell(pressureDataViewModel.getPulseStr());
            table.addCell("  " +pressureDataViewModel.getArrhythmiaStr());
            table.addCell(DATE_FORMATTER.format(pressureData.getDateTime()));
            table.addCell(TIME_FORMATTER.format(pressureData.getDateTime()));
        }

        float[] columnWidths = new float[] {15f, 15f, 17f, 10f, 16f, 17f, 10f};
        table.setWidths(columnWidths);

        subCatPart.add(table);

    }

    private static void createListOfMeasurements(List listCnt, List listLast) {
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

                final String listItemStr = String.format("%s\n\t%s  %s\n\t%s  %s\n\t%s  %s%s\n\n",
                        title,
                        getResourceString(R.string.statistics_title_values), measureViewModel.getValuesStr(),
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

    private static DbHelper getHelper(){
        return recordsContainer.getDbHelper();
    }

    private static String getResourceString(int id){
        return resources.getString(id);
    }
}
