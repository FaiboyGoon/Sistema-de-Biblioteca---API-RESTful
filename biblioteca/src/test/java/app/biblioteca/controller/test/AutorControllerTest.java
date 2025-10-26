package app.biblioteca.controller.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.biblioteca.config.JwtAuthenticationFilter;
import app.biblioteca.config.JwtServiceGenerator;
import app.biblioteca.controller.AutorController;
import app.biblioteca.entity.Autor;
import app.biblioteca.service.AutorService;

@WebMvcTest(AutorController.class)
@AutoConfigureMockMvc(addFilters = false)
class AutorControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AutorService autorService;
    
    @MockitoBean
    JwtServiceGenerator jwtServiceGenerator;

    @MockitoBean
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    ObjectMapper objectMapper;

    Autor autor;

    @BeforeEach
    void setup() {
        autor = new Autor();
        autor.setId(1L);
        autor.setNome("Machado de Assis");
    }


    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário de salvar autor, deve retornar status 201 CREATED e uma mensagem de sucesso")
    void testSaveAutor() throws Exception {
        when(autorService.save(any(Autor.class)))
                .thenReturn("o Autor: 'Machado de Assis' foi salvo com sucesso!");

        mockMvc.perform(post("/api/autores/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(autor)))
                .andExpect(status().isCreated())
                .andExpect(content().string("o Autor: 'Machado de Assis' foi salvo com sucesso!"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário de atualizar autor, deve retornar status 200 OK e uma mensagem de sucesso")
    void testUpdateAutor() throws Exception {
        when(autorService.update(any(Autor.class), eq(1L)))
                .thenReturn("o autor: 'Machado de Assis' de id: '1' foi atualizado com sucesso!");

        mockMvc.perform(put("/api/autores/update/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(autor)))
                .andExpect(status().isOk())
                .andExpect(content().string("o autor: 'Machado de Assis' de id: '1' foi atualizado com sucesso!"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário de deletar autor pelo id, deve retornar uma mensagem de sucesso e status 200 OK")
    void testDeleteAutorById() throws Exception {
        when(autorService.delete(1L))
                .thenReturn("o autor de id: '1' foi deletado com sucesso!");

        mockMvc.perform(delete("/api/autores/deleteById/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("o autor de id: '1' foi deletado com sucesso!"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário de deletar autor pelo nome, deve retornar uma mensagem de sucesso e status 200 OK")
    void testDeleteAutorByNome() throws Exception {
        when(autorService.deleteByNome("Machado"))
                .thenReturn("os autores de nome: 'Machado' foram deletados com sucesso!");

        mockMvc.perform(delete("/api/autores/deleteByNome")
                .param("nome", "Machado"))
                .andExpect(status().isOk())
                .andExpect(content().string("os autores de nome: 'Machado' foram deletados com sucesso!"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário de puxar todos os autores, deve retornar a lista de autores")
    void testFindAllAutores() throws Exception {
        when(autorService.findAll()).thenReturn(Arrays.asList(autor));

        mockMvc.perform(get("/api/autores/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Machado de Assis"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário de procurar um autor pelo id, deve retornar o autor")
    void testFindById() throws Exception {
        when(autorService.findById(1L)).thenReturn(autor);

        mockMvc.perform(get("/api/autores/findById/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Machado de Assis"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário de procurar todos os autores por nome, deve retornar uma lista filtrada")
    void testFindByNomeContainingIgnoreCase() throws Exception {
        when(autorService.findByNomeContainingIgnoreCase("Machado"))
                .thenReturn(Arrays.asList(autor));

        mockMvc.perform(get("/api/autores/findByNome").param("nome", "Machado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Machado de Assis"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário de tentar salvar um autor com nome nulo, deve retornar retornar status 400 Bad Request")
    void testSaveAutorValidationError() throws Exception {
        Autor invalid = new Autor();
        mockMvc.perform(post("/api/autores/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário de tentar encontrar um autor que não existe, deve retornar status 404 Not Found")
    void testFindByIdNotFound() throws Exception {
        when(autorService.findById(999L)).thenThrow(new NoSuchElementException("Not Found"));

        mockMvc.perform(get("/api/autores/findById/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Objeto não encontrado"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário de tentar puxar a lista de autores enquanto vazia, deve retornar lista vazia")
    void testFindAllEmptyList() throws Exception {
        when(autorService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/autores/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
