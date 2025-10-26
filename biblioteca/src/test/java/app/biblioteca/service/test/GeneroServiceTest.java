package app.biblioteca.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import app.biblioteca.entity.Genero;
import app.biblioteca.repository.GeneroRepository;
import app.biblioteca.service.GeneroService;

@ExtendWith(MockitoExtension.class)
class GeneroServiceTest {

    @Mock
    GeneroRepository generoRepository;

    @InjectMocks
    GeneroService generoService;

    Genero genero;

    @BeforeEach
    void setup() {
        genero = new Genero();
        genero.setId(1L);
        genero.setNome("Ficção");
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Testando criar um gênero, deve retornar mensagem de sucesso")
    void testSave() {
        when(generoRepository.save(any(Genero.class))).thenReturn(genero);

        String result = generoService.save(genero);
        assertEquals("o genero: 'Ficção' foi salvo com sucesso!", result);

        verify(generoRepository, times(1)).save(genero);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Testando atualizar um gênero, deve retornar mensagem de sucesso")
    void testUpdate() {
        when(generoRepository.save(any(Genero.class))).thenReturn(genero);

        String result = generoService.update(genero, 1L);
        assertEquals("o genero: 'Ficção' de id: '1' foi atualizado com sucesso!", result);
        assertEquals(1L, genero.getId());

        verify(generoRepository, times(1)).save(genero);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário que testa deletar um gênero pelo ID, deve retornar mensagem de sucesso")
    void testDelete() {
        doNothing().when(generoRepository).deleteById(1L);

        String result = generoService.delete(1L);
        assertEquals("o genero de id: '1' foi deletado com sucesso!", result);

        verify(generoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário que testa deletar um gênero pelo nome, deve retornar mensagem de sucesso")
    void testDeleteByNome() {
        when(generoRepository.deleteByNome("Ficção")).thenReturn(1L);

        String result = generoService.deleteByNome("Ficção");
        assertEquals("os generos de nome: 'Ficção' foram deletados com sucesso!", result);

        verify(generoRepository, times(1)).deleteByNome("Ficção");
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário que testa procurar todos os gêneros com paginação, deve retornar página de resultados")
    void testFindAll() {
        Page<Genero> page = new PageImpl<>(Arrays.asList(genero));
        when(generoRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);

        Page<Genero> result = generoService.findAll(1, 10);
        assertEquals(1, result.getTotalElements());
        assertEquals("Ficção", result.getContent().get(0).getNome());

        verify(generoRepository, times(1)).findAll(PageRequest.of(0, 10));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário que testa procurar um gênero por ID, deve retornar um gênero")
    void testFindById() {
        when(generoRepository.findById(1L)).thenReturn(Optional.of(genero));

        Genero result = generoService.findById(1L);
        assertEquals("Ficção", result.getNome());

        verify(generoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário que testa procurar um gênero por ID inexistente, deve lançar NoSuchElementException")
    void testFindById_NotFound() {
        when(generoRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> generoService.findById(2L));

        verify(generoRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário que testa procurar todos os gêneros por nome, deve retornar lista filtrada")
    void testFindByNome() {
        List<Genero> generos = Arrays.asList(genero);
        when(generoRepository.findByNome("Ficção")).thenReturn(generos);

        List<Genero> result = generoService.findByNome("Ficção");
        assertEquals(1, result.size());
        assertEquals("Ficção", result.get(0).getNome());

        verify(generoRepository, times(1)).findByNome("Ficção");
    }
}
	