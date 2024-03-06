package it.madeworld.pdftoalto;

//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
//import java.io.FileWriter;
import java.io.IOException;

public class PdfToAlto {

    // Constructor
    public PdfToAlto() {
        // Any initialization you might need
    }

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

        // Path to the pdfalto executable
        String pdfAltoExecutablePath = "/Users/barone/Desktop/PDFALTO/pdfalto/pdfalto"; // Replace with the actual path to the executable

        // Generate a file name for the ALTO XML without the '.pdf' extension
        String fileNameWithoutExt = pdfFile.getName().replaceFirst("[.][^.]+$", "");

        // Command to execute pdfalto
        String[] command = {
            pdfAltoExecutablePath,
            "-noImage",  // this option to prevent images from being extracted
            pdfFilePath,
            altoOutputDir + "/" + fileNameWithoutExt + ".xml"
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
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: PdfToAlto <PDF file path> <ALTO XML output directory>");
            System.exit(1);
        }

        String pdfFilePath = args[0];
        String altoOutputDir = args[1];
        PdfToAlto converter = new PdfToAlto();

        try {
            converter.convertPdfToAlto(pdfFilePath, altoOutputDir);
            System.out.println("Conversion completed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}