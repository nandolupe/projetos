package com.skymicrosystems.controleestoque.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.skymicrosystems.controleestoque.services.EstoqueProdutoServiceImpl;
import com.skymicrosystems.controleestoque.services.ProdutoServiceImpl;

import net.sf.jasperreports.engine.JRException;

/**
 * @author luiz_pereira
 *
 */
@Controller
public class RelatorioController {
	
	private static final String FORWARD_LIST = "manutencao-relatorio/list";
	private static final String BASE_DOMAIN = "/relatorio";
	
	@Autowired
	private ProdutoServiceImpl produtoServiceImpl;
	
	@Autowired
	private EstoqueProdutoServiceImpl estoqueProdutoServiceImpl;
	
	@GetMapping(BASE_DOMAIN + "/list")
    public String showList(Model model, RedirectAttributes redirAttrs) {

		
        return FORWARD_LIST;
    }
	
	@GetMapping(path =  BASE_DOMAIN + "/produtos", produces="text/csv")
    public @ResponseBody byte[] executeRelatorioProdutos(Model model, RedirectAttributes redirAttrs) throws JRException, IOException {
        return produtoServiceImpl.exportarProdutos();
    }
	
	@GetMapping(path = BASE_DOMAIN + "/movimentacao-estoque", produces="text/csv")
    public @ResponseBody byte[] executeRelatorioMovimentacaoEstoque(Model model, RedirectAttributes redirAttrs) throws JRException, IOException {
		return estoqueProdutoServiceImpl.exportarMovimentacaoEstoque();
    }
}
