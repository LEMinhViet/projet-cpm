package analyser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import logs.Page;
import logs.Session;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class TraiterARFF {
	
	public void creerARFF(List<Session> sessions, List<Page> pages, String nom) {
		int[] pagesVisites;
		
		try {
			FileWriter fw = new FileWriter(new File(nom));
			BufferedWriter bw = new BufferedWriter(fw);
			
			// "header"
			fw.write("@RELATION session\n");
			fw.write("\n");
			
			// Les attributes
			for (Page page : pages) {
				fw.write("@ATTRIBUTE " + page.getLien() + " {0,1}\n");
			}
			fw.write("\n");
			
			// Les donnees
			fw.write("@DATA\n");
			
			for (Session session : sessions) {
				pagesVisites = session.getPagesVisites();
				
				for (int i = 0; i < pagesVisites.length - 1; i++) {
					fw.write(pagesVisites[i] + ",");
				}
				
				// La dernier element n'a pas ","
				fw.write(pagesVisites[pagesVisites.length - 1] + "\n");
			}
			
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 		
	}
	
	public void traiterARFF_KMeans(String nom, int nbCluster) {
		SimpleKMeans kmeans = new SimpleKMeans();
		
		try {
			
			FileReader fr = new FileReader(nom);
			Instances instances = new Instances(fr);
			fr.close();
			
			kmeans.setNumClusters(nbCluster);
//			kmeans.setDistanceFunction(new EuclideanDistance());
			kmeans.buildClusterer(instances);
			
			ArffSaver arffSaver = new ArffSaver();
			arffSaver.setInstances(kmeans.getClusterCentroids());
			arffSaver.setFile(new File(nom.replace(".arff", "_kmeans.arff")));
			arffSaver.writeBatch();				
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public String interpreterCluster(String nom) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(nom)));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nom.replace(".arff", ".clusters"))));
			
			String ligne = br.readLine();
			ArrayList<String> pages = new ArrayList<String>();			
			
			while(!ligne.contains("@attribute")) {
				ligne = br.readLine();
			}
			
			while(ligne.contains("@attribute")) {
				pages.add(ligne.split(" ")[1]);
				ligne = br.readLine();
			}
			
			while(!ligne.contains("@data")) {
				ligne = br.readLine();
			}
						
			int i = 1;
			ligne = br.readLine();
			while (ligne != null) {
				bw.write("Cluster " + i + " : ");
				
				int count = 0;
				for (String s : ligne.split(",")) {
					if (Integer.parseInt(s) == 1) {
						bw.write(pages.get(count) + " ; ");
					}
					count ++;
				}
				
				bw.write("\n");
				ligne = br.readLine();
				i++;
			}
			
			br.close();
			bw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return nom.replace(".arff", ".clusters");
	}
}
