package concepts;

import utils.encoders.HashEncoder;

public abstract class Concept {

    protected final HashEncoder encoder;
    protected String content;
    protected String name;

    protected String codification;

    protected Concept(HashEncoder encoder) {
        this.encoder = encoder;
    }

    public Concept content(String content){
        this.content = content;
        this.codification = encoder.encode(content.replaceAll(name, ""));
        return this;
    }

    public String codification(){
        return codification;
    }

    public Concept name(String filename){
        this.name = filename;
        return this;
    }

    public String name(){
        return name;
    }

    public byte[] content(){
        return content.getBytes();
    };

    public boolean hasContent() {
        return content != null;
    }
}
