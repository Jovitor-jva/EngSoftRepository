package com.example.demo.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.br.CPF;
import com.example.demo.domain.OrdemServico;
import com.example.demo.domain.PrestadorDeServico;
import com.example.demo.domain.enuns.Perfil;

public class PrestadorDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	@NotEmpty(message = "O campo NOME é mandatório")
	private String nome;

	@NotEmpty(message = "O campo CPF é mandatório")
	@CPF
	private String cpf;

	private String telefone;

	private Set<Integer> perfis = new HashSet<>();
	private List<OrdemServico> osList = new ArrayList<>();

	public PrestadorDTO() {
		super();
	}

	public PrestadorDTO(PrestadorDeServico obj) {
		super();
		this.id = obj.getId();
		this.nome = obj.getNome();
		this.cpf = obj.getCpf();
		this.osList = obj.getOsList();
		obj.getPerfis().forEach(x -> addPerfil(x));
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Set<Perfil> getPerfis() {
		return perfis.stream().map(x -> Perfil.toEnum(x)).collect(Collectors.toSet());
	}

	public void addPerfil(Perfil perfil) {
		this.perfis.add(perfil.getCodigo());
	}

	public List<OrdemServico> getOsList() {
		return osList;
	}

	public void setOsList(List<OrdemServico> osList) {
		this.osList = osList;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public void setPerfis(Set<Integer> perfis) {
		this.perfis = perfis;
	}

}