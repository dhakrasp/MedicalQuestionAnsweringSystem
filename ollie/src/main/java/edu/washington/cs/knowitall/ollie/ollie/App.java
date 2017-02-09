package edu.washington.cs.knowitall.ollie.ollie;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;  
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import edu.knowitall.ollie.Ollie;
import edu.knowitall.ollie.OllieExtraction;
import edu.knowitall.ollie.OllieExtractionInstance;
import edu.knowitall.tool.parse.MaltParser;
import edu.knowitall.tool.parse.graph.DependencyGraph;

/** This is an example class that shows one way of using Ollie from Java. */
public class App {
    // the extractor itself
    private Ollie ollie;

    // the parser--a step required before the extractor
    private MaltParser maltParser;

    // the path of the malt parser model file
    private static final String MALT_PARSER_FILENAME = "C:\\Projects\\OLLIE\\engmalt.linear-1.7.mco";

    public App() throws MalformedURLException {
        // initialize MaltParser
        scala.Option<File> nullOption = scala.Option.apply(null);
        //maltParser = new MaltParser(new File(MALT_PARSER_FILENAME).toURI().toURL(), new Open, nullOption);
        maltParser = new MaltParser(new File(MALT_PARSER_FILENAME));
        // initialize Ollie
        ollie = new Ollie();
    }

    /**
     * Gets Ollie extractions from a single sentence.
     * @param sentence
     * @return the set of ollie extractions
     */
    public Iterable<OllieExtractionInstance> extract(String sentence) {
        // parse the sentence
        DependencyGraph graph = maltParser.dependencyGraph(sentence);

        // run Ollie over the sentence and convert to a Java collection
        Iterable<OllieExtractionInstance> extrs = scala.collection.JavaConversions.asJavaIterable(ollie.extract(graph));
        return extrs;
    } 
    
    public List<OLLIERel> fetchRelationData(String sentence){
    	List<OLLIERel> relList = new ArrayList<OLLIERel>();
    	    	
    	Iterable<OllieExtractionInstance> extrs = extract(sentence);
        // print the extractions.
        for (OllieExtractionInstance inst : extrs) {
        	Double conf = 0.0; 
        	String arg1 = "";
        	String arg2 = "";
        	String rel = "";
        	String attr_txt = "";
        	String attr_arg = "";
        	String attr_rel = "";
        	String enab_txt = "";
        	String enab_phr = "";
        	
            OllieExtraction extr = inst.extr();
            conf = extr.openparseConfidence();
            arg1 = extr.arg1().text();
            rel = extr.rel().text();
            arg2 = extr.arg2().text();
            if(extr.attribution().toList().length() > 0){
            	attr_txt = extr.attribution().get().text();
            	attr_arg = extr.attribution().get().arg();
            	attr_rel = extr.attribution().get().rel();
            }
            
            if(extr.enabler().toList().length() > 0){
            	enab_txt = extr.enabler().get().text();
            	enab_phr = extr.enabler().get().phrase();
            }
            relList.add(new OLLIERel(conf, arg1, arg2, rel, attr_txt, attr_arg, attr_rel, enab_txt, enab_phr));
        }
    	return relList;
    }
    
   public List<OLLIERel> FilterRelations(List<OLLIERel> relList, Double thresh){
	   List<OLLIERel> filList = new ArrayList<OLLIERel>();
	   for(OLLIERel obj : relList){
		   if(obj.getConf() >= thresh)
		   {
			   filList.add(obj);
		   }
	   }
	   return filList;
    }

    public static void main(String args[]) throws MalformedURLException, IOException {
        System.out.println(App.class.getResource("C:\\Projects\\OLLIE\\ollie-master\\ollie-master\\example\\src\\main\\resouces\\logback.xml"));
        // initialize
        App ollieWrapper = new App();
        
        Double thresh = 0.7;
        BufferedReader reader = null;
		BufferedWriter writer1 = null;
		BufferedWriter writer2 = null;
		//File inFile = new File("C:\\Projects\\OLLIE\\ArticleAbstract.txt");
		//File outFile = new File("C:\\Projects\\OLLIE\\ArticleAbstract_Out.txt");
		//File filteredOutFile = new File("C:\\Projects\\OLLIE\\ArticleAbstract_FilteredOut.txt");
		
		File inFile = new File("C:\\Projects\\OLLIE\\Medical.txt");
		File outFile = new File("C:\\Projects\\OLLIE\\Medical_Out.txt");
		File filteredOutFile = new File("C:\\Projects\\OLLIE\\Medical_FilteredOut.txt");
		
		List<OLLIERel> relList = new ArrayList<OLLIERel>();
        List<OLLIERel> filList = new ArrayList<OLLIERel>();
		
        // extract from a single sentence.
        //String sentence = "President Obama will meet with Congressional leaders on Friday, and House Republicans summoned lawmakers back for a Sunday session, in a last-ditch effort to avert a fiscal crisis brought on by automatic tax increases and spending cuts scheduled to hit next week.";
        /*String sentence = "In this study we proposed a method for extracting the meaning of basic human activities by combining hand motion information and two object properties.";
        String sentence2 = "Overall, the results suggest that the best accuracy was obtained by our system with an accuracy classification of 92%, whereas the worst was obtained by a human labeling another human with 74.62%.";
        String sentence3 = "Since it shares semantic knowledge during the factorization, it can resolve the sparsity problem.";
        String sentence4 = "Microsoft co-founder Bill Gates spoke at a conference on Monday.";
        
        
        relList.addAll(ollieWrapper.fetchRelationData(sentence));
        relList.addAll(ollieWrapper.fetchRelationData(sentence2));
        relList.addAll(ollieWrapper.fetchRelationData(sentence3));
        relList.addAll(ollieWrapper.fetchRelationData(sentence4));*/
        
		try {
            reader = new BufferedReader(new FileReader(inFile));
            writer1 = new BufferedWriter(new FileWriter(outFile)); 
            writer2 = new BufferedWriter(new FileWriter(filteredOutFile)); 
           
            String lineRaw;
            while ((lineRaw = reader.readLine()) != null) {
            	String[] line =  lineRaw.split("\t");
            	relList.clear();
        		relList.addAll(ollieWrapper.fetchRelationData(line[1]));
        		for(OLLIERel obj : relList){
        			writer1.write(line[0] + "\t" + obj.getConf() + "\t" + obj.getArg1() + "\t" + obj.getRel() + "\t" + obj.getArg2() + "\t" + obj.getAttTxt() + "\t" + obj.getAttArg() + "\t" + obj.getAttRel() + "\t" + obj.getEnabPhr() + "\t" + obj.getEnabTxt() + "\n");
        			writer1.flush();
        		}
        		filList.clear();
        		filList = ollieWrapper.FilterRelations(relList, thresh);
    
        		for(OLLIERel obj : filList){
        			writer2.write(line[0] + "\t" + obj.getConf() + "\t" + obj.getArg1() + "\t" + obj.getRel() + "\t" + obj.getArg2() + "\t" + obj.getAttTxt() + "\t" + obj.getAttArg() + "\t" + obj.getAttRel() + "\t" + obj.getEnabPhr() + "\t" + obj.getEnabTxt() + "\n");
        			writer2.flush();
        		}
            }
		}
		catch(IOException exc)
		{
			System.out.println(exc.getMessage());
		}
		finally{
			if (reader != null) {
                reader.close();
            }
            if (writer1 != null) {
                writer1.close();
            }
            if (writer2 != null) {
                writer2.close();
            }
		}
    }
}

