package com.canapolis.solucaosaude.view;

import com.canapolis.solucaosaude.dto.PacienteDTO;
import com.canapolis.solucaosaude.dto.PageDTO;
import com.canapolis.solucaosaude.dto.PedidoDTO;
import com.canapolis.solucaosaude.exceptions.DefaultExceptionHandler;
import com.canapolis.solucaosaude.model.Paciente;
import com.canapolis.solucaosaude.repository.PacienteRepository;
import com.canapolis.solucaosaude.service.PacienteService;
import com.canapolis.solucaosaude.utils.SolucaoSaudeUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/paciente")
public class PacienteView extends BaseView {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private PacienteRepository pacienteRepository;

    /*** Busca a lista DE TODOS OS PACIENTES utilizando a paginação */
    @RequestMapping(value = "/lista", method = RequestMethod.GET)
    public String lista(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) throws DefaultExceptionHandler {
        return listaPacientes(model, page, size, null, null);
    }

    private String listaPacientes(Model model, Optional<Integer> page, Optional<Integer> size, PacienteDTO request, String keyword) throws DefaultExceptionHandler {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        int totalElementos = 0;

        PageDTO<PacienteDTO> objetoPage = this.pacienteService.consultarPaginado(currentPage - 1, pageSize, request, keyword);

        if (!ObjectUtils.isEmpty(objetoPage)) {
            int totalPages = objetoPage.getTotalPages().intValue();
            totalElementos = objetoPage.getTotalElements().intValue();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
                model.addAttribute("totalPages", totalPages);
                model.addAttribute("pageSize", pageSize);
                model.addAttribute("currentPage", currentPage);
            }
        }
        model.addAttribute("totalElementos", totalElementos);
        model.addAttribute("objetoPage", objetoPage);

        return "paciente/paciente";
    }

    /*** Busca a lista por KEYWORD dos objetos utilizando a paginação */
    @RequestMapping(value = "/keyword", method = RequestMethod.GET)
    public String keyword(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size, String keyword) throws DefaultExceptionHandler {

        return listaPacientes(model, page, size, null, keyword);
    }

    /*** Chama CADASTRO DE UM NOVO PACIENTE */
    @RequestMapping(value = "/novo-paciente", method = RequestMethod.GET)
    public String novoRegistro(Model model){

        /*OBJETO QUE VAMOS ATRIBUIR OS VALORES DOS CAMPOS NA PÁGINA*/
        model.addAttribute("paciente", new PacienteDTO());
        return "tela2";
    }

    @RequestMapping(value = "/salvar-paciente", method = RequestMethod.POST)
    public String salvar(@ModelAttribute
                               @Valid PacienteDTO pacienteDTO,
                               final BindingResult result,
                               RedirectAttributes redirectAttributes, Model model) throws DefaultExceptionHandler {

        ModelAndView mvComErros = SolucaoSaudeUtil.validarCamposForm(result, "tela2");
        if (ObjectUtils.isNotEmpty(mvComErros)) {
            mvComErros.addObject("paciente", pacienteDTO);
            return "tela2";
        }
        try {
            this.pacienteService.cadastrar(pacienteDTO);
            /* Atributo para o ModelAndView redirecionar e mostrar msg*/
            redirectAttributes.addFlashAttribute("msg_resultado", "Paciente salvo com sucesso!");
            PedidoDTO pedidoDTO = new PedidoDTO();
            pedidoDTO.setPaciente(pacienteDTO);
            Paciente paciente = pacienteRepository.findByCpf(pacienteDTO.getCpf());
            model.addAttribute("paciente", paciente);
            model.addAttribute("pedido", pedidoDTO);
            return "tela3";
        } catch (DefaultExceptionHandler e) {
            ModelAndView mv = SolucaoSaudeUtil.addErrorMessages("tela2", e.getMessage());
            mv.addObject("paciente", pacienteDTO);
            return "";
        }
    }
}
