package src;

/**
 * Array[9] as the structure for Notes
 */
public class Note {

    private static final int MAX_NOTE = 9;
    private boolean[] notes = new boolean[MAX_NOTE];
    private int noteCount = 0;

    /**
     * Initialize Note object
     */
    public Note(){
        // set all numbers to false
        for(int i = 0; i < MAX_NOTE; i++){
            notes[i] = false;
        }
    }

    /**
     * Check if n is already on the notes
     * @param n
     * @return
     */
    public boolean hasN(int n){
        return notes[n];
    }

    /**
     * Remove n from the notes
     * @param n
     */
    public void removeN(int n){
        notes[n] = false;
        noteCount--;
    }

    /**
     * Add n to the notes
     * @param n
     */
    public void addN(int n){
        notes[n] = true;
        noteCount++;
    }
}
