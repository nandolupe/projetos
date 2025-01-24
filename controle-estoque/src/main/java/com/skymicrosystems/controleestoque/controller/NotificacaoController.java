package com.skymicrosystems.controleestoque.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.skymicrosystems.controleestoque.forms.NotificacaoSearchForm;
import com.skymicrosystems.controleestoque.model.Notificacao;
import com.skymicrosystems.controleestoque.services.NotificacaoServiceImpl;
import com.skymicrosystems.controleestoque.utils.BuildManagementUtils;

/**
 * @author luiz_pereira
 *
 */
@Controller
public class NotificacaoController {
	
	private final static Logger logger = LoggerFactory.getLogger(NotificacaoController.class);
	
	private static final String FORWARD_LIST = "manutencao-notificacao/list";
	private static final String BASE_DOMAIN = "/notificacao";

	@Autowired
	private NotificacaoServiceImpl notificacaoServiceImpl;
	
	@Autowired
	private BuildManagementUtils buildManagementUtils;
	
	@GetMapping(BASE_DOMAIN + "/list")
    public String showList(Model model, RedirectAttributes redirAttrs) {

		Notificacao notificacao = new Notificacao();
		this.search(model, new NotificacaoSearchForm(notificacao, 
				buildManagementUtils.getPropertyInteger("default.system.initial-page"), 
				buildManagementUtils.getPropertyInteger("default.system.size-per-page")),
				redirAttrs);
		
        return FORWARD_LIST;
    }
	
	@PostMapping(BASE_DOMAIN + "/search")
    public String search(Model model, NotificacaoSearchForm searchForm, RedirectAttributes redirAttrs) {
		
        this.paginate(model, 
        		new NotificacaoSearchForm(searchForm.getData(), 
        				buildManagementUtils.getPropertyInteger("default.system.initial-page"), 
        				buildManagementUtils.getPropertyInteger("default.system.size-per-page")),
        		redirAttrs);

        return FORWARD_LIST;
    }
	
	@PostMapping(BASE_DOMAIN + "/paginate")
    public String paginate(Model model, NotificacaoSearchForm searchForm, RedirectAttributes redirAttrs) {
        
		try { 
		
	        model.addAttribute("principalObject", searchForm);
	        model.addAttribute("principalList", 
	        		notificacaoServiceImpl.searchPaginate(
	        				searchForm.getData(), 
	        				PageRequest.of(searchForm.getPageNumber() - 1, searchForm.getSize())));
        
		 } catch (Exception e) {
	        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
		 }
        
        return FORWARD_LIST;
    }
}
