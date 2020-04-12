package ru.otus.enteties;

import ru.otus.annotations.Column;
import ru.otus.annotations.Id;

public class User {
    @Id
    @Column(FieldType=DBType.BIGINT, MaxLength = 20)
    private long id;
    @Column(FieldType=DBType.VARCHAR, MaxLength = 255)
    private String name;
    @Column(FieldType=DBType.INT, MaxLength = 3)
    private int age;

    public User() {
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
