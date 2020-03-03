package iutbayonne.projet.zicall.ApprentissageGuitarePackage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import iutbayonne.projet.zicall.R;

public class Chanson_entrainement_guitare extends AppCompatActivity {

    private LinearLayout layoutAffichageChanson;
    private Chanson chanson;
    private Button btnStartPauseAudio;
    private MediaPlayer audioChanson;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chanson_entrainement_guitare);

       this.layoutAffichageChanson = findViewById(R.id.layoutAffichageChanson);
       this.chanson = Chanson.BELLA_CIAO;
       chanson.afficher(layoutAffichageChanson, chanson, this);
       this.btnStartPauseAudio = findViewById(R.id.playPauseAudio);
       audioChanson = MediaPlayer.create(getApplicationContext(), this.chanson.getAudioChanson());
    }

    public void accederAuRecapitulatifDesAccords(View view) {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), Recapitulatif_des_accords.class);
        startActivity(otherActivity);
        if(audioChanson.isPlaying()){
            audioChanson.pause();
            this.btnStartPauseAudio.setText("Play");
        }
    }

    public void choisirAutreChanson(View view){
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), Choix_chanson_entrainement_guitare.class);
        startActivity(otherActivity);
        if(audioChanson.isPlaying()){audioChanson.stop();}
        finish();
    }

    public void lancerAudioChanson(View view) {
        if(!audioChanson.isPlaying()){
            audioChanson.start();
            this.btnStartPauseAudio.setText("Pause");
        }
        else {
            audioChanson.pause();
            this.btnStartPauseAudio.setText("Play");
        }
    }
}