package iutbayonne.projet.zicall.EcriturePartitionPackage;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import static iutbayonne.projet.zicall.EcriturePartitionPackage.SourceImageNotePartition.PARTITION_VIERGE;

public class Partition
{
    /**
     * Liste des lignes qui composent la partition.
     */
    private List<Ligne> lignes;

    /**
     * Liste des notes qui composent la partition.
     */
    private List<NotePartition> notes;

    private int indiceLigneCourante;
    private int indiceNoteCourante;

    /**
     * Indique si la partition est en cours d'écriture.
     */
    private boolean writting;

    private boolean enCoursDeModification;

    public Partition()
    {
        lignes = new ArrayList<>();
        this.ajouterLigne(new Ligne(new NotePartition(PARTITION_VIERGE),
                        new NotePartition(PARTITION_VIERGE),
                        new NotePartition(PARTITION_VIERGE),
                        new NotePartition(PARTITION_VIERGE),
                        new NotePartition(PARTITION_VIERGE),
                        new NotePartition(PARTITION_VIERGE)
                )
        );

        notes = new ArrayList<>();
        indiceLigneCourante = 0;
        indiceNoteCourante = 0;
        writting = false;
        enCoursDeModification = false;

    }

    public void afficher(ListView listView, Context context)
    {
        listView.setAdapter(new LigneAdapteur(context, getLignes(), this));
    }

    public void ajouterLigne(Ligne ligne)
    {
        getLignes().add(ligne);
    }

    public void supprimerLigne(Ligne ligne)
    {
        getLignes().remove(ligne);
    }

    /**
     * Permet d'ajouter une note à la partition et de l'afficher.
     */
    public void ajouterNote(NotePartition note,  ListView listView, Context context)
    {
        getNotes().add(note); //ajout dans la liste de notes

        LigneAdapteur ligneAdapteur = new LigneAdapteur(context, getLignes(), this);
        ligneAdapteur.getView(getIndiceLigneCourante(), listView, (ViewGroup) listView.getParent());
        ajouterNoteALaLigne(getLigneCourante(), note, listView, context); //ajout sur les lignes
    }

    /**
     * Gère l'arrivée en bout de ligne lors de l'écriture de partition.
     */
    public void ajouterNoteALaLigne(Ligne ligne, NotePartition note, ListView listView, Context context)
    {
        ligne.getNoteViaIndex(indiceNoteCourante).setSourceimage(note.getSourceImage());
        this.afficher(listView, context);
        passerALaNoteSuivante();
        listView.setSelection(this.getIndiceLigneCourante());
    }

    public void actualiserIndiceLigneCourante()
    {
        setIndiceLigneCourante(getLignes().size()-1);
    }

    public void incrementerIndiceNoteCourante()
    {
        setIndiceNoteCourante((getIndiceNoteCourante()+1)%6);//on a 6 notes par ligne
    }

    public void passerALaNoteSuivante()
    {
        incrementerIndiceNoteCourante();
        if(estEnFinDeLigne())
        {
            ajouterNouvelleLigneVierge();
        }
    }

    public void ajouterNouvelleLigneVierge()
    {
        ajouterLigne(new Ligne(new NotePartition(PARTITION_VIERGE),
                        new NotePartition(PARTITION_VIERGE),
                        new NotePartition(PARTITION_VIERGE),
                        new NotePartition(PARTITION_VIERGE),
                        new NotePartition(PARTITION_VIERGE),
                        new NotePartition(PARTITION_VIERGE)
                )
        );
        actualiserIndiceLigneCourante();
    }

    public Ligne getLigneCourante()
    {
        return getLignes().get(indiceLigneCourante);
    }

    public boolean estEnFinDeLigne(){
        return getIndiceNoteCourante()==0;
    }

    //getters & setters
    public List<Ligne> getLignes() {
        return lignes;
    }

    public void setLignes(List<Ligne> lignes) {
        this.lignes = lignes;
    }

    public List<NotePartition> getNotes() {
        return notes;
    }

    public void setNotes(List<NotePartition> notes) {
        this.notes = notes;
    }

    public int getIndiceLigneCourante() {
        return indiceLigneCourante;
    }

    public void setIndiceLigneCourante(int indiceLigneCourante) {
        this.indiceLigneCourante = indiceLigneCourante;
    }

    public int getIndiceNoteCourante() {
        return indiceNoteCourante;
    }

    public void setIndiceNoteCourante(int indiceNoteCourante) {
        this.indiceNoteCourante = indiceNoteCourante;
    }

    public void initialiserModificationPartition(){
        this.setIndiceLigneCourante(0);
        this.setIndiceNoteCourante(0);
    }

    public boolean isWritting() {
        return writting;
    }

    public void setWritting(boolean writting) {
        this.writting = writting;
    }

    public boolean isEnCoursDeModification() {
        return enCoursDeModification;
    }

    public void setEnCoursDeModification(boolean enCoursDeModification) {
        this.enCoursDeModification = enCoursDeModification;
    }
}
