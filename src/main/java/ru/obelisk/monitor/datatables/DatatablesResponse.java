package ru.obelisk.monitor.datatables;

import java.util.List;

/**
 * <p>
 * Bean that wraps a response that must be sent back to Datatables to update the
 * table when server-side processing is enabled.
 * <p>
 * Since Datatables only support JSON at the moment, this bean must be converted
 * to JSON by the server.
 * 
 * @author Thibault Duchateau
 * @since 0.8.2
 */
public class DatatablesResponse<T> {

	private final List<T> data;
	private final Long recordsTotal;
	private final Long recordsFiltered;
	private final Integer draw;

	private DatatablesResponse(DataSet<T> dataSet, DatatablesCriterias criterias) {
		this.data = dataSet.getRows();
		this.recordsTotal = dataSet.getTotalRecords();
		this.recordsFiltered = dataSet.getTotalDisplayRecords();
		this.draw = criterias.getInternalCounter();
	}
	
	private DatatablesResponse(DataSet<T> dataSet, int draw) {
		this.data = dataSet.getRows();
		this.recordsTotal = dataSet.getTotalRecords();
		this.recordsFiltered = dataSet.getTotalDisplayRecords();
		this.draw = draw;
	}

	public List<T> getdata() {
		return data;
	}

	public Long getrecordsTotal() {
		return recordsTotal;
	}

	public Long getrecordsFiltered() {
		return recordsFiltered;
	}

	public Integer getdraw() {
		return draw;
	}

	public static <T> DatatablesResponse<T> build(DataSet<T> dataSet, DatatablesCriterias criterias) {
		return new DatatablesResponse<T>(dataSet, criterias);
	}
	
	public static <T> DatatablesResponse<T> build(DataSet<T> dataSet, int draw) {
		return new DatatablesResponse<T>(dataSet, draw);
	}
}