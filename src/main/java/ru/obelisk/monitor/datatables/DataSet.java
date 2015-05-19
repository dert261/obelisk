package ru.obelisk.monitor.datatables;

import java.util.List;

/**
 * <p>
 * Wrapping bean that must be sent back to Datatables when server-side
 * processing is enabled.
 * 
 * @author Thibault Duchateau
 * @since 0.8.2
 */
public final class DataSet<T> {
	
	private final List<T> rows;
	private final Long totalDisplayRecords;
	private final Long totalRecords;

	public DataSet(List<T> rows, Long totalRecords, Long totalDisplayRecords) {
		this.rows = rows;
		this.totalRecords = totalRecords;
		this.totalDisplayRecords = totalDisplayRecords;
	}

	public List<T> getRows() {
		return rows;
	}

	public Long getTotalDisplayRecords() {
		return totalDisplayRecords;
	}

	public Long getTotalRecords() {
		return totalRecords;
	}
}