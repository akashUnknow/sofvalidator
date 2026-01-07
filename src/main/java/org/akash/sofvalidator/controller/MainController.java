package org.akash.sofvalidator.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import org.akash.sofvalidator.service.FileMatchService;
import org.akash.sofvalidator.service.PdfSofParserService;
import org.akash.sofvalidator.service.RarExtractorService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MainController {
    public TextArea resultArea;


    private File sofPdfFile;
    private File rarFile;

    public void uploadSofPdf(ActionEvent actionEvent) {
        FileChooser chooser=new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files","*.pdf")

        );
        sofPdfFile = chooser.showOpenDialog(new Stage());
    }

    public void uploadRar(ActionEvent actionEvent) {
        FileChooser chooser=new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("ZIP Files","*.zip")
        );
        rarFile = chooser.showOpenDialog(new Stage());
    }

    public void validateFiles(ActionEvent actionEvent) {
        if(sofPdfFile==null || rarFile==null){
            resultArea.setText("❌ Please upload both SOF PDF and RAR file.");
            return;
        }
        List<String> extensions = askUserForExtensions();
        if (extensions.isEmpty()) {
            resultArea.setText("❌ No extensions provided.");
            return;
        }

        try {
            PdfSofParserService pdfService = new PdfSofParserService();
            RarExtractorService rarService = new RarExtractorService();
            FileMatchService matchService = new FileMatchService();
            List<String> sofFileNames=pdfService.extractInputFileNames(sofPdfFile, extensions);
//            System.out.println("pdf-->");
//            for(String st:sofFileNames) System.out.println(st);
            List<String> rarFileNames = rarService.extractFileNames(rarFile);
//            System.out.println("Zip-->");
//            for(String st:rarFileNames) System.out.println(st);
            String report = matchService.compare(sofFileNames, rarFileNames);
            resultArea.setText(report);
        }catch (Exception e){
            resultArea.setText("error: "+e.getMessage());
        }
    }

    private List<String> askUserForExtensions() {
        TextInputDialog dialog = new TextInputDialog("dat,inp");
        dialog.setTitle("File Extension Input");
        dialog.setHeaderText("Enter allowed file extensions");
        dialog.setContentText("Extensions (comma separated):");
        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty() || result.get().trim().isEmpty()) {
            return Collections.emptyList();
        }

        return Arrays.stream(result.get().split(","))
                .map(String::trim)
                .toList();
    }
}
