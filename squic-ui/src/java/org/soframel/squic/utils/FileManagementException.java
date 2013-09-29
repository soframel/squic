package org.soframel.squic.utils;

/**
 * User: sophie
 * Date: 22/9/13
 */
public class FileManagementException extends Exception {
    public FileManagementException(String msg){
        super(msg);
    }
    public FileManagementException(Throwable t){
        super(t);
    }
    public FileManagementException(String msg, Throwable t){
        super(msg, t);
    }
}
