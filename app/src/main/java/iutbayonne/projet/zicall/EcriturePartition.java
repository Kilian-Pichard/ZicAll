package iutbayonne.projet.zicall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import iutbayonne.projet.zicall.EcriturePartitionPackage.Ligne;
import iutbayonne.projet.zicall.EcriturePartitionPackage.NotePartition;
import iutbayonne.projet.zicall.EcriturePartitionPackage.Partition;
import iutbayonne.projet.zicall.EcriturePartitionPackage.SourceImageNotePartition;

import static iutbayonne.projet.zicall.EcriturePartitionPackage.SourceImageNotePartition.*;

public class EcriturePartition extends AppCompatActivity {

    private Partition partition;
    private ListView listeLignesPartition;
    private Button btnDemarrageStopEcriture;
    private Button btnDo1;
    private Button btnRe1;
    private Button btnMi1;
    private Button btnFa1;
    private Button btnSol1;
    private Button btnLa1;
    private Button btnSi1;
    private Button btnDo0;
    private Button btnRe0;
    private Button btnMi0;
    private Button btnFa0;
    private Button btnSol0;
    private Button btnLa0;
    private Button btnSi0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecriture_partition);

        partition = new Partition();
        listeLignesPartition = findViewById(R.id.partition_List_view);

        btnDemarrageStopEcriture = findViewById(R.id.btnDemarrageStopEcriture);

        configurerLesBouttons();




        desactiverBouttonsNotes();

        partition.afficher(listeLignesPartition, getApplicationContext());
    }

    public void demarrerArreterEcriture(View view) {
        //onclick sur le bouton demarrer/arreter
        if(partition.isWritting()){
            desactiverBouttonsNotes();
            btnDemarrageStopEcriture.setText("Demarrer écriture");
            partition.setWritting(false);
            partition.ajouterLigne(new Ligne(new NotePartition(PARTITION_VIERGER),
                            new NotePartition(PARTITION_VIERGER),
                            new NotePartition(PARTITION_VIERGER),
                            new NotePartition(PARTITION_VIERGER),
                            new NotePartition(PARTITION_VIERGER),
                            new NotePartition(PARTITION_VIERGER)
                    )
            );
            partition.supprimerLigne(partition.getLignes().get(partition.getIndiceLigneCourante()+1));//supprimer la dernière ligne
            partition.afficher(listeLignesPartition, this);
        }
        else{
            activerBouttonsNotes();
            partition.setWritting(true);
            btnDemarrageStopEcriture.setText("Arrêter écriture");
        }
    }

    public void configurerBouttonAjoutNote(Button bouton, int idBouton, final SourceImageNotePartition source){
        bouton = findViewById(idBouton);
        bouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partition.ajouterNote(new NotePartition(source), listeLignesPartition, getApplicationContext());
            }
        });
    }

    public void configurerLesBouttons(){

        btnDo1 = findViewById(R.id.btnDo1);
        btnDo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partition.ajouterNote(new NotePartition(DO_GAMME_1_NOIRE), listeLignesPartition, getApplicationContext());
            }
        });

        btnRe1 = findViewById(R.id.btnRe1);
        btnRe1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partition.ajouterNote(new NotePartition(RE_GAMME_1_NOIRE), listeLignesPartition, getApplicationContext());
            }
        });

        btnMi1 = findViewById(R.id.btnMi1);
        btnMi1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partition.ajouterNote(new NotePartition(MI_GAMME_1_NOIRE), listeLignesPartition, getApplicationContext());
            }
        });

        btnFa1 = findViewById(R.id.btnFa1);
        btnFa1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partition.ajouterNote(new NotePartition(FA_GAMME_1_NOIRE), listeLignesPartition, getApplicationContext());
            }
        });

        btnSol1 = findViewById(R.id.btnSol1);
        btnSol1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partition.ajouterNote(new NotePartition(SOL_GAMME_1_NOIRE), listeLignesPartition, getApplicationContext());
            }
        });

        btnLa1 = findViewById(R.id.btnLa1);
        btnLa1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partition.ajouterNote(new NotePartition(LA_GAMME_1_NOIRE), listeLignesPartition, getApplicationContext());
            }
        });

        btnSi1 = findViewById(R.id.btnSi1);
        btnSi1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partition.ajouterNote(new NotePartition(SI_GAMME_1_NOIRE), listeLignesPartition, getApplicationContext());
            }
        });

        //////////////////////

        btnDo0 = findViewById(R.id.btnDo0);
        btnDo0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partition.ajouterNote(new NotePartition(DO_GAMME_0_NOIRE), listeLignesPartition, getApplicationContext());
            }
        });

        btnRe0 = findViewById(R.id.btnRe0);
        btnRe0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partition.ajouterNote(new NotePartition(RE_GAMME_0_NOIRE), listeLignesPartition, getApplicationContext());
            }
        });

        btnMi0 = findViewById(R.id.btnMi0);
        btnMi0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partition.ajouterNote(new NotePartition(MI_GAMME_0_NOIRE), listeLignesPartition, getApplicationContext());
            }
        });

        btnFa0 = findViewById(R.id.btnFa0);
        btnFa0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partition.ajouterNote(new NotePartition(FA_GAMME_0_NOIRE), listeLignesPartition, getApplicationContext());
            }
        });

        btnSol0 = findViewById(R.id.btnSol0);
        btnSol0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partition.ajouterNote(new NotePartition(SOL_GAMME_0_NOIRE), listeLignesPartition, getApplicationContext());
            }
        });

        btnLa0 = findViewById(R.id.btnLa0);
        btnLa0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partition.ajouterNote(new NotePartition(LA_GAMME_0_NOIRE), listeLignesPartition, getApplicationContext());
            }
        });

        btnSi0 = findViewById(R.id.btnSi0);
        btnSi0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partition.ajouterNote(new NotePartition(SI_GAMME_0_NOIRE), listeLignesPartition, getApplicationContext());
            }
        });


        /*configurerBouttonAjoutNote(btnDo1, R.id.btnDo1, DO_GAMME_1_RONDE);
        configurerBouttonAjoutNote(btnRe1, R.id.btnRe1, RE_GAMME_1_RONDE);
        configurerBouttonAjoutNote(btnMi1, R.id.btnMi1, MI_GAMME_1_RONDE);
        configurerBouttonAjoutNote(btnFa1, R.id.btnFa1, FA_GAMME_1_RONDE);
        configurerBouttonAjoutNote(btnSol1, R.id.btnSol1, SOL_GAMME_1_RONDE);
        configurerBouttonAjoutNote(btnLa1, R.id.btnLa1, LA_GAMME_1_RONDE);
        configurerBouttonAjoutNote(btnSi1, R.id.btnSi1, SI_GAMME_1_RONDE);
        configurerBouttonAjoutNote(btnDo0, R.id.btnDo0, DO_GAMME_0_RONDE);
        configurerBouttonAjoutNote(btnRe0, R.id.btnRe0, RE_GAMME_0_RONDE);
        configurerBouttonAjoutNote(btnMi0, R.id.btnMi0, MI_GAMME_0_RONDE);
        configurerBouttonAjoutNote(btnFa0, R.id.btnFa0, FA_GAMME_0_RONDE);
        configurerBouttonAjoutNote(btnSol0, R.id.btnSol0, SOL_GAMME_0_RONDE);
        configurerBouttonAjoutNote(btnLa0, R.id.btnLa0, LA_GAMME_0_RONDE);
        configurerBouttonAjoutNote(btnSi0, R.id.btnSi0, SI_GAMME_0_RONDE);*/
    }

    public void desactiverBouttonsNotes(){
        btnDo1.setEnabled(false);
        btnRe1.setEnabled(false);
        btnMi1.setEnabled(false);
        btnFa1.setEnabled(false);
        btnSol1.setEnabled(false);
        btnLa1.setEnabled(false);
        btnSi1.setEnabled(false);
        btnDo0.setEnabled(false);
        btnRe0.setEnabled(false);
        btnMi0.setEnabled(false);
        btnFa0.setEnabled(false);
        btnSol0.setEnabled(false);
        btnLa0.setEnabled(false);
        btnSi0.setEnabled(false);
    }

    public void activerBouttonsNotes(){
        btnDo1.setEnabled(true);
        btnRe1.setEnabled(true);
        btnMi1.setEnabled(true);
        btnFa1.setEnabled(true);
        btnSol1.setEnabled(true);
        btnLa1.setEnabled(true);
        btnSi1.setEnabled(true);
        btnDo0.setEnabled(true);
        btnRe0.setEnabled(true);
        btnMi0.setEnabled(true);
        btnFa0.setEnabled(true);
        btnSol0.setEnabled(true);
        btnLa0.setEnabled(true);
        btnSi0.setEnabled(true);
    }


    public void toutesLesNotes(){
        partition.ajouterLigne(new Ligne(new NotePartition(DO_GAMME_1_NOIRE),
                        new NotePartition(RE_GAMME_1_NOIRE),
                        new NotePartition(MI_GAMME_1_NOIRE),
                        new NotePartition(FA_GAMME_1_NOIRE),
                        new NotePartition(SOL_GAMME_1_NOIRE),
                        new NotePartition(LA_GAMME_1_NOIRE)
                )

        );
        partition.ajouterLigne(new Ligne(new NotePartition(SI_GAMME_1_NOIRE),
                        new NotePartition(DO_DIESE_GAMME_1_NOIRE),
                        new NotePartition(RE_DIESE_GAMME_1_NOIRE),
                        new NotePartition(FA_DIESE_GAMME_1_NOIRE),
                        new NotePartition(SOL_DIESE_GAMME_1_NOIRE),
                        new NotePartition(LA_DIESE_GAMME_1_NOIRE)
                )

        );

        partition.ajouterLigne(new Ligne(new NotePartition(DO_GAMME_0_NOIRE),
                        new NotePartition(RE_GAMME_0_NOIRE),
                        new NotePartition(MI_GAMME_0_NOIRE),
                        new NotePartition(FA_GAMME_0_NOIRE),
                        new NotePartition(SOL_GAMME_0_NOIRE),
                        new NotePartition(LA_GAMME_0_NOIRE)
                )
        );
        partition.ajouterLigne(new Ligne(new NotePartition(SI_GAMME_0_NOIRE),
                        new NotePartition(DO_DIESE_GAMME_0_NOIRE),
                        new NotePartition(RE_DIESE_GAMME_0_NOIRE),
                        new NotePartition(FA_DIESE_GAMME_0_NOIRE),
                        new NotePartition(SOL_DIESE_GAMME_0_NOIRE),
                        new NotePartition(LA_DIESE_GAMME_0_NOIRE)
                )

        );
    }
}
