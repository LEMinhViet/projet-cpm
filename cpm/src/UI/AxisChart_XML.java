package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logs.Utilisateur;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import analyser.AnalyserSession;

public class AxisChart_XML extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int TYPE_INTERACTION = 0;
	 private final int TYPE_LONGEUR = 1;
	private final int NB_ELEMENT = 15;
	private final int NB_SERIE_INTERACTION = 4;
	private final int NB_SERIE_LONGEUR = 1;
	
	private final JPanel contentPanel = new JPanel();
	
	private Box verticalBox; 
	private JPanel panel;
	private JLabel lbl_Choisir;
	private JComboBox<String> comboBox_Type;
	
	private ChartPanel chartPanel;
	private AnalyserSession analyserSession;
	
	/**
	 * Create the frame.
	 */
	public AxisChart_XML(AnalyserSession analyserSession) {
		this.analyserSession = analyserSession;
		
		creerInterface();
		
		comboBox_Type.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		verticalBox.remove(chartPanel);
        		verticalBox.revalidate();
        		chartPanel = new ChartPanel(creerCarte(comboBox_Type.getSelectedIndex()));
        		verticalBox.add(chartPanel);
        	    chartPanel.setPreferredSize(new java.awt.Dimension(800, 480));
        		verticalBox.repaint();
        	}
        });              
	}
	
	private void creerInterface() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 480);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
        
        verticalBox = Box.createVerticalBox();
        contentPanel.add(verticalBox);
        
        panel = new JPanel();
        verticalBox.add(panel);
        
        lbl_Choisir = new JLabel("Choisir le type de comparaison :");
        panel.add(lbl_Choisir);
        
        comboBox_Type = new JComboBox<String>();
        comboBox_Type.setModel(new DefaultComboBoxModel<String>(new String[] {"Par nombre d'interactions avec outil", 
        																	  "Par la longeur du text"}));
        comboBox_Type.setSelectedIndex(0);
        panel.add(comboBox_Type);      
        
        // add the chart to a panel...
        chartPanel = new ChartPanel(creerCarte(comboBox_Type.getSelectedIndex()));
        verticalBox.add(chartPanel);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 480));
	}
	
	private JFreeChart creerCarte(int type) {
		CategoryDataset dataset = createDataset(analyserSession, type);

        // Create the chart...
        JFreeChart chart = ChartFactory.createBarChart(
            "",        				   // chart title
            "Utilisateur",             // domain axis label
            "",       				   // range axis label
            dataset,                   // data
            PlotOrientation.VERTICAL,
            true,                      // include legend
            false,                      // tooltips?
            false                      // URL generator?  Not required...
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // get a reference to the plot for further customisation...
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(0xEE, 0xEE, 0xFF));
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        plot.setBackgroundPaint(Color.black);
        
        CategoryItemRenderer cir = plot.getRenderer(); 
        cir.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        cir.setBaseItemLabelsVisible(true);
        cir.setBaseItemLabelPaint(Color.white);
        
        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
       
        return chart;
	}
	
	private CategoryDataset createDataset(AnalyserSession analyserSession, int type) {
		List<Utilisateur> utilisateurs = analyserSession.getUtilisateurs();
		
		int nbElement;
		if (utilisateurs.size() < NB_ELEMENT) {
			nbElement = utilisateurs.size();
		} else {
			nbElement = NB_ELEMENT;
		}
		
        // row keys...
		String[] series;
		if (type == TYPE_INTERACTION) {
			series = new String[NB_SERIE_INTERACTION];
	        series[0] = "Nombre d'interaction dans l'etape 1";
	        series[1] = "Nombre d'interaction dans l'etape 2";
	        series[2] = "Nombre d'interaction dans l'etape 3";
	        series[3] = "Nombre d'interaction dans l'etape 4";
		} else { // if (type == TYPE_LONGEUR) {
			series = new String[NB_SERIE_LONGEUR];
	        series[0] = "La longeur du text";
		} 

        // column keys...
        String[] categories = new String[nbElement];
        for (int i = 0; i < nbElement; i++) {
        	categories[i] = utilisateurs.get(i).getNom();
        }

        // create the dataset...
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < nbElement; i++) {
        	for (int j = 0; j < series.length; j++) {
        		if (type == TYPE_INTERACTION) {
        			dataset.addValue(utilisateurs.get(i).getNombreAction(Utilisateur.ETAPE[j]), series[j], categories[i]);
        		} else if (type == TYPE_LONGEUR) {
        			System.err.println(utilisateurs.get(i).getNombreText());
        			dataset.addValue(utilisateurs.get(i).getNombreText(), series[j], categories[i]);
        		}
        	}
        }

        return dataset;
    }
}
