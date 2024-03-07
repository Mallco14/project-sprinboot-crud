package com.jhon.developer.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.cglib.proxy.Factory;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "tbl_curso")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCurso;
    @Column(length = 128,nullable = false)
    private String nombreCurso;
    @Column(length = 257,nullable = false)
    private String descripcionCurso;
    @Column(nullable= false)
    private String nivelCurso;
    @Column(name="estado_publicado")
    private boolean isPublicado;

}
