package com.unicap.os.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.unicap.os.repositories.ClienteRepository;
import com.unicap.os.repositories.OrdemDeServicoRepository;
import com.unicap.os.repositories.PrestadorRepository;
import com.unicap.os.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unicap.os.domain.Cliente;
import com.unicap.os.domain.OrdemDeServico;
import com.unicap.os.domain.PrestadorDeServico;
import com.unicap.os.domain.dtos.OrdemDeServicoDTO;
import com.unicap.os.domain.enuns.Status;

@Service
public class OSService {

	@Autowired
	private OrdemDeServicoRepository repository;

	@Autowired
	private PrestadorService prestadorService;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private PrestadorRepository prestadorRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	/*
	 * FindById
	 */
	public OrdemDeServico findById(Integer id) {
		Optional<OrdemDeServico> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + OrdemDeServico.class.getName()));
	}

	/*
	 * FindAll
	 */
	public List<OrdemDeServico> findAll() {
		return repository.findAll();
	}

	/*
	 * Create
	 */
	public @Valid OrdemDeServico create(@Valid OrdemDeServicoDTO obj) {
		return repository.save(fromDTO(obj));
	}

	/*
	 * Update
	 */
	public @Valid OrdemDeServico update(@Valid OrdemDeServicoDTO obj, Integer id) {
		OrdemDeServico oldObj = findById(id);
		oldObj = updateData(obj, oldObj);
		return repository.save(oldObj);
	}

	/*
	 * Atualiza dados da OS
	 */
	private OrdemDeServico updateData(@Valid OrdemDeServicoDTO obj, OrdemDeServico oldObj) {
		oldObj.setPrioridade(obj.getPrioridade());
		oldObj.setObservacoes(obj.getObservacoes());
		oldObj.setTecnico(prestadorService.findById(obj.getPrestador()));
		oldObj.setStatus(obj.getStatus());
		
		if (obj.getStatus().equals(Status.ENCERRADO)) {
			oldObj.setDataFechamento(LocalDateTime.now());
		}

		return oldObj;
	}

	/*
	 * Transforma um DTO em Model
	 */
	private OrdemDeServico fromDTO(OrdemDeServicoDTO obj) {
		OrdemDeServico newObj = new OrdemDeServico();
		newObj.setId(obj.getId());
		newObj.setObservacoes(obj.getObservacoes());

		PrestadorDeServico tec = prestadorService.findById(obj.getPrestador());
		tec.getOsList().add(newObj);
		prestadorRepository.save(tec);

		Cliente cli = clienteService.findById(obj.getCliente());
		cli.getOsList().add(newObj);
		clienteRepository.save(cli);

		newObj.setTecnico(tec);
		newObj.setCliente(cli);

		return newObj;
	}
}
