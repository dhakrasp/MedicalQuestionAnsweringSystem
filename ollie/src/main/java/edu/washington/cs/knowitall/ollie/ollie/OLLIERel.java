package edu.washington.cs.knowitall.ollie.ollie;

public class OLLIERel {
	private Double conf; 
	private String arg1;
	private String arg2;
	private String rel;
	private String attr_txt = "";
	private String attr_arg;
	private String attr_rel;
	private String enab_txt;
	private String enab_phr;
	
	public OLLIERel(Double conf, String arg1, String arg2, String rel, String attr_txt, String attr_arg, String attr_rel, String enab_txt, String enab_phr)
	{
		this.arg1 = arg1;
		this.arg2 = arg2;
		this.conf = conf;
		this.rel = rel;
		this.attr_txt = attr_txt;
		this.attr_arg = attr_arg;
		this.attr_rel = attr_rel;
		this.enab_txt = enab_txt;
		this.enab_phr = enab_phr;
	}
	
	public Double getConf(){
		return this.conf;
	}
	
	public String getArg1(){
		return this.arg1;
	}
	
	public String getArg2(){
		return this.arg2;
	}
	
	public String getRel(){
		return this.rel;
	}
	
	public String getAttTxt(){
		return this.attr_txt;
	}
	
	public String getAttArg(){
		return this.attr_arg;
	}
	
	public String getEnabTxt(){
		return this.enab_txt;
	}
	
	public String getAttRel(){
		return this.attr_rel;
	}
	
	public String getEnabPhr(){
		return this.enab_phr;
	}

}
