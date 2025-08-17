package app.biblioteca.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import app.biblioteca.entity.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long>{

	List<Reserva> findByDataReservaAfter(LocalDate data);
	List<Reserva> findByDataReservaBefore(LocalDate data);
	
}
