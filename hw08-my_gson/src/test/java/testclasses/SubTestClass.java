package testclasses;

public class SubTestClass {
    private String field1 = "Field 1";
    private String field2 = "Field 2";
    private String field3 = "Fielld 3";

    transient String transientString = "Transient string";

    @Override
    public String toString() {
        return "SubTestClass{" +
                "field1='" + field1 + '\'' +
                ", field2='" + field2 + '\'' +
                ", field3='" + field3 + '\'' +
                ", transientString='" + transientString + '\'' +
                '}';
    }
}
