import java.lang.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

public class Addbook extends JFrame implements ActionListener
{
	JLabel userLabel, eNameLabel, authorLabel,yearlabel, qtnLabel;
	JTextField userTF, eNameTF, authorTF,yearTF,qtnTF;
	JButton refreshBtn, loadBtn, updateBtn, delBtn, backBtn, logoutBtn;
	JPanel panel;
	
	String userId;
	
	public Addbook(String userId)
	{
		super("add book");
		
		this.setSize(950, 700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.setLocationRelativeTo(null);
		this.userId = userId;
		
		panel = new JPanel();
		panel.setBackground(new Color(241-222-132));
		panel.setLayout(null);
		
		
		userLabel = new JLabel("Book ID : ");
		userLabel.setBounds(150, 100, 120, 30);
		panel.add(userLabel);
		
		userTF = new JTextField();
		userTF.setBounds(250, 100, 120, 30);
		panel.add(userTF);
		
		
		eNameLabel = new JLabel("Book Name : ");
		eNameLabel.setBounds(150, 150, 120, 30);
		panel.add(eNameLabel);
		
		eNameTF = new JTextField();
		eNameTF.setBounds(250, 150, 120, 30);
		panel.add(eNameTF);
		
		authorLabel = new JLabel("Author Name : ");
		authorLabel.setBounds(150, 200, 120, 30);
		panel.add(authorLabel);
		
		authorTF = new JTextField();
		authorTF.setBounds(250, 200, 120, 30);
		panel.add(authorTF);
		
		yearlabel = new JLabel("Publication Year : ");
		yearlabel.setBounds(150, 250, 120, 30);
		panel.add(yearlabel);
		
		yearTF = new JTextField();
		yearTF.setBounds(250, 250, 120, 30);
		panel.add(yearTF);
		
		
		qtnLabel = new JLabel("Quantity : ");
		qtnLabel.setBounds(150, 300, 120, 30);
		panel.add(qtnLabel);
		
		qtnTF = new JTextField();
		qtnTF.setBounds(250, 300, 120, 30);
		panel.add(qtnTF);
		
		loadBtn = new JButton("Load");
		loadBtn.setBounds(550, 150, 150, 30);
		loadBtn.addActionListener(this);
		panel.add(loadBtn);
		
		logoutBtn = new JButton("Logout");
		logoutBtn.setBounds(350, 450, 120, 30);
		logoutBtn.addActionListener(this);
		panel.add(logoutBtn);
		
		refreshBtn = new JButton("Refresh");
		refreshBtn.setBounds(550, 100, 150, 30);
		refreshBtn.addActionListener(this);
		panel.add(refreshBtn);
		
		
		updateBtn = new JButton("Update");
		updateBtn.setBounds(150, 400, 120, 30);
		updateBtn.setEnabled(false);
		updateBtn.addActionListener(this);
		panel.add(updateBtn);
		
		delBtn = new JButton("Delete");
		delBtn.setBounds(150, 450, 120, 30);
		delBtn.setEnabled(false);
		delBtn.addActionListener(this);
		panel.add(delBtn);
		
		backBtn = new JButton("Back");
		backBtn.setBounds(350, 400, 120, 30);
		backBtn.addActionListener(this);
		panel.add(backBtn);
		
		this.add(panel);
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		String text = ae.getActionCommand();
		
		if(text.equals(backBtn.getText()))
		{
			EmployeeHome me = new EmployeeHome(userId);
			me.setVisible(true);
			this.setVisible(false);
		}
		if(text.equals(refreshBtn.getText()))
		{
			updateBtn.setEnabled(false);
			delBtn.setEnabled(false);
			userTF.setEnabled(true);
			userTF.setText("");
			eNameTF.setText("");
			authorTF.setText("");
			yearTF.setText("");
			qtnTF.setText("");
		}
		else if(text.equals(logoutBtn.getText()))
		{
			Home hg = new Home();
			hg.setVisible(true);
			this.setVisible(false);
		}
		else if(text.equals(loadBtn.getText()))
		{
			loadFromDB();			
		}
		else if(text.equals(updateBtn.getText()))
		{
			updateInDB();
		}
		else if(text.equals(delBtn.getText()))
		{
			deleteFromDB();
		}
		else{}
	}
	
	public void loadFromDB()
	{
		String loadId = userTF.getText();
		String query = "SELECT `book`, `authorName`, `publicationYear` , `availableQuantity`  FROM `book` WHERE `bookId`='"+loadId+"';";     
        Connection con=null;//for connection
        Statement st = null;//for query execution
		ResultSet rs = null;//to get row by row result from DB
		System.out.println(query);
        try
		{
			Class.forName("com.mysql.jdbc.Driver");//load driver
			System.out.println("driver loaded");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/library","root","");
			System.out.println("connection done");//connection with database established
			st = con.createStatement();//create statement
			System.out.println("statement created");
			rs = st.executeQuery(query);//getting result
			System.out.println("results received");
			
			boolean flag = false;
			String bName = null;
			String aName = null;
			int pYear = 0;
			int qtn = 0;			
			while(rs.next())
			{
                bName = rs.getString("bookTitle");
				aName = rs.getString("authorName");
				 pYear = rs.getInt("publicationYear");
				qtn = rs.getInt("availableQuantity");
				flag=true;
				
				eNameTF.setText(bName);
				authorTF.setText(aName);
				yearTF.setText(""+pYear);
				qtnTF.setText(""+qtn);
				userTF.setEnabled(false);
				updateBtn.setEnabled(true);
				delBtn.setEnabled(true);
			}
			if(!flag)
			{
				eNameTF.setText("");
				authorTF.setText("");
				yearTF.setText("");
				userTF.setText("");
				qtnTF.setText("");
				JOptionPane.showMessageDialog(this,"Invalid ID"); 
			}
		}
        catch(Exception ex)
		{
			System.out.println("Exception : " +ex.getMessage());
        }
        finally
		{
            try
			{
                if(rs!=null)
					rs.close();

                if(st!=null)
					st.close();

                if(con!=null)
					con.close();
            }
            catch(Exception ex){}
        }
	}
	
	public void updateInDB()
	{
		String newId = userTF.getText();
		String eName = eNameTF.getText();
		String aName = authorTF.getText();
		int year =0;
		int qtn=0;
		try
		{
			year = Integer.parseInt(yearTF.getText());
			qtn = Integer.parseInt(qtnTF.getText());
			
		}
		catch(Exception e){}
		String query = "UPDATE book SET book='"+eName+"', authorName = '"+aName+"', publicationYear = '"+year+"',availableQuantity = "+qtn+" WHERE bookId`='"+newId+"'";	
        Connection con=null;//for connection
        Statement st = null;//for query execution
		System.out.println(query);
        try
		{
			Class.forName("com.mysql.jdbc.Driver");//load driver
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/library","root","");
			st = con.createStatement();//create statement
			st.executeUpdate(query);
			st.close();
			con.close();
			JOptionPane.showMessageDialog(this, "Success !!!");
			
			updateBtn.setEnabled(false);
			delBtn.setEnabled(false);
			userTF.setEnabled(true);
			userTF.setText("");
			eNameTF.setText("");
			authorTF.setText("");
			yearTF.setText("");
			qtnTF.setText("");
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			JOptionPane.showMessageDialog(this, "Oops !!!");
		}
	}
	
	public void deleteFromDB()
	{
		String newId = userTF.getText();
		String query1 = "DELETE from book WHERE bookId='"+newId+"';";
		System.out.println(query1);

        try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "");
			Statement stm = con.createStatement();
			stm.execute(query1);
			stm.close();
			con.close();
			JOptionPane.showMessageDialog(this, "Success !!!");
			
			updateBtn.setEnabled(false);
			delBtn.setEnabled(false);
			userTF.setEnabled(true);
			userTF.setText("");
			eNameTF.setText("");
			authorTF.setText("");
			yearTF.setText("");
			qtnTF.setText("");
		}
        catch(Exception ex)
		{
			JOptionPane.showMessageDialog(this, "Oops !!!");
        }
	}
	
}