/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uygulama;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 *
 * @author hsynpsdmr
 */
public class KategoriEkleController {
    public ComboBox<String> aracTuruBox;
    public TextField kategoriText;
    
     public void kaydet(ActionEvent event) throws IOException {
    String tur = aracTuruBox.getSelectionModel().getSelectedItem();
    String kategori = kategoriText.getText();
     if(tur == null){
         MesajKutusu.mesajGoster("Lütfen Araç Türünü Seçiniz");
     }
     else{
         if(kategori.equalsIgnoreCase("")){
             MesajKutusu.mesajGoster("Lütfen Kategori Alanını Doldurunuz");
         }
         
         else{
        File oku = new File(tur + "Kategori.txt");
        FileReader fileReader = new FileReader(oku);
        BufferedReader br = new BufferedReader(fileReader);
        String okunan;
        Boolean kategoriVarMi = false;
       
            while ((okunan = br.readLine()) != null) {
                if (okunan.equalsIgnoreCase(kategori)) {
                    kategoriVarMi = true;
                }
            }
            
             if(kategoriVarMi){
              MesajKutusu.mesajGoster("Bu Kategori Zaten Var");
             }
             else{
             File yaz = new File(tur + "Kategori.txt");
                if (!yaz.exists()) {
                    yaz.createNewFile();
                }
                FileWriter fWriter = new FileWriter(yaz, true);
                BufferedWriter bWriter = new BufferedWriter(fWriter);
                bWriter.append(kategori);
                bWriter.newLine();
                bWriter.close();
                MesajKutusu.mesajGoster("Kategori Eklendi");
         }
         }
     }
     }
}
