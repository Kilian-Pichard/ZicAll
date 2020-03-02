package iutbayonne.projet.zicall.ApprentissageGuitarePackage;

import android.app.ActionBar;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import iutbayonne.projet.zicall.R;

public enum Chanson {
    BELLA_CIAO("Bella ciao", "Chant des partisans italiens \n interprété par Samuel et Thomas Nadal", R.raw.bella_ciao, new int[]{R.drawable.bella_ciao_1, R.drawable.bella_ciao_2, R.drawable.bella_ciao_3, R.drawable.bella_ciao_4, R.drawable.bella_ciao_5, R.drawable.bella_ciao_6});

    private String titreChanson;
    private String informationsSupplementaires;
    private int audioChanson;
    private int[] imagesParolesChanson;

    Chanson(String titreChanson, String informationsSupplementaires, int audioChanson, int[] imagesParolesChanson) {
        this.titreChanson = titreChanson;
        this.informationsSupplementaires = informationsSupplementaires;
        this.audioChanson = audioChanson;
        this.imagesParolesChanson = imagesParolesChanson;

    }

    public String getTitreChanson() {
        return titreChanson;
    }

    public void setTitreChanson(String titreChanson) {
        this.titreChanson = titreChanson;
    }

    public String getInformationsSupplementaires() {
        return informationsSupplementaires;
    }

    public void setInformationsSupplementaires(String informationsSupplementaires) {
        this.informationsSupplementaires = informationsSupplementaires;
    }

    public int getAudioChanson() {
        return audioChanson;
    }

    public void setAudioChanson(int audioChanson) {
        this.audioChanson = audioChanson;
    }

    public int[] getImagesParolesChanson() {
        return imagesParolesChanson;
    }

    public void setImagesParolesChanson(int[] imagesParolesChanson) {
        this.imagesParolesChanson = imagesParolesChanson;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void afficher(LinearLayout layout, Chanson chanson, Context context){
        afficherTitre(layout, chanson, context);
        afficherInfos(layout, chanson, context);
        afficherParoles(layout, chanson, context);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void afficherTitre(LinearLayout layout, Chanson chanson, Context context){
        TextView titre = new TextView(context);
        titre.setTextSize(30);
        titre.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        titre.setText(chanson.getTitreChanson());
        layout.addView(titre);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void afficherInfos(LinearLayout layout, Chanson chanson, Context context){
        TextView informations = new TextView(context);
        informations.setTextSize(10);
        informations.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        informations.setText(chanson.getInformationsSupplementaires());
        layout.addView(informations);
    }
    public void afficherParoles(LinearLayout layout, Chanson chanson, Context context){
        for (int imageCourante : chanson.getImagesParolesChanson()){
            ImageView nouvelleImage = new ImageView(context);
            ViewGroup.LayoutParams parametres = new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 550);
            nouvelleImage.setLayoutParams(parametres);
            nouvelleImage.setBackgroundResource(imageCourante);
            layout.addView(nouvelleImage);
        }
        ImageView nouvelleImage = new ImageView(context);
        ViewGroup.LayoutParams parametres = new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 200);
        nouvelleImage.setLayoutParams(parametres);
        nouvelleImage.setBackgroundResource(R.drawable.image_vide_paroles_chanson_pour_decalage_boutton);
        layout.addView(nouvelleImage);
    }
}
