package com.jt.bean.gateway;

import java.util.ArrayList;
import java.util.List;

public class GwConfigs{
	private List<GwConfig> configs;
	
	public GwConfigs(){
		this.configs=new ArrayList<GwConfig>();
	}
	public void addConfig(GwConfig config){
		configs.add(config);
	}
	public boolean removeConfig(GwConfig config){
		return configs.remove(config);
	}
	public GwConfig getConfigByName(String name){
		GwConfig curConfig=null;
		for(GwConfig temp:configs){
			if(name.equalsIgnoreCase(temp.getTaskName())){
				curConfig=temp;
				break;
			}
		}
			return curConfig;
	}

	public List<GwConfig> getConfigs() {
		return configs;
	}

	public void setConfigs(List<GwConfig> configs) {
		this.configs = configs;
	}
	
}
