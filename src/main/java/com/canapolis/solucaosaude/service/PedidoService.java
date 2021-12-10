package com.canapolis.solucaosaude.service;

import com.canapolis.solucaosaude.comum.BaseService;
import com.canapolis.solucaosaude.dto.PedidoDTO;
import com.canapolis.solucaosaude.enums.EnumStatusPedido;
import com.canapolis.solucaosaude.enums.EnumTipoConsulta;
import com.canapolis.solucaosaude.exceptions.DefaultExceptionHandler;
import com.canapolis.solucaosaude.model.Paciente;
import com.canapolis.solucaosaude.model.Pedido;
import com.canapolis.solucaosaude.repository.PacienteRepository;
import com.canapolis.solucaosaude.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Date;

@Service
public class PedidoService extends BaseService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

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
