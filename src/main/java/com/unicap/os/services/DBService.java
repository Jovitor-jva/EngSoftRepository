package com.unicap.os.services;

import java.text.ParseException;
import java.util.Arrays;

import com.unicap.os.repositories.ClienteRepository;
import com.unicap.os.repositories.OrdemDeServicoRepository;
import com.unicap.os.repositories.PrestadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unicap.os.domain.Cliente;
import com.unicap.os.domain.OrdemDeServico;
import com.unicap.os.domain.PrestadorDeServico;
import com.unicap.os.domain.enuns.Perfil;

@Service
public class DBService {

	@Autowired
	private PrestadorRepository prestadorRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private OrdemDeServicoRepository osRepository;

	public void instanciaDB() throws ParseException {
		PrestadorDeServico t1 = new PrestadorDeServico(null, "Valdir Cezar", "561.096.320-66");
		t1.addPerfil(Perfil.ADMIN);
		PrestadorDeServico t2 = new PrestadorDeServico(null, "Matheus Henrique", "07854125698");

		Cliente c1 = new Cliente(null, "Albert Eistein", "09874125895", "43985459585");

		OrdemDeServico os1 = new OrdemDeServico(null, "Troca de placa mãe", t1, c1);

		t1.getOsList().add(os1);
		c1.getOsList().add(os1);

		prestadorRepository.saveAll(Arrays.asList(t1, t2));
		clienteRepository.saveAll(Arrays.asList(c1));
		osRepository.saveAll(Arrays.asList(os1));
	}
}
