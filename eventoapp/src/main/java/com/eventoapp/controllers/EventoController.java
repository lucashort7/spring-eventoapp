package com.eventoapp.controllers;

import com.eventoapp.models.Convidado;
import com.eventoapp.models.Evento;
import com.eventoapp.repository.ConvidadoRepository;
import com.eventoapp.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.naming.Binding;
import javax.validation.Valid;

@Controller
public class EventoController {

    @Autowired
    private EventoRepository er;

    @Autowired
    private ConvidadoRepository cr;

    @RequestMapping(value="/cadastrarEvento", method= RequestMethod.GET)
    public String form(Model model){
        model.addAttribute("evento", new Evento());
        return "evento/formEvento";
    }

    @RequestMapping(value="/cadastrarEvento", method= RequestMethod.POST)
    public String form(@Valid Evento evento, BindingResult results, RedirectAttributes attributes){

        if(results.hasErrors()){
            attributes.addFlashAttribute("mensagem", "Verifique os campos!");
        }else{
            er.save(evento);
            attributes.addFlashAttribute("mensagem", "Evento cadastrado com sucesso!");
        }

        return "redirect:/cadastrarEvento";
    }

    @RequestMapping("/eventos")
    public ModelAndView listaEvento(){
        ModelAndView mv = new ModelAndView("index");
        Iterable<Evento> eventos = er.findAll();
        mv.addObject("eventos", eventos);
        return mv;
    }

    @RequestMapping(value="/{codigo}", method=RequestMethod.GET)
    public ModelAndView detalhesEventos(@PathVariable("codigo") long codigo){
        Evento evento = er.findOne(codigo);
        ModelAndView mv = null;

        if (evento != null){
            mv = new ModelAndView("evento/detalhesEvento");
            mv.addObject("evento", evento);

            Iterable<Convidado> convidados  = cr.findByEvento(evento);
            mv.addObject("convidados", convidados);
        }else{
            mv = new ModelAndView("redirect:/");
        }



        return mv;
    }

    @RequestMapping(value = "/deletar")
    public String deletarEvento(@RequestParam("codigo") Long codigo){
        Evento evento = er.findByCodigo(codigo);
        er.delete(evento);
        return "redirect:/eventos";
    }

    @RequestMapping("/deletarConvidado")
    public String deletarConvidado(String rg){
        Convidado convidado = cr.findOne(rg);

        Long codEvento = convidado.getEvento().getCodigo();

        cr.delete(convidado);
        return "redirect:/"+codEvento;
    }

    @RequestMapping(value="/{codigo}", method=RequestMethod.POST)
    public String detalhesEventosPost(@PathVariable("codigo") long codigo, @Valid Convidado convidado,
                                        BindingResult result, RedirectAttributes redirect){
        if(result.hasErrors()){
            redirect.addFlashAttribute("mensagem", "Verifique os campos!");
        }else{
            Evento evento = er.findByCodigo(codigo);
            convidado.setEvento(evento);
            cr.save(convidado);
            redirect.addFlashAttribute("mensagem", "Convidado adicionado com sucesso!");
        }

        return "redirect:/{codigo}";
    }
}
