package com.unicap.os.services;

import java.util.List;
import java.util.Optional;

import com.unicap.os.repositories.ClienteRepository;
import com.unicap.os.services.exceptions.DataIntegratyViolationException;
import com.unicap.os.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unicap.os.domain.Cliente;
import com.unicap.os.domain.dtos.ClienteDTO;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repository;

	// Busca por ID
	public Cliente findById(Integer id) {
		Optional<Cliente> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}

	// Lista todos clientes
	public List<Cliente> findAll() {
		return repository.findAll();
	}

	// Create
	public Cliente create(ClienteDTO obj) {
		if (findByCpf(obj).getClass().equals(Cliente.class)) {
			throw new DataIntegratyViolationException("CPF já cadastrado na base de dados!");
		}
		
		Cliente newObj = new Cliente(null, obj.getNome(), obj.getCpf(), obj.getTelefone());
		return repository.save(newObj);
	}
	
	// Update
	public Cliente update(ClienteDTO obj, Integer id) {
		Cliente oldObj = findById(id);
		
		if (findByCpf(obj) != null && findByCpf(obj).getId() != id) {
			throw new DataIntegratyViolationException("CPF já cadastrado na base de dados!");
		}
		
		oldObj.setNome(obj.getNome());
		oldObj.setCpf(obj.getCpf());
		oldObj.setTelefone(obj.getTelefone());
		
		return repository.save(oldObj);
	}

	// Busca por CPF
	private Cliente findByCpf(ClienteDTO objDTO) {
		Cliente obj = repository.findByCpf(objDTO.getCpf());
		if (obj != null) {
			return obj;
		}
		return null;
	}

}
