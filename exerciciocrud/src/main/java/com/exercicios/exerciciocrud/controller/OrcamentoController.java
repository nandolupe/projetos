package com.exercicios.exerciciocrud.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.exercicios.exerciciocrud.forms.OrcamentoSearchForm;
import com.exercicios.exerciciocrud.model.Orcamento;
import com.exercicios.exerciciocrud.services.OrcamentoServiceImpl;

/**
 * @author luiz_pereira
 *
 */
@Controller
public class OrcamentoController {
	
	private static final String FORWARD_FORM = "manutencao-orcamento/form";
	private static final String FORWARD_LIST = "manutencao-orcamento/list";
	private static final String BASE_DOMAIN = "/orcamento";
	private static final String FORWARD_REDIRECT_LIST = "redirect:" + BASE_DOMAIN + "/list";

	@Autowired
	private OrcamentoServiceImpl orcamentoServiceImpl;
	
	@GetMapping(BASE_DOMAIN + "/list")
    public String showList(Model model, RedirectAttributes redirAttrs) {

		Orcamento orcamento = new Orcamento();
		this.search(model, new OrcamentoSearchForm(orcamento, 
				1, 
				10),
				redirAttrs);
		
        return FORWARD_LIST;
    }
	
	@PostMapping(BASE_DOMAIN + "/search")
    public String search(Model model, OrcamentoSearchForm searchForm, RedirectAttributes redirAttrs) {
		
        this.paginate(model, 
        		new OrcamentoSearchForm(searchForm.getData(), 
        				1, 
        				10),
        		redirAttrs);

        return FORWARD_LIST;
    }
	
	@PostMapping(BASE_DOMAIN + "/paginate")
    public String paginate(Model model, OrcamentoSearchForm searchForm, RedirectAttributes redirAttrs) {
        
		try { 
		
	        model.addAttribute("principalObject", searchForm);
	        model.addAttribute("principalList", 
	        		orcamentoServiceImpl.searchPaginate(
	        				searchForm.getData(), 
	        				PageRequest.of(searchForm.getPageNumber() - 1, searchForm.getSize())));
        
		 } catch (Exception e) {
	        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
		 }
        
        return FORWARD_LIST;
    }
	
	@GetMapping(BASE_DOMAIN + "/show-form")
    public String showForm(Model model, Orcamento orcamento) {
		
		this.initForm(null, model);
		
		return FORWARD_FORM;
    }
	
	@PostMapping(BASE_DOMAIN + "/add")
    public String add(@Valid Orcamento orcamento, BindingResult result, Model model, RedirectAttributes redirAttrs) {
        if (result.hasErrors()) {
        	this.initForm(orcamento, model);
            return FORWARD_FORM;
        }
        
        Orcamento orcamentoSave = null;
        		
        try {
        
        	orcamentoSave = orcamentoServiceImpl.save(orcamento);
	        redirAttrs.addFlashAttribute("success", "manutencao.orcamento.label.save.success");
	        this.initForm(orcamentoSave, model);
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        	this.initForm(orcamento, model);
        	return FORWARD_FORM;
        }
        
        return FORWARD_REDIRECT_LIST; 
    }
	
	@GetMapping(BASE_DOMAIN + "/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
		
		this.initForm(new Orcamento(id), model);
        
        return FORWARD_FORM;
    }
	
	@PostMapping(BASE_DOMAIN + "/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Orcamento orcamento, BindingResult result, Model model, RedirectAttributes redirAttrs) {
        if (result.hasErrors()) {
        	orcamento.setIdOrcamento(id);
        	this.initForm(orcamento, model);
            return FORWARD_FORM;
        }
        
        Orcamento orcamentoSave = null;
        
        try {
            
        	orcamentoSave = orcamentoServiceImpl.update(orcamento);
	        redirAttrs.addFlashAttribute("success", "manutencao.orcamento.label.update.success");
	        this.initForm(orcamentoSave, model);
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        	this.initForm(orcamento, model);
        	return FORWARD_FORM;
        }
		
        return FORWARD_REDIRECT_LIST;
    }
	
	@GetMapping(BASE_DOMAIN + "/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model, RedirectAttributes redirAttrs) {
		
		try {
            
			orcamentoServiceImpl.delete(id);
			redirAttrs.addFlashAttribute("success", "manutencao.orcamento.label.exclude.success");
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        }
		
        return FORWARD_REDIRECT_LIST;
    }

	private void initForm(Orcamento orcamento, Model model) {
		
		if (orcamento == null) {
			orcamento = new Orcamento();
			
		} else if (orcamento.getIdOrcamento() != null) {
			orcamento = orcamentoServiceImpl.findById(orcamento.getIdOrcamento()).get();
		}
		
		model.addAttribute("orcamento", orcamento);
	}
}
