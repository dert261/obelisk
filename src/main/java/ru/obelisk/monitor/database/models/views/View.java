package ru.obelisk.monitor.database.models.views;

public class View {
	public interface Datatable{}
	public interface Location extends Datatable {}
	public interface DevicePool extends Datatable {}
	public interface Timezone extends Datatable {}
	public interface TimePeriod extends Datatable {}
	public interface MMTimePeriods extends Datatable {}
	public interface TimeScheduleGroup extends Datatable {}
	public interface PbxStation extends Datatable {}
	public interface PbxStationGroup extends Datatable {}
	public interface Partition extends Datatable {}
	public interface CallingSearchSpace extends Datatable {}
}
