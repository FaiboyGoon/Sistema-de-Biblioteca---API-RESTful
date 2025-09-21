package app.biblioteca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.biblioteca.entity.Autor;
import app.biblioteca.repository.AutorRepository;

@Service
public class AutorService {
	@Autowired
	private AutorRepository autorRepository;
	
	public String save(Autor autor) {
		this.autorRepository.save(autor);
		return "o Autor: '"+ autor.getNome()+"' foi salvo com sucesso!";
	}
	public String update(Autor autor, long id) {
		autor.setId(id);
		this.autorRepository.save(autor);
		return "o autor: '"+ autor.getNome()+"' de id: '"+id+ "' foi atualizado com sucesso!";
	}
	
	public String delete(long id) {
		this.autorRepository.deleteById(id);
		return "o autor de id: '" +id+ "' foi deletado com sucesso!";
	}
	
	public String deleteByNome(String nome) {
		this.autorRepository.deleteByNome(nome);
		return "os autores de nome: '" + nome +"' foram deletados com sucesso!";
	}
	
	public List<Autor> findAll(){
		List<Autor> lista = this.autorRepository.findAll();
		return lista;
	}
	
	public Autor findById(Long id) {
		Autor autor = this.autorRepository.findById(id).get();
		return autor;
	}
	
	public List<Autor> findByNomeContainingIgnoreCase(String nome){
		List<Autor> lista = this.autorRepository.findByNomeContainingIgnoreCase(nome);
		return lista;
	}
}
