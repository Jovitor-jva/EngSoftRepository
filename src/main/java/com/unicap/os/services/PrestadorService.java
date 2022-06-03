package com.unicap.os.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.unicap.os.repositories.PrestadorRepository;
import com.unicap.os.services.exceptions.DataIntegratyViolationException;
import com.unicap.os.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unicap.os.domain.PrestadorDeServico;
import com.unicap.os.domain.dtos.PrestadorDTO;

@Service
public class PrestadorService {

	@Autowired
	private PrestadorRepository repository;

	/*
	 * Busca por ID
	 */
	public PrestadorDeServico findById(Integer id) {
		Optional<PrestadorDeServico> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + PrestadorDeServico.class.getName()));
	}

	/*
	 * Listando todos os Tecnicos
	 */
	public List<PrestadorDeServico> findAll() {
		return repository.findAll();
	}

	/*
	 * Create
	 */
	public PrestadorDTO create(PrestadorDTO obj) {
		if (findByCpf(obj).getClass().equals(PrestadorDeServico.class))
			throw new DataIntegratyViolationException("CPF já cadastrado na base de dados!");
		
		PrestadorDeServico prestadorDeServico = new PrestadorDeServico(null, obj.getNome(), obj.getCpf());
		obj.getPerfis().forEach(x -> prestadorDeServico.addPerfil(x));
		return new PrestadorDTO(repository.save(prestadorDeServico));
	}

	/*
	 * Update
	 */
	public @Valid PrestadorDeServico update(@Valid PrestadorDTO obj, Integer id) {
		PrestadorDeServico oldObj = findById(id);

		if (findByCpf(obj) != null && findByCpf(obj).getId() != id) {
			throw new DataIntegratyViolationException("CPF já cadastrado na base de dados!");
		}

		oldObj.setCpf(obj.getCpf());
		oldObj.setNome(obj.getNome());
		obj.getPerfis().forEach(x -> oldObj.addPerfil(x));
		return repository.save(oldObj);
	}

	/*
	 * Busca por CPF
	 */
	private PrestadorDeServico findByCpf(PrestadorDTO objDTO) {
		PrestadorDeServico obj = repository.findByCpf(objDTO.getCpf());
		if (obj != null) {
			return obj;
		}
		return null;
	}

}
