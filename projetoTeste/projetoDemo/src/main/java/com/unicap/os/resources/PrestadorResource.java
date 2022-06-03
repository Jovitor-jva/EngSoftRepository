package com.unicap.os.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.unicap.os.services.PrestadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.unicap.os.domain.dtos.PrestadorDTO;

@RestController
@RequestMapping(value = "/tecnicos")
public class PrestadorResource {

	@Autowired
	private PrestadorService service;
 
	@GetMapping(value = "/{id}")
	public ResponseEntity<PrestadorDTO> findById(@PathVariable Integer id) {
		return ResponseEntity.ok().body(new PrestadorDTO(service.findById(id)));
	}

	@GetMapping()
	public ResponseEntity<List<PrestadorDTO>> findAll() {
		List<PrestadorDTO> list = service.findAll().stream().map(x -> new PrestadorDTO(x)).collect(Collectors.toList());
		return ResponseEntity.ok().body(list);
	}
	
	@PostMapping()
	public ResponseEntity<PrestadorDTO> create(@Valid @RequestBody PrestadorDTO obj) {
		obj = service.create(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<PrestadorDTO> update(@Valid @RequestBody PrestadorDTO obj, @PathVariable Integer id) {
		obj = new PrestadorDTO(service.update(obj, id));
		return ResponseEntity.ok().build();
	}

}

















