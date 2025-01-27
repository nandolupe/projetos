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

import com.exercicios.exerciciocrud.forms.ProdutoSearchForm;
import com.exercicios.exerciciocrud.model.Produto;
import com.exercicios.exerciciocrud.services.ProdutoServiceImpl;

/**
 * @author luiz_pereira
 *
 */
@Controller
public class ProdutoController {
	
	private static final String FORWARD_FORM = "manutencao-produto/form";
	private static final String FORWARD_LIST = "manutencao-produto/list";
	private static final String BASE_DOMAIN = "/produto";
	private static final String FORWARD_REDIRECT_LIST = "redirect:" + BASE_DOMAIN + "/list";

	@Autowired
	private ProdutoServiceImpl produtoServiceImpl;
	
	@GetMapping(BASE_DOMAIN + "/list")
    public String showList(Model model, RedirectAttributes redirAttrs) {

		Produto produto = new Produto();
		this.search(model, new ProdutoSearchForm(produto, 
				1, 
				10),
				redirAttrs);
		
        return FORWARD_LIST;
    }
	
	@PostMapping(BASE_DOMAIN + "/search")
    public String search(Model model, ProdutoSearchForm searchForm, RedirectAttributes redirAttrs) {
		
        this.paginate(model, 
        		new ProdutoSearchForm(searchForm.getData(), 
        				1, 
        				10),
        		redirAttrs);

        return FORWARD_LIST;
    }
	
	@PostMapping(BASE_DOMAIN + "/paginate")
    public String paginate(Model model, ProdutoSearchForm searchForm, RedirectAttributes redirAttrs) {
        
		try { 
		
	        model.addAttribute("principalObject", searchForm);
	        model.addAttribute("principalList", 
	        		produtoServiceImpl.searchPaginate(
	        				searchForm.getData(), 
	        				PageRequest.of(searchForm.getPageNumber() - 1, searchForm.getSize())));
        
		 } catch (Exception e) {
	        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
		 }
        
        return FORWARD_LIST;
    }
	
	@GetMapping(BASE_DOMAIN + "/show-form")
    public String showForm(Model model, Produto produto) {
		
		this.initForm(null, model);
		
		return FORWARD_FORM;
    }
	
	@PostMapping(BASE_DOMAIN + "/add")
    public String add(@Valid Produto produto, BindingResult result, Model model, RedirectAttributes redirAttrs) {
        if (result.hasErrors()) {
        	this.initForm(produto, model);
            return FORWARD_FORM;
        }
        
        Produto produtoSave = null;
        		
        try {
        
        	produtoSave = produtoServiceImpl.save(produto);
	        redirAttrs.addFlashAttribute("success", "manutencao.produto.label.save.success");
	        this.initForm(produtoSave, model);
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        	this.initForm(produto, model);
        	return FORWARD_FORM;
        }
        
        return FORWARD_REDIRECT_LIST; 
    }
	
	@GetMapping(BASE_DOMAIN + "/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
		
		this.initForm(new Produto(id), model);
        
        return FORWARD_FORM;
    }
	
	@PostMapping(BASE_DOMAIN + "/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Produto produto, BindingResult result, Model model, RedirectAttributes redirAttrs) {
        if (result.hasErrors()) {
        	produto.setIdProduto(id);
        	this.initForm(produto, model);
            return FORWARD_FORM;
        }
        
        Produto produtoSave = null;
        
        try {
            
        	produtoSave = produtoServiceImpl.update(produto);
	        redirAttrs.addFlashAttribute("success", "manutencao.produto.label.update.success");
	        this.initForm(produtoSave, model);
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        	this.initForm(produto, model);
        	return FORWARD_FORM;
        }
		
        return FORWARD_REDIRECT_LIST;
    }
	
	@GetMapping(BASE_DOMAIN + "/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model, RedirectAttributes redirAttrs) {
		
		try {
            
			produtoServiceImpl.delete(id);
			redirAttrs.addFlashAttribute("success", "manutencao.produto.label.exclude.success");
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        }
		
        return FORWARD_REDIRECT_LIST;
    }

	private void initForm(Produto produto, Model model) {
		
		if (produto == null) {
			produto = new Produto();
			
		} else if (produto.getIdProduto() != null) {
			produto = produtoServiceImpl.findById(produto.getIdProduto()).get();
		}
		
		model.addAttribute("produto", produto);
	}
}
