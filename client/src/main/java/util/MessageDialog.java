package util;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 * @author Ruan
 */
public class MessageDialog {

    static public void show(Component context, Response response) {
        JOptionPane.showMessageDialog(
            context,
            response.getMessage(),
            response.isSuccess() ? "Info" : "Erro",
            response.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
        );
    }
    
}