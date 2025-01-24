package com.skymicrosystems.controleestoque.enums;

public enum MedidaEnum {
	
	UNIT("UNIDADE"), KG("KILOGRAMA"), GR("GRAMAS"), L("LITRO"), ML("MILILITRO");
	
	private MedidaEnum(String name) {
		this.name = name;
	}
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String[] toList() {
		return new String[]{KG.getName(), GR.getName(),L.getName(), ML.getName() };
	}
}
