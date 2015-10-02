package ru.obelisk.monitor.web.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletResponse;






//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.obelisk.message.data.Channel;
//import ru.obelisk.message.data.ChannelsList;
//import ru.obelisk.message.data.ChannelsListImpl;
import ru.obelisk.message.data.HostInfo;
import ru.obelisk.message.data.HostInfoListImpl;
import ru.obelisk.message.data.Peer;

@Controller
public class JsonController {
	@Autowired private HostInfoListImpl hosts;
	
	@RequestMapping(value = {"/channels.json"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody List<Channel> channelsData(HttpServletResponse res) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception {
		List<Channel> list = new ArrayList<Channel>();
		List<HostInfo> hostList = new CopyOnWriteArrayList<HostInfo>(hosts.getHostInfoList());
		Iterator<HostInfo> hostIter = hostList.iterator();
		while(hostIter.hasNext()){
			HostInfo host = hostIter.next();
			list.addAll(host.getChannels().getChannels());
		}
		return list;
	}
	
	@RequestMapping(value = {"/peers.json"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody List<Peer> peersData(HttpServletResponse res) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception {
		
		List<Peer> list = new ArrayList<Peer>();
		List<HostInfo> hostList = new CopyOnWriteArrayList<HostInfo>(hosts.getHostInfoList());
		Iterator<HostInfo> hostIter = hostList.iterator();
		while(hostIter.hasNext()){
			HostInfo host = hostIter.next();
			list.addAll(host.getPeers().getPeers());
		}	
		return list;
	}
		
	@RequestMapping(value = {"/activeChannelCount.json"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody int[] activeChannelsCount(HttpServletResponse res) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception {
		
		List<Channel> list = new ArrayList<Channel>();
		List<HostInfo> hostList = new CopyOnWriteArrayList<HostInfo>(hosts.getHostInfoList());
		Iterator<HostInfo> hostIter = hostList.iterator();
		while(hostIter.hasNext()){
			HostInfo host = hostIter.next();
			list.addAll(host.getChannels().getChannels());
		}
						
		int[] count = {list.size()};
		return count;
	}
	
	@RequestMapping(value = {"/activePeerCount.json"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody int activePeersCount(HttpServletResponse res) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception {
		
		List<Peer> list = new ArrayList<Peer>();
		List<HostInfo> hostList = new CopyOnWriteArrayList<HostInfo>(hosts.getHostInfoList());
		Iterator<HostInfo> hostIter = hostList.iterator();
		while(hostIter.hasNext()){
			HostInfo host = hostIter.next();
			list.addAll(host.getPeers().getPeers());
		}	
		
		return list.size();
	}
	
	@RequestMapping(value = {"/activeHostCount.json"}, method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
	public @ResponseBody int activeHostsCount(HttpServletResponse res) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException, AuthenticationFailedException, Exception {
		return hosts.getHostInfoList().size();
	}
	
	
}
