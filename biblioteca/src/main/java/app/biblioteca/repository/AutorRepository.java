package app.biblioteca.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import app.biblioteca.entity.Autor;

public interface AutorRepository extends JpaRepository<Autor, Long>{
	Autor findByNomeContainingIgnoreCase(String nome);

}
