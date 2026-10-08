package com.academico.application;

import com.academico.domain.model.Curso;
import com.academico.domain.repository.CursoRepository;

import java.util.List;
import java.util.Objects;

public class CursoService {

    private final CursoRepository repository;

    public CursoService(CursoRepository repository) {
        this.repository = Objects.requireNonNull(repository, "El repositorio de cursos es obligatorio.");
    }

    public void registrar(Curso curso) {
        validarCurso(curso);

        List<Curso> cursos = repository.listar();

        boolean idDuplicado = cursos.stream()
                .anyMatch(c -> c.getId() == curso.getId());
        if (idDuplicado) {
            throw new IllegalArgumentException("Ya existe un curso con ese ID.");
        }

        curso.setNombre(curso.getNombre().trim());
        cursos.add(curso);

        repository.guardar(cursos);
    }

    public List<Curso> listar() {
        return repository.listar();
    }

    public boolean actualizar(Curso curso) {
        validarCurso(curso);

        List<Curso> cursos = repository.listar();

        for (Curso c : cursos) {

            if (c.getId() == curso.getId()) {

                c.setNombre(curso.getNombre().trim());
                c.setCreditos(curso.getCreditos());

                repository.guardar(cursos);

                return true;
            }
        }

        return false;
    }

    public boolean eliminar(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que cero.");
        }

        List<Curso> cursos = repository.listar();

        boolean eliminado = cursos.removeIf(c -> c.getId() == id);

        if (eliminado) {
            repository.guardar(cursos);
        }

        return eliminado;
    }

    private void validarCurso(Curso curso) {
        if (curso == null) {
            throw new IllegalArgumentException("El curso es obligatorio.");
        }
        if (curso.getId() <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que cero.");
        }
        if (curso.getNombre() == null || curso.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (curso.getCreditos() <= 0) {
            throw new IllegalArgumentException("Los créditos deben ser mayores que cero.");
        }
    }
}