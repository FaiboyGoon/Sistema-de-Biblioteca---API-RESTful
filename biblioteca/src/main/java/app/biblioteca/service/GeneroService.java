package app.biblioteca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import app.biblioteca.entity.Genero;
import app.biblioteca.repository.GeneroRepository;

@Service
public class GeneroService {
	@Autowired
	private GeneroRepository generoRepository;
	
	public String save(Genero genero) {
		this.generoRepository.save(genero);
		return "o genero: '"+ genero.getNome()+"' foi salvo com sucesso!";
	}
	public String update(Genero genero, long id) {
		genero.setId(id);
		this.generoRepository.save(genero);
		return "o genero: '"+ genero.getNome()+"' de id: '"+id+ "' foi atualizado com sucesso!";
	}
	
	public String delete(long id) {
		this.generoRepository.deleteById(id);
		return "o genero de id: '" +id+ "' foi deletado com sucesso!";
	}
	
	public String deleteByNome(String nome) {
		this.generoRepository.deleteByNome(nome);
		return "os generos de nome: '" + nome +"' foram deletados com sucesso!";
	}
	
	public Page<Genero> findAll(int numPage, int qtidPorPagina){
		PageRequest config = PageRequest.of(numPage-1, qtidPorPagina);
		return this.generoRepository.findAll(config);
	}
	
	public Genero findById(Long id) {
		Genero genero = this.generoRepository.findById(id).get();
		return genero;
	}
	
	public List<Genero> findByNome(String nome){
		List<Genero> lista = this.generoRepository.findByNome(nome);
		return lista;
	}
}
