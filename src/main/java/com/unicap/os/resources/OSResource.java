package com.unicap.os.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.unicap.os.domain.dtos.OrdemDeServicoDTO;
import com.unicap.os.services.OSService;
@CrossOrigin("*")
@RestController
@RequestMapping(value = "/os")
public class OSResource {

	@Autowired
	private OSService service;

	;
	@GetMapping(value = "/{id}")
	public ResponseEntity<OrdemDeServicoDTO> findById(@PathVariable Integer id) {
		return ResponseEntity.ok().body(new OrdemDeServicoDTO(service.findById(id)));
	}

	@GetMapping()
	public ResponseEntity<List<OrdemDeServicoDTO>> findAll() {
		List<OrdemDeServicoDTO> listDTO = service.findAll().stream().map(x -> new OrdemDeServicoDTO(x))
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(listDTO);
	}

	@PostMapping
	public ResponseEntity<OrdemDeServicoDTO> create(@Valid @RequestBody OrdemDeServicoDTO obj) {
		obj = new OrdemDeServicoDTO(service.create(obj));
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<OrdemDeServicoDTO> update(@Valid @RequestBody OrdemDeServicoDTO obj, @PathVariable Integer id) {
		obj = new OrdemDeServicoDTO(service.update(obj, id));
		return ResponseEntity.ok().build();
	}
	
}
