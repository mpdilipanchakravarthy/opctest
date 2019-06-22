package examples.javafish.clients.opc;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import javafish.clients.opc.JOpc;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.exception.ComponentNotFoundException;
import javafish.clients.opc.exception.ConnectivityException;
import javafish.clients.opc.exception.SynchReadException;
import javafish.clients.opc.exception.SynchWriteException;
import javafish.clients.opc.exception.UnableAddGroupException;
import javafish.clients.opc.exception.UnableAddItemException;
import javafish.clients.opc.variant.Variant;
import javafish.clients.opc.variant.VariantList;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class Main {

	private JFrame frame;
	private JTextField Tagvalue01;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblTagValue = new JLabel("Tag Value");
		lblTagValue.setBounds(81, 68, 63, 17);
		frame.getContentPane().add(lblTagValue);
		
		Tagvalue01 = new JTextField();
		Tagvalue01.setEditable(false);
		Tagvalue01.setBounds(154, 65, 86, 20);
		frame.getContentPane().add(Tagvalue01);
		Tagvalue01.setColumns(10);
		
		JButton btnTagRead = new JButton("Tag Read");
		btnTagRead.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					opc(0,false);
				} catch (SynchWriteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnTagRead.setBounds(312, 65, 89, 23);
		frame.getContentPane().add(btnTagRead);
		
		JButton button = new JButton("Tag Read");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("mouse");
			}
		});
		button.setBounds(228, 133, 89, 23);
		frame.getContentPane().add(button);
		
		JComboBox comboBox = new JComboBox();
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				System.out.println(comboBox.getSelectedItem());
			}
		});
		comboBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			@Override
			public void mouseClicked(MouseEvent e) {
			
			}
		});
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5"}));
		comboBox.setBounds(81, 145, 119, 48);
		frame.getContentPane().add(comboBox);
		
		JButton tag_write = new JButton("tagwrite_true");
		tag_write.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					opc(1,true);
					opc(0,false);
				} catch (SynchWriteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		tag_write.setBounds(312, 99, 112, 23);
		frame.getContentPane().add(tag_write);
		
		JButton btnTagwritefalse = new JButton("tagwrite_false");
		btnTagwritefalse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					opc(1,false);
					opc(0,false);
				} catch (SynchWriteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnTagwritefalse.setBounds(312, 158, 112, 23);
		frame.getContentPane().add(btnTagwritefalse);
	}
	public void opc(int type, Boolean value) throws SynchWriteException
	{
		 Main test = new Main();
		    
		    JOpc.coInitialize();
		    
		    JOpc jopc = new JOpc("localhost", "Kepware.KEPServerEX.V6", "JOPC1");

		    OpcItem item1 = new OpcItem("Mixer01.Main_PLC.CheckMaterial.HandShake01", true, "");
		    //OpcItem item1 = new OpcItem("Random. true, "");
		    //OpcItem item1 = new OpcItem("Random.String", true, "");
		    
		    OpcGroup group = new OpcGroup("group1", true, 500, 0.0f);
		    
		    group.addItem(item1);
		    jopc.addGroup(group);
		    
		    try {
		      jopc.connect();
		      System.out.println("JOPC client is connected...");
		    }
		    catch (ConnectivityException e2) {
		      e2.printStackTrace();
		    }
		    
		    try {
		      jopc.registerGroups();
		      System.out.println("OPCGroup are registered...");
		    }
		    catch (UnableAddGroupException e2) {
		      e2.printStackTrace();
		    }
		    catch (UnableAddItemException e2) {
		      e2.printStackTrace();
		    }
		    
		    synchronized(test) {
		      try {
				test.wait(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    }
		    
		    // Synchronous reading of item
		    int cycles = 1;
		    int acycle = 0;
		    while (acycle++ < cycles) {
		      synchronized(test) {
		        try {
					test.wait(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		      }
		      
		      try {
		        OpcItem responseItem = jopc.synchReadItem(group, item1);
		        System.out.println(responseItem);
		        System.out.println(Variant.getVariantName(responseItem.getDataType()) + ": " + responseItem.getValue());
		        Tagvalue01.setText( responseItem.getValue().toString());
		        if(type==1){
		       /* VariantList list = new VariantList(Variant.VT_BOOL);
		        VariantList list1 = new VariantList(Variant.LENGTH_4);
		        list.add(new Variant(1));*/
		        Variant varin = new Variant(value);
		        System.out.println("Original Variant type: " +
		            Variant.getVariantName(varin.getVariantType()) + ", " + varin);
		        item1.setValue(varin);
		 
		        jopc.synchWriteItem(group, item1);
		        }
		      }
		      catch (ComponentNotFoundException e1) {
		        e1.printStackTrace();
		      }
		      catch (SynchReadException e) {
		        e.printStackTrace();
		      }
		    }
		    
		    JOpc.coUninitialize();
	}
}
