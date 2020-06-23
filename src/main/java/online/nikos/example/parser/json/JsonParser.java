package online.nikos.example.parser.json;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JsonParser {

    private String str;
    private int i;

    private Map<String, String> map;

    public Map<String, String> parse(String json) {

        this.map = new HashMap<>();
        this.str = json;
        this.i=0;

        int depth = -1;

        int arrayIndex=0;

        boolean arrayPending = false;

        boolean arrayPendingObj = false;

        String[] strNames = new String[100];

        String currentString="";

        while(this.i < this.str.length()) {

            this.skipWhiteSpace();
            if(this.i >= this.str.length()){//in case an empty line exists at the bottom of json
                break;
            }

            char c = this.str.charAt(i);

            if(c=='{'){ //start object event
                depth += 1;
                if(depth==0) {
                    strNames[depth] = "";
                }else{
                    strNames[depth] =  strNames[depth-1];
                }
            } else if(c=='}'){//end object event
                myData(strNames[depth],currentString);
                currentString = "";
                depth -= 1;
                arrayPendingObj = false;
            } else if(c==','){//end value event, start key event
                myData(strNames[depth],currentString);
                currentString = "";
                if(!arrayPending) {
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
                strNames[depth] += "/" + stripWrapQuotes(currentString);
                currentString = "";
                if (arrayPending){
                    arrayPendingObj = true; //its an objects array, not String array
                }
            } else if(c=='[') {//start array event
                arrayPending = true;
                arrayIndex = 0;
                strNames[depth] += "[" + (arrayIndex++) + "]";
            } else if(c==']') {//end array event
                arrayPending = false;
                myData(strNames[depth],currentString);
                currentString = "";
            } else{
                currentString += c;
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
