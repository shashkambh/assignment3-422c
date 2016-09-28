/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Shashank Kambhampati
 * skk834
 * 16445 
 * Manuel Lopez
 * ml36724
 * 16480
 * Slip days used: 0
 * Git URL: https://github.com/shashkambh/assignment3-422c.git
 * Fall 2016
 */


package assignment3;
import java.util.*;
import java.io.*;

public class Main {
    
    // static variables and constants only here.
    
    private static final int LETTERS_IN_ALPHABET = 26;
    private static String INPUT1 = "";
    private static String INPUT2 = "";

    /**
     * Waits for input from user and performs both DFS and BFS on the two words given.
     * @param args The command line args. Not used.
     */
    public static void main(String[] args) throws Exception {
        
        Scanner kb; // input Scanner for commands
        kb = new Scanner(System.in);// default from Stdin
        initialize();
        
        ArrayList<String> input = parse(kb);
        
        // Keep going as long as parse returns two words
        while(!input.isEmpty()){
            INPUT1 = input.get(0);
            INPUT2 = input.get(1);

            // Perform BFS and DFS
            ArrayList<String> ladderdfs = getWordLadderDFS(INPUT1, INPUT2);
            ArrayList<String> ladderbfs = getWordLadderBFS(INPUT1, INPUT2);
            
            // Output to standard out
            printLadder(ladderbfs);
            printLadder(ladderdfs);
            input = parse(kb);
        }
    }
    
    /**
     * Empty function, supposed to initialize constants.
     * Never used.
     */
    public static void initialize() { }
    
    /**
     * @param keyboard Scanner connected to System.in
     * @return ArrayList of 2 Strings containing start word and end word. 
     * If word is /quit, return empty ArrayList. 
     */
    public static ArrayList<String> parse(Scanner keyboard) {
        ArrayList<String> userCommand = new ArrayList<>();

        String in = keyboard.next();
        while(in.length() != 5){
            in = keyboard.next();
        }

        if(!in.equals("/quit")){
            String end = keyboard.nextLine().trim();
            while(end.length() != 5){
                end = keyboard.next();
            }

            if(!end.equals("/quit")){
                userCommand.add(in.toUpperCase());
                userCommand.add(end.toUpperCase());
            }
        }

        return userCommand;
    }

    /**
     * Performs a depth first search on two given words for a word ladder between them.
     * @param start The word used for the start of the search
     * @param end The word to be searched for
     * @return A word ladder between the two if found, else an empty ArrayList.
     */
    public static ArrayList<String> getWordLadderDFS(String start, String end) {
        Set<String> dict = makeDictionary();
        
        // Don't allow the start word to appear again
        dict.remove(start);

        StringBuilder startPoint = new StringBuilder(start);

        ArrayList<String> ladder = null;

        // Try going forward. If that fails, go backwards.
        // If both fail, then search fails
        try{
            ladder = DFSHelper(startPoint, end, dict);

            if(ladder != null){
                Collections.reverse(ladder);
            }
        } catch(StackOverflowError e){
            try{
                dict = makeDictionary();
                StringBuilder endPoint = new StringBuilder(end);
                ladder = DFSHelper(endPoint, start, dict);
            } catch(StackOverflowError e2){
                ladder = null;
            }
        }

        if(start.equals(end)){
            ladder.add(start);
        }

        return ladder;
    }

    /**
     * Helper function for the recursive call for DFS.
     * @param start The word used for the start of the search
     * @param end The word to be searched for
     * @param dict The dictionary to be used for the search
     * @return A word ladder between the two if found, else an empty ArrayList.
     */
    private static ArrayList<String> DFSHelper(StringBuilder start, String end, Set<String> dict){

        ArrayList <String> ladder = null;
        boolean found = false;

        // If start equals end, finish
        // If not, search. Removes words from dict as their paths are tried.
        if(start.toString().equals(end)){
            ladder=new ArrayList<String>();
            ladder.add(end);
        } else {
            //First check for any words that share letters with end
            for(int i = 0; i < start.length() && !found; i++){
                StringBuilder next = new StringBuilder(start.toString());
                next.setCharAt(i, end.charAt(i));

                //If a given word is in dictionary, try going along that path
                if(dict.contains(next.toString())){
                    dict.remove(next.toString());
                    ladder = DFSHelper(next, end, dict);
                    if(ladder != null){
                        found = true;
                        ladder.add(start.toString());
                    }
                }
            }
            
            //If no such words are found, go in some random direction
            for(int i = 0; i < start.length() && !found; i++){
                
                for(int j = 0; j < LETTERS_IN_ALPHABET && !found; j++){
                    StringBuilder next = new StringBuilder(start.toString());

                    next.setCharAt(i, (char)('A' + j));
                    
                    //If a given word is in dictionary, try going along that path
                    if(dict.contains(next.toString())){
                        dict.remove(next.toString());
                        ladder = DFSHelper(next, end, dict);
                        if(ladder != null){
                            found = true;
                            ladder.add(start.toString());
                        }
                    }
                    
                }
            }

        } 
        
        return ladder;
    }
    
    /**
     * Performs a breadth first search on two given words for a word ladder between them.
     * @param start The word used for the start of the search
     * @param end The word to be searched for
     * @return A word ladder between the two if found, else an empty ArrayList.
     */
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
        
        ArrayList<String> queue = new ArrayList<String>();
        StringBuilder currentLadder = new StringBuilder(start);
        HashMap<String, String> parents = new HashMap<String, String>();
        Set<String> dict = makeDictionary(); 
        boolean found = false;

        
        String letSee = start;
        parents.put(start, "");
        
        if(!currentLadder.toString().equals(end)){
            // While not found and there are more words to search, keep searching
            while( (parents.get(end) == null) && !found && (start.length() == end.length()) ){
                // Go through all possible words
                for(int i = 0; i<currentLadder.length() && !found; i++){
                    for(int j = 0; j < LETTERS_IN_ALPHABET && !found; j++){
                        StringBuilder next = new StringBuilder(letSee);
    
                        next.setCharAt(i, (char)('A' + j));

                        // If a given word is in dict, add it to queue
                        if(dict.contains(next.toString()) && (parents.get(next.toString()) == null)
                                && !queue.contains(next.toString())){

                             queue.add(next.toString());        //adds all combinations(adjacent)
                             parents.put(next.toString(), letSee);
                             if(next.toString().equals(end)){
                                 found = true;
                             }
                         }
                    }
                }

                // If queue isn't empty, try again. Else no path found
                if(!queue.isEmpty()){
                    letSee = queue.remove(0);
                } else {
                    parents.put(end, "");
                }
            }
        }

        // Construct path based on search using parents of words in search
        ArrayList<String> ladder = new ArrayList<String>();
        if(found){
            ladder.add(end);
            String current = parents.get(end);
            while(!current.equals("")){
                ladder.add(current);
                current = parents.get(current);
            }
            Collections.reverse(ladder);
        } else {
            ladder = null;
        }
        return ladder; 
    }

    /**
     * Creates a dictionary from a text file.
     * @return a Set containing all the words in the text file
     */
    public static Set<String>  makeDictionary () {
        Set<String> words = new HashSet<String>();
        Scanner infile = null;
        try {
            infile = new Scanner (new File("five_letter_words.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("Dictionary File not Found!");
            e.printStackTrace();
            System.exit(1);
        }
        while (infile.hasNext()) {
            words.add(infile.next().toUpperCase());
        }
        return words;
    }
    
    /**
     * Prints out a given ladder found by a search function to standard output.
     * @param ladder The ladder to be printed
     */
    public static void printLadder(ArrayList<String> ladder) {
        if(ladder.size() > 0){
            System.out.println("a " + (ladder.size() - 2) + "-rung word ladder exists between " 
                    + ladder.get(0).toLowerCase() + " and " 
                    + ladder.get(ladder.size() - 1).toLowerCase() + ".");

            for(String e : ladder){
                System.out.println(e.toLowerCase());
            }
        } else {
            System.out.println("no word ladder can be found between " + INPUT1.toLowerCase()
                    + " and " + INPUT2.toLowerCase() + ".");
        }

    }
}
