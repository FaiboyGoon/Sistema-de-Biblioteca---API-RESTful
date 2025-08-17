package app.biblioteca.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

import app.biblioteca.entity.Reserva;
import app.biblioteca.service.ReservaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {
	@Autowired
	private ReservaService reservaService;
	
	@PostMapping("/save")
	public ResponseEntity<String> save(@Valid @RequestBody Reserva reserva) {
		try {
			String mensagem = this.reservaService.save(reserva);
			return new ResponseEntity<>(mensagem, HttpStatus.CREATED);
		}catch(Exception e) {
			return new ResponseEntity<>("Erro ao criar a Reserva: " +e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<String> update(@Valid @RequestBody Reserva reserva,@PathVariable long id) {
		try {
			String mensagem = this.reservaService.update(reserva, id);
			return new ResponseEntity<>(mensagem, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("Erro ao atualizar a reserva "+ e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/deleteById/{id}")
	public ResponseEntity<String> delete(@PathVariable long id) {
		try {
			String mensagem = this.reservaService.delete(id);
			return new ResponseEntity<>(mensagem, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("Erro ao deletar a Reserva: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAll(){
		try {
			List<Reserva> lista = this.reservaService.findAll();
			return new ResponseEntity<List<Reserva>>(lista, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/findById/{id}")
	public ResponseEntity<?> findById(@PathVariable long id){
		try {
			Reserva reserva = this.reservaService.findById(id);
			return new ResponseEntity<Reserva>(reserva, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/findByDataReservaAfter")
	public ResponseEntity<?> findByDataReservaAfter(@Valid @RequestParam @DateTimeFormat LocalDate data){
		try {
			List<Reserva> lista = this.reservaService.findByDataReservaAfter(data);
			return new ResponseEntity<List<Reserva>>(lista, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/findByDataReservaBefore")
	public ResponseEntity<?> findByDataReservaBefore(@Valid @RequestParam @DateTimeFormat LocalDate data){
		try {
			List<Reserva> lista = this.reservaService.findByDataReservaBefore(data);
			return new ResponseEntity<List<Reserva>>(lista, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
}
