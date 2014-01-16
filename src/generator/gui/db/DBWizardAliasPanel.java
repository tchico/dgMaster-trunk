/*
 * DBWizardAliasPanel.java
 *
 * Created on 25 May 2007, 02:49
 */

package generator.gui.db;


import generator.db.DBGeneratorDefinition;
import generator.db.DBMetaDataManager;
import generator.db.DBTable;
import generator.db.DBTableGenerator;
import generator.db.Err;
import generator.db.ResultsData;
import generator.gui.ErrorMsgPanel;
import generator.gui.SwingWorker;
import generator.misc.DBDriverInfo;
import generator.misc.Utils;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

/**
 *
 * @author  Michael
 */
public class DBWizardAliasPanel extends javax.swing.JPanel implements IWizardStep
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Vector<DBDriverInfo> vDBInfo;
    
    private DefaultComboBoxModel modelDBData;
    private DefaultComboBoxModel modelDB;
    private DefaultComboBoxModel modelSchema;
    private TableTBModel    modelTB;
    
    private Logger logger = Logger.getLogger(DBWizardAliasPanel.class);
    private DBMetaDataManager dbMeta;
    
//    private HashMap hmValues;
    
    private DBGeneratorDefinition dbGeneratorDefinition;
    
    /** Creates new form DBWizardAliasPanel */
    public DBWizardAliasPanel()
    {
        initComponents();
        
        modelDBData = new DefaultComboBoxModel();
        cmbDriver.setModel(modelDBData);
        cmbDriver.setRenderer( new DBListDriverInfoRenderer() );
        
        modelDB = new DefaultComboBoxModel();
        cmbDB.setModel(modelDB);
        cmbDB.setRenderer( new DBListDBRenderer() );
        
        modelSchema = new DefaultComboBoxModel();
        cmbSchemas.setModel(modelSchema);
        cmbSchemas.setRenderer( new DBListDBRenderer() );
        
        modelTB = new TableTBModel();
        tblTables.setModel(modelTB);
        
        TableColumn col = tblTables.getColumnModel().getColumn(0);
        col.setMaxWidth(40);
        dbMeta = new DBMetaDataManager();
        
//        hmValues = new HashMap();
    }
    
    public DBWizardAliasPanel(Vector<DBDriverInfo> vDBInfo)
    {
        this();
        this.vDBInfo = vDBInfo;
        
        for(int i = 0; i < this.vDBInfo.size(); i++)
            modelDBData.addElement(this.vDBInfo.elementAt(i));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbDriver = new javax.swing.JComboBox();
        txtURL = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtUser = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        btnDBConnect = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        cmbDB = new javax.swing.JComboBox();
        cmbSchemas = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblTables = new javax.swing.JTable();
        btnSelectAll = new javax.swing.JButton();
        btnInverse = new javax.swing.JButton();
        btnUnselect = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();

        jList3.setModel(new javax.swing.AbstractListModel() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(jList3);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Database Alias"));

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Database Driver:");

        cmbDriver.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbDriver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbDriverActionPerformed(evt);
            }
        });

        jLabel2.setText("URL:");

        jLabel3.setText("Username:");

        jLabel5.setText("Password:");

        btnDBConnect.setText("Connect");
        btnDBConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDBConnectActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel1)
                    .add(jLabel2)
                    .add(jLabel3)
                    .add(jLabel5))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(btnDBConnect)
                    .add(txtURL, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 319, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, txtPassword)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, txtUser, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 102, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(cmbDriver, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(cmbDriver, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(txtURL, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(txtUser, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5)
                    .add(txtPassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(14, 14, 14)
                .add(btnDBConnect)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cmbDB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbDB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbDBActionPerformed(evt);
            }
        });

        cmbSchemas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbSchemas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSchemasActionPerformed(evt);
            }
        });

        tblTables.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tblTables.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblTables.setShowHorizontalLines(false);
        tblTables.setShowVerticalLines(false);
        tblTables.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTablesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblTables);

        btnSelectAll.setText("Select all");
        btnSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectAllActionPerformed(evt);
            }
        });

        btnInverse.setText("Inverse");
        btnInverse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInverseActionPerformed(evt);
            }
        });

        btnUnselect.setText("Unselect all");
        btnUnselect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnselectActionPerformed(evt);
            }
        });

        jLabel6.setText("Database:");

        jLabel7.setText("Schemas:");

        jLabel8.setText("Tables:");

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jLabel7)
                            .add(jLabel6))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(cmbSchemas, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(cmbDB, 0, 261, Short.MAX_VALUE)))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jLabel8)
                        .add(18, 18, 18)
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel3Layout.createSequentialGroup()
                                .add(btnSelectAll)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(btnInverse)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(btnUnselect))
                            .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE))))
                .addContainerGap(56, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel6)
                    .add(cmbDB, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel7)
                    .add(cmbSchemas, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel8)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 239, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnUnselect)
                    .add(btnInverse)
                    .add(btnSelectAll))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel9.setText("...NOTE: Generating database data is feasible, but not finished yet, see dgmaster website...");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel9)
                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel9))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents
            
    
    private void btnInverseActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnInverseActionPerformed
    {//GEN-HEADEREND:event_btnInverseActionPerformed
        Boolean selected;
        String tableName;
        for(int i=0; i<modelTB.getRowCount(); i++)
        {
            selected  = (Boolean)modelTB.getValueAt(i,0);
            tableName = (String)modelTB.getValueAt(i,1);
            if(tableName.length()>0)
                modelTB.setValueAt(new Boolean(!selected),i,0 );
        }
    }//GEN-LAST:event_btnInverseActionPerformed
    
    private void btnUnselectActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnUnselectActionPerformed
    {//GEN-HEADEREND:event_btnUnselectActionPerformed
        String tableName;
        for(int i=0; i<modelTB.getRowCount(); i++)
        {
            tableName = (String)modelTB.getValueAt(i,1);
            if(tableName.length()>0)
                modelTB.setValueAt(new Boolean(false),i,0 );
        }
    }//GEN-LAST:event_btnUnselectActionPerformed
    
    private void btnSelectAllActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSelectAllActionPerformed
    {//GEN-HEADEREND:event_btnSelectAllActionPerformed
        String tableName;
        for(int i=0; i<modelTB.getRowCount(); i++)
        {
            tableName = (String)modelTB.getValueAt(i,1);
            if(tableName.length()>0)
                modelTB.setValueAt(new Boolean(true),i,0 );
        }
    }//GEN-LAST:event_btnSelectAllActionPerformed
    
    private void tblTablesMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblTablesMouseClicked
    {//GEN-HEADEREND:event_tblTablesMouseClicked
                                    }//GEN-LAST:event_tblTablesMouseClicked
    
    private void cmbSchemasActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbSchemasActionPerformed
    {//GEN-HEADEREND:event_cmbSchemasActionPerformed
        String schema = (String) modelSchema.getSelectedItem();
        String db     = (String) modelDB.getSelectedItem();
        Object obj[]  = new Object[2];
        ArrayList<Object[]> alData = new ArrayList<Object[]>();
        
        modelTB.removeAllItems();
        
        //retrieve the tables for this db and schema
        ResultsData<String> rsdTable = dbMeta.getTables(db, schema);
        List<String> alTable;
        alTable = rsdTable.getData();
        
        //if the above fails, try to retrieve tables for the specific db only
        if(rsdTable.size()==0)
            rsdTable = dbMeta.getTables(1,db);

        //if that one fails as well, try to retrieve tables for the specific schema only
        if(rsdTable.size()==0)
            rsdTable = dbMeta.getTables(2,schema);

        //if that one fails, do a vendor specific search
        if(rsdTable.size()==0)
            rsdTable = dbMeta.getTablesByVendor();
        
        alTable = rsdTable.getData();
        if(alTable.size()==0)
            return;
        
        for (String elem : alTable)
        {
            obj = new Object[2];
            obj[0] = Boolean.valueOf(false);
            obj[1] = elem;
            alData.add(obj);
        }
        modelTB.setData(alData);             
    }//GEN-LAST:event_cmbSchemasActionPerformed
    
    
    private void cmbDBActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbDBActionPerformed
    {//GEN-HEADEREND:event_cmbDBActionPerformed
        String db = (String) modelDB.getSelectedItem();
        if(db==null)
            return;
        
        ResultsData<String> rsdSchema;
        List<String> alSchema;
        
        // for the selected database retrieve its schemas....
        rsdSchema = dbMeta.getSchemas(db);
        
        //it might be the case that there were erors retrieving the schemas for this DB
        //if this is the case, try to retrieve all the schemas for this connection, (bypass the DB)
        if( rsdSchema.getStatus()==Err.QUERY_ERR )
            rsdSchema = dbMeta.getSchemas();
        
        alSchema = rsdSchema.getData();
        modelSchema.removeAllElements();
        for (String elem : alSchema)
            modelSchema.addElement( elem );
        
        //no schemas retrieved, try to retrieve the tables for this database
        if(alSchema.size()==0)
            updateTables(db);        
    }//GEN-LAST:event_cmbDBActionPerformed
    
    
    private void updateTables(String db)
    {
        ResultsData<String> rsdTable;
        List<String> alTable;
        Object obj[] = new Object[2];
        ArrayList<Object[]> alData = new ArrayList<Object[]>();
        
        rsdTable = dbMeta.getTables(1, db);
        alTable = rsdTable.getData();
        
        modelTB.removeAllItems();
        if(alTable.size()==0)
            return;
        
        
        for (String elem : alTable)
        {
            obj = new Object[2];
            obj[0] = Boolean.valueOf(false);
            obj[1] = elem;
            alData.add(obj);
        }
        modelTB.setData(alData);
        
    }
    
    private void btnDBConnectActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDBConnectActionPerformed
    {//GEN-HEADEREND:event_btnDBConnectActionPerformed
        final DBDriverInfo dbDriverInfo = (DBDriverInfo)modelDBData.getSelectedItem();
        if(dbDriverInfo==null)
        {
            JOptionPane.showMessageDialog(this,"Please select a database driver first","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        
        final SwingWorker worker = new SwingWorker()
        {
            public Object construct()
            {
                String location = dbDriverInfo.getLocationClass();
                String dbClass = dbDriverInfo.getLoadClass();
                String url     = txtURL.getText();
                String username= txtUser.getText();
                String password= txtPassword.getText();
                List<String> alDB;
                ResultsData<String> rsdDB;
                
                dbMeta.setConnectionProperties(location, dbClass,url,username,password);
                
                if(dbMeta.isConnected())
                    dbMeta.disconnect();
                try
                {
                    dbMeta.connect();
                }
                catch (Exception ex)
                {
                    String msg = Utils.getErrorMessage(ex);
                    logger.error("Error while connecting to db",ex);
                    showErrorMessage("Error, while connecting to db", msg);
                }
                
                //catalogues are supposed to be the databases
                rsdDB = dbMeta.getCatalogues();
                alDB = rsdDB.getData();
                modelDB.removeAllElements();
                for (String elem : alDB)
                    modelDB.addElement( elem );
                
                //...but are not supported by all vendors, if catalogues are null,
                //...try to retrieve the schemas
                if(modelDB.getSize()==0)
                {
                    rsdDB = dbMeta.getSchemas();
                    alDB = rsdDB.getData();
                    modelSchema.removeAllElements();
                    for (String elem : alDB)
                        modelSchema.addElement( elem );
                }
                return null;
            }
        };
        worker.start();
    }//GEN-LAST:event_btnDBConnectActionPerformed


    private void showErrorMessage(String msg1, String msg2)
    {
        JDialog dlg = new JDialog();
        ErrorMsgPanel pnlNotYet = new ErrorMsgPanel();
        int width, height, posX, posY;

        width  = this.getRootPane().getWidth();
        height = this.getRootPane().getHeight();

        pnlNotYet.setDialog(dlg);
        pnlNotYet.setErrorMsg(msg1, msg2);

        dlg.getContentPane().setLayout( new BorderLayout() );
        dlg.getContentPane().add(pnlNotYet, BorderLayout.CENTER);
        dlg.setLocationRelativeTo(this.getRootPane().getParent());

        dlg.setTitle("Error");
        dlg.pack();


        posX =  getX() + (width-dlg.getWidth())/2;
        posY =  getY() + (height-dlg.getHeight())/3;

        dlg.setLocation(posX,posY);
        dlg.setResizable(true);
        dlg.setModal(true);
        dlg.setVisible(true);
    }


    private void cmbDriverActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbDriverActionPerformed
    {//GEN-HEADEREND:event_cmbDriverActionPerformed
        int sel = cmbDriver.getSelectedIndex();
        DBDriverInfo dbDriverInfo;
        
        if(sel==-1)
            return;
        dbDriverInfo = (DBDriverInfo) modelDBData.getElementAt(sel);
        txtURL.setText(dbDriverInfo.getConString());
    }//GEN-LAST:event_cmbDriverActionPerformed
    
    
    private ArrayList<String> getSelectedTableNames()
    {
        ArrayList<String> alTables = new ArrayList<String>();
        
        String tableName;
        for(int i=0; i<modelTB.getRowCount(); i++)
        {
            tableName = (String)modelTB.getValueAt(i,1);
            if(tableName.length()>0)
                alTables.add(tableName);
        }
        return alTables;
    }
    
    
    private ArrayList<DBTableGenerator> getDBTables()
    {
        ArrayList<String> alTables;
        ArrayList<DBTableGenerator> alDBTables;
        DBTable dbTable;
        String catalog, tableName;
        
        catalog  = (String)modelDB.getSelectedItem();
        alTables = getSelectedTableNames();
        alDBTables = new ArrayList<DBTableGenerator>();
        for(int i=0; i<alTables.size(); i++)
        {
            if (modelTB.getValueAt(i,0).equals(Boolean.TRUE))
            {
                tableName = alTables.get(i);
                dbTable = dbMeta.getTableInfo(catalog, tableName);
                alDBTables.add( new DBTableGenerator(dbTable) );
            }
        }
        return alDBTables;
    }
   
    
    public boolean isWizardStepValid()
    {
        boolean valid = true;
        ArrayList<String> aTables = getSelectedTableNames();
        if(aTables.size()==0)
        {
            logger.debug("Tables not selected!");
            JOptionPane.showMessageDialog(this,"Please select at least one table","Error",JOptionPane.ERROR_MESSAGE);
            valid = false;
        }
        return valid;
    }

    public void setDBGeneratorDefinition(DBGeneratorDefinition dbGeneratorDefinition)
    {
        this.dbGeneratorDefinition = dbGeneratorDefinition;
    }

    public DBGeneratorDefinition getDBGeneratorDefinition()
    {
        DBDriverInfo dbDriverInfo = (DBDriverInfo)modelDBData.getSelectedItem();
        ArrayList<DBTableGenerator> aDBTables = getDBTables();
        
        dbGeneratorDefinition.setDbDriver(dbDriverInfo.getName());
        dbGeneratorDefinition.setDBTableGenerators(aDBTables);
        dbGeneratorDefinition.setUser(txtUser.getText());
        dbGeneratorDefinition.setPassword(txtPassword.getText());
        dbGeneratorDefinition.setDbURL(txtURL.getText());
        
        return dbGeneratorDefinition;
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDBConnect;
    private javax.swing.JButton btnInverse;
    private javax.swing.JButton btnSelectAll;
    private javax.swing.JButton btnUnselect;
    private javax.swing.JComboBox cmbDB;
    private javax.swing.JComboBox cmbDriver;
    private javax.swing.JComboBox cmbSchemas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tblTables;
    private javax.swing.JTextField txtPassword;
    private javax.swing.JTextField txtURL;
    private javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables
    
}



class TableTBModel extends AbstractTableModel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String columnNames[] = {"Selected", "Table"};
    private Object[][] data ={ {new Boolean(false),"" },
    {new Boolean(false),"" },
    {new Boolean(false),"" },
    {new Boolean(false),"" },
    };
    private Object[][] empty;
    private ArrayList<Object[]> alTables;
    private Logger logger;
    
    
    public TableTBModel()
    {
        logger = Logger.getLogger(TableTBModel.class);
        empty  = data;
    }
    
    public ArrayList<Object[]> getUserData()
    {
        return alTables;
    }
    
    public void setData( ArrayList<Object[]> alData )
    {
        this.alTables = alData;
        if(alTables==null || alTables.size()==0)
        {
            data = empty;
            logger.warn("No data to be set in the users table model");
            return;
        }
        
        data = new Object[ alTables.size() ][ 2 ];
        for(int i=0; i<alTables.size(); i++)
        {
            Object[] obj = alTables.get(i);
            data[i][0] = (Boolean) obj[0];
            data[i][1] = (String) obj[1];
        }
    }
    
    public String getColumnName(int col)
    {
        return columnNames[col].toString();
    }
    
    public int getRowCount()
    {
        return data.length;
    }
    
    public int getColumnCount()
    {
        return columnNames.length;
    }
    
    public Object getValueAt(int row, int col)
    {
        return data[row][col];
    }
    
    public boolean isCellEditable(int row, int col)
    {
        if(col==1)
            return false;
        return true;
    }
    
    public void setValueAt(Object value, int row, int col)
    {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }
    
    public Class<? extends Object> getColumnClass(int c)
    {
        return getValueAt(0, c).getClass();
    }
    
    public void removeAllItems()
    {
        data = empty;
        fireTableDataChanged();
    }
    
}
