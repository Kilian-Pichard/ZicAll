package iutbayonne.projet.zicall.EcriturePartitionPackage;

public class NotePartition
{
    /**
     * Contient l'identifiant de l'image de la note.
     */
    private SourceImageNotePartition sourceImage;

    /**
     * Contient l'identifiant du fichier audio de la note.
     */
    private int audioNote;

    private double duree;

    private String nomNote;

    private String typeDeNote;

    public NotePartition(String nomNote, String typeDeNote,SourceImageNotePartition sourceImage, int audioNote, double duree)
    {
        this.nomNote = nomNote;
        this.sourceImage = sourceImage;
        this.audioNote = audioNote;
        this.duree = duree;
        this.typeDeNote = typeDeNote;
    }

    public NotePartition(SourceImageNotePartition sourceImage) {
        this.sourceImage = sourceImage;
    }

    public SourceImageNotePartition getSourceImage() {
        return sourceImage;
    }

    public void setSourceimage(SourceImageNotePartition sourceImage) {
        this.sourceImage = sourceImage;
    }

    public void setAudioNote(int audioNote) {
        this.audioNote = audioNote;
    }

    public int getAudioNote() {
        return this.audioNote;
    }

    public void setDuree(double duree) {
        this.duree = duree;
    }

    public double getDuree() {
        return this.duree;
    }

    public void setNomNote(String nomNote) { this.nomNote = nomNote;}

    public String getNomNote() {
        return this.nomNote + "_" + this.typeDeNote;
    }

    public void setTypeDeNote(String typeDeNote) { this.typeDeNote = typeDeNote;}

    public String getTypeDeNote() {
        return this.typeDeNote;
    }
}
