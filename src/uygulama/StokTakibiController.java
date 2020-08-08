/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uygulama;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import siniflar.KamyonParcasi;
import siniflar.OtomobilParcasi;
import siniflar.ParcaTurleri;
import siniflar.TraktorParcasi;

/**
 *
 * @author Seyf
 */
public class StokTakibiController {

    String tur;
    String kategori;
    String parcaAdi;
    public ComboBox<String> parcaTuruBox;
    public ComboBox<String> kategoriBox;
    public TextField parcaAdiText;
    public TextField yeniStokText;
    public Label stokLabel;
    public Button goruntuleBtn;
    public Button guncelleBtn;
    public Button secBtn;
    Boolean ilkTiklama;

    @FXML
    public void initialize() throws FileNotFoundException {
        kategoriBox.setVisible(false);
        parcaAdiText.setVisible(false);
        yeniStokText.setVisible(false);
        stokLabel.setVisible(false);
        goruntuleBtn.setVisible(false);
        guncelleBtn.setVisible(false);
        ilkTiklama = true;
    }

    public void turSec(ActionEvent event) throws IOException {
        tur = parcaTuruBox.getSelectionModel().getSelectedItem();
        if (tur != null) {
            kategorileriGuncelle(tur);
            if (ilkTiklama) {
                kategoriBox.setVisible(true);
                parcaAdiText.setVisible(true);
                goruntuleBtn.setVisible(true);
                ilkTiklama = false;
            } else {
                MesajKutusu.mesajGoster("Kategoriler Güncellendi !");
            }
        }
        else{
            MesajKutusu.mesajGoster("Lütfen Parça Türü Seçiniz");
        }
    }

    public void goruntule(ActionEvent event) throws IOException {
        kategori = kategoriBox.getSelectionModel().getSelectedItem();
        parcaAdi = parcaAdiText.getText();
        if (kategori == null) {
            MesajKutusu.mesajGoster("Lütfen Kategori Alanını Doldurunuz");
        } else {
            if (parcaAdi.equalsIgnoreCase("")) {
                MesajKutusu.mesajGoster("Lütfen Parça Adı Alanını Doldurunuz");
            } else {
                ParcaTurleri parca = parcaBul(tur, kategori, parcaAdi);
                if (parca != null) {
                    stokLabel.setVisible(true);
                    guncelleBtn.setVisible(true);
                    yeniStokText.setVisible(true);
                    stokLabel.setText("Stok Durumu : " + parca.getStok());
                } else {
                    MesajKutusu.mesajGoster("Aradığınız Parça Kayıtlarda Bulunamadı");
                }
            }

        }
    }

    public void stokGuncelle(ActionEvent event) throws IOException {
        String yeniStok = yeniStokText.getText();
        if (yeniStok.equalsIgnoreCase("")) {
            MesajKutusu.mesajGoster("Lütfen Yeni Stok Alanını Doldurunuz");
        } else {
            try {
                int stok = Integer.parseInt(yeniStok);
                ArrayList<ParcaTurleri> list = getParcaList();
                int parcaIndex = indexBul(tur, kategori, parcaAdi);
                list.get(parcaIndex).setStok(stok);

                File dosya = new File("Parçalar.txt");
                if (!dosya.exists()) {
                    dosya.createNewFile();
                }

                FileWriter bosDYazici = new FileWriter(dosya);
                BufferedWriter bosYazici = new BufferedWriter(bosDYazici);
                bosYazici.write("");
                bosYazici.close();

                FileWriter dYazici = new FileWriter(dosya, true);
                BufferedWriter yazici = new BufferedWriter(dYazici);
                yazici.write("");
                yazici.append("***");

                for (ParcaTurleri parca : list) {
                    yazici.newLine();
                    yazici.append("Parca :");
                    yazici.newLine();
                    yazici.append("TÜR");
                    yazici.newLine();
                    yazici.append(parca.getTur());
                    yazici.newLine();
                    yazici.append("KATEGORİ");
                    yazici.newLine();
                    yazici.append(parca.getKategori());
                    yazici.newLine();
                    yazici.append("AD");
                    yazici.newLine();
                    yazici.append(parca.getParcaAdi());
                    yazici.newLine();
                    yazici.append("STOK");
                    yazici.newLine();
                    yazici.append(Integer.toString(parca.getStok()));
                    yazici.newLine();
                    yazici.append("***");
                }
                yazici.close();
                MesajKutusu.mesajGoster("Stok Güncellendi");
                stokLabel.setText("Yeni Stok Durumu : " + yeniStok);

            } catch (NumberFormatException e) {
                MesajKutusu.mesajGoster("Geçersiz Stok Sayısı");
            }
        }
    }

    private void kategorileriGuncelle(String tur) throws IOException {
        ArrayList<String> list = new ArrayList<String>();

        File file = new File(tur + "Kategori.txt");
        FileReader fileReader = new FileReader(file);
        BufferedReader br = new BufferedReader(fileReader);
        String oku;

        while ((oku = br.readLine()) != null) {
            list.add(oku);
        }

        ObservableList oList = FXCollections.observableList(list);
        kategoriBox.setItems(oList);
    }

    private ArrayList<ParcaTurleri> getParcaList() throws FileNotFoundException {
        ArrayList<ParcaTurleri> list = new ArrayList<ParcaTurleri>();
        File file = new File("Parçalar.txt");
        FileReader fileReader = new FileReader(file);
        BufferedReader br = new BufferedReader(fileReader);
        String oku;

        try {
            while ((oku = br.readLine()) != null) {
                if (oku.equalsIgnoreCase("Parca :")) {
                    OtomobilParcasi otomobilParcasi = null;
                    KamyonParcasi kamyonParcasi = null;
                    TraktorParcasi traktorParcasi = null;

                    while (oku != null && !oku.equalsIgnoreCase("***")) {
                        if (oku.equalsIgnoreCase("TÜR")) {
                            oku = br.readLine();
                            if (oku.equalsIgnoreCase("Otomobil")) {
                                otomobilParcasi = new OtomobilParcasi();
                            }
                            if (oku.equalsIgnoreCase("Kamyon")) {
                                kamyonParcasi = new KamyonParcasi();
                            }
                            if (oku.equalsIgnoreCase("Traktör")) {
                                traktorParcasi = new TraktorParcasi();
                            }
                        }
                        if (oku.equalsIgnoreCase("KATEGORİ")) {
                            oku = br.readLine();
                            if (otomobilParcasi != null) {
                                otomobilParcasi.setKategori(oku);
                            }

                            if (kamyonParcasi != null) {
                                kamyonParcasi.setKategori(oku);
                            }

                            if (traktorParcasi != null) {
                                traktorParcasi.setKategori(oku);
                            }
                        }
                        if (oku.equalsIgnoreCase("AD")) {
                            oku = br.readLine();
                            if (otomobilParcasi != null) {
                                otomobilParcasi.setParcaAdi(oku);
                            }

                            if (kamyonParcasi != null) {
                                kamyonParcasi.setParcaAdi(oku);
                            }

                            if (traktorParcasi != null) {
                                traktorParcasi.setParcaAdi(oku);
                            }
                        }

                        if (oku.equalsIgnoreCase("STOK")) {
                            oku = br.readLine();
                            if (otomobilParcasi != null) {
                                otomobilParcasi.setStok(Integer.parseInt(oku));
                            }

                            if (kamyonParcasi != null) {
                                kamyonParcasi.setStok(Integer.parseInt(oku));
                            }

                            if (traktorParcasi != null) {
                                traktorParcasi.setStok(Integer.parseInt(oku));
                            }
                        }

                        oku = br.readLine();
                    }
                    if (otomobilParcasi != null) {
                        list.add(otomobilParcasi);
                    }

                    if (kamyonParcasi != null) {
                        list.add(kamyonParcasi);
                    }

                    if (traktorParcasi != null) {
                        list.add(traktorParcasi);
                    }

                }
            }

        } catch (IOException | NumberFormatException ex) {
            MesajKutusu.mesajGoster("Hata !");

        }
        return list;
    }

    private ParcaTurleri parcaBul(String tur, String kategori, String parcaAdi) throws FileNotFoundException {
        ArrayList<ParcaTurleri> list = getParcaList();
        for (ParcaTurleri parca : list) {
            if (parca.getTur().equalsIgnoreCase(tur)
                    && parca.getKategori().equalsIgnoreCase(kategori)
                    && parca.getParcaAdi().equalsIgnoreCase(parcaAdi)) {
                return parca;
            }
        }
        return null;
    }
    
    // Overload
     private ParcaTurleri parcaBul(String tur, String kategori, String parcaAdi, int fiyat) throws FileNotFoundException {
        ArrayList<ParcaTurleri> list = getParcaList();
        for (ParcaTurleri parca : list) {
            if (parca.getTur().equalsIgnoreCase(tur)
                    && parca.getKategori().equalsIgnoreCase(kategori)
                    && parca.getParcaAdi().equalsIgnoreCase(parcaAdi)
                    && parca.getFiyat() == fiyat) {
                return parca;
            }
        }
        return null;
    }

    private int indexBul(String tur, String kategori, String parcaAdi) throws FileNotFoundException {
        ArrayList<ParcaTurleri> list = getParcaList();
        for (ParcaTurleri parca : list) {
            if (parca.getTur().equalsIgnoreCase(tur)
                    && parca.getKategori().equalsIgnoreCase(kategori)
                    && parca.getParcaAdi().equalsIgnoreCase(parcaAdi)) {
                return list.indexOf(parca);
            }
        }
        return -1;
    }
    // Overload
    private int indexBul(String tur, String kategori, String parcaAdi, int fiyat) throws FileNotFoundException {
        ArrayList<ParcaTurleri> list = getParcaList();
        for (ParcaTurleri parca : list) {
            if (parca.getTur().equalsIgnoreCase(tur)
                    && parca.getKategori().equalsIgnoreCase(kategori)
                    && parca.getParcaAdi().equalsIgnoreCase(parcaAdi)
                    && parca.getFiyat() == fiyat) {
                return list.indexOf(parca);
            }
        }
        return -1;
    }
}
