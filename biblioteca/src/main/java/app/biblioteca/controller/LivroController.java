package app.biblioteca.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.biblioteca.entity.Livro;
import app.biblioteca.service.LivroService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/livros")
public class LivroController {
	@Autowired
	private LivroService livroService;

	@PostMapping("/save")
	public ResponseEntity<String> save(@Valid @RequestBody Livro livro) {
		try {
			String mensagem = this.livroService.save(livro);
			return new ResponseEntity<>(mensagem, HttpStatus.CREATED);
		}catch(Exception e) {
			return new ResponseEntity<>("Erro ao salvar o Livro! Erro: " +e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<String> update(@Valid @RequestBody Livro livro,@PathVariable long id) {
		try {
			String mensagem = this.livroService.update(livro, id);
			return new ResponseEntity<>(mensagem, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("Erro ao atualizar o Livro! Erro: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/deleteById/{id}")
	public ResponseEntity<String> delete(@PathVariable long id) {
		try {
			String mensagem = this.livroService.delete(id);
			return new ResponseEntity<>(mensagem, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("Erro ao deletar o Livro! Erro: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAll(){
		try {
			List<Livro> lista = this.livroService.findAll();
			return new ResponseEntity<List<Livro>>(lista, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/findByGenerosNome")
	public ResponseEntity<?> findByGenerosNome(@RequestParam String nome){
		try {
			List<Livro> lista = this.livroService.findByGenerosNome(nome);
			return new ResponseEntity<List<Livro>>(lista, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/findByTitulo")
	public ResponseEntity<?> findByTituloContainingIgnoreCase(@RequestParam String titulo){
		try {
			List<Livro> lista = this.livroService.findByTituloContainingIgnoreCase(titulo);
			return new ResponseEntity<List<Livro>>(lista, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/findLivrosMaisPopulares")
	public ResponseEntity<?> findLivrosMaisPopulares(@RequestParam int minReservas){
		try {
			List<Livro> lista = this.livroService.findLivrosMaisPopulares(minReservas);
			return new ResponseEntity<List<Livro>>(lista, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/findById/{id}")
	public ResponseEntity<?> findById(@PathVariable long id){
		try {
			Livro livro = this.livroService.findById(id);
			return new ResponseEntity<Livro>(livro, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
