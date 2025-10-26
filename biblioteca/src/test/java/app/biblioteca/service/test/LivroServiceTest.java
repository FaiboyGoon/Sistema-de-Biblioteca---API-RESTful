package app.biblioteca.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import app.biblioteca.entity.Autor;
import app.biblioteca.entity.Genero;
import app.biblioteca.entity.Livro;
import app.biblioteca.entity.Reserva;
import app.biblioteca.auth.Usuario;
import app.biblioteca.repository.LivroRepository;
import app.biblioteca.service.LivroService;

@ExtendWith(MockitoExtension.class)
class LivroServiceTest {

    @Mock
    LivroRepository livroRepository;

    @InjectMocks
    LivroService livroService;

    Livro livro;
    Autor autor;

    @BeforeEach
    void setup() {
        autor = new Autor();
        autor.setId(1L);
        autor.setNome("Gabriel Garcia Marquez");

        livro = new Livro();
        livro.setId(1L);
        livro.setTitulo("Cem Anos de Solidão");
        livro.setAutor(autor);

        livro.setReservas(new ArrayList<>());

        livro.setGeneros(Arrays.asList(new Genero()));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Testando criar um livro, deve retornar mensagem de sucesso")
    void testSave() {
        when(livroRepository.save(any(Livro.class))).thenReturn(livro);

        String result = livroService.save(livro);
        assertEquals("o livro: 'Cem Anos de Solidão' do autor: 'Gabriel Garcia Marquez' foi salvo com sucesso!", result);

        verify(livroRepository, times(1)).save(livro);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Testando atualizar um livro, deve retornar mensagem de sucesso")
    void testUpdate() {
        when(livroRepository.save(any(Livro.class))).thenReturn(livro);

        String result = livroService.update(livro, 1L);
        assertEquals("o livro: 'Cem Anos de Solidão' do autor: 'Gabriel Garcia Marquez' foi atualizado com sucesso!", result);
        assertEquals(1L, livro.getId());

        verify(livroRepository, times(1)).save(livro);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário que testa deletar um livro pelo ID, deve retornar mensagem de sucesso")
    void testDelete() {
        doNothing().when(livroRepository).deleteById(1L);

        String result = livroService.delete(1L);
        assertEquals("o livro de id: '1' foi deletado com sucesso!", result);

        verify(livroRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário que testa procurar todos os livros, deve retornar lista de livros")
    void testFindAll() {
        List<Livro> livros = Arrays.asList(livro);
        when(livroRepository.findAll()).thenReturn(livros);

        List<Livro> result = livroService.findAll();
        assertEquals(1, result.size());
        assertEquals("Cem Anos de Solidão", result.get(0).getTitulo());

        verify(livroRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário que testa procurar livros por gênero, deve retornar lista filtrada")
    void testFindByGenerosNome() {
        List<Livro> livros = Arrays.asList(livro);
        when(livroRepository.findByGenerosNome("Ficção")).thenReturn(livros);

        List<Livro> result = livroService.findByGenerosNome("Ficção");
        assertEquals(1, result.size());

        verify(livroRepository, times(1)).findByGenerosNome("Ficção");
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário que testa procurar livros por título, deve retornar lista filtrada")
    void testFindByTituloContainingIgnoreCase() {
        List<Livro> livros = Arrays.asList(livro);
        when(livroRepository.findByTituloContainingIgnoreCase("Cem Anos")).thenReturn(livros);

        List<Livro> result = livroService.findByTituloContainingIgnoreCase("Cem Anos");
        assertEquals(1, result.size());

        verify(livroRepository, times(1)).findByTituloContainingIgnoreCase("Cem Anos");
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário que testa procurar livros mais populares, deve retornar lista filtrada")
    void testFindLivrosMaisPopulares() {
        List<Livro> livros = Arrays.asList(livro);
        when(livroRepository.findLivrosMaisPopulares(5)).thenReturn(livros);

        List<Livro> result = livroService.findLivrosMaisPopulares(5);
        assertEquals(1, result.size());

        verify(livroRepository, times(1)).findLivrosMaisPopulares(5);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário que testa procurar um livro por ID, deve retornar um livro")
    void testFindById() {
        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));

        Livro result = livroService.findById(1L);
        assertEquals("Cem Anos de Solidão", result.getTitulo());

        verify(livroRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário que testa procurar um livro por ID inexistente, deve lançar NoSuchElementException")
    void testFindById_NotFound() {
        when(livroRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> livroService.findById(2L));

        verify(livroRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário que testa disponibilidade do livro quando menos de 5 reservas, deve retornar true")
    void testVerificarDisponibilidade_True() {
        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));

        boolean available = livroService.verificarDisponibilidade(1L);
        assertTrue(available);

        verify(livroRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário que testa disponibilidade do livro quando mais de 4 reservas, deve retornar false")
    void testVerificarDisponibilidade_False() {
        for (int i = 0; i < 5; i++) {
            Reserva reserva = new Reserva();
            reserva.setId((long) i);
            reserva.setLivro(livro);
            reserva.setDataReserva(LocalDate.now());
            reserva.setUsuario(null);
            livro.getReservas().add(reserva);
        }

        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));

        boolean available = livroService.verificarDisponibilidade(1L);
        assertFalse(available);

        verify(livroRepository, times(1)).findById(1L);
    }
}
