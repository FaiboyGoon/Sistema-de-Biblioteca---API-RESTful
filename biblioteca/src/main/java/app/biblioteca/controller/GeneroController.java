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

import app.biblioteca.entity.Genero;
import app.biblioteca.service.GeneroService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/generos")
public class GeneroController {
	@Autowired
	private GeneroService generoService;
	
	@PostMapping("/save")
	public ResponseEntity<String> save(@Valid @RequestBody Genero genero) {
		try {
			String mensagem = this.generoService.save(genero);
			return new ResponseEntity<>(mensagem, HttpStatus.CREATED);
		}catch(Exception e) {
			return new ResponseEntity<>("Erro ao salvar o Genero! Erro: " +e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<String> update(@Valid @RequestBody Genero genero,@PathVariable long id) {
		try {
			String mensagem = this.generoService.update(genero, id);
			return new ResponseEntity<>(mensagem, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("Erro ao atualizar o Genero! Erro: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/deleteById/{id}")
	public ResponseEntity<String> delete(@PathVariable long id) {
		try {
			String mensagem = this.generoService.delete(id);
			return new ResponseEntity<>(mensagem, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("Erro ao deletar o Genero! Erro: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/deleteByNome")
	public ResponseEntity<String> deleteByNome(@RequestParam String nome) {
		try {
			String mensagem = this.generoService.deleteByNome(nome);
			return new ResponseEntity<>(mensagem, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("Erro ao deletar o Genero! Erro: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAll(){
		try {
			List<Genero> lista = this.generoService.findAll();
			return new ResponseEntity<List<Genero>>(lista, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/findById/{id}")
	public ResponseEntity<?> findById(@PathVariable long id){
		try {
			Genero genero = this.generoService.findById(id);
			return new ResponseEntity<Genero>(genero, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/findByNome")
	public ResponseEntity<?> findById(@RequestParam String nome){
		try {
			List<Genero> lista = this.generoService.findByNome(nome);
			return new ResponseEntity<List<Genero>>(lista, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
