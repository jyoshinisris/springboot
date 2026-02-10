package org.test.crash_course_springboot.exceptions;

public class UnauthorizedActionException extends Throwable{
    public UnauthorizedActionException (String s){
        super(s);
    }
}
