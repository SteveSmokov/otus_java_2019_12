package testclasses;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainTestClass {
    private int i = 2;
    private boolean bln = true;
    private double dbl = 123.456;
    private long lng = 123456L;
    private char chr = 'C';
    private Integer itgr = 123;
    private String str = "String";
    private Date date = new Date();
    private Float flt = Float.valueOf(123);
    private SubTestClass child = new SubTestClass();
    private List<String> lst = Arrays.asList("String1","String2","String3","String4");
    private Integer[] arrayInt = {1,2,3,4};
    private Set<Integer> setInt = new HashSet<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6));
    private Map<String, String> map = Stream.of(new String[][] {{ "String1", "String2" },
            { "String2", "String3" },}).collect(Collectors.toMap(data -> data[0], data -> data[1]));

    private final String finalString = "Final string";
    transient String transString = "Transient string";

    @Override
    public String toString() {
        return "MainTestClass{" +
                "i=" + i +
                ", bln=" + bln +
                ", dbl=" + dbl +
                ", lng=" + lng +
                ", chr=" + chr +
                ", itgr=" + itgr +
                ", str='" + str + '\'' +
                ", date=" + date +
                ", flt=" + flt +
                ", lst=" + lst +
                ", arrayInt=" + Arrays.toString(arrayInt) +
                ", setInt=" + setInt +
                ", map=" + map +
                ", finalString='" + finalString + '\'' +
                ", transString='" + transString + '\'' +
                '}';
    }
}
