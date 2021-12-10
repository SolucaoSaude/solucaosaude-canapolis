package com.canapolis.solucaosaude.controller;

import com.canapolis.solucaosaude.comum.BaseController;
import com.canapolis.solucaosaude.dto.PedidoDTO;
import com.canapolis.solucaosaude.exceptions.DefaultExceptionHandler;
import com.canapolis.solucaosaude.model.Pedido;
import com.canapolis.solucaosaude.service.PedidoService;
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
@RequestMapping("/pedido")
public class PedidoController extends BaseController {

    @Autowired
    PedidoService pedidoService;

    @PostMapping()
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<PedidoDTO> cadastrar(@Valid @RequestBody PedidoDTO pedidoDTO) throws DefaultExceptionHandler {
        Pedido pedido = this.pedidoService.cadastrar(pedidoDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(pedido.getId()).toUri();
        return ResponseEntity.created(location).body(super.convertTo(pedido, PedidoDTO.class));
    }
}
