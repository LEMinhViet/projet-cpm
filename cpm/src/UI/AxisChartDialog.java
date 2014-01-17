package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logs.Page;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import analyser.AnalyserSession;

import javax.swing.Box;

public class AxisChartDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int NB_ELEMENT = 10;
	private final int NB_SERIE = 3;
	
	private final JPanel contentPanel = new JPanel();
	
	private Box verticalBox; 
	private JPanel panel;
	private JLabel lbl_Choisir;
	private JComboBox<String> comboBox_Type;
	
	private ChartPanel chartPanel;
	private AnalyserSession analyserSession;
	
	private int typeDeTrier = 0;
	
	/**
	 * Create the dialog.
	 */
	public AxisChartDialog(AnalyserSession analyserSession) {
		this.analyserSession = analyserSession;
		
		creerInterface();
		
        comboBox_Type.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		typeDeTrier = comboBox_Type.getSelectedIndex();

        		verticalBox.remove(chartPanel);
        		verticalBox.revalidate();
        		chartPanel = new ChartPanel(creerCarte());
        		verticalBox.add(chartPanel);
        		verticalBox.repaint();
        	}
        });                
	}
	
	private void creerInterface() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 640, 480);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
        
        verticalBox = Box.createVerticalBox();
        contentPanel.add(verticalBox);
        
        panel = new JPanel();
        verticalBox.add(panel);
        
        lbl_Choisir = new JLabel("Choisir le type de tryage :");
        panel.add(lbl_Choisir);
        
        // add the chart to a panel...
        chartPanel = new ChartPanel(creerCarte());
        verticalBox.add(chartPanel);
        chartPanel.setPreferredSize(new java.awt.Dimension(640, 480));
        
        comboBox_Type = new JComboBox<String>();
        comboBox_Type.setModel(new DefaultComboBoxModel<String>(new String[] {"Par nombre de visites total", 
        																	  "Par nombre de visites en debut de session", 
        																	  "Par nombre de visites en fin de session"}));
        comboBox_Type.setSelectedIndex(0);
        panel.add(comboBox_Type);      
	}
	
	private JFreeChart creerCarte() {
		CategoryDataset dataset = createDataset(analyserSession);

        // Create the chart...
        JFreeChart chart = ChartFactory.createBarChart(
            "",        				   // chart title
            "Categorie",               // domain axis label
            "Nombre de visites",       // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            true,                      // include legend
            true,                      // tooltips?
            false                      // URL generator?  Not required...
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(0xEE, 0xEE, 0xFF));
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
        
        return chart;
	}
	
	private CategoryDataset createDataset(AnalyserSession analyserSession) {
		analyserSession.trierPages(typeDeTrier);
		List<Page> pages = analyserSession.getPages();
		
		int nbElement;
		if (pages.size() < NB_ELEMENT) {
			nbElement = pages.size();
		} else {
			nbElement = NB_ELEMENT;
		}
		
        // row keys...
		String[] series = new String[NB_SERIE];
        series[0] = "Nombre de visites";
        series[1] = "Nombre de visites en debut de session";
        series[2] = "Nombre de visites en fin de session";

        // column keys...
        String[] categories = new String[nbElement];
        for (int i = 0; i < nbElement; i++) {
        	categories[i] = pages.get(i).getLien();
        }

        // create the dataset...
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < nbElement; i++) {
        	dataset.addValue(pages.get(i).getNombre(Page.NOMBRE_TOTAL), series[0], categories[i]);
        	dataset.addValue(pages.get(i).getNombre(Page.NOMBRE_DEBUT), series[1], categories[i]);
        	dataset.addValue(pages.get(i).getNombre(Page.NOMBRE_FIN), series[2], categories[i]);
        }

        return dataset;

    }
}
