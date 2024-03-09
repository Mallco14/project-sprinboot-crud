package com.jhon.developer.reports;

import com.jhon.developer.entity.Curso;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class CursoReporteEXCEL {

    /**CREACION DEL LIBRO*/
    private XSSFWorkbook workbook;

    /**CREACION DE LA HOJA*/
    private XSSFSheet sheet;

    /**CREACION DE LA LISTA DE CURSOS*/
    private List<Curso> listCursos;

    public CursoReporteEXCEL(List<Curso> listCursos) {
        this.listCursos = listCursos;
        this.workbook= new XSSFWorkbook();
    }

    private void writeHeader(){
        /**CREAMOS UNA HOJA EN EL LIBRRO*/
        sheet = workbook.createSheet("cursos");
        /**CREANDO LA FILA 0, QUE CORRESPONDE AL HEADER*/
        Row row = sheet.createRow(0);
        /**CREANDO UN STYLE DE CELDA*/
        CellStyle style = workbook.createCellStyle();
        /***CREAMOS EL ESTILO*/
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        /**CARGAMOS EL ESTILO DE LA LETRA A LA CELDA*/
        style.setFont(font);

        /**CREAMOS LA CELDA Y AGREGAMOS EL VALOR*/
        /**importando el metodo para crear celdas**/
        createCell(row,0,"ID",style);
        createCell(row,1,"NOMBRE",style);
        createCell(row,2,"DESCRIPCION",style);
        createCell(row,3,"NIVEL",style);
        createCell(row,4,"PUBLICADO",style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style){
        /**Aqui ajustamos de forma automatica el ancho de la columna, con la hoja*/
        sheet.autoSizeColumn(columnCount);
        /**Creamos celda a partir de las columnas*/
        Cell cell = row.createCell(columnCount);

        /**CONVIERTIENDO TODOS LOS DATOS A STRING, PARA QUE PUEDAN ALMACENARLOS
         * TENER EN CUENTA QUE ESTOS*/
        if(value instanceof Integer){
            cell.setCellValue((Integer)value);

        }else if(value instanceof Boolean){
            System.out.println("conviertiendovalor");
            cell.setCellValue((Boolean)value);
        }else {
            cell.setCellValue((String) value);
        }
        /**Pasamos el style que estamos colocando como parametro.*/
        cell.setCellStyle(style);


    }

    private void writeDataLines(){
        /**Declaramos la fila que empezara llenar la data
         * es 1, porque recordemos que la 0 es la cabecera*/
        int rowCount = 1;
        /**Creamos el estilo en nuestro libro*/
        CellStyle style = workbook.createCellStyle();
        /**Creamos el estilo font*/
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        /**lo seteamos al style del libro*/
        style.setFont(font);

        /**Creamos un for each, porque recorrera la lista de cursos,
         * Esta a la vez va creando una celda*/
        for(Curso curso: listCursos){
            /**En la hoja creamos una fila, y esta sumara 1 cada vez que pase
             * en el primer pase es 1*/
            Row row = sheet.createRow(rowCount++);
            /**Declaramos como inicio columnCount0, ya que cuando entre sera 1
             * obtenemos los datos y el estilo*/
            int columnCount = 0;
            createCell(row,columnCount++,curso.getIdCurso(),style);
            createCell(row,columnCount++,curso.getNombreCurso(),style);
            createCell(row,columnCount++,curso.getDescripcionCurso(),style);
            createCell(row,columnCount++,curso.getNivelCurso(),style);
            createCell(row,columnCount++,curso.isPublicado(),style);

        }
    }

    public void export(HttpServletResponse response) throws IOException {
        /** LLAMAMOS A LOS METODOS CREADOS*/
        writeHeader();
        writeDataLines();
        /**Este obtiene el flujo de salida del servele, que se usa
         * para dar datos de repuesta*/
        ServletOutputStream outputStream = response.getOutputStream();

        /**Escribe el contenido de trabajo en el libro de excel*/
        workbook.write(outputStream);
        /**Cuando se escribe se cierra el libro*/
        workbook.close();

        /**Se cierra el flujo de salida*/
        outputStream.close();

    }
}
