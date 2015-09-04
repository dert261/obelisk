package ru.obelisk.monitor.database.models.entity.enums;

public enum CalendarMonths {
	NOTSELECT(0),
	JANUARY(1),
	FEBRUARY(2),
	MARCH(3),
	APRIL(4),
	MAY(5),
	JUNE(6),
	JULY(7),
	AUGUST(8),
	SEPTEMBER(9),
	OCTOBER(10),
	NOVEMBER(11),
	DECEMBER(12);
	
	CalendarMonths(){	
	}
	
	private int num;
		
	private CalendarMonths(int num){
		this.num = num;
	}

	public int getNumber(){
		return num;
	}
}