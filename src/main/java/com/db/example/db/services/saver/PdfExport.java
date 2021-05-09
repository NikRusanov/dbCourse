package com.db.example.db.services.saver;

import com.db.example.db.entities.Group;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.Map;


public class PdfExport {

    private final Map<Group, String> teacherMarkStatistic;
    private final String teacherName;
    public PdfExport(Map<Group, String> teacherMarkStatistic, String teacherName) {
        this.teacherMarkStatistic = teacherMarkStatistic;
        this.teacherName = teacherName;
    }


    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4) ;
        PdfWriter.getInstance(document, response.getOutputStream());
        try {
            document.open();
            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            font.setSize(18);
            font.setColor(Color.BLUE);
            Paragraph paragraph = new Paragraph("Teacher " + teacherName + "marks average value", font);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100f);
            table.setWidths(new float[] {4.0f, 4.0f});
            table.setSpacingBefore(10);
            writeTableHeader(table);
            writeTableData(table);
            document.add(table);
        } catch (DocumentException ex) {
            ex.printStackTrace();
        } finally {
            document.close();
        }

    }



    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);
        cell.setPhrase(new Phrase("Group name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Average value", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        teacherMarkStatistic
                .forEach((key, value) -> {
                    table.addCell(key.getName());
                    table.addCell(String.valueOf(value));
                });
    }
}
