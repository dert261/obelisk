package ru.obelisk.monitor.data;

//import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ru.obelisk.message.data.HostInfo;
import ru.obelisk.message.data.HostInfoListImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

@Component
public class DataRefresher {
	
	private static Logger logger = LogManager.getLogger(DataRefresher.class);
	
	@Autowired HostInfoListImpl hosts;
	
	@Scheduled(initialDelay=1000, fixedRate=1000)
	public void refresh(){
		Date curr = new Date();
		List<HostInfo> removedHosts = new CopyOnWriteArrayList<HostInfo>();
		
		Iterator<HostInfo> iter = hosts.getHostInfoList().iterator();
		//for(HostInfo host : hosts.getHostInfoList()){
		while(iter.hasNext()){
			HostInfo host = iter.next();
			int secs = Seconds.secondsBetween(new DateTime(host.getTimestamp()), new DateTime(curr)).getSeconds();
			if(secs>10){
				removedHosts.add(host);
			}
		}	
		//}
		if(removedHosts.size()>0)
			logger.info("Remove host. No packet from host: {}",removedHosts);
		hosts.getHostInfoList().removeAll(removedHosts);
		
	}
}
