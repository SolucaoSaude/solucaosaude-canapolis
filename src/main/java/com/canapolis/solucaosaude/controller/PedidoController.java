package com.canapolis.solucaosaude.controller;

import com.canapolis.solucaosaude.comum.BaseController;
import com.canapolis.solucaosaude.dto.PageDTO;
import com.canapolis.solucaosaude.dto.PedidoDTO;
import com.canapolis.solucaosaude.exceptions.DefaultExceptionHandler;
import com.canapolis.solucaosaude.model.Pedido;
import com.canapolis.solucaosaude.service.PedidoService;
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
@RequestMapping("/pedido")
public class PedidoController extends BaseController {

    @Autowired
    PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<PedidoDTO>> listarTodos(String order) throws DefaultExceptionHandler {
        return ResponseEntity.ok(super.convertListTo(this.pedidoService.listarTodos(Sort.by(order)), PedidoDTO.class));
    }

    @PostMapping(value = "/pesquisar", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PageDTO<PedidoDTO>> consultarPaginado(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                                @RequestBody(required = false) PedidoDTO pedido) throws DefaultExceptionHandler {
        PageDTO<PedidoDTO> pedidoDTOPageDTO = this.pedidoService.consultarPaginado(page, size, pedido, null);
        return ResponseEntity.ok(pedidoDTOPageDTO);
    }

    @PostMapping()
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<PedidoDTO> cadastrar(@Valid @RequestBody PedidoDTO pedidoDTO) throws DefaultExceptionHandler {
        Pedido pedido = this.pedidoService.cadastrar(pedidoDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(pedido.getId()).toUri();
        return ResponseEntity.created(location).body(super.convertTo(pedido, PedidoDTO.class));
    }
}
