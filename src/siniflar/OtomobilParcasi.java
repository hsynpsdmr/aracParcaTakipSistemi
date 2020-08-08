/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siniflar;

/**
 *
 * @author Seyf
 */
public class OtomobilParcasi extends ParcaTurleri implements ParcaInterface {
    public OtomobilParcasi(){
        setTur("Otomobil");
    }
    
    @Override
   public String getTur(){
        return this.tur;
    }
   
     @Override
   public void setTur(String tur){
        this.tur = tur;
    }
}
