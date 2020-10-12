

import java.io.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.*;

import org.w3c.dom.Document;

public class Unir {
	public static String[] LeerXML(File d) throws IOException {
		String np = "";
		String tamori = "";
		String ext = "";

		try {

			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document xml = dBuilder.parse(d);
			String nump = xml.getElementsByTagName("Numero_de_partes").item(0).getTextContent();
			np = nump;
			String tamano = xml.getElementsByTagName("tamano_original").item(0).getTextContent();
			tamori = tamano;
			String extension = xml.getElementsByTagName("extension").item(0).getTextContent();
			ext = extension;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[] { np, tamori, ext };

	}

	public static void main(String[] Args) throws IOException {
		String rg = "";
		int e=0;
		FileInputStream leer;
		FileOutputStream escribir;
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("Ficheros XML (*.xml)", "xml");

		fc.setDialogTitle("Escoge el archivo xml del archivo a Unir");
		fc.setFileFilter(xmlfilter);
		fc.showOpenDialog(null);
		File xml = fc.getSelectedFile();
		System.out.println(xml);

		String[] v = Unir.LeerXML(xml);
		int npartes = Integer.parseInt(v[0]);
		double to = Double.parseDouble(v[1]);
		String extension = v[2];
		System.out.println(extension);

		JFileChooser fc2 = new JFileChooser();

		fc2.setDialogTitle("Escoge los archivos requeridos para unir");
		fc2.setMultiSelectionEnabled(true);
		fc2.showOpenDialog(null);
		File[] partes = null;
		if (e == fc2.APPROVE_OPTION) {
		partes = fc2.getSelectedFiles();
		}
		if (e == fc2.CANCEL_OPTION) {
			System.exit(0);
		}
		JFileChooser fcg = new JFileChooser();
		int g = fcg.showSaveDialog(null);
		if (g == fcg.APPROVE_OPTION) {
			rg = fcg.getSelectedFile().getAbsolutePath().concat(extension);
		}
		File ffg = new File(rg);
		if (g == fcg.CANCEL_OPTION) {
			System.exit(0);

		}

		escribir = new FileOutputStream(ffg);

		for (int i = 0; i < partes.length; i++) {
			int leido = 0;
			leer = new FileInputStream(partes[i]);
			leido = leer.read();

			while (leido != -1) {
				escribir.write(leido);
				leido = leer.read();
			}

		}
		if (ffg.length() != to) {
			JOptionPane.showMessageDialog(null,
					"Error,el tamaño del archivo no coincide con el original, vuelva a intentar unirlo+");
			System.out.println(ffg.length() + " " + to);
			ffg.delete();
		}

		/*
		 * if (a == to && partes.length == npartes) { break; }
		 */

	}
}
