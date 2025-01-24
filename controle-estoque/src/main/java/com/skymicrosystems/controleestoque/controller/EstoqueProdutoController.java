package com.skymicrosystems.controleestoque.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.skymicrosystems.controleestoque.forms.EstoqueProdutoSearchForm;
import com.skymicrosystems.controleestoque.model.EstoqueProduto;
import com.skymicrosystems.controleestoque.services.EstoqueProdutoServiceImpl;
import com.skymicrosystems.controleestoque.utils.BuildManagementUtils;

/**
 * @author luiz_pereira
 *
 */
@Controller
public class EstoqueProdutoController {
	
	
	private final static Logger logger = LoggerFactory.getLogger(EstoqueProdutoController.class);
	
	private static final String FORWARD_FORM = "manutencao-estoque-produto/form";
	private static final String FORWARD_LIST = "manutencao-estoque-produto/list";
	private static final String BASE_DOMAIN = "/estoque-produto";
	private static final String FORWARD_REDIRECT_LIST = "redirect:" + BASE_DOMAIN + "/list";

	@Autowired
	private EstoqueProdutoServiceImpl estoqueProdutoServiceImpl;
	
	@Autowired
	private BuildManagementUtils buildManagementUtils;
	
	@GetMapping(BASE_DOMAIN + "/list")
    public String showList(Model model, RedirectAttributes redirAttrs) {

		EstoqueProduto estoqueProduto = new EstoqueProduto();
		this.search(model, new EstoqueProdutoSearchForm(estoqueProduto, 
				buildManagementUtils.getPropertyInteger("default.system.initial-page"), 
				buildManagementUtils.getPropertyInteger("default.system.size-per-page")),
				redirAttrs);
		
        return FORWARD_LIST;
    }
	
	@PostMapping(BASE_DOMAIN + "/search")
    public String search(Model model, EstoqueProdutoSearchForm searchForm, RedirectAttributes redirAttrs) {
		
        this.paginate(model, 
        		new EstoqueProdutoSearchForm(searchForm.getData(), 
        				buildManagementUtils.getPropertyInteger("default.system.initial-page"), 
        				buildManagementUtils.getPropertyInteger("default.system.size-per-page")),
        		redirAttrs);

        return FORWARD_LIST;
    }
	
	@PostMapping(BASE_DOMAIN + "/paginate")
    public String paginate(Model model, EstoqueProdutoSearchForm searchForm, RedirectAttributes redirAttrs) {
        
		try { 
		
	        model.addAttribute("principalObject", searchForm);
	        model.addAttribute("principalList", 
	        		estoqueProdutoServiceImpl.searchPaginate(
	        				searchForm.getData(), 
	        				PageRequest.of(searchForm.getPageNumber() - 1, searchForm.getSize())));
        
		 } catch (Exception e) {
	        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
		 }
        
        return FORWARD_LIST;
    }
	
	@GetMapping(BASE_DOMAIN + "/show-form")
    public String showForm(Model model, EstoqueProduto estoqueProduto) {
		
		this.initForm(null, model);
		
		return FORWARD_FORM;
    }
	
	@PostMapping(BASE_DOMAIN + "/add")
    public String add(@Valid EstoqueProduto estoqueProduto, BindingResult result, Model model, RedirectAttributes redirAttrs) {
		try {
            
			estoqueProdutoServiceImpl.registry(estoqueProduto.getProduto().getIdProduto(), estoqueProduto.getQuantidade());
			redirAttrs.addFlashAttribute("success", "manutencao.estoqueProduto.label.registry.success");
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        }
		
		return "redirect:/produto/list";
    }
	
	private void initForm(EstoqueProduto estoqueProduto, Model model) {
		
		if (estoqueProduto == null) {
			estoqueProduto = new EstoqueProduto();
			
		} else if (estoqueProduto.getIdEstoqueProduto() != null) {
			estoqueProduto = estoqueProdutoServiceImpl.findById(estoqueProduto.getIdEstoqueProduto()).get();
		}
		
		model.addAttribute("estoqueProduto", estoqueProduto);
	}
}
