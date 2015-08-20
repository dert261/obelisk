package ru.obelisk.monitor.graph;

import java.io.Serializable;

public abstract class Node implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int index=0;
	private String type;
//	private boolean sisyphus = false;
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Node [index=" + index + ", type=" + type + "]";
	}

/*	public boolean isSisyphus() {
		return sisyphus;
	}

	public void setSisyphus(boolean sisyphus) {
		this.sisyphus = sisyphus;
	}
*/
/*	@Override
	public String toString() {
		return "Node [index=" + index + ", type=" + type + ", sisyphus="
				+ sisyphus + "]";
	}
*/	
}
