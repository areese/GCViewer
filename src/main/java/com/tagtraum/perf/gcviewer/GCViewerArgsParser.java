package com.tagtraum.perf.gcviewer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import com.tagtraum.perf.gcviewer.exp.DataWriterType;

/**
 * Parser for commandline arguments. 
 * 
 * @author engineersamuel
 */
public class GCViewerArgsParser {
    private static final int ARG_POS_GCFILE = 0;
    private static final int ARG_POS_SUMMARY_FILE = 1;
    private static final int ARG_POS_CHART_FILE = 2;
    
    private int argumentCount;
    private String chartFilePath;
    private String gcfile;
    private String summaryFilePath;
    private DataWriterType type = DataWriterType.SUMMARY;
    
    public int getArgumentCount() {
        return argumentCount;
    }
    
    public String getChartFilePath() {
        return chartFilePath;
    }
    
    public String getGcfile() {
        return gcfile;
    }
    
    public String getSummaryFilePath() {
        return summaryFilePath;
    }

    public DataWriterType getType() { 
        return type; 
    }

    /**
     * Parse arguments given in parameter. If an illegal argument is given, an exception is thrown.
     * 
     * @param args command line arguments to be parsed
     * @throws GCViewerArgsParserException notify about illegal argument
     */
    public void parseArguments(String[] args) throws GCViewerArgsParserException {
        List<String> argsList = new ArrayList<String>(Arrays.asList(args));
        
        Map<String,String> argsMap= new HashMap<>();
        // first find all of the args that are -<flag> <value>, and take those.
        // if none of those are presnet, then fall back and parse whatever is left
        ListIterator<String> itr = argsList.listIterator();
        while(itr.hasNext()) {
            String arg = itr.next();
            if (arg.startsWith("-")) {
                itr.remove();
                // we have an arg, so eat it and the next.
                String value = null;
                if (itr.hasNext()) {
                    value = itr.next();
                    if (value.startsWith("-")) {
                        itr.previous();
                    } else {
                        itr.remove();
                    }
                }
                argsMap.put(arg, value);
                
            }
        }
        
        for (Entry<String, String> e: argsMap.entrySet()) {
            switch (e.getKey()) {
            case "-t":
                type = parseType(e.getValue());
                break;
            }
        }

        argumentCount = argsList.size();
        gcfile = safeGetArgument(argsList, ARG_POS_GCFILE);
        summaryFilePath = safeGetArgument(argsList, ARG_POS_SUMMARY_FILE);
        chartFilePath = safeGetArgument(argsList, ARG_POS_CHART_FILE);
    }

    private DataWriterType parseType(String type) throws GCViewerArgsParserException {
        try {
            return DataWriterType.valueOf(type.toUpperCase());
        }
        catch (IllegalArgumentException e) {
            throw new GCViewerArgsParserException(type);
        }
    }
    
    private String safeGetArgument(List<String> arguments, int index) {
        if (arguments.size() > index) {
            return arguments.get(index);
        }
        else {
            return null;
        }
    }

}
