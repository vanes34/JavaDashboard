import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RegistrationForm extends JDialog {
    private JTextField txName;
    private JTextField txEmail;
    private JTextField txPhone;
    private JTextField txAddress;
    private JPasswordField passwordField2;
    private JButton registerButton;
    private JButton cancelButton;
    private JPasswordField txpassword;
    private JPanel registerPanel;

    public RegistrationForm(JFrame parent) {
        super(parent);
       setTitle ( "Create a new account " );
       setContentPane ( registerPanel );
       setMinimumSize ( new Dimension (450 , 474) );
       setModal ( true );
       setLocationRelativeTo(parent);
       setDefaultCloseOperation ( DISPOSE_ON_CLOSE );
       pack ();
        registerButton.addActionListener ( new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        } );
        cancelButton.addActionListener ( new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        } );
        setVisible ( true );
    }

    private void registerUser() {
      String name=txName.getText ();
      String email=txEmail.getText ();
      String phone=txPhone.getText ();
      String address=txAddress.getText ();
      String password=String.valueOf ( txpassword.getPassword () );
      String confirmPassword=String.valueOf ( passwordField2.getPassword () );


if (name.isEmpty () || email.isEmpty () || phone.isEmpty () || password.isEmpty ()){
    JOptionPane.showMessageDialog ( this , " Please enter all fields" ,
            "Try again", JOptionPane.ERROR_MESSAGE );
    return;
}

if(!password.equals (confirmPassword )){
    JOptionPane.showMessageDialog ( this ,
            "Confirm Password does not match " ,
            "Try again" ,
            JOptionPane.ERROR_MESSAGE);
    return;
}
user=addUserToDataBase(name, email , phone , address , password);
if(user != null){
    dispose ();
}
else {
    JOptionPane.showMessageDialog ( this ,
            "Failed to register new user",
            "Try again",
    JOptionPane.ERROR_MESSAGE);
}
    }
public User user;
    private User addUserToDataBase(String name, String email, String phone, String address, String password) {
        User user = null;
        final String DB_URL = "jdbc:sqlite:C:/Users/User/IdeaProjects/JavaDashboard/users.db";


        try {
            try (Connection conn = DriverManager.getConnection ( DB_URL );
                 Statement stmt = conn.createStatement ()) {
                String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                        "name TEXT NOT NULL," +
                        "email TEXT NOT NULL," +
                        "phone TEXT NOT NULL," +
                        "address TEXT NOT NULL," +
                        "password TEXT NOT NULL);";

                System.out.println ( "The database is established " );

                stmt.execute ( createTableSQL );
                String sql = "INSERT INTO users (name , email, phone ,address , password)" +
                        "VALUES (? ,?,?,? ,?)";
                PreparedStatement preparedStatement = conn.prepareStatement ( sql );
                preparedStatement.setString ( 1, name );
                preparedStatement.setString ( 2, email );
                preparedStatement.setString ( 3, phone );
                preparedStatement.setString ( 4, address );
                preparedStatement.setString ( 5, password );


                int addedRows = preparedStatement.executeUpdate ();
                if (addedRows > 0) {
                    user = new User ();
                    user.name = name;
                    user.email = email;
                    user.phone = phone;
                    user.address = address;
                    user.password = password;
                }

            } catch (SQLException e) {
                e.printStackTrace ();
            }
            return user;
        } catch (Exception e) {
            throw new RuntimeException ( e );
        }
    }
    public static void main(String[] args) {
        RegistrationForm myform= new RegistrationForm ( null );
        User user =myform.user;
        if(user!= null){
            System.out.println ("Successful registration of "+ user.name);
        }
        else {
            System.out.println ("Registration canceled");
        }
    }
}
