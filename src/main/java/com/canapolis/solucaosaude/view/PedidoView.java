package com.canapolis.solucaosaude.view;

import com.canapolis.solucaosaude.dto.PacienteDTO;
import com.canapolis.solucaosaude.dto.PageDTO;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class PedidoView extends BaseView {
    
    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PacienteRepository pacienteRepository;

    /*** Busca a lista DE TODOS OS PEDIDOS utilizando a paginação */
    @RequestMapping(value = "/lista", method = RequestMethod.GET)
    public String lista(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) throws DefaultExceptionHandler {
        return listaPedidos(model, page, size, null, null);
    }

    private String listaPedidos(Model model, Optional<Integer> page, Optional<Integer> size, PedidoDTO request, String keyword) throws DefaultExceptionHandler {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        int totalElementos = 0;

        PageDTO<PedidoDTO> objetoPage = this.pedidoService.consultarPaginado(currentPage - 1, pageSize, request, keyword);

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

        return "pedido/pedido";
    }

    /*** Busca a lista por KEYWORD dos objetos utilizando a paginação */
    @RequestMapping(value = "/keyword", method = RequestMethod.GET)
    public String keyword(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size, String keyword) throws DefaultExceptionHandler {

        return listaPedidos(model, page, size, null, keyword);
    }

    /*** Chama CADASTRO DE UM NOVO PEDIDO */
    @GetMapping("/pedido/novo-pedido")
    public String novoPedido() throws Exception {
        return "tela1";
    }
//    @RequestMapping(value = "/pedido/novo", method = RequestMethod.GET)
//    public String novoRegistro(Model model){
//
//        /*OBJETO QUE VAMOS ATRIBUIR OS VALORES DOS CAMPOS NA PÁGINA*/
//        model.addAttribute("pedido", new PedidoDTO());
//        return "tela3";
//    }

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
