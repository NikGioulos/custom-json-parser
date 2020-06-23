package online.nikos.example.parser.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class JsonParserTest {

    private JsonParser parser = new JsonParser();

    private Map<String, String> map;

    @Test
    public void testEmptyMapEmptyJson(){
        String doc = "{}";
        map = parser.parse(doc);
        Assertions.assertTrue(map.isEmpty());
    }

    @Test
    public void testOneKeyWhenOneNameJson(){
        String doc = "{\"hello\":\"world\"}";
        map = parser.parse(doc);
        Assertions.assertEquals(1,map.size());
    }

    @Test
    public void testObjectJson(){
        String doc = "{\"hello\":\"world\", \"myName\":\"myValue\"}";
        map = parser.parse(doc);
        Assertions.assertEquals(2,map.size());
    }

    @Test
    public void testJsonWithArray(){
        String doc = "{\n" +
                "\"name\":\"john\",\n" +
                "\"age\":30,\n" +
                "\"vehicles\":{\n" +
                "\t\"aircraft\":{\n" +
                "\t\t\"name\":\"Boeing\",\n" +
                "\t\t\"model\":\"747\",\n" +
                "\t\t\"date\":{\n" +
                "\t\t\t\"y\":2004,\n" +
                "\t\t\t\"m\":10\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t\"cars\":[\n" +
                "\t\t{\n" +
                "\t\t\t\"name\":\"Ford\",\n" +
                "\t\t\t\"model\":\"Fiesta\",\n" +
                "\t\t\t\"date\":{\n" +
                "\t\t\t\t\"y\":1993,\n" +
                "\t\t\t\t\"m\":11\n" +
                "\t\t\t}\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"name\":\"BMW\",\n" +
                "\t\t\t\"model\":\"X3\",\n" +
                "\t\t\t\"date\":{\n" +
                "\t\t\t\t\"y\":2011,\n" +
                "\t\t\t\t\"m\":12\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t],\n" +
                "\t\"bike\":{\n" +
                "\t\t\"name\":\"Yamaha\",\n" +
                "\t\t\"model\":\"XT\",\n" +
                "\t\t\"date\":{\n" +
                "\t\t\t\"y\":1993,\n" +
                "\t\t\t\"m\":11\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}\n" +
                "}";
        map = parser.parse(doc);
        Assertions.assertEquals("john",map.get("/name"));
        Assertions.assertEquals("30",map.get("/age"));
        Assertions.assertEquals("Boeing",map.get("/vehicles/aircraft/name"));
        Assertions.assertEquals("747",map.get("/vehicles/aircraft/model"));
        Assertions.assertEquals("2004",map.get("/vehicles/aircraft/date/y"));
        Assertions.assertEquals("Ford",map.get("/vehicles/cars[0]/name"));
        Assertions.assertEquals("Fiesta",map.get("/vehicles/cars[0]/model"));
        Assertions.assertEquals("11",map.get("/vehicles/cars[0]/date/m"));
        Assertions.assertEquals("BMW",map.get("/vehicles/cars[1]/name"));
        Assertions.assertEquals("X3",map.get("/vehicles/cars[1]/model"));
        Assertions.assertEquals("12",map.get("/vehicles/cars[1]/date/m"));
        Assertions.assertEquals("11",map.get("/vehicles/bike/date/m"));
    }

}
