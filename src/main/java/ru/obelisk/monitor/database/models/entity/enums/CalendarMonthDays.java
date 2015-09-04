package ru.obelisk.monitor.database.models.entity.enums;

public enum CalendarMonthDays {
	NOTSELECT(0),
	CH1(1),
	CH2(2),
	CH3(3),
	CH4(4),
	CH5(5),
	CH6(6),
	CH7(7),
	CH8(8),
	CH9(9),
	CH10(10),
	CH11(11),
	CH12(12),
	CH13(13),
	CH14(14),
	CH15(15),
	CH16(16),
	CH17(17),
	CH18(18),
	CH19(19),
	CH20(20),
	CH21(21),
	CH22(22),
	CH23(23),
	CH24(24),
	CH25(25),
	CH26(26),
	CH27(27),
	CH28(28),
	CH29(29),
	CH30(30),
	CH31(31);
	
	private int num;
	
	CalendarMonthDays(){	
	}
	
	private CalendarMonthDays(int num){
		this.num = num;
	}

	public int getNumber(){
		return num;
	}
}