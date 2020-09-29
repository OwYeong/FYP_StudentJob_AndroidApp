package database;

import java.io.Serializable;

public class data implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public int id;
    public String content;

    public data(int id, String content) {
        this.id=id;
        this.content=content;
    }
}
