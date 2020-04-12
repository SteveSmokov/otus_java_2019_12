package converter;

        import com.google.gson.Gson;
        import org.junit.jupiter.api.Assertions;
        import org.junit.jupiter.api.BeforeAll;
        import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.Test;
        import ru.otus.converter.DiyGson;
        import testclasses.MainTestClass;
        import testclasses.SubTestClass;

        import java.text.DateFormat;
        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.*;
        import java.util.stream.Collectors;
        import java.util.stream.Stream;

public class TestDiyGson {
    private Gson gson;
    private DiyGson diyGson;

    @BeforeEach
    public void TestRun(){
        gson = new Gson();
        diyGson = new DiyGson();
    }

    @Test
    public void booleanTest(){
        Boolean bln =true;
        Assertions.assertEquals(gson.toJson(bln),diyGson.toJSON(bln));
    }

    @Test
    public void intTest(){
        int i = 2;
        Assertions.assertEquals(gson.toJson(i),diyGson.toJSON(i));
    }

    @Test
    public void doubleTest(){
        double d = 2;
        Assertions.assertEquals(gson.toJson(d),diyGson.toJSON(d));
    }

    @Test
    public void FloatTest(){
        Float f =Float.valueOf(12543);
        Assertions.assertEquals(gson.toJson(f),diyGson.toJSON(f));
    }

    @Test
    public void DateTest() {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        try {
            Date d = dateFormat.parse("01.01.2012 02:03:05");
            Assertions.assertEquals(gson.toJson(d),diyGson.toJSON(d));
        } catch (ParseException E){
            E.printStackTrace();
        }

    }

    @Test
    public void StringTest(){
        String s = "String";
        Assertions.assertEquals(gson.toJson(s),diyGson.toJSON(s));
    }

    @Test
    public void charTest(){
        char c = 'c';
        Assertions.assertEquals(gson.toJson(c),diyGson.toJSON(c));
    }

    @Test
    public void setTest(){
        Set<Integer> setInt = new HashSet<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6));
        Assertions.assertEquals(gson.toJson(setInt),diyGson.toJSON(setInt));
    }

    @Test
    public void arrayTest(){
        int[] aInt = {1,2,3,4};
        Assertions.assertEquals(gson.toJson(aInt),diyGson.toJSON(aInt));
    }

    @Test
    public void ListTest(){
        List<Integer> intList = Arrays.asList(1,2,3,4,5,6);
        Assertions.assertEquals(gson.toJson(intList),diyGson.toJSON(intList));
    }

    @Test
    public void MapTest(){
        Map<String,String> map = Stream.of(new String[][] {{ "Key 1", "String 1"},
                { "Key 2", "String2"}}).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        Assertions.assertEquals(gson.toJson(map),diyGson.toJSON(map));
    }

    @Test
    public void SubClassTest(){
        SubTestClass subTestClass = new SubTestClass();
        Assertions.assertEquals(gson.toJson(subTestClass),diyGson.toJSON(subTestClass));
    }

    @Test
    public void MainTest(){
        MainTestClass mtc = new MainTestClass();
        Assertions.assertEquals(gson.toJson(mtc), diyGson.toJSON(mtc));
    }
}
