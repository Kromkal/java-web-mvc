package com.vison.webmvc;

import java.lang.reflect.Method;

/**
 *
 * @author vison.cao <visonforcoding@gmail.com>
 */
public class Test {
    
    public static void main(String[] args) throws Exception {
        Class stdClass = Student.class;
        Method method = stdClass.getMethod("getScore", String.class);
        Object obj = stdClass.getDeclaredConstructor().newInstance();
        System.out.print(method.invoke(obj, "888"));
    }
    
}

class Student extends Person {
    
    public int getScore(String type) {
        return 99;
    }
    
    private int getGrade(int year) {
        return 1;
    }
}

class Person {
    
    public String getName() {
        return "Person";
    }
}
