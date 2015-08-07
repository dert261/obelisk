package ru.obelisk.monitor.graph;

public class HandsetInNode extends Node {

	private static final long serialVersionUID = -5363734607450797922L;
	
	private String numberPatternA;
	private String numberPatternB;
		
	
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
		return "HandsetInNode [numberPatternA=" + numberPatternA
				+ ", numberPatternB=" + numberPatternB + "]";
	}
}
