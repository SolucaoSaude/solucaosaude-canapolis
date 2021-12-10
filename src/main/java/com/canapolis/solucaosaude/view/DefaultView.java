package com.canapolis.solucaosaude.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DefaultView {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String iniciar() {
        return "tela1";
    }
}
