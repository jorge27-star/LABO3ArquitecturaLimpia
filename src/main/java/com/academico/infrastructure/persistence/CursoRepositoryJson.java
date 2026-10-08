package com.academico.infrastructure.persistence;

import com.academico.domain.model.Curso;
import com.academico.domain.repository.CursoRepository;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CursoRepositoryJson implements CursoRepository {

    private final Path archivo = Path.of("data", "cursos.json");
    private final Gson gson = new Gson();

    @Override
    public List<Curso> listar() {
        if (Files.notExists(archivo)) {
            return new ArrayList<>();
        }

        try (var reader = Files.newBufferedReader(archivo)) {

            Type tipo = new TypeToken<List<Curso>>() {}.getType();

            List<Curso> cursos = gson.fromJson(reader, tipo);
 
            return cursos != null ? cursos : new ArrayList<>();

        } catch (IOException | JsonParseException e) {
            throw new IllegalStateException("No se pudieron leer los cursos.", e);
        }
    }

    @Override
    public void guardar(List<Curso> cursos) {
        try {
            Files.createDirectories(archivo.getParent());
            try (var writer = Files.newBufferedWriter(archivo)) {
                gson.toJson(cursos, writer);
            }
        } catch (IOException e) {
            throw new IllegalStateException("No se pudieron guardar los cursos.", e);
        }
    }
}