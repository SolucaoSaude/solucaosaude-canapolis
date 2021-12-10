package com.canapolis.solucaosaude.controller;

import com.canapolis.solucaosaude.comum.BaseController;
import com.canapolis.solucaosaude.dto.PacienteDTO;
import com.canapolis.solucaosaude.dto.PageDTO;
import com.canapolis.solucaosaude.exceptions.DefaultExceptionHandler;
import com.canapolis.solucaosaude.model.Paciente;
import com.canapolis.solucaosaude.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/paciente")
public class PacienteController extends BaseController {

    @Autowired
    PacienteService pacienteService;

//    @GetMapping
//    public ResponseEntity<List<PacienteDTO>> listarTodos(String order) throws DefaultExceptionHandler {
//        return ResponseEntity.ok(super.convertListTo(this.pacienteService.listarTodos(Sort.by(order)), PacienteDTO.class));
//    }
//
//    @PostMapping(value = "/pesquisar", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<PageDTO<PacienteDTO>> consultarPaginado(@RequestParam(value = "page", defaultValue = "0") Integer page,
//                                                                  @RequestParam(value = "size", defaultValue = "10") Integer size,
//                                                                  @RequestBody(required = false) PacienteDTO paciente) throws DefaultExceptionHandler {
//        PageDTO<PacienteDTO> pacienteDTOPageDTO = this.pacienteService.consultarPaginado(page, size, paciente, null);
//        return ResponseEntity.ok(pacienteDTOPageDTO);
//    }

    @PostMapping()
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<PacienteDTO> cadastrar(@Valid @RequestBody PacienteDTO pacienteDTO) throws DefaultExceptionHandler {
        Paciente paciente = this.pacienteService.cadastrar(pacienteDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(location).body(super.convertTo(paciente, PacienteDTO.class));
    }
}
