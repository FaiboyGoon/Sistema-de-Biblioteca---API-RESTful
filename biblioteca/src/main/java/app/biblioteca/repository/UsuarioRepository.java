package app.biblioteca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import app.biblioteca.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	Usuario findByEmail(String email);
	List<Usuario> findByNome(String nome);
}
