package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class EncryptionApp extends JFrame {

    private JTextField keyField,plaintextField, ciphertextField;
    private JButton encryptButton, decryptButton;

    public EncryptionApp() {
        initializeUI();
    }

    private void initializeUI() {
        JFrame frame = new JFrame("Encryption App");
        setTitle("Encryption App"); // 设置窗口标题
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗口关闭操作
        setSize(800, 400); // 设置窗口大小
        Color bgm = new Color(99,153,255);

        JPanel panel = new JPanel(new GridLayout(4, 2)); // 创建一个面板，使用4行2列的网格布局
        JLabel keyLabel = new JLabel("Keylable:");
        JLabel plaintextLabel = new JLabel("Plaintext:"); // 创建“明文”标签
        JLabel ciphertextLabel = new JLabel("Ciphertext:"); // 创建“密文”标签

        panel.setBackground(Color.orange);
        frame.add(panel);
        frame.setVisible(true);



        keyField = new JTextField();
        plaintextField = new JTextField(); // 创建明文输入文本框
        ciphertextField = new JTextField(); // 创建密文输入文本框，

        panel.add(keyLabel);// 将密钥标签添加到面板
        panel.add(keyField);
        panel.add(plaintextLabel); // 将明文标签添加到面板
        panel.add(plaintextField); // 将明文输入文本框添加到面板
        panel.add(ciphertextLabel); // 将密文标签添加到面板
        panel.add(ciphertextField); // 将密文输入文本框添加到面板

        encryptButton = new JButton("Encrypt"); // 创建“加密”按钮
        decryptButton = new JButton("Decrypt"); // 创建“解密”按钮



        //加密
        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keytext = keyField.getText();
                String plaintext = plaintextField.getText();
                boolean ischar = true;
                if(plaintext.length()==1) {
                    ischar =true;
                    String x = Integer.toBinaryString(plaintext.charAt(0));
                    plaintext = String.format("%8s", x).replace(' ', '0');
                } else ischar =false;   //判断是二进制还是ascill码,进行加密
                String ciphertext = S_DES.encrypt(plaintext,keytext,ischar);
                ciphertextField.setText(ciphertext);
            }
        });
        //解密
        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keytext = keyField.getText();
                String ciphertext = ciphertextField.getText();
                boolean ischar = true;
                if(ciphertext.length()==1) {
                    ischar =true;
                    String x = Integer.toBinaryString(ciphertext.charAt(0));
                    ciphertext = String.format("%8s", x).replace(' ', '0');
                } else ischar =false;   //判断是二进制还是ascill码,进行解密.
                String plaintext = S_DES.decode(ciphertext,keytext,ischar);
                plaintextField.setText(plaintext);
            }
        });

        panel.add(encryptButton); // 将加密按钮添加到面板
        panel.add(decryptButton); // 将解密按钮添加到面板

        add(panel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EncryptionApp();
            }
        });

    }
}
