/*
 * dgMaster: A versatile, open source data generator.
 *(c) 2007 M. Michalakopoulos, mmichalak@gmail.com
 */

package generator.panels;
import generator.extenders.RandomiserInstance;
import generator.extenders.RandomiserPanel;
import java.util.LinkedHashMap;
import javax.swing.JOptionPane;


/**
 * A simple names generator that generates data of the form:
 *  [Title] [Firstname|Initial] [MiddleInitial] [Lastname]
 *
 * At least one of the fields is required.
 */
public class PanelFullnameRandomiser extends RandomiserPanel
{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -5656348532743908605L;
	/** Creates new form PanelFullname */
    public PanelFullnameRandomiser()
    {
        initComponents();
        radFirstInitialOnly.setSelected(true);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescription = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        chkTitle = new javax.swing.JCheckBox();
        chkFirstname = new javax.swing.JCheckBox();
        radFirstInitialOnly = new javax.swing.JRadioButton();
        radFirstFull = new javax.swing.JRadioButton();
        chkMiddle = new javax.swing.JCheckBox();
        chkLast = new javax.swing.JCheckBox();
        spinNull = new javax.swing.JSpinner();

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
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE))
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
        jLabel3.setText("Null values percentage:");

        jLabel1.setText("Include the following info:");

        chkTitle.setText("Title (Mr, Mrs, Ms, Miss, Dr)");
        chkTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkTitle.setMargin(new java.awt.Insets(0, 0, 0, 0));

        chkFirstname.setText("First name:");
        chkFirstname.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkFirstname.setMargin(new java.awt.Insets(0, 0, 0, 0));
        chkFirstname.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkFirstnameActionPerformed(evt);
            }
        });

        buttonGroup1.add(radFirstInitialOnly);
        radFirstInitialOnly.setText("Initial only");
        radFirstInitialOnly.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        radFirstInitialOnly.setMargin(new java.awt.Insets(0, 0, 0, 0));

        buttonGroup1.add(radFirstFull);
        radFirstFull.setText("In full");
        radFirstFull.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        radFirstFull.setMargin(new java.awt.Insets(0, 0, 0, 0));
        radFirstFull.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                radFirstFullActionPerformed(evt);
            }
        });

        chkMiddle.setText("Middle initial");
        chkMiddle.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkMiddle.setMargin(new java.awt.Insets(0, 0, 0, 0));

        chkLast.setText("Last name");
        chkLast.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkLast.setMargin(new java.awt.Insets(0, 0, 0, 0));

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(chkLast)
                            .add(chkTitle)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(chkFirstname)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(radFirstInitialOnly)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(radFirstFull))
                            .add(chkMiddle)))
                    .add(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jLabel3)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(spinNull, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(243, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(chkTitle)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(chkFirstname)
                            .add(radFirstInitialOnly)
                            .add(radFirstFull))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(chkMiddle)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(chkLast, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                .add(22, 22, 22)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(spinNull, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel3))
                .addContainerGap())
        );
        jPanel1.getAccessibleContext().setAccessibleName("Generated values");

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
    
    private void chkFirstnameActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkFirstnameActionPerformed
    {//GEN-HEADEREND:event_chkFirstnameActionPerformed
        boolean selected = chkFirstname.isSelected();
        
        //if the user does not want a firstname, then these options are not relevant
        //probably, the radio buttons should be hidden rather than disabled in such a case
        if(selected)
        {
            radFirstFull.setEnabled(true);
            radFirstInitialOnly.setEnabled(true);
        }
        else
        {
            radFirstFull.setEnabled(false);
            radFirstInitialOnly.setEnabled(false);
        }
    }//GEN-LAST:event_chkFirstnameActionPerformed
    
    private void radFirstFullActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_radFirstFullActionPerformed
    {//GEN-HEADEREND:event_radFirstFullActionPerformed
        
    }//GEN-LAST:event_radFirstFullActionPerformed
    
    
    /*
     * Performs a validation check prior to saving the data. Each user-defined
     * panel may have an arbitrary number of information on it that the developer
     * would like to validate before saving. This is the method that handles this
     * validation.
     *
     * This method is automatically called by dgMaster, when the user
     * clicks the Save button.
     */
    public boolean isFormValid()
    {
        String name;
        Integer nullField;
        boolean lTitle, lFirst, lMiddle, lLastname;
        boolean result;
        
        //get name value, no need to perform check on description, that can be empty.
        name = txtName.getText().trim();
        nullField = (Integer) spinNull.getModel().getValue();
        
        //run checks
        if(name.length()==0)
        {
            JOptionPane.showMessageDialog(this,"Please provide a value for the name.","Required field",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        //since we have checkfields, we would ideally like the user to select something.
        //if the user selects none of the checkfields, this constitutes an invalid form,
        //the generator should actually generate something! We can let the generator handle this
        //but we would like to have valid data on the form as well.
        lTitle = chkTitle.isSelected();
        lFirst = chkFirstname.isSelected();
        lMiddle = chkMiddle.isSelected();
        lLastname = chkLast.isSelected();
        
        //now perform the check, we want at least one checkbox selected
        result = lTitle || lFirst || lMiddle || lLastname;
        if(result==false)
        {
            JOptionPane.showMessageDialog(this,"Please tick at least one of the check boxes.","Required field",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if( nullField<0 || nullField>100 )
        {
            JOptionPane.showMessageDialog(this,"Please provide a value in the range [0..100] for the Null field.","Invalid field",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    /*
     * Gathers the data from the form and creates a RandomiserInstance object,
     * which is returned to the dgMaster application, (so as to be saved in xml format).
     * This method will only be called if isFormValid() returns true.
     */
    public RandomiserInstance getRandomiserInstance()
    {
        String name, description;
        boolean lTitle, lFirstName, lFirstNameFull, lFirstInitial, lMiddle, lLastname;
        Integer nullField;
        LinkedHashMap<String,String> hashMap = new LinkedHashMap<String,String>();
        
        //this is what will be returned
        RandomiserInstance ri = new RandomiserInstance();
        
        //get field values
        name = txtName.getText().trim();
        description = txtDescription.getText().trim();
        nullField = (Integer) spinNull.getModel().getValue();
        
        //get the boolean variables
        lTitle = chkTitle.isSelected();
        lFirstName = chkFirstname.isSelected();
        lFirstNameFull = radFirstFull.isSelected();
        lFirstInitial = radFirstInitialOnly.isSelected();
        lMiddle = chkMiddle.isSelected();
        lLastname = chkLast.isSelected();
        
        //create the hashmap, this will be converted to xml by dgMaster
        hashMap.put("includeTitle",""+lTitle);
        hashMap.put("includeFirstName",""+lFirstName);
        hashMap.put("firstNameFull",""+lFirstNameFull);
        hashMap.put("firstNameInitial",""+lFirstInitial);
        hashMap.put("includeInitialMiddle",""+lMiddle);
        hashMap.put("includeLastName",""+lLastname);
        hashMap.put("nullField",nullField.toString());
        
        ri.setRandomiserType("FullnameRandomiser");
        ri.setName(name);
        ri.setDescription(description);
        ri.setProperties(hashMap);
        
        return ri;
    }
    
    
    /*
     * Populates the panel with existing information. This is the xml information
     * as returned by the RandomserInstance in method getRandomiserInstance()
     */    
    public void initialise(RandomiserInstance ri)
    {
        //get the values from the RandomiserInstance
        //and put them on the form        
        txtName.setText( ri.getName() );
        txtDescription.setText( ri.getDescription() );
        
        String sNull;
        boolean lTitle, lFirstName, lFirstNameFull, lFirstInitial, lMiddle, lLastname;
        String  sTitle, sFirstName, sFirstNameFull, sFirstInitial, sMiddle, sLastname;
        LinkedHashMap<String,String> hashMap;
        
        //retrieve the hashmap values, these are always Strings
        hashMap = ri.getProperties();
        sTitle  = (String) hashMap.get("includeTitle");
        sFirstName    = (String)hashMap.get("includeFirstName");
        sFirstNameFull= (String)hashMap.get("firstNameFull");
        sFirstInitial = (String)hashMap.get("firstNameInitial");
        sMiddle       = (String)hashMap.get("includeInitialMiddle");
        sLastname     = (String)hashMap.get("includeLastName");
        
        //convert retrieved Strings to boolean
        lTitle        = Boolean.valueOf(sTitle);
        lFirstName    = Boolean.valueOf(sFirstName);
        lFirstNameFull= Boolean.valueOf(sFirstNameFull);
        lFirstInitial = Boolean.valueOf(sFirstInitial);
        lMiddle       = Boolean.valueOf(sMiddle);
        lLastname     = Boolean.valueOf(sLastname);
        
        //... and set the values of the check and radio boxes
        chkTitle.setSelected(lTitle);
        
        chkFirstname.setSelected(lFirstName);
        radFirstInitialOnly.setSelected(lFirstInitial);
        radFirstFull.setSelected(lFirstNameFull);
        
        chkMiddle.setSelected(lMiddle);
        chkLast.setSelected(lLastname);
        
        //...and the null value spinner
        sNull  = (String) hashMap.get("nullField");
        spinNull.setValue(Integer.parseInt(sNull));
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox chkFirstname;
    private javax.swing.JCheckBox chkLast;
    private javax.swing.JCheckBox chkMiddle;
    private javax.swing.JCheckBox chkTitle;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton radFirstFull;
    private javax.swing.JRadioButton radFirstInitialOnly;
    private javax.swing.JSpinner spinNull;
    private javax.swing.JTextArea txtDescription;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables
    
}
