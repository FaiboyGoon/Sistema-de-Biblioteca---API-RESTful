package app.biblioteca.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin("*")
public class ReservaController {
	@Autowired
	private ReservaService reservaService;
	
	@PostMapping("/save")
	public ResponseEntity<String> save(@Valid @RequestBody Reserva reserva) {
			String mensagem = this.reservaService.save(reserva);
			return new ResponseEntity<>(mensagem, HttpStatus.CREATED);
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<String> update(@Valid @RequestBody Reserva reserva,@PathVariable long id) {
			String mensagem = this.reservaService.update(reserva, id);
			return new ResponseEntity<>(mensagem, HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteById/{id}")
	public ResponseEntity<String> delete(@PathVariable long id) {
			String mensagem = this.reservaService.delete(id);
			return new ResponseEntity<>(mensagem, HttpStatus.OK);
	}
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAll(){
			List<Reserva> lista = this.reservaService.findAll();
			return new ResponseEntity<List<Reserva>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/findById/{id}")
	public ResponseEntity<?> findById(@PathVariable long id){
			Reserva reserva = this.reservaService.findById(id);
			return new ResponseEntity<Reserva>(reserva, HttpStatus.OK);
	}
	
	@GetMapping("/findByDataReservaAfter")
	public ResponseEntity<?> findByDataReservaAfter(@Valid @RequestParam @DateTimeFormat LocalDate data){
			List<Reserva> lista = this.reservaService.findByDataReservaAfter(data);
			return new ResponseEntity<List<Reserva>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/findByDataReservaBefore")
	public ResponseEntity<?> findByDataReservaBefore(@Valid @RequestParam @DateTimeFormat LocalDate data){
			List<Reserva> lista = this.reservaService.findByDataReservaBefore(data);
			return new ResponseEntity<List<Reserva>>(lista, HttpStatus.OK);
	}
	
}
