package UI;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class TexteDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public TexteDialog(String nomFichier) {
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		try {
		    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) {
	        System.out.println("UIManager Exception : "+e);
		}
		
		setBounds(100, 100, 500, 400);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane);
			JTextArea textArea = new JTextArea();
			textArea.setEditable(false);
			scrollPane.setViewportView(textArea);
			
			try {
				textArea.setText(lireFichier(nomFichier));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public String lireFichier(String nomFichier) throws IOException {
		InputStream ips = new FileInputStream(nomFichier);
		InputStreamReader ipsr = new InputStreamReader(ips);
		BufferedReader br = new BufferedReader(ipsr);

		String ligne;
		StringBuilder sb = new StringBuilder();
		
		while ((ligne = br.readLine()) != null) {
			sb.append(ligne + "\n");
		}
		br.close();
		
		return sb.toString();
	}

}
