package com.canapolis.solucaosaude.service;

import com.canapolis.solucaosaude.comum.BaseService;
import com.canapolis.solucaosaude.dto.PacienteDTO;
import com.canapolis.solucaosaude.enums.EnumAtivoInativo;
import com.canapolis.solucaosaude.exceptions.DefaultExceptionHandler;
import com.canapolis.solucaosaude.model.Paciente;
import com.canapolis.solucaosaude.repository.PacienteRepository;
import com.canapolis.solucaosaude.utils.SolucaoSaudeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class PacienteService extends BaseService {

    @Autowired
    private PacienteRepository pacienteRepository;

    public Optional<Paciente> consultarPorCpfCartaoSus(final String cpf, final String cartaoSus) {
        return this.pacienteRepository.findByCpfCartaoSus(SolucaoSaudeUtil.toNumber(cpf), SolucaoSaudeUtil.toNumber(cartaoSus));
    }

    @Transactional(rollbackFor = DefaultExceptionHandler.class)
    public Paciente cadastrar(PacienteDTO pacienteDTO) throws DefaultExceptionHandler {
        this.validarPaciente(pacienteDTO);
        try {
            Optional<Paciente> paciente = this.consultarPorCpfCartaoSus(pacienteDTO.getCpf(), pacienteDTO.getCartaoSus());
            if (paciente.isEmpty()) {
                pacienteDTO.setId(null);
                pacienteDTO.setNome(pacienteDTO.getNome());
                pacienteDTO.setCpf(pacienteDTO.getCpf().replaceAll("\\D", ""));
                pacienteDTO.setCartaoSus(pacienteDTO.getCartaoSus().replaceAll("\\D", ""));
                pacienteDTO.setTelefone(pacienteDTO.getTelefone().replaceAll("\\D", ""));
                pacienteDTO.setDataNascimento(pacienteDTO.getDataNascimento());
                pacienteDTO.setGenero(pacienteDTO.getGenero());
                pacienteDTO.setEmail(pacienteDTO.getEmail());
                if (StringUtils.isEmpty(pacienteDTO.getAtivoInativo())) {
                    pacienteDTO.setAtivoInativo(EnumAtivoInativo.ATIVO.getCodigo());
                } else {
                    pacienteDTO.setAtivoInativo(pacienteDTO.getAtivoInativo());
                }
            } else {
                throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                        "Operação inválida! Cpf ou cartão SUS já cadastrados.");
                }
            return this.pacienteRepository.save(super.convertToModel(pacienteDTO, Paciente.class));
        } catch (Exception e) {
            throw new DefaultExceptionHandler(HttpStatus.EXPECTATION_FAILED.value(), e.getMessage());
        }
    }

    private void validarPaciente(PacienteDTO request) throws DefaultExceptionHandler{
        if(StringUtils.isEmpty(request.getNome().toLowerCase())){
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Operação inválida! O nome do paciente não pode ser nulo.");
        }
        if(StringUtils.isEmpty(request.getCpf().toLowerCase())){
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Operação inválida! O cpf do paciente não pode ser nulo.");
        }
        if (!SolucaoSaudeUtil.isCPF(request.getCpf())) {
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Operação inválida! O cpf inserido é inválido.");
        }
        if(StringUtils.isEmpty(request.getCartaoSus().toLowerCase())){
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Operação inválida! O cartão SUS do paciente não pode ser nulo.");
        }
        if(StringUtils.isEmpty(request.getTelefone().toLowerCase())){
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Operação inválida! O telefone do paciente não pode ser nulo.");
        }
        if(StringUtils.isEmpty(request.getDataNascimento())){
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Operação inválida! O data de nascimento do paciente não pode ser nulo.");
        }
        if(StringUtils.isEmpty(request.getGenero().toLowerCase())){
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Operação inválida! O genero do paciente não pode ser nulo.");
        }
        if(StringUtils.isEmpty(request.getEmail().toLowerCase())){
            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
                    "Operação inválida! O email do paciente não pode ser nulo.");
        }
    }
}
