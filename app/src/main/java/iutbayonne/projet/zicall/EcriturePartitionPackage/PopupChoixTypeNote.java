package iutbayonne.projet.zicall.EcriturePartitionPackage;

import android.app.Dialog;
import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import iutbayonne.projet.zicall.R;

public class PopupChoixTypeNote extends Dialog {

    private ImageView choix_croche;
    private ImageView choix_noire;
    private ImageView choix_blanche;
    private ImageView choix_ronde;

    public PopupChoixTypeNote(@NonNull Context context) {
        super(context, R.style.Theme_AppCompat_DayNight);
        setContentView(R.layout.popup_choix_type_note);

        this.choix_croche = findViewById(R.id.choix_croche);
        this.choix_noire = findViewById(R.id.choix_noire);
        this.choix_blanche = findViewById(R.id.choix_blanche);
        this.choix_ronde = findViewById(R.id.choix_ronde);
    }

    public void build(){
        show();
    }

    public ImageView getChoix_croche() {
        return choix_croche;
    }

    public ImageView getChoix_noire() {
        return choix_noire;
    }

    public ImageView getChoix_blanche() {
        return choix_blanche;
    }

    public ImageView getChoix_ronde() {
        return choix_ronde;
    }
}
