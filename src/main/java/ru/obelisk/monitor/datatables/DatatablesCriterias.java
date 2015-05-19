package ru.obelisk.monitor.datatables;

/*
 * [The "BSD licence"]
 * Copyright (c) 2012 Dandelion
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 3. Neither the name of Dandelion nor the names of its contributors 
 * may be used to endorse or promote products derived from this software 
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import ru.obelisk.monitor.datatables.utils.StringUtils;
import ru.obelisk.monitor.datatables.ColumnDef.SortDirection;
import ru.obelisk.monitor.datatables.DTConstants;

/**
 * <p>
 * POJO that wraps all the parameters sent by Datatables to the server when
 * server-side processing is enabled. This bean can then be used to build SQL
 * queries.
 * 
 * @author Thibault Duchateau
 * @since 0.8.2
 */
public class DatatablesCriterias implements Serializable {

	private static final long serialVersionUID = 8661357461501153387L;
	
	private String search;
	private Integer displayStart;
	private Integer displaySize;
	private List<ColumnDef> columnDefs;
	private List<ColumnDef> sortingColumnDefs;
	private Integer internalCounter;

	public DatatablesCriterias() {
	}

	public DatatablesCriterias(String search, Integer displayStart, Integer displaySize, List<ColumnDef> columnDefs,
			List<ColumnDef> sortingColumnDefs, Integer internalCounter) {
		this.search = search;
		this.displayStart = displayStart;
		this.displaySize = displaySize;
		this.columnDefs = columnDefs;
		this.sortingColumnDefs = sortingColumnDefs;
		this.internalCounter = internalCounter;
	}

	public Integer getDisplayStart() {
		return displayStart;
	}

	public Integer getDisplaySize() {
		return displaySize;
	}

	public String getSearch() {
		return search;
	}

	public Integer getInternalCounter() {
		return internalCounter;
	}

	public List<ColumnDef> getColumnDefs() {
		return columnDefs;
	}

	public List<ColumnDef> getSortingColumnDefs() {
		return sortingColumnDefs;
	}

	/**
	 * @return true if a column is filterable, false otherwise.
	 */
	public Boolean hasOneFilterableColumn() {
		for (ColumnDef columnDef : this.columnDefs) {
			if (columnDef.isFilterable()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return true if a column is being filtered, false otherwise.
	 */
	public Boolean hasOneFilteredColumn() {
		for (ColumnDef columnDef : this.columnDefs) {
			if (StringUtils.isNotBlank(columnDef.getSearch()) || StringUtils.isNotBlank(columnDef.getSearchFrom())
					|| StringUtils.isNotBlank(columnDef.getSearchTo())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return true if a column is being sorted, false otherwise.
	 */
	public Boolean hasOneSortedColumn() {
		return !sortingColumnDefs.isEmpty();
	}

	/**
	 * Map the request parameters into a bean to ease the build of SQL queries.
	 * 
	 * @param request
	 *            The request sent by Datatables containing all the parameters.
	 * @return a DatatablesCriterias bean.
	 */
	public static DatatablesCriterias getFromRequest(HttpServletRequest request) {

		if (request != null) {

			String sSearch = request.getParameter(DTConstants.DT_S_SEARCH);
			String sEcho = request.getParameter(DTConstants.DT_S_ECHO);
			String sDisplayStart = request.getParameter(DTConstants.DT_I_DISPLAY_START);
			String sDisplayLength = request.getParameter(DTConstants.DT_I_DISPLAY_LENGTH);
			String sColNumber = request.getParameter(DTConstants.DT_I_COLUMNS);
			String sSortingColNumber = request.getParameter(DTConstants.DT_I_SORTING_COLS);

			Integer iEcho = StringUtils.isNotBlank(sEcho) ? Integer.parseInt(sEcho) : -1;
			Integer iDisplayStart = StringUtils.isNotBlank(sDisplayStart) ? Integer.parseInt(sDisplayStart) : -1;
			Integer iDisplayLength = StringUtils.isNotBlank(sDisplayLength) ? Integer.parseInt(sDisplayLength) : -1;
			Integer colNumber = StringUtils.isNotBlank(sColNumber) ? Integer.parseInt(sColNumber) : -1;
			Integer sortingColNumber = StringUtils.isNotBlank(sSortingColNumber) ? Integer.parseInt(sSortingColNumber)
					: -1;

			List<ColumnDef> columnDefs = new ArrayList<ColumnDef>();
			List<ColumnDef> sortingColumnDefs = new LinkedList<ColumnDef>();

			for (int i = 0; i < colNumber; i++) {

				ColumnDef columnDef = new ColumnDef();

				columnDef.setName(request.getParameter(DTConstants.DT_M_DATA_PROP + i));
				columnDef.setFilterable(Boolean.parseBoolean(request.getParameter(DTConstants.DT_B_SEARCHABLE + i)));
				columnDef.setSortable(Boolean.parseBoolean(request.getParameter(DTConstants.DT_B_SORTABLE + i)));
				
				String columnSearch = request.getParameter(DTConstants.DT_S_COLUMN_SEARCH + i);
				if(StringUtils.isNotBlank(columnSearch)) {
					String[] splittedSearch = columnSearch.split("~");
					if("~".equals(columnSearch)) {
						columnDef.setSearch("");
					}
					else if(columnSearch.startsWith("~")) {
						columnDef.setSearchTo(splittedSearch[1]);
					}
					else if(columnSearch.endsWith("~")) {
						columnDef.setSearchFrom(splittedSearch[0]);
					}
					else if(columnSearch.contains("~")){
						columnDef.setSearchFrom(splittedSearch[0]);
						columnDef.setSearchTo(splittedSearch[1]);
					}
					else{
						columnDef.setSearch(columnSearch);
					}
				}
				
				columnDefs.add(columnDef);
			}

			// Sorted column management
			for (int i = 0; i < sortingColNumber; i++) {
				String sSortingCol = request.getParameter(DTConstants.DT_I_SORT_COL + i);
				Integer sortingCol = StringUtils.isNotBlank(sSortingCol) ? Integer.parseInt(sSortingCol) : -1;
				ColumnDef sortedColumnDef = columnDefs.get(sortingCol);

				String sortingColDirection = request.getParameter(DTConstants.DT_S_SORT_DIR + i);
				if (StringUtils.isNotBlank(sortingColDirection)) {
					sortedColumnDef.setSortDirection(SortDirection.valueOf(sortingColDirection.toUpperCase()));
				}

				sortingColumnDefs.add(sortedColumnDef);
			}

			return new DatatablesCriterias(sSearch, iDisplayStart, iDisplayLength, columnDefs, sortingColumnDefs, iEcho);
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		return "DatatablesCriterias [search=" + search + ", displayStart=" + displayStart + ", displaySize="
				+ displaySize + ", columnDefs=" + columnDefs + ", sortingColumnDefs=" + sortingColumnDefs
				+ ", internalCounter=" + internalCounter + "]";
	}
}