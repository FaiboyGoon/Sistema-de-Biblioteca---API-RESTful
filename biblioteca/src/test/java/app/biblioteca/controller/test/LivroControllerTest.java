package app.biblioteca.controller.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;
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
import app.biblioteca.controller.LivroController;
import app.biblioteca.entity.Autor;
import app.biblioteca.entity.Genero;
import app.biblioteca.entity.Livro;
import app.biblioteca.service.LivroService;

@WebMvcTest(LivroController.class)
@AutoConfigureMockMvc(addFilters = false)
class LivroControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    LivroService livroService;

    @MockitoBean
    JwtServiceGenerator jwtServiceGenerator;

    @MockitoBean
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    ObjectMapper objectMapper;

    Livro livro;
    Autor autor;
    Genero genero;

    @BeforeEach
    void setup() {
        autor = new Autor();
        autor.setId(1L);
        autor.setNome("J. K. Rowling");

        genero = new Genero();
        genero.setId(1L);
        genero.setNome("Fantasia");

        livro = new Livro();
        livro.setId(1L);
        livro.setTitulo("Harry Potter e a Pedra Filosofal");
        livro.setAutor(autor);
        livro.setGeneros(Arrays.asList(genero));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário de salvar livro, deve retornar uma mensagem de sucesso e status 201 CREATED")
    void testSaveLivro() throws Exception {
        when(livroService.save(any(Livro.class)))
            .thenReturn("o livro: 'Harry Potter e a Pedra Filosofal' do autor: 'J. K. Rowling' foi salvo com sucesso!");

        mockMvc.perform(post("/api/livros/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(livro)))
                .andExpect(status().isCreated())
                .andExpect(content().string("o livro: 'Harry Potter e a Pedra Filosofal' do autor: 'J. K. Rowling' foi salvo com sucesso!"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário de atualizar livro, deve retornar uma mensagem de sucesso status 200 OK")
    void testUpdateLivro() throws Exception {
        when(livroService.update(any(Livro.class), eq(1L)))
            .thenReturn("o livro: 'Harry Potter e a Pedra Filosofal' do autor: 'J. K. Rowling' foi atualizado com sucesso!");

        mockMvc.perform(put("/api/livros/update/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(livro)))
                .andExpect(status().isOk())
                .andExpect(content().string("o livro: 'Harry Potter e a Pedra Filosofal' do autor: 'J. K. Rowling' foi atualizado com sucesso!"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário de deletar livro pelo id, deve retornar uma mensagem de sucesso e status 200 OK")
    void testDeleteLivroById() throws Exception {
        when(livroService.delete(1L))
            .thenReturn("o livro de id: '1' foi deletado com sucesso!");

        mockMvc.perform(delete("/api/livros/deleteById/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("o livro de id: '1' foi deletado com sucesso!"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário de puxar todos os livros, deve retornar a lista de livros")
    void testFindAllLivros() throws Exception {
        when(livroService.findAll()).thenReturn(Arrays.asList(livro));

        mockMvc.perform(get("/api/livros/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].titulo").value("Harry Potter e a Pedra Filosofal"))
                .andExpect(jsonPath("$[0].autor.nome").value("J. K. Rowling"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário de procurar livro pelo id, deve retornar o livro")
    void testFindById() throws Exception {
        when(livroService.findById(1L)).thenReturn(livro);

        mockMvc.perform(get("/api/livros/findById/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titulo").value("Harry Potter e a Pedra Filosofal"))
                .andExpect(jsonPath("$.autor.nome").value("J. K. Rowling"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário de procurar livros por gênero, deve retornar lista filtrada")
    void testFindByGenerosNome() throws Exception {
        when(livroService.findByGenerosNome("Fantasia")).thenReturn(Arrays.asList(livro));

        mockMvc.perform(get("/api/livros/findByGenerosNome")
                .param("nome", "Fantasia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Harry Potter e a Pedra Filosofal"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário de procurar livros por título, deve retornar lista filtrada")
    void testFindByTituloContainingIgnoreCase() throws Exception {
        when(livroService.findByTituloContainingIgnoreCase("Harry Potter")).thenReturn(Arrays.asList(livro));

        mockMvc.perform(get("/api/livros/findByTitulo")
                .param("titulo", "Harry Potter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Harry Potter e a Pedra Filosofal"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário de procurar livros mais populares, deve retornar lista com mínimo de reservas")
    void testFindLivrosMaisPopulares() throws Exception {
        when(livroService.findLivrosMaisPopulares(5)).thenReturn(Arrays.asList(livro));

        mockMvc.perform(get("/api/livros/findLivrosMaisPopulares")
                .param("minReservas", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Harry Potter e a Pedra Filosofal"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário de tentar salvar livro inválido, deve retornar status 400 Bad Request")
    void testSaveLivroValidationError() throws Exception {
        Livro invalid = new Livro();
        mockMvc.perform(post("/api/livros/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário de tentar encontrar livro inexistente, deve retornar status 404 Not Found")
    void testFindByIdNotFound() throws Exception {
        when(livroService.findById(999L)).thenThrow(new NoSuchElementException("Not Found"));

        mockMvc.perform(get("/api/livros/findById/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Objeto não encontrado"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário de puxar a lista de livros enquanto vazia, deve retornar lista vazia")
    void testFindAllEmptyList() throws Exception {
        when(livroService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/livros/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
