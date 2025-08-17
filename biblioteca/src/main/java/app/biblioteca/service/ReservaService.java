package app.biblioteca.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.biblioteca.entity.Reserva;
import app.biblioteca.repository.ReservaRepository;

@Service
public class ReservaService {
	@Autowired
	private ReservaRepository reservaRepository;
	@Autowired
	private LivroService livroService;

	public String save(Reserva reserva) {
		Long idLivro = reserva.getLivro().getId();
		boolean isDisponivel = this.livroService.verificarDisponibilidade(idLivro);
		if(isDisponivel == true) {
			this.reservaRepository.save(reserva);
			return "A reserva no dia: "+ reserva.getDataReserva() +" foi criada com sucesso!";
		}else {
			throw new RuntimeException("O livro já possui muitas reservas, mais reservas não serão possíveis");
		}
	}

	public String update(Reserva reserva, long id) {
		reserva.setId(id);
		this.reservaRepository.save(reserva);
		return "a reserva no dia:"+ reserva.getDataReserva() +" foi atualizada com sucesso!";
	}

	public String delete(long id) {
		this.reservaRepository.deleteById(id);
		return "a reserva de id:" +id+ " foi deletada com sucesso!";
	}

	public List<Reserva> findAll(){
		List<Reserva> lista = this.reservaRepository.findAll();
		return lista;
	}

	public List<Reserva> findByDataReservaAfter(LocalDate data){
		List<Reserva> lista = this.reservaRepository.findByDataReservaAfter(data);
		return lista;
	}

	public List<Reserva> findByDataReservaBefore(LocalDate data){
		List<Reserva> lista = this.reservaRepository.findByDataReservaBefore(data);
		return lista;
	}

	public Reserva findById(Long id) {
		Reserva reserva = this.reservaRepository.findById(id).get();
		return reserva;
	}
}
