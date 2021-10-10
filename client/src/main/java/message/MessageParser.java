package message;

import interfaces.Transmissible;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ruan
 */
abstract public class MessageParser<Type extends Transmissible> {

    public static final String SEPARATOR = ";";
    
    private final ArrayList<String> conteudo;
    private final Type entity;
    
    abstract protected String getOperationDescription();

    abstract protected List<String> getEspecificInfo();

    public MessageParser(Type entity) {
        this.entity = entity;
        this.conteudo = new ArrayList<>();
        this.conteudo.addAll(this.getEspecificInfo());
    }
    
    public String getMessage() {
        return this.getConteudo().stream().reduce(this.getOperationDescription(), (String content1, String content2) -> {
            return content1.concat(MessageParser.SEPARATOR).concat(content2);
        });
    }

    public byte[] getMessageBytes() {
        return this.getMessage().getBytes();
    }
    
    public Type getEntity() {
        return entity;
    }
    
    public ArrayList<String> getConteudo() {
        return this.conteudo;
    }
    
}
