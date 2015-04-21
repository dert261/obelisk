package ru.obelisk.monitor.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*import ru.obelisk.message.data.ChannelsList;
import ru.obelisk.message.data.ChannelsListImpl;*/
import ru.obelisk.message.data.HostInfo;
import ru.obelisk.message.data.HostInfoListImpl;
/*import ru.obelisk.message.data.PeersList;
import ru.obelisk.message.data.PeersListImpl;*/

@Component
public class HandleRecivedObjectImpl implements HandleRecivedObject {
	
	/*@Autowired private ChannelsListImpl channels;
	@Autowired private PeersListImpl peers;*/
	@Autowired private HostInfoListImpl hostInfos;
	
	@Override
	public void handleObject(Object object) {
		/*if(object instanceof PeersList){
			PeersList recivedPeers = (PeersList) object;
			peers.mergePeers(recivedPeers);
		} else if (object instanceof ChannelsList){
			ChannelsList recivedChannels = (ChannelsList) object;
			channels.mergeChannels(recivedChannels);
		} else*/
		if (object instanceof HostInfo){
			HostInfo recivedHostInfo = (HostInfo) object;
			hostInfos.mergeHostInfos(recivedHostInfo);
		} else {
			
		}
	}

}
