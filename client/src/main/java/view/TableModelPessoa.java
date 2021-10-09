package view;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.Pessoa;

/**
 * @author Ruan
 */
public class TableModelPessoa extends AbstractTableModel {

    private List<Pessoa> data;

    public TableModelPessoa() {
        this.data = new ArrayList<Pessoa>();
    }
    
    @Override
    public int getRowCount() {
        return this.data.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Pessoa pessoa = this.data.get(rowIndex);
        switch (columnIndex) {
            case 0: return pessoa.getCpf();
            case 1: return pessoa.getNome();
            case 2: return pessoa.getEndereco();
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "CPF";
            case 1: return "Nome";
            case 2: return "Endereço";
        }
        return "";
    }

}