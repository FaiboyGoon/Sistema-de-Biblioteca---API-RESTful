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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.biblioteca.config.JwtAuthenticationFilter;
import app.biblioteca.config.JwtServiceGenerator;
import app.biblioteca.controller.GeneroController;
import app.biblioteca.entity.Genero;
import app.biblioteca.service.GeneroService;

@WebMvcTest(GeneroController.class)
@AutoConfigureMockMvc(addFilters = false)
class GeneroControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    GeneroService generoService;

    @MockitoBean
    JwtServiceGenerator jwtServiceGenerator;

    @MockitoBean
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    ObjectMapper objectMapper;

    Genero genero;

    @BeforeEach
    void setup() {
        genero = new Genero();
        genero.setId(1L);
        genero.setNome("Romance");
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário de salvar gênero, deve retornar uma mensagem de sucesso e status 201 CREATED")
    void testSaveGenero() throws Exception {
        when(generoService.save(any(Genero.class)))
                .thenReturn("o genero: 'Romance' foi salvo com sucesso!");

        mockMvc.perform(post("/api/generos/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(genero)))
                .andExpect(status().isCreated())
                .andExpect(content().string("o genero: 'Romance' foi salvo com sucesso!"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário de atualizar gênero, deve retornar uma mensagem de sucesso e status 200 OK")
    void testUpdateGenero() throws Exception {
        when(generoService.update(any(Genero.class), eq(1L)))
                .thenReturn("o genero: 'Romance' de id: '1' foi atualizado com sucesso!");

        mockMvc.perform(put("/api/generos/update/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(genero)))
                .andExpect(status().isOk())
                .andExpect(content().string("o genero: 'Romance' de id: '1' foi atualizado com sucesso!"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário de deletar gênero pelo id, deve retornar mensagem de sucesso e status 200 OK")
    void testDeleteGeneroById() throws Exception {
        when(generoService.delete(1L))
                .thenReturn("o genero de id: '1' foi deletado com sucesso!");

        mockMvc.perform(delete("/api/generos/deleteById/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("o genero de id: '1' foi deletado com sucesso!"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário de deletar gênero pelo nome, deve retornar mensagem de sucesso e status 200 OK")
    void testDeleteGeneroByNome() throws Exception {
        when(generoService.deleteByNome("Romance"))
                .thenReturn("os generos de nome: 'Romance' foram deletados com sucesso!");

        mockMvc.perform(delete("/api/generos/deleteByNome")
                .param("nome", "Romance"))
                .andExpect(status().isOk())
                .andExpect(content().string("os generos de nome: 'Romance' foram deletados com sucesso!"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário de puxar todos os gêneros (paginados por causa da modal no front), deve retornar a lista de gêneros")
    void testFindAllGeneros() throws Exception {
        Page<Genero> page = new PageImpl<>(List.of(genero));
        when(generoService.findAll(1, 10)).thenReturn(page);

        mockMvc.perform(get("/api/generos/findAll/{numPage}/{qtidPorPagina}", 1, 10))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].nome").value("Romance"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário de procurar gênero por id, deve retornar o gênero")
    void testFindById() throws Exception {
        when(generoService.findById(1L)).thenReturn(genero);

        mockMvc.perform(get("/api/generos/findById/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Romance"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário de procurar gêneros por nome, deve retornar lista filtrada")
    void testFindByNome() throws Exception {
        when(generoService.findByNome("Romance")).thenReturn(Arrays.asList(genero));

        mockMvc.perform(get("/api/generos/findByNome")
                .param("nome", "Romance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Romance"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário de puxar lista vazia de gêneros, deve retornar lista vazia e status 200 OK")
    void testFindAllEmptyList() throws Exception {
        Page<Genero> emptyPage = new PageImpl<>(Collections.emptyList());
        when(generoService.findAll(1, 10)).thenReturn(emptyPage);

        mockMvc.perform(get("/api/generos/findAll/{numPage}/{qtidPorPagina}", 1, 10))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário de salvar gênero com dados inválidos, deve retornar status 400 Bad Request")
    void testSaveGeneroValidationError() throws Exception {
        Genero invalid = new Genero();

        mockMvc.perform(post("/api/generos/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário de procurar gênero inexistente, deve retornar status 404 Not Found")
    void testFindByIdNotFound() throws Exception {
        when(generoService.findById(999L)).thenThrow(new NoSuchElementException("Not found"));

        mockMvc.perform(get("/api/generos/findById/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Objeto não encontrado"));
    }
}
