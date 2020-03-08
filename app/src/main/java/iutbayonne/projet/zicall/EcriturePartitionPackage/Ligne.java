package iutbayonne.projet.zicall.EcriturePartitionPackage;

public class Ligne {
    private NotePartition note1;
    private NotePartition note2;
    private NotePartition note3;
    private NotePartition note4;
    private NotePartition note5;
    private NotePartition note6;

    public Ligne(NotePartition note1, NotePartition note2, NotePartition note3, NotePartition note4, NotePartition note5, NotePartition note6) {
        this.note1 = note1;
        this.note2 = note2;
        this.note3 = note3;
        this.note4 = note4;
        this.note5 = note5;
        this.note6 = note6;
    }

    public NotePartition getNote1() {
        return note1;
    }

    public void setNote1(NotePartition note1) {
        this.note1 = note1;
    }

    public NotePartition getNote2() {
        return note2;
    }

    public void setNote2(NotePartition note2) {
        this.note2 = note2;
    }

    public NotePartition getNote3() {
        return note3;
    }

    public void setNote3(NotePartition note3) {
        this.note3 = note3;
    }

    public NotePartition getNote4() {
        return note4;
    }

    public void setNote4(NotePartition note4) {
        this.note4 = note4;
    }

    public NotePartition getNote5() {
        return note5;
    }

    public void setNote5(NotePartition note5) {
        this.note5 = note5;
    }

    public NotePartition getNote6() {
        return note6;
    }

    public void setNote6(NotePartition note6) {
        this.note6 = note6;
    }

    public void setImageNote(NotePartition note, SourceImageNotePartition sourceImage){
        note.setSourceimage(sourceImage);
    }

    public SourceImageNotePartition getSourceImageNote(NotePartition note){
        return note.getSourceImage();
    }

    public NotePartition getNoteViaIndex(int index){
        NotePartition noteARetourner;
        switch (index){
            case 0:
                noteARetourner = note1;
                break;
            case 1:
                noteARetourner = note2;
                break;
            case 2:
                noteARetourner = note3;
                break;
            case 3:
                noteARetourner = note4;
                break;
            case 4:
                noteARetourner = note5;
                break;
            default:
                noteARetourner = note6;
                break;
        }
        return noteARetourner;
    }
}
