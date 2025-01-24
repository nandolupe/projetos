package com.skymicrosystems.controleestoque.forms;

import com.skymicrosystems.controleestoque.model.Empresa;

public class EmpresaSearchForm extends DefaultSearchForm<Empresa> {

	public EmpresaSearchForm(Empresa data, Integer pageNumber, Integer size) {
		super.setData(data);
		super.setPageNumber(pageNumber);
		super.setSize(size);
	}
	
}
