package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


//import com.example.demo.domain.Pessoa;
import com.example.demo.dtos.ClienteDTO;
import com.example.demo.domain.Cliente;
//import com.example.demo.repositories.PessoaRepository;
//import com.example.demo.repositories.ClienteRepository;
import com.example.demo.services.exceptions.ObjectNotFoundException;

public class ClienteService {

    private static final Logger LOG = LoggerFactory.getLogger(ClienteService.class);

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    /*
     * Busca Cliente pelo ID
     */
    public Cliente findById(Integer id) {
        LOG.info("Service - BUSCANDO CLIENTE POR ID");
        Optional<Cliente> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
    }

    /*
     * Busca todos os Clientes da base de dados
     */
    public List<Cliente> findAll() {
        LOG.info("Service - BUSCANDO TODOS OS CLIENTES DO BANCO");
        return repository.findAll();
    }

    /*
     * Cria um Cliente
     */
    public Cliente create(ClienteDTO objDTO) {
        LOG.info("Service - CRIANDO NOVO CLIENTE");
        if (findByCPF(objDTO) != null) {
            throw new DataIntegratyViolationException("CPF já cadastrado na base de dados!");
        }

        return repository.save(new Cliente(null, objDTO.getNome(), objDTO.getCpf(), objDTO.getTelefone(),
                encoder.encode(objDTO.getSenha())));
    }

    /*
     * Atualiza um Cliente
     */
    public Cliente update(Integer id, @Valid ClienteDTO objDTO) {
        LOG.info("Service - ATUALIZANDO CLIENTE");
        Cliente oldObj = findById(id);

        if (findByCPF(objDTO) != null && findByCPF(objDTO).getId() != id) {
            throw new DataIntegratyViolationException("CPF já cadastrado na base de dados!");
        }

        oldObj.setNome(objDTO.getNome());
        oldObj.setCpf(objDTO.getCpf());
        oldObj.setTelefone(objDTO.getTelefone());
        return repository.save(oldObj);
    }

    /*
     * Deleta um Cliente pelo ID
     */
    public void delete(Integer id) {
        LOG.info("Service - DELETANDO CLIENTE");
        Cliente obj = findById(id);

        if (obj.getOsList().size() > 0) {
            throw new DataIntegratyViolationException("Pessoa possui Ordens de Serviço, não pode ser deletado!");
        }

        repository.deleteById(id);
    }

    /*
     * Busca Cliente pelo CPF
     */
    private Cliente findByCPF(ClienteDTO objDTO) {
        Cliente obj = pessoaRepository.findByCPF(objDTO.getCpf());

        if (obj != null) {
            return obj;
        }
        return null;
    }
}