package com.example.demo.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Cliente;
import com.example.demo.domain.OrdemServico;
import com.example.demo.domain.PrestadorDeServico;
import com.example.demo.domain.enuns.Prioridade;
import com.example.demo.domain.enuns.Status;
import com.example.demo.dtos.OrdemDeServicoDTO;
//import com.example.demo.repositories.OSRepository;
import com.example.demo.services.exceptions.ObjectNotFoundException;

public class OsService {

    @Autowired
	private OSRepository repository;

	@Autowired
	private PrestadorService prestadorService;

	@Autowired
	private ClienteService clienteService;

	public OrdemServico findById(Integer id) {
		Optional<OrdemServico> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + OrdemServico.class.getName()));
	}

	public List<OrdemServico> findAll() {
		return repository.findAll();
	}

	public OrdemServico create(@Valid OrdemDeServicoDTO obj) {
		return fromDTO(obj);
	}

	public OrdemServico update(@Valid OrdemDeServicoDTO obj) {
		findById(obj.getId());
		return fromDTO(obj);
	}
	
	private OrdemServico fromDTO(OrdemDeServicoDTO obj) {
		OrdemServico newObj = new OrdemServico();
		newObj.setId(obj.getId());
		newObj.setObservacoes(obj.getObservacoes());
		newObj.setPrioridade(Prioridade.toEnum(obj.getPrioridade().getCodigo()));
		newObj.setStatus(Status.toEnum(obj.getStatus().getCodigo()));

		PrestadorDeServico tec = prestadorService.findById(obj.getTecnico());
		Cliente cli = clienteService.findById(obj.getCliente());

		newObj.setTecnico(tec);
		newObj.setCliente(cli);

		if(newObj.getStatus().getCodigo().equals(2)) {
			newObj.setDataFechamento(LocalDateTime.now());
		}
		
		return repository.save(newObj);
	}

    
}
