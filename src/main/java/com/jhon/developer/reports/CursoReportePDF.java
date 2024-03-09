package com.jhon.developer.reports;

import com.jhon.developer.entity.Curso;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;


import java.awt.*;
import java.io.IOException;
import java.util.List;

public class CursoReportePDF {
    private List<Curso> listaCursos;

    public CursoReportePDF(List<Curso> listaCursos){
        this.listaCursos = listaCursos;
    }

    public void writeTableHeader(PdfPTable pdfPTable){

         /**Creando una ueva celda y dando caracteristicas**/
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.blue);
        cell.setPadding(5);
        /**Creando el tipo de fuente de la cabecera*/
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.white);

        /**Creando las cabecera con los datos de nuetra entity**/
        cell.setPhrase(new Phrase("ID",font));
        pdfPTable.addCell(cell);/**Con esto agregamos la celda a la tabla*/
        cell.setPhrase(new Phrase("NOMBRE",font));
        pdfPTable.addCell(cell);
        /**CELL PERSONALIZADO*/
        cell = new PdfPCell();
        cell.setBackgroundColor(Color.blue);
        cell.setPadding(5);
        cell.setPhrase(new Phrase("DESCRIPCION",font));
        pdfPTable.addCell(cell);

        cell.setPhrase(new Phrase("NIVEL",font));
        pdfPTable.addCell(cell);
        cell.setPhrase(new Phrase("PUBLICADO",font));
        pdfPTable.addCell(cell);

    }

    public void writeTableData(PdfPTable pdfPTable){
        /**Aqui estamos generando un foreach, para recorrer
         * la listaCursos.*/
        for (Curso curso:listaCursos){
            /**Aqui estamos colocando dentro de la celda de la tabla,
             * el valor de cada listaCurso*/
            pdfPTable.addCell(String.valueOf(curso.getIdCurso()));
            pdfPTable.addCell(curso.getNombreCurso());
            pdfPTable.addCell(curso.getDescripcionCurso());
            pdfPTable.addCell(curso.getNivelCurso());
            pdfPTable.addCell(String.valueOf(curso.isPublicado()));
        }
    }

    public void export(HttpServletResponse httpServletResponse) throws IOException {
        /**CREAMOS EL DOCUEMNTO PDF Y DEFINIMO EL TAMAÑO*/
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document,httpServletResponse.getOutputStream());
        /**ABRIMOS EL DOCUMENTO PARA ESCRIBIR DENTRO DE EL*/
        document.open();
        /**AQUI SE ESTA COLOCANDO EL TAMAÑO,COLOR,FUENTE DEL TITULO*/
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.blue);
        /**AQUI ESTAMOS PASANDO EL TITULO Y CENTRAMOS*/
        Paragraph p = new Paragraph("Lista de cursos", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        /**AQUI ESTAMOS AGREGANDO AL DOCUMENTO*/
        document.add(p);

        /**AQUI ESTAMOS CREANDO LA TABLA DONDE ESTARAN LOS DATOS
         * DEFINIMOS TAMAÑO,TAMAÑO DE COLUMNAS,*/
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.3f,3.0f,2.0f,2.0f,1.5f});
        table.setSpacingBefore(10);

        /**AGREGAMOS LA TABLA CREADA A LOS METODOS HEADER Y DATA**/
        writeTableHeader(table);
        writeTableData(table);

        /**AGREGAMOS LA TABLA A LA PÁGINA*/
        document.add(table);

        /**CERRAMOS EL DOCUMENTO PARA PODER ABRIR, SI NO SALE ERROR*/
        document.close();

    }

}
