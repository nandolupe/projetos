package com.exercicios.exerciciocrud.rest.controller;

import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.exercicios.exerciciocrud.dto.AjaxResponseBody;
import com.exercicios.exerciciocrud.forms.ProdutoSearchForm;
import com.exercicios.exerciciocrud.model.Produto;
import com.exercicios.exerciciocrud.services.ProdutoServiceImpl;

@RestController
public class ProdutoRestController {
	
	@Autowired
	private ProdutoServiceImpl produtoServiceImpl;
	
	@PostMapping("/rest/produto/list")
    public ResponseEntity<?> getSearchResultViaAjax(
            @Valid @RequestBody ProdutoSearchForm search, Errors errors) {

        AjaxResponseBody result = new AjaxResponseBody();
        
        System.out.println(search.getData().getNomeProduto());
        
        //If error, just return a 400 bad request, along with the error message
        if (errors.hasErrors()) {

            result.setMsg(errors.getAllErrors()
                        .stream().map(x -> x.getDefaultMessage())
                        .collect(Collectors.joining(",")));

            return ResponseEntity.badRequest().body(result);

        }

        Page<Produto> searchPaginate = 
        		produtoServiceImpl.searchPaginate(
        				search.getData(), 
        				PageRequest.of(0, 40));
        
        
        if (searchPaginate.isEmpty()) {
            result.setMsg("Produto nao encontrado!");
        } else {
            result.setMsg("success");
        }
        result.setResult(searchPaginate.getContent());

        return ResponseEntity.ok(result);

    }
}
