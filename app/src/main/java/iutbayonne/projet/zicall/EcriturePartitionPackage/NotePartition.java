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

    public NotePartition(String nomNote, SourceImageNotePartition sourceImage, int audioNote, double duree)
    {
        this.nomNote = nomNote;
        this.sourceImage = sourceImage;
        this.audioNote = audioNote;
        this.duree = duree;
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
        return audioNote;
    }

    public void setDuree(double duree) {
        this.duree = duree;
    }

    public double getDuree() {
        return duree;
    }

    public void setNomNote(String nomNote) { this.nomNote = nomNote;}

    public String getNomNote() {
        return nomNote;
    }
}
