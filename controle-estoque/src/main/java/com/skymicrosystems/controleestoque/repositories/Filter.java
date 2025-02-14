package com.skymicrosystems.controleestoque.repositories;

import java.util.List;

public class Filter {
	
	private String field;
	private QueryOperator operator;
	private String value;
	private List<String> values;// Used in case of IN operator
	
	public Filter() {}
	
	public Filter(String field, QueryOperator operator, List<String> values) {
		super();
		this.field = field;
		this.operator = operator;
		this.values = values;
	}
	
	public Filter(String field, QueryOperator operator, String value) {
		super();
		this.field = field;
		this.operator = operator;
		this.value = value;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public QueryOperator getOperator() {
		return operator;
	}
	public void setOperator(QueryOperator operator) {
		this.operator = operator;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public List<String> getValues() {
		return values;
	}
	public void setValues(List<String> values) {
		this.values = values;
	}
}
