package app.biblioteca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.biblioteca.entity.Genero;
import app.biblioteca.entity.Livro;
import app.biblioteca.repository.LivroRepository;

@Service
public class LivroService {

	@Autowired
	private LivroRepository livroRepository;
	
	public String save(Livro livro) {
		this.livroRepository.save(livro);
		return "o Livro: '"+ livro.getTitulo()+"' do autor: '"+ livro.getAutor().getNome()+"' foi salvo com sucesso!";
	}
	
	public String update(Livro livro, long id) {
		livro.setId(id);
		this.livroRepository.save(livro);
		return "o Livro: '"+ livro.getTitulo()+"' do autor: '"+ livro.getAutor().getNome()+"' foi atualizado com sucesso!";
	}
	
	public String delete(long id) {
		this.livroRepository.deleteById(id);
		return "o Livro de id: '" +id+ "' foi deletado com sucesso!";
	}
	
	public List<Livro> findAll(){
		List<Livro> lista = this.livroRepository.findAll();
		return lista;
	}
	
	public List<Livro> findByGenerosNome(String nome){
		Genero genero = new Genero();
		genero.setNome(nome);
		return this.livroRepository.findByGenerosNome(genero.getNome());
	}
	
	public List<Livro> findByTituloContainingIgnoreCase(String titulo){
		List<Livro> lista = this.livroRepository.findByTituloContainingIgnoreCase(titulo);
		return lista;
	}
	
	public List<Livro> findLivrosMaisPopulares(int minReservas){
		List<Livro> lista = this.livroRepository.findLivrosMaisPopulares(minReservas);
		return lista;
	}
	
	public Livro findById(Long id) {
		Livro livro = this.livroRepository.findById(id).get();
		return livro;
	}
	
	public boolean verificarDisponibilidade(Long livroId) {
		Livro livro = this.livroRepository.findById(livroId).get();
		if (livro.getReservas().size() > 4) {
			return false;
		}else {
			return true;
		}
	}
}
