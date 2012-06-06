package com.ebay.build.profiler.render;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

public class ProfileOutputJSON {

	private Map<String, Long> projectSlicing = new LinkedHashMap<String, Long>();
	private Map<String, Long> PhaseSlice = new LinkedHashMap<String, Long>();
	private Map<String, Long> pluginSlice = new LinkedHashMap<String, Long>();
	
	private Map<String,LinkedList<Long>> projectToPluginSlicing = new LinkedHashMap<String,LinkedList<Long>>();
	private Map<String,LinkedList<Long>> projectToPhaseSlicing = new LinkedHashMap<String,LinkedList<Long>>();

	public Map<String, Long> getPhaseSlice() {
		return PhaseSlice;
	}
	public void setPhaseSlice(Map<String, Long> phaseSlice) {
		PhaseSlice = phaseSlice;
	}
	
	public Map<String,LinkedList<Long>> getProjectToPluginSlicing() {
		return projectToPluginSlicing;
	}
	public void setProjectToPluginSlicing(Map<String,LinkedList<Long>> projectToPluginSlicing) {
		this.projectToPluginSlicing = projectToPluginSlicing;
	}
	
	public Map<String, Long> getPluginSlice() {
		return pluginSlice;
	}
	public void setPluginSlice(Map<String, Long> pluginSlice) {
		this.pluginSlice = pluginSlice;
	}
	
	public Map<String, Long> getProjectSlicing() {
		return projectSlicing;
	}
	public void setProjectSlicing(Map<String, Long> projectSlicing) {
		this.projectSlicing = projectSlicing;
	}
	
	public Map<String,LinkedList<Long>> getProjectToPhaseSlicing() {
		return projectToPhaseSlicing;
	}
	public void setProjectToPhaseSlicing(Map<String,LinkedList<Long>> projectToPhaseSlicing) {
		this.projectToPhaseSlicing = projectToPhaseSlicing;
	}
	
	public void toCSV() {
		
		System.out.println("Project Slicing");
		for(String entry : projectSlicing.keySet()) {
			System.out.print(entry);
			System.out.print(",");
			System.out.println();
		}
		
		System.out.println("Plugin Slicing");
		for(Entry<String,LinkedList<Long>> entry : projectToPluginSlicing.entrySet()) {
			System.out.println(csvLine(entry));
		}
		
		System.out.println("Phase Slicing");
		for(Entry<String,LinkedList<Long>> entry : projectToPhaseSlicing.entrySet()) {
			System.out.println(csvLine(entry));
		}
	}
	
	private String csvLine(Entry<String, LinkedList<Long>> entry) {
		StringBuilder sb = new StringBuilder();
		sb.append(entry.getKey());
		for(Long l : entry.getValue()) {
			sb.append(",");
			sb.append(l.toString());
		}
		
		return sb.toString();
	}
	
	
}
