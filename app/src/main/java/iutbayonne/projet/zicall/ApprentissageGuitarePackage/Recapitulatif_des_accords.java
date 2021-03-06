package iutbayonne.projet.zicall.ApprentissageGuitarePackage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import iutbayonne.projet.zicall.MainActivity;
import iutbayonne.projet.zicall.R;

public class Recapitulatif_des_accords extends AppCompatActivity {

    /**
     * Tableau des différents accords.
     */
    private Accord[] accords;

    private Accord accordCourant;
    private int indiceAccord;

    /**
     * Lecteur qui permet de jouer l'accord sélectionné.
     */
    private MediaPlayer audioAccordCourant;

    /**
     * Modifie le son de l'accord lorsqu'on slide vers une image.
     */
    private ViewPager mSlideViewPager;

    /**
     * Permet d'initialiser les images pour slider de gauche à droite.
     */
    private SliderAdapter sliderAdapter;

    /**
     * Bouton qui affiche le popup
     */
    private CardView cv_accord;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recapitulatif_des_accords);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Zic'All</font>"));

        mSlideViewPager = findViewById(R.id.viewpager);
        sliderAdapter = new SliderAdapter(this);

        mSlideViewPager.setAdapter(sliderAdapter);
        mSlideViewPager.addOnPageChangeListener(viewListener);

        cv_accord = findViewById(R.id.accordselectioncardview);

        cv_accord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proposerChoixAccord(v);
            }
        });

        initialiserTableauAccords();
        changerAccord(0);
    }

    /**
     * Méthode qui s'exécute lorsque l'on retourne sur la page notamment lorsqu'on appuie sur le bouton retour.
     * Surchargée afin d'arrêter proprement les Thread de l'activity en cours avant de la quitter.
     */
    @Override
    protected void onRestart()
    {
        super.onRestart();

        changerAccord(0);
    }

    public void initialiserTableauAccords()
    {
        this.accords = new Accord[]{ Accord.DO,
                                Accord.DO_MINEUR,
                                Accord.DO_DIESE,
                                Accord.DO_DIESE_MINEUR,
                                Accord.RE,
                                Accord.RE_MINEUR,
                                Accord.RE_DIESE,
                                Accord.RE_DIESE_MINEUR,
                                Accord.MI,
                                Accord.MI_MINEUR,
                                Accord.FA,
                                Accord.FA_MINEUR,
                                Accord.FA_DIESE,
                                Accord.FA_DIESE_MINEUR,
                                Accord.SOL,
                                Accord.SOL_MINEUR,
                                Accord.SOL_DIESE,
                                Accord.SOL_DIESE_MINEUR,
                                Accord.LA,
                                Accord.LA_MINEUR,
                                Accord.LA_DIESE,
                                Accord.LA_DIESE_MINEUR,
                                Accord.SI,
                                Accord.SI_MINEUR,
                                Accord.AIDE_LECTURE_TABLATURE};
    }

    public void jouerSonAccord(View view)
    {
        this.audioAccordCourant.start();
    }

    public void changerAccord(int nouvelIndiceAccord)
    {
        if (nouvelIndiceAccord != 0)
        {
            if(this.audioAccordCourant.isPlaying())
            {
                this.audioAccordCourant.stop();
            }
            this.audioAccordCourant.release();
            this.audioAccordCourant = null;
        }

        this.indiceAccord = nouvelIndiceAccord;
        this.accordCourant = accords[indiceAccord];
        this.audioAccordCourant = MediaPlayer.create(getApplicationContext(), accordCourant.getAudioAccord());
    }

    public void accederChoixChansonEntrainement(View view)
    {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), Choix_chanson_entrainement_guitare.class);

        if(this.audioAccordCourant.isPlaying())
        {
            this.audioAccordCourant.stop();
        }
        this.audioAccordCourant.release();
        this.audioAccordCourant = null;

        startActivity(otherActivity);
    }

    /**
     * Modifie le son de l'accord lorsqu'on slide vers une image.
     */
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {

        /**
         * Méthode générée automatiquement. Elle nous est inutile mais ne peut pas être supprimée car elle implémente
         * une méthode abstraite de la classe mère.
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * Change l'accord lorsqu'on slide vers une image.
         */
        @Override
        public void onPageSelected(int position) {
            changerAccord(position);
        }

        /**
         * Méthode générée automatiquement. Elle nous est inutile mais ne peut pas être supprimée car elle implémente
         * une méthode abstraite de la classe mère.
         */
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void proposerChoixAccord(View v)
    {
        PopupChoixAccord popupChoixAccord = new PopupChoixAccord(this);
        initPopupChoixAccord(popupChoixAccord);
        popupChoixAccord.build();
    }

    public void initPopupChoixAccord(final PopupChoixAccord unPopup){
        unPopup.doMajeur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(0);
                mSlideViewPager.setCurrentItem(0);
                unPopup.dismiss();
            }
        });

        unPopup.doMineur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(1);
                mSlideViewPager.setCurrentItem(1);
                unPopup.dismiss();
            }
        });

        unPopup.doDiese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(2);
                mSlideViewPager.setCurrentItem(2);
                unPopup.dismiss();
            }
        });

        unPopup.doMineurDiese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(3);
                mSlideViewPager.setCurrentItem(3);
                unPopup.dismiss();
            }
        });

        unPopup.re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(4);
                mSlideViewPager.setCurrentItem(4);
                unPopup.dismiss();
            }
        });

        unPopup.reMineur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(5);
                mSlideViewPager.setCurrentItem(5);
                unPopup.dismiss();
            }
        });

        unPopup.reDiese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(6);
                mSlideViewPager.setCurrentItem(6);
                unPopup.dismiss();
            }
        });

        unPopup.reMineurDiese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(7);
                mSlideViewPager.setCurrentItem(7);
                unPopup.dismiss();
            }
        });

        unPopup.mi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(8);
                mSlideViewPager.setCurrentItem(8);
                unPopup.dismiss();
            }
        });

        unPopup.miMineur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(9);
                mSlideViewPager.setCurrentItem(9);
                unPopup.dismiss();
            }
        });

        unPopup.fa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(10);
                mSlideViewPager.setCurrentItem(10);
                unPopup.dismiss();
            }
        });

        unPopup.faMineur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(11);
                mSlideViewPager.setCurrentItem(11);
                unPopup.dismiss();
            }
        });

        unPopup.faDiese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(12);
                mSlideViewPager.setCurrentItem(12);
                unPopup.dismiss();
            }
        });

        unPopup.faMineurDiese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(13);
                mSlideViewPager.setCurrentItem(13);
                unPopup.dismiss();
            }
        });

        unPopup.sol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(14);
                mSlideViewPager.setCurrentItem(14);
                unPopup.dismiss();
            }
        });

        unPopup.solMineur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(15);
                mSlideViewPager.setCurrentItem(15);
                unPopup.dismiss();
            }
        });

        unPopup.solDiese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(16);
                mSlideViewPager.setCurrentItem(16);
                unPopup.dismiss();
            }
        });

        unPopup.solMineurDiese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(17);
                mSlideViewPager.setCurrentItem(17);
                unPopup.dismiss();
            }
        });

        unPopup.la.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(18);
                mSlideViewPager.setCurrentItem(18);
                unPopup.dismiss();
            }
        });

        unPopup.laMineur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(19);
                mSlideViewPager.setCurrentItem(19);
                unPopup.dismiss();
            }
        });

        unPopup.laDiese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(20);
                mSlideViewPager.setCurrentItem(20);
                unPopup.dismiss();
            }
        });

        unPopup.laMineurDiese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(21);
                mSlideViewPager.setCurrentItem(21);
                unPopup.dismiss();
            }
        });

        unPopup.si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(22);
                mSlideViewPager.setCurrentItem(22);
                unPopup.dismiss();
            }
        });

        unPopup.siMineur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(23);
                mSlideViewPager.setCurrentItem(23);
                unPopup.dismiss();
            }
        });

        unPopup.aide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(24);
                mSlideViewPager.setCurrentItem(24);
                unPopup.dismiss();
            }
        });
    }

    public void accederAccueil()
    {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), MainActivity.class);
        // Vide la pile des activity
        otherActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        if(this.audioAccordCourant.isPlaying())
        {
            this.audioAccordCourant.stop();
        }
        this.audioAccordCourant.release();
        this.audioAccordCourant = null;

        startActivity(otherActivity);
        finish();
    }

    /**
     * Méthode qui s'exécute lorsque l'on appuye sur le bouton "Retour" du smartphone.
     * Surchargée afin d'arrêter proprement les Thread de l'activity en cours avant de la quitter.
     */
    @Override
    public void onBackPressed()
    {
        if(this.audioAccordCourant != null)
        {
            if(this.audioAccordCourant.isPlaying())
            {
                this.audioAccordCourant.stop();
            }
            this.audioAccordCourant.release();
            this.audioAccordCourant = null;
        }

        super.onBackPressed();
    }

    /**
     * Récupère l'interface correspondante à la toolbar désirée et l'affiche.
     */
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_touteslesactivites, menu);
        return true;
    }

    /**
     * Associe chaque bouton de la toolbar à une action.
     */
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

    /**
     * Permet d'initialiser les images pour slider de gauche à droite.
     */
    public class SliderAdapter extends PagerAdapter {

        /**
         * Instance de l'activité à laquelle l'objet SliderAdapter appartient.
         */
        Context context;

        /**
         * Permet de faire le lien entre l'objet SliderAdapter et le layout de l'affichage des accords.
         */
        LayoutInflater layoutInflater;

        public SliderAdapter(Context context)
        {
            this.context = context;
        }

        /**
         * @return Retourne le nombre d'éléments du slider.
         */
        @Override
        public int getCount() {
            return Accord.getCount();
        }

        /**
         * Méthode générée automatiquement. Elle nous est inutile mais ne peut pas être supprimée car elle implémente
         * une méthode abstraite de la classe mère.
         */
        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        /**
         * Initialise les images lorsqu'on slide vers une autre image.
         */
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.slider_layout, container, false);

            ImageView slideImageView = view.findViewById(R.id.imageAccordCourant);
            slideImageView.setImageResource(accords[position].getImageAccord());

            container.addView(view);

            return view;
        }

        /**
         * Détruit l'image lorsqu'on à slider vers une autre image
         */
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
