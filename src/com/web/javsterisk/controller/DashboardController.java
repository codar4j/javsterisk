package com.web.javsterisk.controller;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NfsFileSystem;
import org.hyperic.sigar.ProcState;
import org.hyperic.sigar.SigarException;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.primefaces.model.chart.MeterGaugeChartModel;
import org.primefaces.model.chart.PieChartModel;

import com.web.javsterisk.dao.ParameterDAO;
import com.web.javsterisk.entity.Cpu;
import com.web.javsterisk.entity.CpuInfo;
import com.web.javsterisk.entity.FSystem;
import com.web.javsterisk.entity.Parameter;
import com.web.javsterisk.entity.Ram;
import com.web.javsterisk.entity.Service;

/**
 * 
 * @author atorres
 *
 */

@ManagedBean
@ViewScoped
public class DashboardController extends BaseController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LogManager.getLogger(DashboardController.class);
	
	private ParameterDAO parameterDAO;
	
	@ManagedProperty("#{securityController}")
	private SecurityController securityController;
	
//	private final String CQL_COMMAND = "State.Name.eq=";
	
//	private final String SERVICE_MYSQL = "service.mysql";
	
//	private final String SERVICE_JBOSS = "service.jboss";
	
//	private final String SERVICE_ASTERISK = "service.asterisk";
	
	private DashboardModel mainModel;
	
	private MeterGaugeChartModel ramGaugeModel;
	private PieChartModel diskModel;
	private MeterGaugeChartModel cpuGaugeModel;
	
	private List<Ram> rams;
	private List<FSystem> disks;
	private List<Cpu> cpus;
	
	private CpuInfo cpuInfo;
	
	private List<Service> mysqls;
	
	private List<Service> asterisks;
	
	private List<Service> jbosses;
	
	Parameter param_mysql;
	
	Parameter param_asterisk;
	
	Parameter param_jboss;
	
	@PostConstruct
	public void initDashboard() {	
		parameterDAO = new ParameterDAO();
		log.info("@PostConstruct Dashboard");
		if(securityController.isAuthenticated()){
		
			param_mysql = parameterDAO.findByName("service.mysql");
			param_asterisk = parameterDAO.findByName("service.asterisk");
			param_jboss = parameterDAO.findByName("service.jboss");
			
			mainModel = new DefaultDashboardModel();
			
	        DashboardColumn column1 = new DefaultDashboardColumn();  
	        DashboardColumn column2 = new DefaultDashboardColumn();  
	        DashboardColumn column3 = new DefaultDashboardColumn();        
	        
	        column1.addWidget("mysql");
	        column1.addWidget("cpu");              
	        
	        column2.addWidget("asterisk");
	        column2.addWidget("ram");
	        
	        column3.addWidget("jboss");
	        column3.addWidget("disk");
	          
	        mainModel.addColumn(column1);  
	        mainModel.addColumn(column2);  
	        mainModel.addColumn(column3);  
		}
	}
	
	public DashboardModel getMainModel() {
		return mainModel;
	}

	public void setMainModel(DashboardModel model) {
		this.mainModel = model;
	}
		
	public MeterGaugeChartModel getRamGaugeModel() {
		log.info("getRamGaugeModel()");
		Number value = 35;
		try {
			Mem mem = this.sigar.getMem();		
			
			rams = new ArrayList<Ram>(0);
			
			Ram ram = new Ram();
			ram.setTotal(format(mem.getTotal()));
			ram.setUsed(format(mem.getUsed()));
			ram.setFree(format(mem.getFree()));
			ram.setUsedPercent(formatPct(mem.getUsedPercent()));
			ram.setFreePercent(formatPct(mem.getFreePercent()));
			ram.setRam(mem.getRam());

			value = Double.parseDouble(ram.getUsedPercent());
			
			log.info("-------------------------------");
			log.info("Value : {} %", value);
			log.info("Mem RAM : {} MB", ram.getRam());
			log.info("Mem Total : {}", ram.getTotal());
			log.info("Mem Used : {}", ram.getUsed());
			log.info("Mem Used % : {}", ram.getUsedPercent());
			log.info("Mem Free : {}", ram.getFree());
			log.info("Mem Free % : {}", ram.getFreePercent());
			log.info("-------------------------------");
			
			rams.add(ram);
			
		} catch (SigarException e) {
			log.error("SigarException", e);
		}
		  
        List<Number> intervals = new ArrayList<Number>(0);
        	intervals.add(20);  
        	intervals.add(30);  
        	intervals.add(50);
        	intervals.add(70);
        	intervals.add(100);  
  
        ramGaugeModel = new MeterGaugeChartModel(value, intervals);  
		return ramGaugeModel;
	}

	public void setRamGaugeModel(MeterGaugeChartModel ramGaugeModel) {
		this.ramGaugeModel = ramGaugeModel;
	}
	
	public PieChartModel getDiskModel() {
		log.info("getDiskModel()");
		try {
			long used, avail, total, pct;
			FileSystem[] fslist = this.proxy.getFileSystemList();
			if(fslist.length > 0){
				FileSystem fs = fslist[0];
				FileSystemUsage usage;
				if (fs instanceof NfsFileSystem) {
	                NfsFileSystem nfs = (NfsFileSystem)fs;
	                if (!nfs.ping()) {
	                    println(nfs.getUnreachableMessage());
	                    return null;
	                }
	            }
				usage = this.sigar.getFileSystemUsage(fs.getDirName());	            
	            used = usage.getTotal() - usage.getFree();
	            avail = usage.getAvail();
	            total = usage.getTotal();

	            pct = (long)(usage.getUsePercent() * 100);	            
	                
	            disks = new ArrayList<FSystem>(0);

	            FSystem fileSystem = new FSystem();
	            fileSystem.setDevName(fs.getDevName());
	            fileSystem.setTotal(formatSize(total));
	            fileSystem.setUsed(formatSize(used));
	            fileSystem.setAvail(formatSize(avail));    
	            fileSystem.setUsePct(pct);
	            fileSystem.setDirName(fs.getDirName());
	            fileSystem.setTypeName(fs.getSysTypeName() + "/" + fs.getTypeName());
	            
	            log.info("-------------------------------");
				log.info("Disk Dev Name : {}", fileSystem.getDevName());
				log.info("Disk Total : {} GB", fileSystem.getTotal());
				log.info("Disk Used : {} GB", fileSystem.getUsed());
				log.info("Disk Free : {} GB", fileSystem.getAvail());
				log.info("Disk Used : {} %", fileSystem.getUsePct());
				log.info("Disk Dir Name : {}", fileSystem.getDirName());
				log.info("Disk Type Name : {}", fileSystem.getTypeName());
				log.info("-------------------------------");
	            
	            disks.add(fileSystem);	
	            
	            diskModel = new PieChartModel();
	    		diskModel.set("Used", fileSystem.getUsePct());
	    		diskModel.set("free", 100 - fileSystem.getUsePct());	    			
			}
		} catch (SigarException e) {
			log.error("SigarException", e);
		}
		
		return diskModel;
	}

	public void setDiskModel(PieChartModel diskModel) {
		this.diskModel = diskModel;
	}
	
	public MeterGaugeChartModel getCpuGaugeModel() {
		log.info("getCpuGaugeModel()");
		Number value = 35;
		try {
			
			CpuPerc cpuPerc = this.sigar.getCpuPerc();
			
			cpus = new ArrayList<Cpu>(0);
			
			Cpu cpu = new Cpu();
			cpu.setUserTime(formatPct(calculatePct(cpuPerc.getUser())));
			cpu.setSysTime(formatPct(calculatePct(cpuPerc.getSys())));
			cpu.setIdleTime(formatPct(calculatePct(cpuPerc.getIdle())));
			cpu.setCombined(formatPct(calculatePct(cpuPerc.getCombined())));
			
			try {
				value = Double.parseDouble(cpu.getCombined());
			} catch (NumberFormatException e) {
				log.error(e.getMessage(), e);
			}			
			
			log.info("-------------------------------");
			log.info("value : {} %", value);
			log.info("CPU User Time : {} %", cpu.getUserTime());
			log.info("CPU Sys Time : {} %", cpu.getSysTime());
			log.info("CPU Idle Time : {} %", cpu.getIdleTime());
			log.info("CPU Combined Time : {} %", cpu.getCombined());			
			log.info("-------------------------------");
			
			cpus.add(cpu);
			
		} catch (SigarException e) {
			log.error("SigarException", e);
		}
		  
        List<Number> intervals = new ArrayList<Number>(0);

            intervals.add(20);  
            intervals.add(30);  
            intervals.add(50);
            intervals.add(70);
            intervals.add(100);  
  
        cpuGaugeModel = new MeterGaugeChartModel(value, intervals);  		
		return cpuGaugeModel;
	}

	public void setCpuGaugeModel(MeterGaugeChartModel cpuGaugeModel) {
		this.cpuGaugeModel = cpuGaugeModel;
	}

	public List<Cpu> getCpus() {
		return cpus;
	}

	public void setCpus(List<Cpu> cpus) {
		this.cpus = cpus;
	}
	
	public List<Ram> getRams() {
		return rams;
	}

	public void setRams(List<Ram> rams) {
		this.rams = rams;
	}

	public List<FSystem> getDisks() {
		return disks;
	}

	public void setDisks(List<FSystem> disks) {
		this.disks = disks;
	}

	private static Long format(long value) {
	    return new Long(value / 1024 / 1024);
	}

	private static String formatPct(double value){
		DecimalFormat df = new DecimalFormat("#.##");
		String val = df.format(value).replace(",", ".");
		return 	val;
	}
	
	private long formatSize(long size) {
//        return Sigar.formatSize(size * 1024);
		return size / 1024 / 1024;
    }
	
	private double calculatePct(double pct) {
		return pct * 100;
  }

	public CpuInfo getCpuInfo() {
		try
	    {
	      org.hyperic.sigar.CpuInfo[] infos = sigar.getCpuInfoList();
	      org.hyperic.sigar.CpuInfo info = infos[0];
	      cpuInfo = new com.web.javsterisk.entity.CpuInfo();
	      cpuInfo.setVendor(info.getVendor());
	      cpuInfo.setModel(info.getModel());
	      cpuInfo.setMhz(info.getMhz());
	      cpuInfo.setTotalCpu(info.getTotalCores());
	      cpuInfo.setPhysicalCpu(info.getTotalSockets());
	      cpuInfo.setCoresCpu(info.getCoresPerSocket());
	    } catch (SigarException e) {
	      log.error("SigarException", e);
	    }
		return cpuInfo;
	}

	public void setCpuInfo(CpuInfo cpuInfo) {
		this.cpuInfo = cpuInfo;
	}
	
	public List<Service> getMysqls() {
		try {
			mysqls = new ArrayList<Service>(0);
			ProcState state = null;
			Service mysql = null;
//			param_mysql = parameterDAO.findByName(SERVICE_MYSQL);
			long[] pids = this.shell.findPids(new String[]{"State.Name.eq=" + param_mysql.getValue()});			
//			for(int i = 0; i < pids.length; i++){
			if(pids.length > 0){			
				long pid = pids[0];
				log.info("=================================================");
				log.info("pid : {}", pid);
				mysql = new Service();
				mysql.setPid(pid);
				state = sigar.getProcState(pid);
				log.info("Name : {}", state.getName());
				log.info("Threads : {}", state.getThreads());
				log.info("State : {}", state.getState());
				log.info("Ppid : {}", state.getPpid());
				log.info("Priority : {}", state.getPriority());
				
				mysql.setRunning(state.getState() == 'D' || state.getState() == 'R' || state.getState() == 'S');
				log.info("Running : {}", mysql.isRunning());
				
				mysql.setProcState(state);
				mysqls.add(mysql);
				log.info("=================================================");
			} else {
				log.info("=================================================");
				mysql = new Service();
				mysql.setRunning(false);
				log.info("Running : {}", mysql.isRunning());
				mysql.setProcState(new ProcState());
				mysqls.add(mysql);
				log.info("=================================================");
			}
//			}						
		} catch (SigarException e) {
			log.error("SigarException", e);			
		}	
		return mysqls;
	}

	public void setMysqls(List<Service> mysqls) {
		this.mysqls = mysqls;
	}

	public List<Service> getAsterisks() {
		try {
			asterisks = new ArrayList<Service>(0);
			ProcState state = null;
			Service asterisk = null;
//			Parameter param = parameterDAO.findByName(SERVICE_ASTERISK);
			long[] pids = this.shell.findPids(new String[]{"State.Name.eq=" + param_asterisk.getValue()});			
//			for(int i = 0; i < pids.length; i++){
			if(pids.length > 0){			
				long pid = pids[0];
				log.info("=================================================");
				log.info("pid : {}", pid);
				asterisk = new Service();
				asterisk.setPid(pid);
				state = sigar.getProcState(pid);
				log.info("Name : {}", state.getName());
				log.info("Threads : {}", state.getThreads());
				log.info("State : {}", state.getState());
				log.info("Ppid : {}", state.getPpid());
				log.info("Priority : {}", state.getPriority());
				
				asterisk.setRunning(state.getState() == 'D' || state.getState() == 'R' || state.getState() == 'S');
				log.info("Running : {}", asterisk.isRunning());
				
				asterisk.setProcState(state);
				asterisks.add(asterisk);
				log.info("=================================================");
			} else {
				log.info("=================================================");
				asterisk = new Service();
				asterisk.setRunning(false);
				log.info("Running : {}", asterisk.isRunning());
				asterisk.setProcState(new ProcState());
				asterisks.add(asterisk);
				log.info("=================================================");
			}						
		} catch (SigarException e) {
			log.error("SigarException", e);			
		}	
		return asterisks;
	}

	public void setAsterisks(List<Service> asterisks) {
		this.asterisks = asterisks;
	}

	public List<Service> getJbosses() {
		try {
			jbosses = new ArrayList<Service>(0);
			ProcState state = null;
			Service jboss = null;
//			Parameter param = parameterDAO.findByName(SERVICE_JBOSS);
			long[] pids = this.shell.findPids(new String[]{"State.Name.eq=" + param_jboss.getValue()});			
//			for(int i = 0; i < pids.length; i++){
			if(pids.length > 0){			
				long pid = pids[0];
				log.info("=================================================");
				log.info("pid : {}", pid);
				jboss = new Service();
				jboss.setPid(pid);
				state = sigar.getProcState(pid);
				log.info("Name : {}", state.getName());
				log.info("Threads : {}", state.getThreads());
				log.info("State : {}", state.getState());
				log.info("Ppid : {}", state.getPpid());
				log.info("Priority : {}", state.getPriority());
				
				jboss.setRunning(state.getState() == 'D' || state.getState() == 'R' || state.getState() == 'S');
				log.info("Running : {}", jboss.isRunning());
				
				jboss.setProcState(state);
				jbosses.add(jboss);
				log.info("=================================================");
			} else {
				log.info("=================================================");
				jboss = new Service();
				jboss.setRunning(false);
				log.info("Running : {}", jboss.isRunning());
				jboss.setProcState(new ProcState());				
				jbosses.add(jboss);
				log.info("=================================================");
			}
		} catch (SigarException e) {
			log.error("SigarException", e);			
		}	
		return jbosses;
	}

	public void setJbosses(List<Service> jbosses) {
		this.jbosses = jbosses;
	}

	public Parameter getParam_mysql() {
		return param_mysql;
	}

	public void setParam_mysql(Parameter param_mysql) {
		this.param_mysql = param_mysql;
	}

	public Parameter getParam_asterisk() {
		return param_asterisk;
	}

	public void setParam_asterisk(Parameter param_asterisk) {
		this.param_asterisk = param_asterisk;
	}

	public Parameter getParam_jboss() {
		return param_jboss;
	}

	public void setParam_jboss(Parameter param_jboss) {
		this.param_jboss = param_jboss;
	}

	public SecurityController getSecurityController() {
		return securityController;
	}

	public void setSecurityController(SecurityController securityController) {
		this.securityController = securityController;
	}
	
}
