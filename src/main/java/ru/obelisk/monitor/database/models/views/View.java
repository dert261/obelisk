package ru.obelisk.monitor.database.models.views;

public class View {
	public interface Datatable{}
	public interface Location extends Datatable {}
	public interface DevicePool extends Datatable {}
	public interface Timezone extends Datatable {}
}
