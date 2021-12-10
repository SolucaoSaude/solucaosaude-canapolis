package com.canapolis.solucaosaude.controller;

import com.canapolis.solucaosaude.comum.BaseController;
import com.canapolis.solucaosaude.dto.PacienteDTO;
import com.canapolis.solucaosaude.exceptions.DefaultExceptionHandler;
import com.canapolis.solucaosaude.model.Paciente;
import com.canapolis.solucaosaude.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@Controller
@RequestMapping("/paciente")
public class PacienteController extends BaseController {

    @Autowired
    PacienteService pacienteService;

    @PostMapping()
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<PacienteDTO> cadastrar(@Valid @RequestBody PacienteDTO pacienteDTO) throws DefaultExceptionHandler {
        Paciente paciente = this.pacienteService.cadastrar(pacienteDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(location).body(super.convertTo(paciente, PacienteDTO.class));
    }
}
