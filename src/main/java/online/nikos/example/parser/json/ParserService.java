package online.nikos.example.parser.json;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ParserService {

    @Autowired
    private JsonParser parser;

    public Map<String,String> keyValueMap(String doc) {
        Map<String,String> map = parser.parse(doc);
        return map;
    }

}
