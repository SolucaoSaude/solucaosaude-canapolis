package com.canapolis.solucaosaude.view;

import com.canapolis.solucaosaude.dto.PacienteDTO;
import com.canapolis.solucaosaude.dto.PedidoDTO;
import com.canapolis.solucaosaude.exceptions.DefaultExceptionHandler;
import com.canapolis.solucaosaude.model.Paciente;
import com.canapolis.solucaosaude.repository.PacienteRepository;
import com.canapolis.solucaosaude.service.PedidoService;
import com.canapolis.solucaosaude.utils.SolucaoSaudeUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class PedidoView extends BaseView {
    
    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PacienteRepository pacienteRepository;

    @GetMapping("/pedido/novo-pedido")
    public String novoPedido() throws Exception {
        return "tela1";
    }

    @RequestMapping(value = "/pedido/salvar-pedido", method = RequestMethod.POST)
    public String salvar(@ModelAttribute
                               @Valid PedidoDTO pedidoDTO,
                               final BindingResult result,
                               RedirectAttributes redirectAttributes, Model model, @RequestParam("pacienteId") Integer pacienteId) throws DefaultExceptionHandler {

        ModelAndView mvComErros = SolucaoSaudeUtil.validarCamposForm(result, "tela3");
        if (ObjectUtils.isNotEmpty(mvComErros)) {
            mvComErros.addObject("pedido", pedidoDTO);
            return "tela3";
        }
        try {
            Paciente paciente = pacienteRepository.findByPacienteId(pacienteId);
            pedidoDTO.setPaciente( new PacienteDTO(paciente));
            this.pedidoService.cadastrar(pedidoDTO);
            /* Atributo para o ModelAndView redirecionar e mostrar msg*/
            redirectAttributes.addFlashAttribute("msg_resultado", "Pedido salvo com sucesso!");
            return "tela4";
        } catch (DefaultExceptionHandler e) {
            ModelAndView mv = SolucaoSaudeUtil.addErrorMessages("tela3", e.getMessage());
            mv.addObject("pedido", pedidoDTO);
            return "";
        }
    }
}
