package it.madeworld.pdftoalto;

//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
//import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe utilizzata per la gestione del convertitore Pdf to Alto
 */
public class PdfToAlto {

  private String pdfAltoExecutablePath = null;
  
    // Constructor
  /**
   * Costruttore della Classe
   * 
   * @param pdfAltoExecutablePath PAth completo per indicare la posizione e il nome del file raltivo al cconvertitore Alto
   */
    public PdfToAlto(String pdfAltoExecutablePath) {
      this.pdfAltoExecutablePath = pdfAltoExecutablePath;
    }

    /**
     * Classe utilizzata per verificare e creare il file Alto da un PDF
     * 
     * @param pdfFilePath Path più nome file PDF
     * @param altoOutputDir PAth in cui scaricare il file alto
     * @throws Exception 
     */
    public void convertPdfToAlto(String pdfFilePath, String altoOutputDir) throws Exception {
        // Check if the PDF file exists
        File pdfFile = new File(pdfFilePath);
        if (!pdfFile.exists()) {
            throw new IOException("The PDF file does not exist: " + pdfFilePath);
        }

        // Ensure the output directory exists
        File altoOutputDirFile = new File(altoOutputDir);
        if (!altoOutputDirFile.exists()) {
            boolean wasDirectoryMade = altoOutputDirFile.mkdirs(); // Create the directory if it does not exist
            if (!wasDirectoryMade) {
                throw new IOException("Failed to create the output directory: " + altoOutputDir);
            }
        }


        // Generate a file name for the ALTO XML without the '.pdf' extension
        String fileNameWithoutExt = pdfFile.getName().replaceFirst("[.][^.]+$", "");

        // Command to execute pdfalto
        String[] command = {
            pdfAltoExecutablePath,
            "-noImage",  // this option to prevent images from being extracted
            pdfFilePath,
            altoOutputDir + File.separator + fileNameWithoutExt + ".xml"
        };

        // Execute pdfalto command
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(altoOutputDirFile); // Set the working directory to the output directory
        Process process = processBuilder.start();

        // Wait for the process to complete
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IOException("pdfalto conversion failed with exit code " + exitCode);
        }

        System.out.println("Conversion to ALTO XML completed successfully.");
    }

    // Main method
    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: PdfToAlto <PDF file path> <ALTO XML output directory>");
            System.exit(1);
        }

        String pdfFilePath = args[0];
        String altoOutputDir = args[1];
        PdfToAlto converter = new PdfToAlto("/Users/barone/Desktop/PDFALTO/pdfalto/pdfalto");

        try {
            converter.convertPdfToAlto(pdfFilePath, altoOutputDir);
            System.out.println("Conversion completed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}