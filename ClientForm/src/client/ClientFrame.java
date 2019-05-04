package client;

import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Objects;

import static client.MyClient.OtherUserStatus;

public class ClientFrame extends javax.swing.JFrame {

    private static final String ANALYTICAL_IMG_PATH = "ClientForm/images/analytical.jpg";
    private static final String JOY_IMG_PATH = "ClientForm/images/joy.jpg";
    private static final String SADNESS_IMG_PATH = "ClientForm/images/sadness.jpg";
    private static final String FEAR_IMG_PATH = "ClientForm/images/fear.jpg";
    private static final String TENTATIVE_IMG_PATH = "ClientForm/images/fear.jpg";
    private static final String ANGRY_IMG_PATH = "ClientForm/images/angry.jpg";
    private static final String UNKNOWN_JPG_PATH = "ClientForm/images/unknown.jpg";
    private static final String LOADER_GIF_PATH = "ClientForm/images/loader.gif";

    MyClient client;
    static String msgRecieved;

    private String chatHistory = "";

    public ClientFrame() {
        initComponents();
    }

    public String getEncodedStatus() {
        if (StatusComboBox.getSelectedItem() == "Online") {
            return "###1";
        } else if (StatusComboBox.getSelectedItem() == "Busy") {
            return "###2";
        } else if (StatusComboBox.getSelectedItem() == "Away") {
            return "###3";
        } else if (StatusComboBox.getSelectedItem() == "Offline") {
            return "###4";
        } else return "Error";
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        PortNumberLabel = new javax.swing.JLabel();
        UsernameTextField = new javax.swing.JTextField();
        UsernameLabel = new javax.swing.JLabel();
        PortNumTextField = new javax.swing.JTextField();
        AddressLabel = new javax.swing.JLabel();
        AddressTextField = new javax.swing.JTextField();
        DisconnectBtn = new javax.swing.JButton();
        ConnectBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        SendBtn = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        MsgTextArea = new javax.swing.JTextArea();
        UserStatusLabel = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        UsersStatusArea = new javax.swing.JTextArea();
        StatusLabel = new javax.swing.JLabel();
        StatusComboBox = new javax.swing.JComboBox<>();
        jScrollPane4 = new javax.swing.JScrollPane();
        GroupsjList = new javax.swing.JList<>();
        GroupsLabel = new javax.swing.JLabel();
        JoinBtn = new javax.swing.JButton();
        LeaveBtn = new javax.swing.JButton();
        CreateGroupBtn = new javax.swing.JButton();
        ChatComponent = new JTextPane();

        ChatComponent.setEditable(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Praca inz [DEV]");
        setResizable(false);

        PortNumberLabel.setText("Port #");

        UsernameLabel.setText("Username");

        PortNumTextField.setText("9999");

        AddressLabel.setText("Address");

        AddressTextField.setText("localhost");

        DisconnectBtn.setText("Disconnect");
        DisconnectBtn.setEnabled(false);
        DisconnectBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DisconnectBtnActionPerformed(evt);
            }
        });

        ConnectBtn.setText("Connect");
        ConnectBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConnectBtnActionPerformed(evt);
            }
        });
        ChatComponent.setContentType("text/html");

        jScrollPane1.setViewportView(ChatComponent);

        SendBtn.setText("Send Message");
        SendBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SendBtnActionPerformed(evt);
            }
        });

        MsgTextArea.setColumns(20);
        MsgTextArea.setRows(5);
        jScrollPane2.setViewportView(MsgTextArea);

        UserStatusLabel.setText("Online users:");

        UsersStatusArea.setColumns(20);
        UsersStatusArea.setRows(5);
        jScrollPane3.setViewportView(UsersStatusArea);

        StatusLabel.setText("status");

        StatusComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Online", "Busy", "Away", "Offline"}));
        StatusComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StatusComboBoxActionPerformed(evt);
            }
        });

        GroupsjList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {"DEV", "User emotion 1", "User emotion 2", "User emotion 3", "User emotion 4"};

            public int getSize() {
                return strings.length;
            }

            public String getElementAt(int i) {
                return strings[i];
            }
        });
        jScrollPane4.setViewportView(GroupsjList);

        GroupsLabel.setText("Emotions");
        JoinBtn.setText("1 [DEV]");
        LeaveBtn.setText("2 [DEV]");
        CreateGroupBtn.setText("Dev button");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(69, 69, 69)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(UsernameLabel)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(UsernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(AddressLabel)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(AddressTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(PortNumberLabel))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(ConnectBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(72, 72, 72)))
                                                // .addComponent(DisconnectBtn)))
                                                .addGap(18, 18, 18)
                                                .addComponent(PortNumTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
                                                .addComponent(SendBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jScrollPane2)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                //.addComponent(StatusLabel)
                                                .addGap(18, 18, 18)
                                                // .addComponent(StatusComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap(174, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(39, 39, 39)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(GroupsLabel)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(CreateGroupBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(UserStatusLabel)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                                        .addComponent(JoinBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(LeaveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING)))
                                                .addGap(0, 51, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(AddressLabel)
                                        .addComponent(AddressTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(UsernameLabel)
                                        .addComponent(UsernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(PortNumberLabel)
                                        .addComponent(PortNumTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                //.addComponent(StatusLabel)
                                //.addComponent(StatusComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(33, 33, 33)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        // .addComponent(DisconnectBtn)
                                                        .addComponent(ConnectBtn)))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(UserStatusLabel)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(30, 30, 30)
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(GroupsLabel)
                                                        .addComponent(CreateGroupBtn))
                                                .addGap(18, 18, 18)
                                                .addComponent(jScrollPane4)))
                                .addGap(29, 29, 29)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(SendBtn)
                                        .addComponent(JoinBtn)
                                        .addComponent(LeaveBtn))
                                .addContainerGap(42, Short.MAX_VALUE))
        );

        pack();
    }

    public String convertMessageToHtml(String message) {
        String emotion = StringUtils.substringBetween(message, "|[", "]");
        if (emotion != null && emotion.length() > 0 && !"null".equals(emotion)) {
            String imgsrc = "";
            try {
                switch (emotion.toLowerCase()) {
                    case "joy": {
                        imgsrc = new File(JOY_IMG_PATH).toURL().toExternalForm();
                        break;
                    }
                    case "sadness": {
                        imgsrc = new File(SADNESS_IMG_PATH).toURL().toExternalForm();
                        break;
                    }
                    case "analytical": {
                        imgsrc = new File(ANALYTICAL_IMG_PATH).toURL().toExternalForm();
                        break;
                    }
                    case "fear": {
                        imgsrc = new File(FEAR_IMG_PATH).toURL().toExternalForm();
                        break;
                    }
                    case "unknown": {
                        imgsrc = new File(UNKNOWN_JPG_PATH).toURL().toExternalForm();
                        break;
                    }
                    case "loader": {
                        imgsrc = new File(LOADER_GIF_PATH).toURL().toExternalForm();
                        break;
                    }
                    case "anger": {
                        imgsrc = new File(ANGRY_IMG_PATH).toURL().toExternalForm();
                        break;
                    }
                    case "tentative": {
                        imgsrc = new File(TENTATIVE_IMG_PATH).toURL().toExternalForm();
                        break;
                    }
                    default: {
                        imgsrc = new File(UNKNOWN_JPG_PATH).toURL().toExternalForm();
                        System.err.println("Not correct type for emotion in converting " + emotion);
                    }
                }
            } catch (MalformedURLException ex) {
                System.err.println(ex);
            }
            String convertedMessage = message.replace("|[" + emotion + "]", "");
            return "<img src='" + imgsrc + "' width='20' height='20'/>&nbsp;&nbsp;" + convertedMessage + "<br>";
        } else {
            return "<p>" + message + "</p><br/>";
        }
    }

    public void addMessage(String message) {
        chatHistory += "<p>" + message + "</p><br>";
    }

    private void DisconnectBtnActionPerformed(java.awt.event.ActionEvent evt) {
        client.closeConnection();
        DisconnectBtn.setEnabled(false);
        ConnectBtn.setEnabled(true);
    }

    public void closeFrame() {
        if (client != null) {
            client.SendMessage("logout");
        }
    }

    private void ConnectBtnActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("HashMap in the beginning of Connect size " + OtherUserStatus.size());
        client = new MyClient(AddressTextField.getText(), Integer.parseInt(PortNumTextField.getText()), this);
        DisconnectBtn.setEnabled(true);
        ConnectBtn.setEnabled(false);
        client.SendMessage("#####" + UsernameTextField.getText());
        client.ReadMessage();
        System.out.println("HashMap in the end of Thread 1 size " + OtherUserStatus.size());
        System.out.println("Size of HashMap  after thread 2 is " + MyClient.OtherUserStatus.size());
    }

    private void SendBtnActionPerformed(java.awt.event.ActionEvent evt) {
        client.SendMessage(MsgTextArea.getText());
        MsgTextArea.setText("");

    }

    private void StatusComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (client.isConnected == true) {
                client.status = (String) StatusComboBox.getSelectedItem();
                client.UserStatusChanged = true;
                client.SendMessage(getEncodedStatus());
            } else {

            }
        } catch (Exception ex) {
            System.out.println("Connected Not Pressed, client is still not instantiated");
        }
    }

    public void setStatusAreaText(String statuses) {
        if (!Objects.isNull(statuses)) {
            UsersStatusArea.setText(statuses);
        }
    }

    public void setChatComponentText(String chatText) {
        if (!Objects.isNull(chatText)) {
            ChatComponent.setText(chatText);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(ClientFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClientFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClientFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClientFrame().setVisible(true);
            }
        });
    }

    private javax.swing.JLabel AddressLabel;
    private javax.swing.JTextField AddressTextField;
    private javax.swing.JButton ConnectBtn;
    private javax.swing.JButton CreateGroupBtn;
    private javax.swing.JButton DisconnectBtn;
    private javax.swing.JLabel GroupsLabel;
    private javax.swing.JList<String> GroupsjList;
    private javax.swing.JButton JoinBtn;
    private javax.swing.JButton LeaveBtn;
    private javax.swing.JTextArea MsgTextArea;
    private javax.swing.JTextField PortNumTextField;
    private javax.swing.JLabel PortNumberLabel;
    private javax.swing.JButton SendBtn;
    private javax.swing.JComboBox<String> StatusComboBox;
    private javax.swing.JLabel StatusLabel;
    private javax.swing.JLabel UserStatusLabel;
    private javax.swing.JLabel UsernameLabel;
    private javax.swing.JTextField UsernameTextField;
    private javax.swing.JTextArea UsersStatusArea;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private JTextPane ChatComponent;
}
