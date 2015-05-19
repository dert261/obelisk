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

	private final List<T> aaData;
	private final Long iTotalRecords;
	private final Long iTotalDisplayRecords;
	private final Integer sEcho;

	private DatatablesResponse(DataSet<T> dataSet, DatatablesCriterias criterias) {
		this.aaData = dataSet.getRows();
		this.iTotalRecords = dataSet.getTotalRecords();
		this.iTotalDisplayRecords = dataSet.getTotalDisplayRecords();
		this.sEcho = criterias.getInternalCounter();
	}

	public List<T> getAaData() {
		return aaData;
	}

	public Long getiTotalRecords() {
		return iTotalRecords;
	}

	public Long getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}

	public Integer getsEcho() {
		return sEcho;
	}

	public static <T> DatatablesResponse<T> build(DataSet<T> dataSet, DatatablesCriterias criterias) {
		return new DatatablesResponse<T>(dataSet, criterias);
	}
}