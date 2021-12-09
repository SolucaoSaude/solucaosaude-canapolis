package com.canapolis.solucaosaude.service;

import com.canapolis.solucaosaude.comum.BaseService;
import com.canapolis.solucaosaude.dto.PageDTO;
import com.canapolis.solucaosaude.dto.PedidoDTO;
import com.canapolis.solucaosaude.enums.EnumStatusPedido;
import com.canapolis.solucaosaude.enums.EnumTipoConsulta;
import com.canapolis.solucaosaude.exceptions.DefaultExceptionHandler;
import com.canapolis.solucaosaude.model.Paciente;
import com.canapolis.solucaosaude.model.Pedido;
import com.canapolis.solucaosaude.repository.PacienteRepository;
import com.canapolis.solucaosaude.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService extends BaseService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    public List<Pedido> listarTodos(Sort sort) throws DefaultExceptionHandler {
        return this.pedidoRepository.findAll(sort);
    }

//    public Pedido consultarPorId(final Integer id) throws DefaultExceptionHandler{
//        if (ObjectUtils.isEmpty(id)) {
//            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
//                    "Operação inválida! O campo 'id' é obrigatório.");
//        }
//        try {
//            return this.pedidoRepository.findById(id).orElseThrow(
//                    () -> new DefaultExceptionHandler(HttpStatus.NOT_FOUND.value(),
//                            "Nenhuma informação encontrada para os parâmetros informados.")
//            );
//        } catch (DefaultExceptionHandler e) {
//            throw e;
//        } catch (Exception e) {
//            throw new DefaultExceptionHandler(HttpStatus.EXPECTATION_FAILED.value(), e.getMessage());
//        }
//    }

    public PageDTO<PedidoDTO> consultarPaginado(int page, int size, PedidoDTO pedidoDTO, String keyword) throws DefaultExceptionHandler {
        try {
            PageRequest pageable = PageRequest.of(
                    page,
                    size,
                    Sort.Direction.ASC, "pedido.postoSaude");

            Page<Pedido> pages;
            if (!ObjectUtils.isEmpty(keyword)) {
                pages = this.pedidoRepository.findByKeyword(keyword, pageable);
            } else if (!ObjectUtils.isEmpty(pedidoDTO)) {
                pages = this.pedidoRepository.findAll(Example.of(super.convertToModel(pedidoDTO, Pedido.class)), pageable);
            } else {
                pages = this.pedidoRepository.findAll(pageable);
            }
            if (!pages.isEmpty()) {
                final int totalElements = (int) pages.getTotalElements();
                final int totalPages = pages.getTotalPages();
                final boolean isFirst = pages.isFirst();
                final boolean isLast = pages.isLast();

                List<PedidoDTO> pedidoDTOList = pages.stream()
                        .map(entity -> super.convertToDTO(entity, PedidoDTO.class)).collect(Collectors.toList());

                PageDTO<PedidoDTO> pageDTO = new PageDTO<PedidoDTO>();
                pageDTO.setTotalElements(Integer.valueOf(totalElements));
                pageDTO.setTotalPages(Integer.valueOf(totalPages));
                pageDTO.setFirst(isFirst);
                pageDTO.setLast(isLast);
                pageDTO.setContent(pedidoDTOList);

                return pageDTO;
            }
        } catch (Exception e) {
            if (e instanceof DefaultExceptionHandler) {
                throw e;
            } else {
                throw new DefaultExceptionHandler(e);
            }
        }
        return null;
    }

    @Transactional(rollbackFor = DefaultExceptionHandler.class)
    public Pedido cadastrar(PedidoDTO pedidoDTO) throws DefaultExceptionHandler {
        if (ObjectUtils.isEmpty(pedidoDTO)) {
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Operação inválida! O objeto tem que estar preenchido.");
        }
        this.validarPedido(pedidoDTO);
        try {
            Paciente paciente = pacienteRepository.findById(pedidoDTO.getPaciente().getId()).get();
            if (paciente == null) {
                throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                        "Operação inválida. O id do paciente informado não existe.");
            }
            if (paciente.getAtivoInativo() == "I" || paciente.getAtivoInativo() == null) {
                throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                        "Operação inválida. O paciente INATIVO não pode marcar consulta.");
            }
            pedidoDTO.setId(null);
            pedidoDTO.getPostoSaude();
            if (StringUtils.isEmpty(pedidoDTO.getStatusPedido())) {
                pedidoDTO.setStatusPedido(EnumStatusPedido.ABERTO.getCodigo());
            } else {
                pedidoDTO.setStatusPedido(pedidoDTO.getStatusPedido());
            }
            if (StringUtils.isEmpty(pedidoDTO.getTipoConsulta())) {
                pedidoDTO.setTipoConsulta(EnumTipoConsulta.GERAL.getCodigo());
            } else {
                pedidoDTO.setTipoConsulta(pedidoDTO.getTipoConsulta());
            }
            if (StringUtils.isEmpty(pedidoDTO.getDataPedido())) {
                pedidoDTO.setDataPedido(new Date());
            }
            Pedido pedido = convertToModel(pedidoDTO, Pedido.class);
            pedido.setPaciente(paciente);

            return this.pedidoRepository.save(pedido);
        } catch (Exception e) {
            throw new DefaultExceptionHandler(HttpStatus.EXPECTATION_FAILED.value(), e.getMessage());
        }
    }

    @Transactional(rollbackFor = DefaultExceptionHandler.class)
    public void deletar(final Integer id) throws DefaultExceptionHandler {
        if (ObjectUtils.isEmpty(id)) {
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(), "Operação inválida! O campo ID é obrigatório.");
        }
        try {
            Pedido pedido = this.pedidoRepository.findById(id).orElseThrow(
                    () -> new DefaultExceptionHandler(HttpStatus.NOT_FOUND.value(), "Nenhuma informação encontrada para os parâmetros informados.")
            );
            this.pedidoRepository.delete(pedido);
        } catch (DefaultExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            throw new DefaultExceptionHandler(HttpStatus.EXPECTATION_FAILED.value(), e.getMessage());
        }
    }

    private void validarPedido(PedidoDTO request) throws DefaultExceptionHandler{
        if(StringUtils.isEmpty(request.getPostoSaude().toLowerCase())){
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Operação inválida! O posto de saúde não pode ser nulo.");
        }
        if(StringUtils.isEmpty(request.getTipoConsulta().toLowerCase())){
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Operação inválida! O tipo de consulta não pode ser nulo.");
        }
    }
}
