package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.domain.PrestadorDeServico;
import com.example.demo.domain.enuns.Perfil;
import com.example.demo.dtos.PrestadorDTO;
//import com.example.demo.services.exceptions.DataIntegratyViolationException;
import com.example.demo.services.exceptions.ObjectNotFoundException;


@Service
public class PrestadorService {

	Logger log = LoggerFactory.getLogger(PrestadorService.class);

	@Autowired
	private PrestadorRepository repository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	/*
	 * Busca Tecnico pelo ID
	 */
	public PrestadorService() {}
    
    public PrestadorDeServico findById(Integer id) {
		log.info("SERVICE - BUSCANDO TÉCNICO POR ID");
		Optional<PrestadorDeServico> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + PrestadorDeServico.class.getName()));
	}

	/*
	 * Busca todos os Tecnicos da base de dados
	 */
	public List<PrestadorDeServico> findAll() {
		log.info("SERVICE - BUSCANDO TODOS OD TÉCNICOS");
		return repository.findAll();
	}

	/*
	 * Cria um Tecnico
	 */
	public PrestadorDeServico create(PrestadorDeServico objDTO) {
		log.info("SERVICE - CRIANDO NOVO TÉCNICO");
		if (findByCPF(objDTO) != null) {
			throw new DataIntegratyViolationException("CPF já cadastrado na base de dados!");
		}

		PrestadorDeServico newTec = new PrestadorDeServico(null, objDTO.getNome(), objDTO.getCpf(), objDTO.getTelefone(),
				encoder.encode(objDTO.getSenha()));

		if (objDTO.getPerfis().contains(Perfil.ADMIN)) {
			newTec.addPerfil(Perfil.ADMIN);
		}

		return repository.save(newTec);
	}

	/*
	 * Atualiza um Tecnico
	 */
	public PrestadorDeServico update(Integer id, @Valid PrestadorDTO objDTO) {
		log.info("SERVICE - ATUALIZANDO TÉCNICO");
		PrestadorDeServico oldObj = findById(id);

		if (findByCPF(objDTO) != null && findByCPF(objDTO).getId() != id) {
			throw new DataIntegratyViolationException("CPF já cadastrado na base de dados!");
		}

		oldObj.setNome(objDTO.getNome());
		oldObj.setCpf(objDTO.getCpf());
		oldObj.setTelefone(objDTO.getTelefone());
		if (objDTO.getPerfis().contains(Perfil.ADMIN)) {
			oldObj.addPerfil(Perfil.ADMIN);
		}
		return repository.save(oldObj);
	}

	/*
	 * Deleta um Tecnico pelo ID
	 */
	public void delete(Integer id) {
		log.info("SERVICE - DELETANDO TÉCNICO");
		PrestadorDeServico obj = findById(id);

		if (obj.getOsList().size() > 0) {
			throw new DataIntegratyViolationException("Técnico possui Ordens de Serviço, não pode ser deletado!");
		}

		repository.deleteById(id);
	}

	/*
	 * Busca Tecnico pelo CPF
	 */
	private PrestadorDeServico findByCPF(PrestadorDTO objDTO) {
		log.info("SERVICE - ANALIZANDO SE O CPF ESTÁ CADASTRADO NO BANCO");
		PrestadorDeServico obj = pessoaRepository.findByCPF(objDTO.getCpf());

		if (obj != null) {
			return obj;
		}
		return null;
	}

}