package com.canapolis.solucaosaude.view;

import com.canapolis.solucaosaude.dto.PacienteDTO;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/paciente")
public class PacienteView extends BaseView {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private PacienteRepository pacienteRepository;

    @RequestMapping(value = "/")
    public String iniciar() {
        return "tela1";
    }

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
