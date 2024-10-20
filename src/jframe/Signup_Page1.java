/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jframe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.Action;
import javax.swing.JOptionPane;
import static jframe.DBConnection.con;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.util.Random;
import javax.swing.Timer;

/**
 *
 * @author Chamod
 */
public class Signup_Page1 extends javax.swing.JFrame {

    private int targetX;
    private int targetX1;
    private int deltaX;
    private int deltaX1;
    private Timer timer;
    private Timer timer1;
    
    /**
     * Creates new form Signup_Page
     */
    
    //access to anyform //////////////////////////////////////////////   
private static Signup_Page1 instance;
private String name1;
private String pass;
private String id;
public static Signup_Page1 getInstance(){
    if(instance==null){
        instance=new  Signup_Page1();
    }
    return instance;
}
    
public  String getname(){
    return name1;
}
public void setname(String name1){
    this.name1=name1;
}

public  String getpass(){
    return pass;
}
public void setpass(String pass){
    this.pass=pass;
}
    
public String getid(){
    return id;
}
public void setid(String id){
    this.id=id;
}

public void com(){
  
    
}

//////////////////////////////////////////////////////////////////
    
    
    public Signup_Page1() {
        initComponents();
        animatePane2();
    }
   private void animatePane2() {
    int initialX = jPanel2.getX();
    targetX = initialX +880;  // Adjust this value to set the distance to move
    deltaX =20;  // Adjust this value to set the speed of the animation

    timer = new Timer(10, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int currentX = jPanel2.getX();
            System.out.println(currentX);
            if (currentX < targetX) {
                jPanel2.setLocation(currentX + deltaX, jPanel2.getY());
            } else {
                timer.stop();
            }
        }
    });
    timer.start();

    int initialX1 = jPanel1.getX();
    targetX1 = initialX1 - 310;  // Adjust this value to set the distance to move
    deltaX1 = 10;  // Adjust this value to set the speed of the animation

    timer1 = new Timer(10, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int currentX1 = jPanel1.getX();
            if (currentX1 > targetX1) {
                jPanel1.setLocation(currentX1 - deltaX1, jPanel1.getY());
            } else {
                timer1.stop();
            }
        }
    });
    timer1.start();
}

    
    
    
    // Method to generate a random verification code
    public int generateVerificationCode() {
        Random secureRandom = new Random();
        return secureRandom.nextInt(999999); // Generates a random 6-digit code
    }
    
    private void sendVerificationEmail(String toEmail, int verificationCode) {
        String host = "smtp.gmail.com"; // SMTP server (change if you're using a different provider)
        int port = 587; // SMTP port (change if needed)
        String username = "channarox573@gmail.com"; // Your email address
        String password = "ooim vnbz vuzy yaka"; // Your email password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.ssl.trust","*");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Email Verification");
            message.setText("Your verification code is: " + verificationCode);

            Transport.send(message);

            System.out.println("Verification email sent.");

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    
    
    
    
    public void insertSignupdetails(){
     
    String id_number=jCTextField2.getText();
    String name=jCTextField1.getText();
    String contact_number=jCTextField3.getText();
    String loginid=jCTextField4.getText();
    String password=jCTextField5.getText();
   int verify = generateVerificationCode();
    
        try {
            con = DBConnection.getconnection();
            String adminsql="INSERT INTO admin(ID_Number,Name,Contact) VALUES(?,?,?)";
            PreparedStatement adminpst=con.prepareStatement(adminsql);
            
            adminpst.setString(1, id_number);
            adminpst.setString(2, name);
            adminpst.setString(3, contact_number);
            
            String loginsql="INSERT INTO signin(LoginID,ID_Number,Verify_Code) VALUES(?,?,?)";
            PreparedStatement loginpst=con.prepareStatement(loginsql);
            
            loginpst.setString(1, loginid);
            loginpst.setString(2, id_number);
            loginpst.setInt(3, verify);
            
            String login="INSERT INTO login(ID_Number) VALUES(?)";
            PreparedStatement login1=con.prepareStatement(login);
            
          
            login1.setString(1, id_number);
           
            
           int updateRowCounta = adminpst.executeUpdate();
           int updateRowCountl = loginpst.executeUpdate();
           int updateRowCount2=login1.executeUpdate();
           if (updateRowCounta > 0 && updateRowCountl > 0 && updateRowCount2 >0) {
             sendVerificationEmail(loginid, verify);
             JOptionPane.showMessageDialog(this, "Record Inserted Successfully. Check your email for verification.");
           
       
            setname(loginid);
            setpass(password);
            setid(id_number);
            verification_code vc= new verification_code(getname(), getpass(), getid());
            vc.setVisible(true);
           
        
    
        } else {
            JOptionPane.showMessageDialog(this, "Record Insertion Failure");
        }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }
    public boolean validateSignup(){
    String username=jCTextField1.getText();
    String id_number=jCTextField2.getText();
    String contact_number=jCTextField3.getText();
    String email=jCTextField4.getText();
    String password=jCTextField5.getText();
    
    if(username.equals(""))
    {
        JOptionPane.showMessageDialog(this, "Please Enter username");
        return false;
    }
     if(id_number.equals(""))
    {
        JOptionPane.showMessageDialog(this, "Please Enter Id Number");
        return false;
    }
      if(contact_number.equals(""))
    {
        JOptionPane.showMessageDialog(this, "Please Enter Contact Number");
        return false;
    }
       if(email.equals(""))
    {
        JOptionPane.showMessageDialog(this, "Please Enter Email");
        return false;
    }
        if(password.equals(""))
    {
        JOptionPane.showMessageDialog(this, "Please Enter Password");
        return false;
    }
        return true;
    
    }
    public  boolean checkduplicate()
    {
        String username=jCTextField4.getText();
        boolean isExist=false;
        
        try {
             con = DBConnection.getconnection();
            
            PreparedStatement pst=con.prepareStatement("select * from signin where LoginID=?");
            pst.setString(1, username);
            ResultSet rs=pst.executeQuery();
            if(rs.next())
            {
                isExist=true;
            }
            
            }
         catch (Exception e) 
        {
             e.printStackTrace();
        }
        return isExist;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rSMaterialButtonCircleBeanInfo1 = new rojerusan.RSMaterialButtonCircleBeanInfo();
        rSMaterialButtonCircleBeanInfo2 = new rojerusan.RSMaterialButtonCircleBeanInfo();
        rSMaterialButtonCircleBeanInfo3 = new rojerusan.RSMaterialButtonCircleBeanInfo();
        rSMaterialButtonCircleBeanInfo4 = new rojerusan.RSMaterialButtonCircleBeanInfo();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        rSMaterialButtonCircle1 = new rojerusan.RSMaterialButtonCircle();
        rSMaterialButtonCircle2 = new rojerusan.RSMaterialButtonCircle();
        jCTextField1 = new app.bolivia.swing.JCTextField();
        jCTextField2 = new app.bolivia.swing.JCTextField();
        jCTextField3 = new app.bolivia.swing.JCTextField();
        jCTextField4 = new app.bolivia.swing.JCTextField();
        jCTextField5 = new app.bolivia.swing.JCTextField();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1200, 720));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 720));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jframe/Untitled-1.jpg"))); // NOI18N
        jLabel1.setText("jLabel1");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 800, 580));

        jLabel4.setFont(new java.awt.Font("Adobe Caslon Pro Bold", 0, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 189, 52));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Welcome to SLIATE Library");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 30, 800, -1));

        jPanel2.setBackground(new java.awt.Color(120, 176, 223));
        jPanel2.setPreferredSize(new java.awt.Dimension(309, 720));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("SIGNUP PAGE");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Name");
        jLabel3.setPreferredSize(new java.awt.Dimension(75, 15));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("ID Number");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Contact Number");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("E-Mail");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Password");

        rSMaterialButtonCircle1.setText("SIGNIN");
        rSMaterialButtonCircle1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rSMaterialButtonCircle1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonCircle1ActionPerformed(evt);
            }
        });

        rSMaterialButtonCircle2.setText("LOGIN");
        rSMaterialButtonCircle2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonCircle2ActionPerformed(evt);
            }
        });

        jCTextField1.setBackground(new java.awt.Color(120, 176, 223));
        jCTextField1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        jCTextField1.setForeground(new java.awt.Color(255, 255, 255));
        jCTextField1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jCTextField1.setPlaceholder("Enter Your Name here...");
        jCTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jCTextField1FocusLost(evt);
            }
        });

        jCTextField2.setBackground(new java.awt.Color(120, 176, 223));
        jCTextField2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        jCTextField2.setForeground(new java.awt.Color(255, 255, 255));
        jCTextField2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jCTextField2.setPlaceholder("Enter Your ID here...");

        jCTextField3.setBackground(new java.awt.Color(120, 176, 223));
        jCTextField3.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        jCTextField3.setForeground(new java.awt.Color(255, 255, 255));
        jCTextField3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jCTextField3.setPlaceholder("Enter Your Contact Number here...");
        jCTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCTextField3ActionPerformed(evt);
            }
        });

        jCTextField4.setBackground(new java.awt.Color(120, 176, 223));
        jCTextField4.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        jCTextField4.setForeground(new java.awt.Color(255, 255, 255));
        jCTextField4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jCTextField4.setPlaceholder("Enter Your E-Mail here...");

        jCTextField5.setBackground(new java.awt.Color(120, 176, 223));
        jCTextField5.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        jCTextField5.setForeground(new java.awt.Color(255, 255, 255));
        jCTextField5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jCTextField5.setPlaceholder("Enter Your Password here...");
        jCTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCTextField5ActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Create New Account Here");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(rSMaterialButtonCircle2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rSMaterialButtonCircle1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jCTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jCTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jCTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jCTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jCTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addGap(32, 32, 32)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addComponent(rSMaterialButtonCircle1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(rSMaterialButtonCircle2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(83, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 893, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jCTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCTextField3ActionPerformed

    private void jCTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCTextField5ActionPerformed

    private void rSMaterialButtonCircle1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle1ActionPerformed
        // TODO add your handling code here:
        if(validateSignup()==true)
        {
            if(checkduplicate()==false)
            {
          insertSignupdetails(); 
          Login_Page1 login=new Login_Page1();
          login.setVisible(true);
          this.dispose();
            }
            else{
                JOptionPane.showMessageDialog(this, "Email already exsits");
            }
        }
        
        
    }//GEN-LAST:event_rSMaterialButtonCircle1ActionPerformed

    private void jCTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jCTextField1FocusLost
        // TODO add your handling code here:
        if(checkduplicate()==true)
        {
            JOptionPane.showMessageDialog(this, "username alredy exsits");
        }
    }//GEN-LAST:event_jCTextField1FocusLost

    private void rSMaterialButtonCircle2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle2ActionPerformed
        // TODO add your handling code here:
        Login_Page1 log =new Login_Page1();
        log.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_rSMaterialButtonCircle2ActionPerformed

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
            java.util.logging.Logger.getLogger(Signup_Page1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Signup_Page1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Signup_Page1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Signup_Page1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Signup_Page1().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private app.bolivia.swing.JCTextField jCTextField1;
    private app.bolivia.swing.JCTextField jCTextField2;
    private app.bolivia.swing.JCTextField jCTextField3;
    private app.bolivia.swing.JCTextField jCTextField4;
    private app.bolivia.swing.JCTextField jCTextField5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle1;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle2;
    private rojerusan.RSMaterialButtonCircleBeanInfo rSMaterialButtonCircleBeanInfo1;
    private rojerusan.RSMaterialButtonCircleBeanInfo rSMaterialButtonCircleBeanInfo2;
    private rojerusan.RSMaterialButtonCircleBeanInfo rSMaterialButtonCircleBeanInfo3;
    private rojerusan.RSMaterialButtonCircleBeanInfo rSMaterialButtonCircleBeanInfo4;
    // End of variables declaration//GEN-END:variables

    private String Random(int length) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
