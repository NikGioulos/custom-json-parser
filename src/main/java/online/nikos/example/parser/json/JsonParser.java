package online.nikos.example.parser.json;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class JsonParser {

    private String str;
    private int i;

    private Map<String, String> map;

    public Map<String, String> parse(String json) {

        this.map = new LinkedHashMap<>(); //since we want FIFO sorting
        this.str = json;
        this.i=0;

        int depth = -1;
        int MAX_DEPTH = 100;

        int arrayIndex=0;

        //it indicates whether we are in array or not
        boolean arrayPending = false;

        //it indicates if an array contains primitive or object items
        boolean arrayPendingObj = false;

        String[] strNames = new String[MAX_DEPTH];

        StringBuffer currentString = new StringBuffer();

        while(this.i < this.str.length()) {

            this.skipWhiteSpace();
            if(this.i >= this.str.length()){//in case an empty line exists at the bottom of json
                break;
            }

            char c = this.str.charAt(i);

            if(c=='{'){ //start object event
                strNames[++depth] = depth==0 ? "" : strNames[depth-1];
            } else if(c=='}'){//end object event
                myData(strNames[depth],currentString.toString());
                currentString.setLength(0); //clear
                depth -= 1;
                arrayPendingObj = false;
            } else if(c==','){//end value event, start key event
                myData(strNames[depth],currentString.toString());
                currentString.setLength(0); //clear
                if(!arrayPending) { //we are NOT within array
                    int lastSlash = strNames[depth].lastIndexOf("/");
                    strNames[depth] = strNames[depth].substring(0, lastSlash);
                } else if(arrayPendingObj){ //we are within an array of object items
                    int lastSlash = strNames[depth].lastIndexOf("/");
                    strNames[depth] = strNames[depth].substring(0, lastSlash);
                    arrayPendingObj = false;
                } else{ //we are within an array of primitive items
                    strNames[depth] = strNames[depth].substring(0,strNames[depth].length()-2) + (arrayIndex++) + "]";
                }
            } else if(c==':') {//end name, start value event
                strNames[depth] += "/" + stripWrapQuotes(currentString.toString());
                currentString.setLength(0); //clear
                if (arrayPending){
                    arrayPendingObj = true; //its an objects array, not String array
                }
            } else if(c=='[') {//start array event
                arrayPending = true;
                arrayIndex = 0;
                strNames[depth] += "[" + (arrayIndex++) + "]";
            } else if(c==']') {//end array event
                arrayPending = false;
                myData(strNames[depth],currentString.toString());
                currentString.setLength(0); //clear
            } else{
                currentString.append(c);
            }
            this.i++;
        }
        return map;
    }

    private boolean isWhiteSpace(char c) {
        return c==' ' || c=='\r' || c=='\n' || c=='\t';
    }

    private void skipWhiteSpace() {
        while(this.i < this.str.length() && this.isWhiteSpace(this.str.charAt(this.i))) this.i++;
    }

    private String stripWrapQuotes(String s){
        if(s.startsWith("\"") || s.startsWith("\'")){
            s = s.substring(1, s.length()-1);
        }
        return s;
    }

    private void myData(String name, String value){
        if(value==null || value.isEmpty()){
            return;
        }
        String v = stripWrapQuotes(value);

        //System.out.println(name + ":" + v);
        map.put(name,v);
    }

}
