package online.nikos.example.parser.json;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/v1/parser")
public class ParserController {

    @Autowired
    private ParserService service;

    @RequestMapping(path = "/json", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Map> keyValueMap(@RequestBody String doc){
        Map<String,String> map = service.keyValueMap(doc);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

}
