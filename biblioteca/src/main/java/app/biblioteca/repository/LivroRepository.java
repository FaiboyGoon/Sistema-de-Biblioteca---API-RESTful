package app.biblioteca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import app.biblioteca.entity.Livro;

public interface LivroRepository extends JpaRepository<Livro, Long>{
	List<Livro> findByGenerosNome(String nome);
	List<Livro> findByTituloContainingIgnoreCase(String titulo);
	
	@Query("FROM Livro l WHERE SIZE(l.reservas) > :minReservas")
	List<Livro> findLivrosMaisPopulares(int minReservas);

}
