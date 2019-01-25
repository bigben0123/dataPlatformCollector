package org.data.collector;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "harvest.config")
public class PropertiesConfig {

    private String hehe;
    
    private String[] txtarray;
    
    private List<Map<String,String>> listmap;
    
    private List<String> liststr;
    
    private Map<String,String> routeMapping;
    private Map<String,String> clientsMap;

	public Map<String,String> getRouteMapping() {
		return routeMapping;
	}

	public void setRouteMapping(Map<String,String> routeMapping) {
		this.routeMapping = routeMapping;
	}

	public String getHehe() {
		return hehe;
	}

	public void setHehe(String hehe) {
		this.hehe = hehe;
	}

	public String[] getTxtarray() {
		return txtarray;
	}

	public void setTxtarray(String[] txtarray) {
		this.txtarray = txtarray;
	}

	public List<Map<String,String>> getListmap() {
		return listmap;
	}

	public void setListmap(List<Map<String,String>> listmap) {
		this.listmap = listmap;
	}

	public List<String> getListstr() {
		return liststr;
	}

	public void setListstr(List<String> liststr) {
		this.liststr = liststr;
	}

	@Override
	public String toString() {
		return "PropertiesConfig [hehe=" + hehe + ", txtarray=" + Arrays.toString(txtarray) + ", listmap=" + listmap + ", liststr="
				+ liststr + ", routeMapping=" + routeMapping + "]";
	}

	public Map<String,String> getClientsMap() {
		return clientsMap;
	}

	public void setClientsMap(Map<String,String> clientsMap) {
		this.clientsMap = clientsMap;
	}
    
    
    

    
}