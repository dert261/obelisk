package ru.obelisk.monitor.database.models.entity.enums;

public enum CalendarDays {
	NOTSELECT(0),
	MONDAY(1),
	TUESDAY(2),
	WEDNESDAY(3),
	THURSDAY(4),
	FRIDAY(5),
	SATURDAY(6),
	SUNDAY(7);
	
	CalendarDays(){	
	}
	
	private int num;
	
	
	private CalendarDays(int num){
		this.num = num;
	}

	public int getNumber(){
		return num;
	}
}