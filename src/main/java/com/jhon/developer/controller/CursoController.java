package com.jhon.developer.controller;


import com.jhon.developer.entity.Curso;
import com.jhon.developer.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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




}
