/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * <Student1 Name>
 * <Student1 EID>
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Git URL:
 * Fall 2016
 */


package assignment3;
import java.util.*;
import java.io.*;

public class Main {
	
	// static variables and constants only here.
	
    private static final int LETTERS_IN_ALPHABET = 26;

	public static void main(String[] args) throws Exception {
		
		Scanner kb;	// input Scanner for commands
		PrintStream ps;	// output file
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default from Stdin
			ps = System.out;			// default to Stdout
		}
		initialize();
		
        ArrayList<String> input = parse(kb);

        while(!input.isEmpty()){
            ArrayList<String> ladder = getWordLadderDFS(input.get(0), input.get(1));
            ps.println(ladder);
            input = parse(kb);
        }
	}
	
	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests.  So call it 
		// only once at the start of main.
	}
	
	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of 2 Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList. 
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
        ArrayList<String> userCommand = new ArrayList<>();

        String in = keyboard.next();

        if(!in.equals("/quit")){
            String end = keyboard.nextLine().trim();
            userCommand.add(in.toUpperCase());
            userCommand.add(end.toUpperCase());
        }

        return userCommand;
	}
	
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		ArrayList<String> ladder = new ArrayList<>();

		Set<String> dict = makeDictionary();
		
        StringBuilder startPoint = new StringBuilder(start);

        ladder = DFSHelper(ladder, startPoint, end, dict);

        if(start.equals(end)){
            ladder.add(start);
        } 

        for(int i = 0; i < ladder.size() - 1; i++){
            for(int j = i + 1; j < ladder.size(); j++){
                if(ladder.get(i).equals(ladder.get(j))){
                    for(int toRemove = j; j > i; j--){
                        ladder.remove(toRemove);
                    }
                }
            }
        }
        

        
        return ladder;
	}

    public static ArrayList<String> DFSHelper(ArrayList<String> currentLadder, StringBuilder start, String end, Set<String> dict){
        boolean found = false;
        
        currentLadder.add(start.toString());

        if(!start.toString().equals(end)){
            for(int i = 0; i < start.length(); i++){
                
                for(int j = 0; j < LETTERS_IN_ALPHABET && !found; j++){
                    StringBuilder next = new StringBuilder(start.toString());

                    next.setCharAt(i, (char)('A' + j));
                    
                    if(dict.contains(next.toString()) && !currentLadder.contains(next.toString())){

                        currentLadder = DFSHelper(currentLadder, next, end, dict);
                        if(currentLadder.get(currentLadder.size() - 1).equals(end)){
                            found = true; 
                            currentLadder.add(0, start.toString());
                        }
                    }
                }
            }

        } else {
        	found = true;
        }
        if(!found){
            currentLadder.remove(currentLadder.size() - 1);
        }
        
        return currentLadder;
    }
	
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
		
		// TODO some code
		Set<String> dict = makeDictionary();
		// TODO more code
		
		return null; // replace this line later with real return
	}
    
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
	
	public static void printLadder(ArrayList<String> ladder) {
		
	}
}
