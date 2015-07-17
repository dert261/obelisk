package ru.obelisk.monitor.graph;

import java.io.Serializable;

public class HandsetInNode implements Node, Serializable{

	private static final long serialVersionUID = -5363734607450797922L;
	
	private int index=0;
	private String type;
	private String numberPatternA;
	private String numberPatternB;
		
	public int getIndex(){
		return this.index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public String getType(){
		return this.type;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public String getNumberPatternA() {
		return numberPatternA;
	}
	public void setNumberPatternA(String numberPatternA) {
		this.numberPatternA = numberPatternA;
	}
	public String getNumberPatternB() {
		return numberPatternB;
	}
	public void setNumberPatternB(String numberPatternB) {
		this.numberPatternB = numberPatternB;
	}

	@Override
	public String toString() {
		return "HeadsetInNode [index=" + index + ", type=" + type
				+ ", numberPatternA=" + numberPatternA + ", numberPatternB="
				+ numberPatternB + "]";
	}
}
