/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siniflar;

/**
 *
 * @author hsynpsdmr
 */
public class TraktorParcasi extends ParcaTurleri implements ParcaInterface {
   public TraktorParcasi(){
        setTur("Traktör");
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
