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

    public NotePartition(SourceImageNotePartition sourceImage, int audioNote, double duree)
    {
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

    public int getAudioNote() {
        return audioNote;
    }

    public void setAudioNote(int audioNote) {
        this.audioNote = audioNote;
    }

    public double getDuree() {
        return duree;
    }

    public void setDuree(double duree) {
        this.duree = duree;
    }
}
