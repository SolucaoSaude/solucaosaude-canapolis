package com.canapolis.solucaosaude.service;

import com.canapolis.solucaosaude.comum.BaseService;
import com.canapolis.solucaosaude.dto.PacienteDTO;
import com.canapolis.solucaosaude.dto.PageDTO;
import com.canapolis.solucaosaude.enums.EnumAtivoInativo;
import com.canapolis.solucaosaude.exceptions.DefaultExceptionHandler;
import com.canapolis.solucaosaude.model.Paciente;
import com.canapolis.solucaosaude.repository.PacienteRepository;
import com.canapolis.solucaosaude.utils.SolucaoSaudeUtil;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PacienteService extends BaseService {

    @Autowired
    private PacienteRepository pacienteRepository;

    public Paciente consultarPorCpf(final String cpf) {
        return this.pacienteRepository.findByCpf(SolucaoSaudeUtil.toNumber(cpf));
    }

//    public Optional<Paciente> consultarPorEmailSenha(final String email, final String senha) {
//        return this.pacienteRepository.findByEmailAndSenha(email, senha);
//    }
//
//    public Optional<Paciente> consultarPorSenha(final String senha) {
//        return this.pacienteRepository.findBySenha(senha);
//    }

    public Optional<Paciente> consultarPorCpfCartaoSus(final String cpf, final String cartaoSus) {
        return this.pacienteRepository.findByCpfCartaoSus(SolucaoSaudeUtil.toNumber(cpf), SolucaoSaudeUtil.toNumber(cartaoSus));
    }

    public List<Paciente> listarTodos(Sort sort) throws DefaultExceptionHandler {
        return this.pacienteRepository.findAll(sort);
    }

//    public Paciente consultarPorId(final Integer id) throws DefaultExceptionHandler{
//        if (ObjectUtils.isEmpty(id)) {
//            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
//                    "Operação inválida! O campo 'id' é obrigatório.");
//        }
//        try {
//            return this.pacienteRepository.findById(id).orElseThrow(
//                    () -> new DefaultExceptionHandler(HttpStatus.NOT_FOUND.value(),
//                            "Nenhuma informação encontrada para o ID informado.")
//            );
//        } catch (DefaultExceptionHandler e) {
//            throw e;
//        } catch (Exception e) {
//            throw new DefaultExceptionHandler(HttpStatus.EXPECTATION_FAILED.value(), e.getMessage());
//        }
//    }

    public PageDTO<PacienteDTO> consultarPaginado(int page, int size, PacienteDTO pacienteDTO, String keyword) throws DefaultExceptionHandler {
        try {
            PageRequest pageable = PageRequest.of(
                    page,
                    size,
                    Sort.Direction.ASC, "nome");

            Page<Paciente> pages;
            if (!ObjectUtils.isEmpty(keyword)) {
                pages = this.pacienteRepository.findByKeyword(keyword, pageable);
            } else if (!ObjectUtils.isEmpty(pacienteDTO)) {
                pages = this.pacienteRepository.findAll(Example.of(super.convertToModel(pacienteDTO, Paciente.class)), pageable);
            } else {
                pages = this.pacienteRepository.findAll(pageable);
            }
            if (!pages.isEmpty()) {
                final int totalElements = (int) pages.getTotalElements();
                final int totalPages = pages.getTotalPages();
                final boolean isFirst = pages.isFirst();
                final boolean isLast = pages.isLast();

                List<PacienteDTO> pacienteDTOList = pages.stream()
                        .map(entity -> super.convertToDTO(entity, PacienteDTO.class)).collect(Collectors.toList());

                PageDTO<PacienteDTO> pageDTO = new PageDTO<PacienteDTO>();
                pageDTO.setTotalElements(Integer.valueOf(totalElements));
                pageDTO.setTotalPages(Integer.valueOf(totalPages));
                pageDTO.setFirst(isFirst);
                pageDTO.setLast(isLast);
                pageDTO.setContent(pacienteDTOList);

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

//    @Transactional(rollbackFor = DefaultExceptionHandler.class)
//    public Paciente atualizar(PacienteDTO pacienteDTO) throws DefaultExceptionHandler {
//        this.validaAtualizarPaciente(pacienteDTO);
//        if (ObjectUtils.isEmpty(pacienteDTO) || ObjectUtils.isEmpty(pacienteDTO.getId())) {
//            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(), "Operação inválida! O campo ID é obrigatório.");
//        }
//        try {
//            Paciente paciente = this.consultarPorId(pacienteDTO.getId());
//
//            paciente.setNome(pacienteDTO.getNome());
////            paciente.setCpf(pacienteDTO.getCpf().replaceAll("\\D", ""));
//            paciente.setCartaoSus(pacienteDTO.getCartaoSus().replaceAll("\\D", ""));
//            paciente.setTelefone(pacienteDTO.getTelefone().replaceAll("\\D", ""));
//            paciente.setDataNascimento(pacienteDTO.getDataNascimento());
//            paciente.setGenero(pacienteDTO.getGenero());
//            if (!StringUtils.isEmpty(pacienteDTO.getAtivoInativo())) {
//                paciente.setAtivoInativo(pacienteDTO.getAtivoInativo());
//            } else {
//                paciente.setAtivoInativo(EnumAtivoInativo.ATIVO.getCodigo());
//            }
//            paciente.setEmail(pacienteDTO.getEmail());
//            paciente.setConfirmaEmail(pacienteDTO.getConfirmaEmail());
//            if (!StringUtils.isEmpty(pacienteDTO.getSenha())) {
//                paciente.setSenha(pacienteDTO.getSenha());
//                paciente.setConfirmaSenha(pacienteDTO.getConfirmaSenha());
//            }
//            return pacienteRepository.save(paciente);
//
//        } catch (DefaultExceptionHandler e) {
//            throw e;
//        } catch (Exception e) {
//            throw new DefaultExceptionHandler(HttpStatus.EXPECTATION_FAILED.value(), e.getMessage());
//        }
//    }

//    public void ativar(final Integer id) throws DefaultExceptionHandler {
//        if (ObjectUtils.isEmpty(id)) {
//            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(), "Operação inválida! O campo ID é obrigatório.");
//        }
//        try {
//            Paciente paciente = this.pacienteRepository.findById(id).orElseThrow(
//                    () -> new DefaultExceptionHandler(HttpStatus.NOT_FOUND.value(), "Nenhuma informação encontrada para os parâmetros informados.")
//            );
//            paciente.setAtivoInativo(EnumAtivoInativo.ATIVO.getCodigo());
//            this.pacienteRepository.save(paciente);
//        } catch (DefaultExceptionHandler e) {
//            throw e;
//        } catch (Exception e) {
//            throw new DefaultExceptionHandler(HttpStatus.EXPECTATION_FAILED.value(), e.getMessage());
//        }
//    }

//    @Transactional(rollbackFor = DefaultExceptionHandler.class)
//    public void deletar(final Integer id) throws DefaultExceptionHandler {
//        if (ObjectUtils.isEmpty(id)) {
//            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(), "Operação inválida! O campo ID é obrigatório.");
//        }
//        try {
//            Paciente paciente = this.pacienteRepository.findById(id).orElseThrow(
//                    () -> new DefaultExceptionHandler(HttpStatus.NOT_FOUND.value(), "Nenhuma informação encontrada para os parâmetros informados.")
//            );
//            // inativar - NAO DELETA DO BANCO, APENAS INATIVA PACIENTE
//            paciente.setAtivoInativo(EnumAtivoInativo.INATIVO.getCodigo());
//            this.pacienteRepository.save(paciente);
//            // deletar - DELETA DO BANCO
////            this.pacienteRepository.delete(paciente);
//        } catch (DefaultExceptionHandler e) {
//            throw e;
//        } catch (Exception e) {
//            throw new DefaultExceptionHandler(HttpStatus.EXPECTATION_FAILED.value(), e.getMessage());
//        }
//    }

//    public Paciente iniciarPedido(final String email, final String senha) throws DefaultExceptionHandler {
//        if (StringUtils.isEmpty(email)) {
//            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(), "Operação inválida! O campo 'email' é obrigatório.");
//        }
//        if (StringUtils.isEmpty(senha)) {
//            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(), "Operação inválida! O campo 'senha' é obrigatório.");
//        }
//        try {
//            Paciente paciente = this.pacienteRepository.findByEmailAndSenha(email, senha).orElseThrow(
//                    () -> new DefaultExceptionHandler(HttpStatus.NOT_FOUND.value(), "Nenhuma informação encontrada para os parâmetros informados.")
//            );
//            if (EnumAtivoInativo.valueOfCodigo(paciente.getAtivoInativo()) == EnumAtivoInativo.INATIVO) {
//                throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(), "Operação inválida! Não é possível iniciar pedido de consulta para usuário inativo.");
//            }
//            return this.pacienteRepository.save(paciente);
//        } catch (Exception e) {
//            if (e instanceof DefaultExceptionHandler) {
//                throw e;
//            } else {
//                throw new DefaultExceptionHandler(e);
//            }
//        }
//    }

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

//    private void validaAtualizarPaciente(PacienteDTO request) throws DefaultExceptionHandler{
//        if(StringUtils.isEmpty(request.getNome().toLowerCase())){
//            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
//                    "Operação inválida! O nome do paciente não pode ser nulo.");
//        }
////        if(StringUtils.isEmpty(request.getCpf().toLowerCase())){
////            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
////                    "Operação inválida! O cpf do paciente não pode ser nulo.");
////        }
////        if (!CanapolisUtil.isCPF(request.getCpf())) {
////            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
////                    "Operação inválida! O cpf inserido é inválido.");
////        }
//        if(StringUtils.isEmpty(request.getCartaoSus().toLowerCase())){
//            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
//                    "Operação inválida! O cartão SUS do paciente não pode ser nulo.");
//        }
//        if(StringUtils.isEmpty(request.getTelefone().toLowerCase())){
//            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
//                    "Operação inválida! O telefone do paciente não pode ser nulo.");
//        }
//        if(StringUtils.isEmpty(request.getDataNascimento())){
//            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
//                    "Operação inválida! O data de nascimento do paciente não pode ser nulo.");
//        }
//        if(StringUtils.isEmpty(request.getGenero().toLowerCase())){
//            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
//                    "Operação inválida! O genero do paciente não pode ser nulo.");
//        }
//        if(StringUtils.isEmpty(request.getEmail().toLowerCase())){
//            throw new DefaultExceptionHandler(HttpStatus.BAD_REQUEST.value(),
//                    "Operação inválida! O email do paciente não pode ser nulo.");
//        }
//    }
}
