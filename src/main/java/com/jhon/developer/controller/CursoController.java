package com.jhon.developer.controller;


import com.jhon.developer.entity.Curso;
import com.jhon.developer.reports.CursoReporteEXCEL;
import com.jhon.developer.reports.CursoReportePDF;
import com.jhon.developer.repository.CursoRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class CursoController {
    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    public String home(){
        return "redirect:/cursos";
    }

    @GetMapping("/cursos")
    public String listarCursos(Model model){
        List<Curso> cursos = cursoRepository.findAll();
        model.addAttribute("cursos", cursos);
        return "/cursos";
    }
    @GetMapping("/cursos/nuevo")
    public String agregarCurso(Model model){
        Curso curso = new Curso();
        curso.setPublicado(true);
        model.addAttribute("curso",curso);
        model.addAttribute("pageTitle","Nuevo Curso");
        return "curso_form";
    }

    /**
     * ESTE METODO ES PARA GUARDAR EN LA BASE DE DATOS, SEA PARA
     * CREAR O ACTUALIZAR
     * */
    @PostMapping("/cursos/save")
    public String guardarCurso(Curso curso, RedirectAttributes redirectAttributes){
        try {
            if(curso.getIdCurso() == null){
                cursoRepository.save(curso);
                System.out.println(curso.toString());
                redirectAttributes.addFlashAttribute("message", "El curso ha sido guardado con exito");
            }else {
                cursoRepository.save(curso);
                System.out.println(curso.toString());
                redirectAttributes.addFlashAttribute("message", "El curso ha sido actualizado con exito");
            }

        }catch (Exception e){
            redirectAttributes.addAttribute("message", e.getMessage());

        }
        return "redirect:/cursos";
    }

    /**actualizar**/

    @GetMapping("cursos/{id}")
    public String editarCurso(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes){
        try {
            Curso curso_id = cursoRepository.findById(id).get();
            model.addAttribute("pageTitle", "Editar curso : " + id);
            model.addAttribute("curso",curso_id);
            return "curso_form";
        }catch (Exception e){
            redirectAttributes.addAttribute("message", e.getMessage());
        }
        return "redirect:/cursos";
    }

    @GetMapping("/cursos/delete/{id}")
    public String eliminarCurso(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes){
            try {
                cursoRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("message","El curso fue eliminado");
                return "redirect:/cursos";
            }catch (Exception e){
                redirectAttributes.addAttribute("message", e.getMessage());
            }
            return "redirect:/cursos";

    }
    @GetMapping("/export/pdf")
    public void generarReportePDF(HttpServletResponse response) throws IOException {
            /**Configuración del tipo de contenido a "application/pdf"**/
            response.setContentType("application/pdf");
            /**Creación de un formato de fecha para incluir en el nombre del archivo**/
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date());

            /**ESTE INDICA DE COMO DEBE MANEJARSE EL CONTENIDO
             * SI DEBE MOSTRARLO EN LINEA O DESCARGA*/
            String headerKey = "Content-Disposition";

            /**AQUI INDICAMOS CON EL ATTACHEMENTE, QUE ES PARA DESCARGAS
             * Y FILENAME ARMAMOS EL ARCHIVO + EXTENSION*/
            String headerValue="attachment; filename=cursos" + currentDateTime +".pdf";
            response.setHeader(headerKey,headerValue);

            /**CREAMOS LA LISTA DE LOS CURSOS A MOSTRAR*/
            List<Curso> listCurso = cursoRepository.findAll();
            /**PASAMOS LA LISTA A LA VARIBLE DE CURSO REPORTES, PARA QUE PUEDA USARSE EN SUS METODOS*/
            CursoReportePDF cursoReportePDF = new CursoReportePDF(listCurso);
            /**LLAMAMOS AL METODO EXPORT QUE REALIZA TODA LA LOGICA**/
            cursoReportePDF.export(response);

    }

    @GetMapping("/export/excel")
    public void generalReporteExcel(HttpServletResponse response) throws IOException {
        //Declaramos el tipo de valor que va recibir
        response.setContentType("application/octet-stream");
        //Generamos la fecha para ponerle en el nombre del archivo(opcional)
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = format.format(new Date());

        //Le indicamos que tipo de contenido es y como lo manejaremos
        response.setHeader("Content-Disposition","attachment;filename=cursos"+currentDateTime+".xlsx");
        List<Curso> cursos = cursoRepository.findAll();
        //Llamamos a la clase del reporteExcel y le mandamos la lista de cursos
        CursoReporteEXCEL cursoReporteEXCEL = new CursoReporteEXCEL(cursos);

        //Llamamos al metodo para que exporte el excel
        cursoReporteEXCEL.export(response);

    }







}
