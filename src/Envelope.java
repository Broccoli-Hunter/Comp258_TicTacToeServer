
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author laura
 */
public class Envelope implements Serializable{ //interface
    //2 rules for serializable: need an empty constructor, and need public getters and setters
    // aka POJO = plain old java object
    
    
    private String id;
    private String args;
    private Object contents;
    
    
    public Envelope(){};

    public Envelope(String id, String args, Object contents) {
        this.id = id;
        this.args = args;
        this.contents = contents;
    }

    public String getId() {
        return id;
    }

    public String getArgs() {
        return args;
    }

    public Object getContents() {
        return contents;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public void setContents(Object contents) {
        this.contents = contents;
    }
    
    
    
    
    
    
}
