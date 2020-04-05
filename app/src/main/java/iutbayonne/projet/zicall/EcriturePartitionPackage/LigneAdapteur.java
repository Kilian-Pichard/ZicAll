package iutbayonne.projet.zicall.EcriturePartitionPackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import java.util.List;
import iutbayonne.projet.zicall.R;

public class LigneAdapteur extends BaseAdapter
{
    /**
     * Instance de l'activité à laquelle l'objet LigneAdapteur appartient.
     */
    private Context context;

    private Partition partition;

    /**
     * Liste des lignes dont on souhaite configurer l'affichage.
     */
    private List<Ligne> listeLignes;

    /**
     * Permet de faire le lien entre l'objet LigneAdapteur et le layout d'une ligne.
     */
    private LayoutInflater inflater;

    public LigneAdapteur(Context context, List<Ligne> listeLignes, Partition partition)
    {
        this.context = context;
        this.listeLignes = listeLignes;
        this.inflater = LayoutInflater.from(context);
        this.partition = partition;
    }

    /**
     * @return Retourne le nombre de lignes à adapter.
     */
    @Override
    public int getCount()
    {
        return listeLignes.size();
    }

    /**
     * @return Retourn la ligne dont la position est passée en paramètre.
     */
    @Override
    public Ligne getItem(int position) {
        return listeLignes.get(position);
    }

    /**
     * Méthode générée automatiquement. Elle nous est inutile mais ne peut pas être supprimée car elle implémente
     * une méthode abstraite de la classe mère.
     */
    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    /**
     * @return Retourne la vue générée par le LigneAdapteur.
     */
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
            setOnClicksImagesNotes(ligneCourante, notesDeLaLigne, imagesDesNotesDeLaLigne, position, context);
        }

        return convertView;
    }

    public void afficherNote(NotePartition note, ImageView image)
    {
        image.setImageResource(note.getSourceImage().getSourceImage());
    }

    public void afficherToutesLesNotes(NotePartition[] notes, ImageView[] images)
    {
        for(int i = 0; i < notes.length; i++)
        {
            afficherNote(notes[i],images[i]);
        }
    }

    /**
     * Rends les notes de la ligne cliquables pour pouvoir les modifier.
     */
    public void setOnClicksImagesNotes(final Ligne ligne, final NotePartition[] notes, final ImageView[] images, final int position, final Context context)
    {
        for (int i = 0; i < notes.length; i++) {
            final int finalI = i;
            images[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(notes[finalI].getSourceImage().getSourceImage() != R.drawable.partition_vierge){
                        partition.setEnCoursDeModification(true);
                        //éviter qu'une note sélectionnée pour modification et non modifiée garde l'image de modification
                        afficherToutesLesNotes(notes, images);
                                //.getLigneCourante().setImageNote(partition.getLigneCourante().getNoteViaIndex(partition.getIndiceNoteCourante()), partition.getLigneCourante().getNoteViaIndex(partition.getIndiceNoteCourante()).getSourceImage());

                        partition.setIndiceLigneCourante(position);
                        partition.setIndiceNoteCourante(finalI);

                        //définir image de la note courante sur "note en cours de modifaction"
                        images[finalI].setImageResource(R.drawable.image_note_en_cours_de_modification);//on met à jour la vue
                    }
                }
            });
        }
    }
}
