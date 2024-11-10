/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jframe;

import java.awt.Event;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import static jframe.DBConnection.con;

/**
 *
 * @author Chamod
 */
public class Manage_Students extends javax.swing.JFrame {
private String mail;
private String id;
private File selectedFile;
    /**
     * Creates new form Manage_Books
     */
    public void cleartable()
    {
        DefaultTableModel model=(DefaultTableModel) rSTableMetro2.getModel();
        model.setRowCount(0);
    }
    public Manage_Students() {
        initComponents();
        setBookDetailsToTable();
    }
    String student_id,student_name,course,password,address;
    int contact_number;
    DefaultTableModel model;
    public void setBookDetailsToTable()
    {
        try {
             con = DBConnection.getconnection();
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery("select * from student");
            
            while (rs.next()) {                
                String bookid=rs.getString("StudentID");
                String bookname=rs.getString("Student_Name");
                String author=rs.getString("Course");
                String contact_number=rs.getString("Contact_Number");
               
                
                Object[] obj={bookid,bookname,author,contact_number};
                model =(DefaultTableModel)rSTableMetro2.getModel();
                model.addRow(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public boolean addstudent() {
    boolean isAdded = false;

    student_id = jCTextField3.getText();
    student_name = jCTextField4.getText();
    course = rSComboMetro1.getSelectedItem().toString();
    contact_number = Integer.parseInt(jCTextField8.getText());
    address = jCTextField9.getText();

    try {
        con = DBConnection.getconnection();
        
        // Check if StudentID already exists
        String checkSql = "SELECT COUNT(*) FROM student WHERE StudentID = ?";
        PreparedStatement checkPst = con.prepareStatement(checkSql);
        checkPst.setString(1, student_id);
        ResultSet rs = checkPst.executeQuery();
        

        if (rs.next()) {
            // Show a message if StudentID already exists
            JOptionPane.showMessageDialog(null, "Student ID '" + student_id + "' already exists. Please use a different ID.", "Duplicate ID Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String sql = "INSERT INTO student (StudentID, student_name, course, contact_number, Photo, address) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, student_id);
        pst.setString(2, student_name);
        pst.setString(3, course);
        pst.setInt(4, contact_number);
        pst.setNull(5, java.sql.Types.BLOB);
        pst.setString(6, address);
        
        int rowcount = pst.executeUpdate();
        if (rowcount > 0) {
            isAdded = true;
            setid();
            uploadPhoto();
            JOptionPane.showMessageDialog(null, "Student added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            isAdded = false;
            JOptionPane.showMessageDialog(null, "Failed to add student.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    return isAdded;
}

    public boolean updatestudent()
    {
        boolean isupdated=false;
        student_id=jCTextField3.getText();
        student_name=jCTextField4.getText();
        course=rSComboMetro1.getSelectedItem().toString();
        contact_number=Integer.parseInt(jCTextField8.getText());
        address=jCTextField9.getText();
        try{
            con=DBConnection.getconnection();
            String sql="update student set student_name = ?,course = ?,contact_number = ?,Address=? where StudentID = ?";
            PreparedStatement pst =con.prepareStatement(sql);
            pst.setString(1, student_name);
            pst.setString(2,course);
            pst.setInt(3, contact_number);
            pst.setString(4,address);
            pst.setString(5, student_id);
            
            int rowcount=pst.executeUpdate();
            if(rowcount>0)
            {
              isupdated=true;
              setid();
              uploadPhoto();
            }
            else
            {
                isupdated=false;
            }   
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
         return  isupdated;
    }
      public boolean deletestudent()
    {
        boolean isdeleted=false;
        student_id=jCTextField3.getText();
        
        try{
            con=DBConnection.getconnection();
            String sql="delete from student where StudentID = ?";
            PreparedStatement pst =con.prepareStatement(sql);
            pst.setString(1,student_id);
            
            int rowcount=pst.executeUpdate();
            if(rowcount>0)
            {
              isdeleted=true;
            }
            else
            {
                isdeleted=false;
            }   
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
         return  isdeleted;
    }
      
       public void setid() {
    id=jCTextField3.getText();
    try {
        con = DBConnection.getconnection();
        
        // Check if email already exists
        String checkSql = "SELECT COUNT(*) FROM student WHERE StudentID = ?";
        PreparedStatement checkPst = con.prepareStatement(checkSql);
        checkPst.setString(1, id);
        ResultSet rs = checkPst.executeQuery();
        rs.next();
        int count = rs.getInt(1);

        if (count == 0) {
            // Insert only if email does not exist
            String sql = "INSERT INTO student (Photo) VALUES (?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, id);
            int rowcount = pst.executeUpdate();
            if (rowcount > 0) {
                JOptionPane.showMessageDialog(this, "ID set successfully");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to set ID");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Photo uploaded successfully");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
       
      public void uploadPhoto() {
    id = jCTextField3.getText();

    if (selectedFile == null) {
       
        return;
    }

    try {
        con = DBConnection.getconnection();

        PreparedStatement pstmt = con.prepareStatement("UPDATE student SET Photo = ? WHERE StudentID = ?");
        FileInputStream fis = new FileInputStream(selectedFile);

        pstmt.setBlob(1, fis);
        pstmt.setString(2, id);
        int rowcount = pstmt.executeUpdate();

        if (rowcount > 0) {
            System.out.println("Photo uploaded successfully!");
        } else {
            System.out.println("Failed to upload photo. No matching ID found.");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

       
      public void displayPhoto(String photoPath) {
        try {
            BufferedImage img = ImageIO.read(new File(photoPath));
            Image resizedImage = img.getScaledInstance(130, 150, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(resizedImage);
            jLabel2.setIcon(icon);
            jLabel2.revalidate();
            jLabel2.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
       private void displayPhotoFromDatabase(String studentID) {
    try {
        con = DBConnection.getconnection();
        String sql = "SELECT Photo ,Address FROM student WHERE StudentID = ?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, studentID);
        ResultSet rs = pst.executeQuery();
        

        if (rs.next()) {
            String address=rs.getString(2);
            jCTextField9.setText(address);
            byte[] imgBytes = rs.getBytes("Photo");
            if (imgBytes != null) {
                // Convert the byte array to a BufferedImage
                InputStream in = new ByteArrayInputStream(imgBytes);
                BufferedImage img = ImageIO.read(in);
                
                // Resize and set the image to the JLabel
                Image resizedImage = img.getScaledInstance(130, 150, Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(resizedImage);
                jLabel2.setIcon(icon);
                jLabel2.revalidate();
                jLabel2.repaint();
            } else {
                System.out.println("No image found for the selected student.");
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
       
       
        public void searchs(String str) {
    model = (DefaultTableModel) rSTableMetro2.getModel();
    TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(model);
    rSTableMetro2.setRowSorter(trs);
    trs.setRowFilter(RowFilter.regexFilter("(?i)" + str));
}
        
        

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jCTextField3 = new app.bolivia.swing.JCTextField();
        jCTextField4 = new app.bolivia.swing.JCTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jCTextField8 = new app.bolivia.swing.JCTextField();
        rSComboMetro1 = new rojerusan.RSComboMetro();
        jLabel2 = new javax.swing.JLabel();
        rSMaterialButtonRectangle3 = new rojerusan.RSMaterialButtonRectangle();
        jLabel7 = new javax.swing.JLabel();
        jCTextField9 = new app.bolivia.swing.JCTextField();
        rSMaterialButtonCircle2 = new rojerusan.RSMaterialButtonCircle();
        rSMaterialButtonCircle3 = new rojerusan.RSMaterialButtonCircle();
        rSMaterialButtonCircle1 = new rojerusan.RSMaterialButtonCircle();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        rSTableMetro2 = new rojerusan.RSTableMetro();
        jLabel17 = new javax.swing.JLabel();
        jCTextField1 = new app.bolivia.swing.JCTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(120, 176, 223));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Contact_26px.png"))); // NOI18N
        jLabel3.setText("Enter Student Id");
        jLabel3.setPreferredSize(new java.awt.Dimension(75, 15));

        jCTextField3.setBackground(new java.awt.Color(120, 176, 223));
        jCTextField3.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        jCTextField3.setForeground(new java.awt.Color(255, 255, 255));
        jCTextField3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jCTextField3.setPlaceholder("Enter Student id...");
        jCTextField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jCTextField3FocusLost(evt);
            }
        });
        jCTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCTextField3ActionPerformed(evt);
            }
        });

        jCTextField4.setBackground(new java.awt.Color(120, 176, 223));
        jCTextField4.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        jCTextField4.setForeground(new java.awt.Color(255, 255, 255));
        jCTextField4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jCTextField4.setPlaceholder("Enter Student Name...");
        jCTextField4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jCTextField4FocusLost(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Moleskine_26px.png"))); // NOI18N
        jLabel4.setText("Enter Student Name");
        jLabel4.setPreferredSize(new java.awt.Dimension(75, 15));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Collaborator_Male_26px.png"))); // NOI18N
        jLabel5.setText("Course Name");
        jLabel5.setPreferredSize(new java.awt.Dimension(75, 15));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Unit_26px.png"))); // NOI18N
        jLabel6.setText("Contact Number");
        jLabel6.setPreferredSize(new java.awt.Dimension(75, 15));

        jCTextField8.setBackground(new java.awt.Color(120, 176, 223));
        jCTextField8.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        jCTextField8.setForeground(new java.awt.Color(255, 255, 255));
        jCTextField8.setToolTipText("");
        jCTextField8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jCTextField8.setPlaceholder("Course Name...");
        jCTextField8.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jCTextField8FocusLost(evt);
            }
        });

        rSComboMetro1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "IT", "ENG", "ACC", "MNG", "THM", "EN" }));
        rSComboMetro1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 205, 82), 5));

        rSMaterialButtonRectangle3.setText("UPLOAD");
        rSMaterialButtonRectangle3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonRectangle3ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Unit_26px.png"))); // NOI18N
        jLabel7.setText("Address");
        jLabel7.setPreferredSize(new java.awt.Dimension(75, 15));

        jCTextField9.setBackground(new java.awt.Color(120, 176, 223));
        jCTextField9.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        jCTextField9.setForeground(new java.awt.Color(255, 255, 255));
        jCTextField9.setToolTipText("");
        jCTextField9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jCTextField9.setPlaceholder("Address...");

        rSMaterialButtonCircle2.setText("ADD");
        rSMaterialButtonCircle2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonCircle2ActionPerformed(evt);
            }
        });

        rSMaterialButtonCircle3.setText("UPDATE");
        rSMaterialButtonCircle3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonCircle3ActionPerformed(evt);
            }
        });

        rSMaterialButtonCircle1.setText("DELETE");
        rSMaterialButtonCircle1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonCircle1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jCTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rSComboMetro1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(rSMaterialButtonCircle2, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(92, 92, 92)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(rSMaterialButtonRectangle3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(117, 117, 117))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(rSMaterialButtonCircle3, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(93, 93, 93))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jCTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rSMaterialButtonCircle1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jCTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(rSMaterialButtonRectangle3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rSComboMetro1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jCTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rSMaterialButtonCircle2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rSMaterialButtonCircle3, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rSMaterialButtonCircle1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(29, Short.MAX_VALUE))))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        rSTableMetro2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        rSTableMetro2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Student_ID", "Name", "Course", "Contact_Number"
            }
        ));
        rSTableMetro2.setIntercellSpacing(new java.awt.Dimension(0, 0));
        rSTableMetro2.setRowHeight(30);
        rSTableMetro2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rSTableMetro2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(rSTableMetro2);

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel17.setText("Student Details");

        jCTextField1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jCTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jCTextField1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jCTextField1.setPlaceholder("Search...");
        jCTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jCTextField1KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel17)
                .addGap(267, 267, 267)
                .addComponent(jCTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jScrollPane2)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        jPanel2.setBackground(new java.awt.Color(120, 176, 223));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Rewind_48px.png"))); // NOI18N
        jLabel1.setText("BACK");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jCTextField3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jCTextField3FocusLost
        // TODO add your handling code here:

    }//GEN-LAST:event_jCTextField3FocusLost

    private void jCTextField4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jCTextField4FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jCTextField4FocusLost

    private void jCTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCTextField3ActionPerformed

    private void rSMaterialButtonCircle1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle1ActionPerformed
        // TODO add your handling code here:
         if(deletestudent()==true)
        {
            JOptionPane.showMessageDialog(this, "Student Deleted");
            cleartable();
            setBookDetailsToTable();
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Student Deletion Failed");
        }
    }//GEN-LAST:event_rSMaterialButtonCircle1ActionPerformed

    private void rSMaterialButtonCircle2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle2ActionPerformed
        // TODO add your handling code here:
        if(addstudent()==true)
        {
            JOptionPane.showMessageDialog(this, "Student Added");
            cleartable();
            setBookDetailsToTable();
            
        }
        else
        {
            JOptionPane.showMessageDialog(this, "student Addition Failed");
        }
    }//GEN-LAST:event_rSMaterialButtonCircle2ActionPerformed

    private void rSMaterialButtonCircle3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle3ActionPerformed
        // TODO add your handling code here:
        if(updatestudent()==true)
        {
            JOptionPane.showMessageDialog(this, "Student Updated");
            cleartable();
             setBookDetailsToTable();
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Book Updation Failed");
        }
    }//GEN-LAST:event_rSMaterialButtonCircle3ActionPerformed

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        // TODO add your handling code here:
        mail=Login_Page1.getInstance().getmail();
        Home_Page1 home =new Home_Page1();
        home.jLabel3.setText(Login_Page1.getInstance().getName());
        home.setVisible(true);
        this.dispose();

    }//GEN-LAST:event_jLabel1MouseClicked

    private void rSTableMetro2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rSTableMetro2MouseClicked
    int viewRow = rSTableMetro2.getSelectedRow();
    
    // Convert the view row index to the corresponding model row index
    int modelRow = rSTableMetro2.convertRowIndexToModel(viewRow);
    
    // Get the table model
    TableModel model = rSTableMetro2.getModel();

    // Set the selected row's data to the appropriate text fields and combo box
    jCTextField3.setText(model.getValueAt(modelRow, 0).toString()); // Assuming this is StudentID
    jCTextField4.setText(model.getValueAt(modelRow, 1).toString()); // Assuming this is another column value
    rSComboMetro1.setSelectedItem(model.getValueAt(modelRow, 2).toString()); // Assuming this is another column value
    jCTextField8.setText(model.getValueAt(modelRow, 3).toString()); // Assuming this is another column value
    
    // Fetch and display the image and address from the database using the StudentID
    String studentID = model.getValueAt(modelRow, 0).toString();
    displayPhotoFromDatabase(studentID);
        
    }//GEN-LAST:event_rSTableMetro2MouseClicked

    private void jCTextField8FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jCTextField8FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jCTextField8FocusLost

    private void rSMaterialButtonRectangle3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonRectangle3ActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(this, "File selected: " + selectedFile.getAbsolutePath());
            displayPhoto(selectedFile.getAbsolutePath());
        }
    }//GEN-LAST:event_rSMaterialButtonRectangle3ActionPerformed

    private void jCTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCTextField1KeyReleased
        // TODO add your handling code here:
        String searchstring=jCTextField1.getText();
        searchs(searchstring);
    }//GEN-LAST:event_jCTextField1KeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Manage_Students.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Manage_Students.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Manage_Students.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Manage_Students.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Manage_Students().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private app.bolivia.swing.JCTextField jCTextField1;
    private app.bolivia.swing.JCTextField jCTextField3;
    private app.bolivia.swing.JCTextField jCTextField4;
    private app.bolivia.swing.JCTextField jCTextField8;
    private app.bolivia.swing.JCTextField jCTextField9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private rojerusan.RSComboMetro rSComboMetro1;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle1;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle2;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle3;
    private rojerusan.RSMaterialButtonRectangle rSMaterialButtonRectangle3;
    private rojerusan.RSTableMetro rSTableMetro2;
    // End of variables declaration//GEN-END:variables
}
