package iutbayonne.projet.zicall.EcriturePartitionPackage;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import static iutbayonne.projet.zicall.EcriturePartitionPackage.SourceImageNotePartition.PARTITION_VIERGER;

public class Partition {
    private List<Ligne> lignes;
    private List<NotePartition> notes;
    private int indiceLigneCourante;
    private int indiceNoteCourante;
    private boolean writting;

    public Partition(){
        lignes = new ArrayList<>();
        this.ajouterLigne(new Ligne(new NotePartition(PARTITION_VIERGER),
                        new NotePartition(PARTITION_VIERGER),
                        new NotePartition(PARTITION_VIERGER),
                        new NotePartition(PARTITION_VIERGER),
                        new NotePartition(PARTITION_VIERGER),
                        new NotePartition(PARTITION_VIERGER)
                )

        );

        notes = new ArrayList<>();
        indiceLigneCourante = 0;
        indiceNoteCourante = 0;
        writting = false;

    }

    public void afficher(ListView listView, Context context){
        listView.setAdapter(new LigneAdapteur(context, getLignes(), this));
    }

    public void ajouterLigne(Ligne ligne){
        getLignes().add(ligne);
    }

    public void supprimerLigne(Ligne ligne){
        getLignes().remove(ligne);
    }

    public void ajouterNote(NotePartition note,  ListView listView, Context context){
        getNotes().add(note); //ajout dans la liste de notes

        LigneAdapteur ligneAdapteur = new LigneAdapteur(context, getLignes(), this);
        ligneAdapteur.getView(getIndiceLigneCourante(), listView, (ViewGroup) listView.getParent());
        ajouterNoteALaLigne(getLigneCourante(), note, listView, context); //ajout sur les lignes

    }

    public void ajouterNoteALaLigne(Ligne ligne, NotePartition note, ListView listView, Context context){
        ligne.getNoteViaIndex(indiceNoteCourante).setSourceimage(note.getSourceImage());
        this.afficher(listView, context);
        passerALaNoteSuivante();
        listView.setSelection(this.getIndiceLigneCourante());
    }

    public void actualiserIndiceLigneCourante(){
        setIndiceLigneCourante(getLignes().size()-1);
    }

    public void incrementerIndiceNoteCourante(){
        setIndiceNoteCourante((getIndiceNoteCourante()+1)%6);//on a 6 notes par ligne
    }

    public void passerALaNoteSuivante(){
        incrementerIndiceNoteCourante();
        if(estEnFinDeLigne()){
            ajouterNouvelleLigneVierge();
        }
    }

    public void ajouterNouvelleLigneVierge(){
        ajouterLigne(new Ligne(new NotePartition(PARTITION_VIERGER),
                        new NotePartition(PARTITION_VIERGER),
                        new NotePartition(PARTITION_VIERGER),
                        new NotePartition(PARTITION_VIERGER),
                        new NotePartition(PARTITION_VIERGER),
                        new NotePartition(PARTITION_VIERGER)
                )
        );
        actualiserIndiceLigneCourante();
    }

    public Ligne getLigneCourante(){
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

    public boolean isWritting() {
        return writting;
    }

    public void setWritting(boolean writting) {
        this.writting = writting;
    }
}
