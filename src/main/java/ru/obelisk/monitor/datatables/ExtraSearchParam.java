package ru.obelisk.monitor.datatables;

import java.io.Serializable;
import java.util.List;

public class ExtraSearchParam implements Serializable {

	private static final long serialVersionUID = 4151220973866376356L;
	private String name;
	private boolean filterable;
	private List<String> search;
		
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isFilterable() {
		return filterable;
	}

	public void setFilterable(boolean filterable) {
		this.filterable = filterable;
	}

	public List<String> getSearch() {
		return search;
	}

	public void setSearch(List<String> search) {
		this.search = search;
	}

	@Override
	public String toString() {
		return "ExtraSearchParam [name=" + name + ", filterable=" + filterable
				+ ", search=" + search + "]";
	}
		
	
}