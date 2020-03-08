package iutbayonne.projet.zicall.EcriturePartitionPackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import iutbayonne.projet.zicall.R;

public class LigneAdapteur extends BaseAdapter {
    private Context context;
    private Partition partition;
    private List<Ligne> listeLignes;
    private LayoutInflater inflater;

    public LigneAdapteur(Context context, List<Ligne> listeLignes, Partition partition) {
        this.context = context;
        this.listeLignes = listeLignes;
        this.inflater = LayoutInflater.from(context);
        this.partition = partition;
    }

    @Override
    public int getCount() {
        return listeLignes.size();
    }

    @Override
    public Ligne getItem(int position) {
        return listeLignes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.adapteur_ligne_partition, null);

        //on récupère la ligne courante
        final Ligne ligneCourante = getItem(position);

        final NotePartition note1 = ligneCourante.getNote1();
        final NotePartition note2 = ligneCourante.getNote2();
        final NotePartition note3 = ligneCourante.getNote3();
        final NotePartition note4 = ligneCourante.getNote4();
        final NotePartition note5 = ligneCourante.getNote5();
        final NotePartition note6 = ligneCourante.getNote6();

        final ImageView imageNote1 = convertView.findViewById(R.id.note_1);
        final ImageView imageNote2 = convertView.findViewById(R.id.note_2);
        final ImageView imageNote3 = convertView.findViewById(R.id.note_3);
        final ImageView imageNote4 = convertView.findViewById(R.id.note_4);
        final ImageView imageNote5 = convertView.findViewById(R.id.note_5);
        final ImageView imageNote6 = convertView.findViewById(R.id.note_6);

        NotePartition[] notesDeLaLigne = new NotePartition[]{note1, note2, note3, note4, note5, note6};
        ImageView[] imagesDesNotesDeLaLigne = new ImageView[]{imageNote1, imageNote2, imageNote3, imageNote4, imageNote5, imageNote6};

        afficherToutesLesNotes(notesDeLaLigne, imagesDesNotesDeLaLigne);

        if (!partition.isWritting()){
            setOncliksImagesNotes(ligneCourante, notesDeLaLigne, imagesDesNotesDeLaLigne);
        }

        return convertView;
    }

    public void afficherNote(NotePartition note, ImageView image){
        image.setImageResource(note.getSourceImage().getSourceImage());
    }

    public void afficherToutesLesNotes(NotePartition[] notes, ImageView[] images){
        for(int i = 0; i < notes.length; i++){
            afficherNote(notes[i],images[i]);
        }
    }

    public void setOncliksImagesNotes(final Ligne ligne, final NotePartition[] notes, final ImageView[] images ){
        for (int i = 0; i < notes.length; i++) {
            final int finalI = i;
            images[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!partition.isWritting()){


                        if(notes[finalI].getSourceImage().getSourceImage() != R.drawable.partition_vierge){
                            final PopupChoixTypeNote popupChoixTypeNote = new PopupChoixTypeNote(context);
                            popupChoixTypeNote.build();

                            popupChoixTypeNote.getChoix_croche().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    GestionnaireTypeNotePartition gestionnaireTypeNotePartition =
                                            new GestionnaireTypeNotePartition(ligne.getSourceImageNote(notes[finalI]), "croche");
                                    ligne.setImageNote(notes[finalI], gestionnaireTypeNotePartition.getNouvelleSourceImageNote());//on modifie la source dans l'objet note
                                    images[finalI].setImageResource(ligne.getSourceImageNote(notes[finalI]).getSourceImage());//on met à jour la vue
                                    popupChoixTypeNote.dismiss();
                                }
                            });

                            popupChoixTypeNote.getChoix_noire().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    GestionnaireTypeNotePartition gestionnaireTypeNotePartition =
                                            new GestionnaireTypeNotePartition(ligne.getSourceImageNote(notes[finalI]), "noire");
                                    ligne.setImageNote(notes[finalI], gestionnaireTypeNotePartition.getNouvelleSourceImageNote());//on modifie la source dans l'objet note
                                    images[finalI].setImageResource(ligne.getSourceImageNote(notes[finalI]).getSourceImage());//on met à jour la vue
                                    popupChoixTypeNote.dismiss();
                                }
                            });

                            popupChoixTypeNote.getChoix_blanche().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    GestionnaireTypeNotePartition gestionnaireTypeNotePartition =
                                            new GestionnaireTypeNotePartition(ligne.getSourceImageNote(notes[finalI]), "blanche");
                                    ligne.setImageNote(notes[finalI], gestionnaireTypeNotePartition.getNouvelleSourceImageNote());//on modifie la source dans l'objet note
                                    images[finalI].setImageResource(ligne.getSourceImageNote(notes[finalI]).getSourceImage());//on met à jour la vue
                                    popupChoixTypeNote.dismiss();
                                }
                            });

                            popupChoixTypeNote.getChoix_ronde().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    GestionnaireTypeNotePartition gestionnaireTypeNotePartition =
                                            new GestionnaireTypeNotePartition(ligne.getSourceImageNote(notes[finalI]), "ronde");
                                    ligne.setImageNote(notes[finalI], gestionnaireTypeNotePartition.getNouvelleSourceImageNote());//on modifie la source dans l'objet note
                                    images[finalI].setImageResource(ligne.getSourceImageNote(notes[finalI]).getSourceImage());//on met à jour la vue
                                    popupChoixTypeNote.dismiss();
                                }
                            });
                        }
                    }
                }
            });
        }
    }
}
