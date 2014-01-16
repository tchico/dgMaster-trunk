/*
 * dgMaster: A versatile, open source data generator.
 *(c) 2007 M. Michalakopoulos, mmichalak@gmail.com
 */


package generator.panels;
import generator.extenders.RandomiserInstance;
import generator.extenders.RandomiserPanel;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;


public class PanelNumIntegerRandomiser extends RandomiserPanel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5436416742835388861L;

	Logger logger = Logger.getLogger(PanelNumIntegerRandomiser.class);
    /** Creates new form PanelDoubleGenerator */
    public PanelNumIntegerRandomiser()
    {
        initComponents();
        newModel.addColumn("From");
        newModel.addColumn("To");
        newModel.addColumn("Percentage");
        
        tblRanges.setModel(newModel);        
        loadButtonImages();
    }
    
    private void loadButtonImages() {
        URL urlAdd = this.getClass().getClassLoader().getResource("generator/images/list-add-small.png");
        URL urlRemSelect = this.getClass().getClassLoader().getResource("generator/images/list-remove-small.png");
        
        btnAdd.setIcon(new ImageIcon(urlAdd));
        btnRemove.setIcon(new ImageIcon(urlRemSelect));
    }

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    @SuppressWarnings("deprecation")
	private void initComponents()
    {
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescription = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        txtFrom = new javax.swing.JTextField();
        txtTo = new javax.swing.JTextField();
        txtPercent = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblRanges = new javax.swing.JTable();
        btnRemove = new javax.swing.JButton();
        spinNull = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Case description"));
        jLabel4.setText("Name:");

        jLabel5.setText("Description:");

        txtDescription.setColumns(20);
        txtDescription.setFont(new java.awt.Font("Tahoma", 0, 11));
        txtDescription.setLineWrap(true);
        txtDescription.setRows(5);
        jScrollPane1.setViewportView(txtDescription);

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel4)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel5))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(txtName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 207, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(txtName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel5)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Percentages of generated values"));
        txtFrom.setNextFocusableComponent(txtTo);

        txtTo.setNextFocusableComponent(txtPercent);

        txtPercent.setNextFocusableComponent(btnAdd);

        btnAdd.setIcon(new javax.swing.ImageIcon("C:\\javaprojects\\GenGUI\\images\\list-add-small.png"));
        btnAdd.setText("Add range");
        btnAdd.setNextFocusableComponent(tblRanges);
        btnAdd.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAddActionPerformed(evt);
            }
        });

        jScrollPane2.setNextFocusableComponent(btnRemove);
        tblRanges.setNextFocusableComponent(btnRemove);
        jScrollPane2.setViewportView(tblRanges);

        btnRemove.setIcon(new javax.swing.ImageIcon("C:\\javaprojects\\GenGUI\\images\\list-remove-small.png"));
        btnRemove.setText("Remove Selected");
        btnRemove.setNextFocusableComponent(spinNull);
        btnRemove.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnRemoveActionPerformed(evt);
            }
        });

        jLabel7.setText("Percentage:");

        jLabel1.setText("From:");

        jLabel3.setText("To:");

        jLabel6.setText("Null:");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(btnRemove, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 142, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jLabel6)
                            .add(jLabel1)
                            .add(jLabel3)
                            .add(jLabel7))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(btnAdd, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, txtFrom)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, txtTo)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, txtPercent)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, spinNull, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel1)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(txtFrom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                    .add(txtTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(jLabel3))))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel7)
                            .add(txtPercent, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnAdd))
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 103, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel6)
                    .add(spinNull, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnRemove))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnRemoveActionPerformed
    {//GEN-HEADEREND:event_btnRemoveActionPerformed
        int row = tblRanges.getSelectedRow();
        if(row==-1)
            return;
        newModel.removeRow(row);
    }//GEN-LAST:event_btnRemoveActionPerformed
    
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAddActionPerformed
    {//GEN-HEADEREND:event_btnAddActionPerformed
        String sFrom, sTo, sPercent;
        Integer iValues[] = new Integer[3];
        int    error=0;
        
        
        sFrom   = txtFrom.getText();
        sTo     = txtTo.getText();
        sPercent= txtPercent.getText();
        
        try
        {iValues[0] = new Integer(sFrom);}
        catch(Exception e)
        {error=1;}
        
        try
        {iValues[1] = new Integer(sTo);}
        catch(Exception e)
        {error=2;}
        
        try
        {iValues[2] = new Integer(sPercent);}
        catch(Exception e)
        {error=3;}
        
        if(iValues[0].intValue()>iValues[1].intValue())
        {
            JOptionPane.showMessageDialog(this,"From value should be lower than to value.","Invalid field",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if(error>0)
        {
            JOptionPane.showMessageDialog(this,"Values for the fields From, To, Percentage should be numeric.","Invalid field",JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(iValues[2].intValue()<=0)
        {
            JOptionPane.showMessageDialog(this,"Percentage field should be a positive integer.","Invalid field",JOptionPane.ERROR_MESSAGE);
            return;
        }

        newModel.addRow(iValues);
    }//GEN-LAST:event_btnAddActionPerformed
    
    public void initialise(RandomiserInstance ri)
    {
        HashMap<String,String> hashmap;
        String sMax, sIntValue, sNull;
        int    iMax;
        Integer intValues[] = new Integer[3];
        
        txtName.setText(ri.getName());
        txtDescription.setText(ri.getDescription());
        
        hashmap = ri.getProperties();
        sMax = (String)hashmap.get("rangesNum");
        try
        {
            iMax = Integer.parseInt(sMax);
            for(int i=0; i<iMax; i++)
            {
                sIntValue =(String)hashmap.get("fromField"+i);
                intValues[0] = new Integer(sIntValue);
                sIntValue =(String)hashmap.get("toField"+i);
                intValues[1] = new Integer(sIntValue);
                sIntValue =(String)hashmap.get("percentField"+i);
                intValues[2] = new Integer(sIntValue);
                
                newModel.addRow(intValues);
            }
            sNull  = (String) hashmap.get("nullField");
            spinNull.setValue(Integer.parseInt(sNull));
        }
        catch(Exception e)
        {
            logger.warn("Error while setting properties:",e);
        }
    }
    
    public RandomiserInstance getRandomiserInstance()
    {
        RandomiserInstance ri = new RandomiserInstance();
        LinkedHashMap<String,String> hashmap = new LinkedHashMap<String,String>();
        Integer intValues[] = new Integer[3];
        int rowCount,error;
        String tempData;
        
        ri.setName(txtName.getText());
        ri.setDescription(txtDescription.getText());
        ri.setRandomiserType("NumIntegerRandomiser");
        
        error=0;
        rowCount = tblRanges.getRowCount();
        hashmap.put("rangesNum",""+rowCount);
        for(int i = 0; i<rowCount; i++)
        {
            try
            {
                for(int k=0; k<3; k++)
                {
                    try
                    {
                        intValues[k] = (Integer) newModel.getValueAt(i,k);
                    }
                    catch(ClassCastException e)
                    {
                        tempData = (String) newModel.getValueAt(i,k);
                        intValues[k] = new Integer(tempData);                       
                    }                    
                }
                hashmap.put("fromField"+i,""+ intValues[0].intValue() );
                hashmap.put("toField"+i,""+ intValues[1].intValue() );
                hashmap.put("percentField"+i,""+ intValues[2].intValue() );
            }
            catch(Exception e)
            {
                logger.warn("Problem retrieving table values",e);
                error = i;
            }
        }
        if(error>0)
        {
            JOptionPane.showMessageDialog(this,"Values for the fields From, To, Percentage should be numeric.","Invalid field",JOptionPane.ERROR_MESSAGE);
            return null;
        }
     
        hashmap.put("nullField",""+spinNull.getValue());
        ri.setProperties(hashmap);
        return ri;
    }
    
    public boolean isFormValid()
    {
        String  name;
        Integer intValues[] = new Integer[3];
        int     percent;
        String  tempData;
        
        //get field values
        name = txtName.getText().trim();
        
        //run checks, empty strings, no data in the table
        if(name.length()==0)
        {
            JOptionPane.showMessageDialog(this,"Please provide a value for the name.","Required field",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(newModel.getRowCount()==0)
        {
            JOptionPane.showMessageDialog(this,"Please add at least one range in the ranges table.","Required field",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        //retrieve the values from the table and make sure they are numbers
        //make sure that percentages add up to 100
        percent   = 0;
        for(int i = 0; i<newModel.getRowCount(); i++)
        {
            try
            {
                for(int k=0; k<3; k++)
                {
                    try
                    {
                        intValues[k] = (Integer) newModel.getValueAt(i,k);
                    }
                    catch(ClassCastException e)
                    {
                        tempData = (String) newModel.getValueAt(i,k);
                        intValues[k] = new Integer(tempData);                       
                    }
                }
                percent+=intValues[2];
            }
            catch(Exception e)
            {
                logger.warn("Error retrieving data from table",e);
                JOptionPane.showMessageDialog(this,"Values in the table should be numerical.","Required numerical data",JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        if(percent!=100)
        {
            JOptionPane.showMessageDialog(this,"Percentages in the table should add up to 100.","Invalid data",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private DefaultTableModel newModel = new DefaultTableModel();
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnRemove;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner spinNull;
    private javax.swing.JTable tblRanges;
    private javax.swing.JTextArea txtDescription;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPercent;
    private javax.swing.JTextField txtTo;
    // End of variables declaration//GEN-END:variables
    
}
