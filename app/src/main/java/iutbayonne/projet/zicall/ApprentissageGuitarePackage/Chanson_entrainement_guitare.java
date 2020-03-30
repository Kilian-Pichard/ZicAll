package iutbayonne.projet.zicall.ApprentissageGuitarePackage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import iutbayonne.projet.zicall.MainActivity;
import iutbayonne.projet.zicall.R;

public class Chanson_entrainement_guitare extends AppCompatActivity
{

    private ScrollView scrollView;
    private LinearLayout layoutAffichageChanson;
    private Chanson chanson;
    private Button btnStartPauseAudio;
    private MediaPlayer audioChanson;
    private AutoScroll autoScroll;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chanson_entrainement_guitare);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        this.scrollView = findViewById(R.id.scrollViewChanson);
        this.layoutAffichageChanson = findViewById(R.id.layoutAffichageChanson);

        this.chanson = Chanson.BELLA_CIAO;
        this.chanson.afficher(layoutAffichageChanson, chanson, this);
        this.btnStartPauseAudio = findViewById(R.id.playPauseAudio);
        this.audioChanson = MediaPlayer.create(getApplicationContext(), this.chanson.getAudioChanson());
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();

        this.scrollView.scrollTo(0,0);
        this.audioChanson = MediaPlayer.create(getApplicationContext(), this.chanson.getAudioChanson());
    }

    public void lancerAudioChanson(View view)
    {
        if(!audioChanson.isPlaying())
        {
            this.audioChanson.start();
            this.autoScroll = new AutoScroll();
            this.btnStartPauseAudio.setText("Pause");
        }
        else
        {
            this.audioChanson.pause();
            this.autoScroll.arreter();
            this.btnStartPauseAudio.setText("Play");
        }
    }

    public void accederAccueil()
    {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), MainActivity.class);
        // Vide la pile des activity
        otherActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        if(audioChanson.isPlaying())
        {
            audioChanson.stop();
            autoScroll.arreter();
        }
        audioChanson.release();
        audioChanson = null;

        startActivity(otherActivity);
        finish();
    }

    public class AutoScroll extends Thread
    {
        private boolean enMarche;

        public AutoScroll()
        {
            this.enMarche = true;
            start();
        }

        public void run()
        {
            while(enMarche && audioChanson.isPlaying())
            {
                try
                {
                    Thread.sleep(125);
                }
                catch(InterruptedException ie)
                {

                }
                scrollView.smoothScrollBy(0, 2);
            }
        }

        public void arreter()
        {
            this.enMarche = false;
            this.interrupt();
        }
    }

    @Override
    public void onBackPressed()
    {
        if(audioChanson != null)
        {
            if(audioChanson.isPlaying())
            {
                audioChanson.stop();
                autoScroll.arreter();
            }

            audioChanson.release();
            audioChanson = null;
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_touteslesactivites, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                accederAccueil();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
