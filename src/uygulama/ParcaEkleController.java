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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import siniflar.KamyonParcasi;
import siniflar.OtomobilParcasi;
import siniflar.ParcaTurleri;
import siniflar.TraktorParcasi;

/**
 *
 * @author hsynpsdmr
 */
public class ParcaEkleController {

    public TableView<ParcaTurleri> table;
    public ComboBox<String> parcaTuruBox;
    public ComboBox<String> kategoriBox;
    public TextField parcaAdiText;
    public TextField stokText;
    public TextField fiyatText;
    public Button kaydetBtn;
    public Button secBtn;
    String tur;
    Boolean ilkTiklama;

    @FXML
    public void initialize() throws FileNotFoundException {
        table.setEditable(true);
        parcaListele();
        kategoriBox.setVisible(false);
        parcaAdiText.setVisible(false);
        stokText.setVisible(false);
        fiyatText.setVisible(false);
        kaydetBtn.setVisible(false);
        ilkTiklama = true;
    }

    public void sec(ActionEvent event) throws IOException {
        tur = parcaTuruBox.getSelectionModel().getSelectedItem();
        if (tur != null) {
            kategorileriGuncelle(tur);
            if (ilkTiklama) {
                kategoriBox.setVisible(true);
                parcaAdiText.setVisible(true);
                kaydetBtn.setVisible(true);
                stokText.setVisible(true);
                fiyatText.setVisible(true);
                ilkTiklama = false;
            } else {
                MesajKutusu.mesajGoster("Kategoriler Güncellendi !");
            }
        }
        else{
            MesajKutusu.mesajGoster("Lütfen Parça Türü Seçiniz");
        }
    }

    public void kaydet(ActionEvent event) throws IOException {
        String kategori = kategoriBox.getSelectionModel().getSelectedItem();
        String parcaAdi = parcaAdiText.getText();
        String stok = stokText.getText();
        String fiyat = fiyatText.getText();
        if (kategori == null) {
            MesajKutusu.mesajGoster("Lütfen Kategori Alanını Doldurunuz");
        } else {
            if (parcaAdi.equalsIgnoreCase("") || stok.equalsIgnoreCase("")) {
                MesajKutusu.mesajGoster("Lütfen Parça Adı ve Stok Alanlarını Doldurunuz");
            } else {
                if (parcaVarMi(tur, kategori, parcaAdi)) {
                    MesajKutusu.mesajGoster("Bu Parça Zaten Kayıtlarda Var");
                } else {
                    try {
                        int stokInt = Integer.parseInt(stok);
                        double fiyatDouble = Double.parseDouble(fiyat);
                        File dosya = new File("Parçalar.txt");
                        if (!dosya.exists()) {
                            dosya.createNewFile();
                        }
                        FileWriter dYazici = new FileWriter(dosya, true);
                        BufferedWriter yazici = new BufferedWriter(dYazici);
                        yazici.newLine();
                        yazici.append("Parca :");
                        yazici.newLine();
                        yazici.append("TÜR");
                        yazici.newLine();
                        yazici.append(tur);
                        yazici.newLine();
                        yazici.append("KATEGORİ");
                        yazici.newLine();
                        yazici.append(kategori);
                        yazici.newLine();
                        yazici.append("AD");
                        yazici.newLine();
                        yazici.append(parcaAdi);
                        yazici.newLine();
                        yazici.append("STOK");
                        yazici.newLine();
                        yazici.append(Integer.toString(stokInt));
                        yazici.newLine();
                        yazici.append("FİYAT");
                        yazici.newLine();
                        yazici.append(Double.toString(fiyatDouble));
                        yazici.newLine();
                        yazici.append("***");
                        yazici.close();
                        MesajKutusu.mesajGoster("Parça Eklendi");

                    } catch (NumberFormatException a) {
                        MesajKutusu.mesajGoster("Hatalı Stok Sayısı !");
                    }
                }

            }
            parcaListele();

        }
    }

    public void parcaListele() throws FileNotFoundException {

        table.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("tur"));
        table.getColumns().get(1).setCellValueFactory(new PropertyValueFactory("kategori"));
        table.getColumns().get(2).setCellValueFactory(new PropertyValueFactory("parcaAdi"));
        table.getColumns().get(3).setCellValueFactory(new PropertyValueFactory("stok"));
        table.getColumns().get(4).setCellValueFactory(new PropertyValueFactory("fiyat"));
        
        ArrayList<ParcaTurleri> list = getParcaList();
        ObservableList data = FXCollections.observableList(list);
        table.setItems(data);
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

    private boolean parcaVarMi(String tur, String kategori, String parcaAdi) throws FileNotFoundException {
        ArrayList<ParcaTurleri> list = getParcaList();
        for (ParcaTurleri parca : list) {
            if (parca.getTur().equalsIgnoreCase(tur)
                    && parca.getKategori().equalsIgnoreCase(kategori)
                    && parca.getParcaAdi().equalsIgnoreCase(parcaAdi)) {
                return true;
            }
        }
        return false;
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
                        
                         if (oku.equalsIgnoreCase("FİYAT")) {
                            oku = br.readLine();
                            if (otomobilParcasi != null) {
                                otomobilParcasi.setFiyat(Double.parseDouble(oku));
                            }

                            if (kamyonParcasi != null) {
                                kamyonParcasi.setFiyat(Double.parseDouble(oku));
                            }

                            if (traktorParcasi != null) {
                                traktorParcasi.setFiyat(Double.parseDouble(oku));
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

}
