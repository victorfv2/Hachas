

import java.io.*;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Dividir {
	public static String fe, fs;

	private static boolean isNumeric(String cadena) {
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	public static void XML(int npartes, long tamano_ori, String ext, File r) throws IOException {

		try {
			String partes = Integer.toString(npartes);
			String tamo = Long.toString(tamano_ori);

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("INFO_fichero");
			doc.appendChild(rootElement);

			Element npts = doc.createElement("Numero_de_partes");
			npts.setTextContent(partes);
			rootElement.appendChild(npts);

			Element tam_original = doc.createElement("tamano_original");
			tam_original.setTextContent(tamo);
			rootElement.appendChild(tam_original);
			
			Element extension = doc.createElement("extension");
			extension.setTextContent(ext);
			rootElement.appendChild(extension);

			// Se escribe el contenido del XML en un archivo
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(r);
			transformer.transform(source, result);
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}

	public static void main(String[] Args) throws IOException {
		String nump = " ";
		int p = 0, nbytes = 0;
		JFileChooser fce = new JFileChooser();
		fce.setDialogTitle("Escoja  el archivo a dividir");
		fce.showOpenDialog(fce);

		fe = fce.getSelectedFile().getAbsolutePath();
		String ext= fe.substring(fe.lastIndexOf("."));
		System.out.println("extension "+ ext);
		do {
			nump = JOptionPane.showInputDialog(null, "Introduce en cuantas partes quieres dividir el archivo");
			if (Integer.parseInt(nump) == JOptionPane.CANCEL_OPTION) {
					System.exit(0);
			}
		} while (isNumeric(nump) == false);

		int numps = Integer.parseInt(nump);
		File fie = new File(fe);
		JFileChooser fcg = new JFileChooser();
		fcg.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int guardar = fcg.showSaveDialog(fcg);
		if (guardar == JFileChooser.APPROVE_OPTION) {
			fs = fcg.getSelectedFile().getAbsolutePath();
		}
		File fg = new File(fs);
		int tambuff = (int) (fie.length() / numps);
		String xml = String.format("%s.%s", fie.getAbsolutePath(), "xml");
		File fxml = new File(xml);

		byte[] buffer = new byte[tambuff + 1];
		FileInputStream fis = new FileInputStream(fie);
		BufferedInputStream bis = new BufferedInputStream(fis);

		while ((nbytes = bis.read(buffer)) > 0) {
			String exp = String.format("%s.%03d", fie.getName(), p++);
			File ffinal = new File(fg, exp);

			FileOutputStream escribir = new FileOutputStream(ffinal);
			escribir.write(buffer, 0, nbytes);

		}

		Dividir.XML(numps, fie.length(),ext, fxml);
	}
}