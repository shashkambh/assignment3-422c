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

            if(ladder != null){
                printLadder(ladder);
            } else {
                System.out.println("no word ladder can be found between " + 
                        input.get(0).toLowerCase() + " and " + input.get(1).toLowerCase() + ".");
            }
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
		Set<String> dict = makeDictionary();
		
        dict.remove(start);

        StringBuilder startPoint = new StringBuilder(start);

		ArrayList<String> ladder = DFSHelper(startPoint, end, dict);
        if(ladder != null){
            Collections.reverse(ladder);
        }

        return ladder;
	}

	public static ArrayList<String> DFSHelper(StringBuilder start, String end, Set<String> dict){
		ArrayList <String> ladder = null;
		boolean found = false;
		if(start.toString().equals(end)){
			ladder=new ArrayList<String>();
			ladder.add(end);
		} else {
            for(int i = 0; i < start.length() && !found; i++){
                StringBuilder next = new StringBuilder(start.toString());
                next.setCharAt(i, end.charAt(i));
                if(dict.contains(next.toString())){
                    dict.remove(next.toString());
                    ladder = DFSHelper(next, end, dict);
                    if(ladder != null){
                        found = true;
                        ladder.add(start.toString());
                    }
                }
            }

            for(int i = 0; i < start.length() && !found; i++){
				
                for(int j = 0; j < LETTERS_IN_ALPHABET && !found; j++){
                    StringBuilder next = new StringBuilder(start.toString());

                    next.setCharAt(i, (char)('A' + j));
                    
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
        System.out.println("a " + (ladder.size() - 2) + "-rung word ladder exists between " 
                + ladder.get(0).toLowerCase() + " and " 
                + ladder.get(ladder.size() - 1).toLowerCase() + ".");

        for(String e : ladder){
            System.out.println(e.toLowerCase());
	    }
    }
}