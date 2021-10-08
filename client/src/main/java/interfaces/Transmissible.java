package interfaces;

import java.util.List;

/**
 * @author Ruan
 */
public interface Transmissible {
    
    public List<String> getInfoInsert();
    
    
    public List<String> getInfoUpdate();
    
    
    public List<String> getInfoGet();
    
    
    public List<String> getInfoDelete();
    
    
    public List<String> getInfoList();
    
}