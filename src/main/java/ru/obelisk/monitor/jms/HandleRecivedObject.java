package ru.obelisk.monitor.jms;

import org.springframework.stereotype.Component;

@Component
public interface HandleRecivedObject {
	public void handleObject(Object object);
}
