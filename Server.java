import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

class Server extends JFrame 
{

    ServerSocket server;
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    //declare components
    private JLabel heading = new JLabel("Server Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);

    //Constructor..
    public Server() {
            try {
                server=new ServerSocket(5050);
                System.out.println("server is ready to accept connection");
                System.out.println("waiting...");
                socket=server.accept();

                br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out=new PrintWriter(socket.getOutputStream());

                createGUI();
                handleEvents();
                startReading();
                startWriting();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        private void handleEvents() {

            messageInput.addKeyListener(new KeyListener() {
    
                @Override
                public void keyTyped(KeyEvent e) {

                    //throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
                }
    
                @Override
                public void keyPressed(KeyEvent e) {
                    
                    //throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
                }
    
                @Override
                public void keyReleased(KeyEvent e) {
                    
                    //throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
                    //System.out.println("key released "+ e.getKeyCode());
                    if (e.getKeyCode() == 10) {
                        //System.out.println("you have pressed enter button");
                        String contentToSend=messageInput.getText();
                        messageArea.append("Me :" + contentToSend + "\n");
                        out.println(contentToSend);
                        out.flush();
                        messageInput.setText("");
                        messageInput.requestFocus();
                    }
                }
                
            });
        }

        private void createGUI()
    {
        this.setTitle("Server Messager");
        this.setSize(600,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("server_icon.png"));
        heading.setHorizontalTextPosition(JLabel.CENTER);
        heading.setVerticalTextPosition(JLabel.BOTTOM);
        heading.setHorizontalAlignment(JLabel.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(JLabel.CENTER);

        //frame ka Layout set karenge
        this.setLayout(new BorderLayout());

        //adding the components to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);


        this.setVisible(true);
    }

        //start reading [Method]
        public void startReading() {
            // thread-read karke deta rahega
            Runnable r1=()->{

                System.out.println("reader started..");

                try{

                while(true) {
                
                    String msg=br.readLine();
                    if(msg.equals("exit")) {
                        System.out.println("Client terminated the chat");
                        JOptionPane.showMessageDialog(this, "Client Terminated the chat");
                        messageInput.setEnabled(false);
                        
                        socket.close();
                        
                        break;
                    }
                    //System.out.println("Client : "+ msg);
                    messageArea.append("Client : " + msg+"\n");

                }

                }catch (Exception e) {
                    //e.printStackTrace();
                    System.out.println("connection is closed");
                
                }

            };

            new Thread(r1).start();

        }

        public void startWriting() {
                // thread - data user lega and send karega client tak
                Runnable r2=()->{
                    System.out.println("writer started..");
                    
                    try {

                    while(!socket.isClosed()) {
                    

                            BufferedReader br1 = new BufferedReader(new  InputStreamReader(System.in));
                            String content=br1.readLine();
                            out.println(content);
                            out.flush();

                            if(content.equals("exit"))
                            {
                                socket.close();
                                break;
                            }

                        } 
                        
                    } catch(Exception e)
                    {
                        //e.printStackTrace();
                        System.out.println("connection is closed");
                    }

                };

                new Thread(r2).start();
            }
        
    
    public static void main(String[] args) {
        System.out.println("this is server..going to start server");
        new Server(); 
    }
}