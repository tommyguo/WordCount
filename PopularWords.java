package project5;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * This class determines the frequency of words in a file and displays the words
 * along with their frequencies to the user based on three orders: alphabetical,
 * frequency, and scarcity. It displays all unique words in a file or a certain
 * number of unique words specified by the user.
 * 
 * @author Thomas Guo
 */
public class PopularWords {
	public static void main(String[] args) {
		String fileString;
		String keyword;
		String outputNumberString;
		int outputNumber = -1;

		// verifies that there are at least 2 arguments provided
		if (args.length < 2) {
			System.err.println("Invalid number of arguments. Needs two or three");
			System.exit(0);
		}
		// verifies argument 3
		if (args.length > 2) {
			outputNumberString = args[2];
			try {
				outputNumber = Integer.parseInt(outputNumberString);
				if (outputNumber < 1) {
					System.err.println("Argument three should be greater than 0");
					System.exit(0);
				}
			} catch (IllegalArgumentException ex) {
				System.err.println("Argument three should be an integer");
				System.exit(0);
			}

		}

		// verifies argument 2
		keyword = args[1];
		if (!(keyword.equalsIgnoreCase("name") || keyword.equalsIgnoreCase("frequency")
				|| keyword.equalsIgnoreCase("scarcity"))) {
			System.err.println("Invalid keyword for second argument");
			System.exit(0);
		}

		// verifies argument 1
		fileString = args[0];
		File file = new File(fileString);

		try {
			// parses entire file and places every unique word in hashmap
			HashMap<String, Integer> hmap = parseFile(file);

			// if no third argument is provided, sets it to number of unique
			// words
			if (outputNumber == -1) {
				outputNumber = hmap.size();
			}

			// if third argument is larger than number of unique words, sets it
			// to number of unique words
			if (outputNumber > hmap.size()) {
				outputNumber = hmap.size();
			}

			// create a set and iterator that iterates over key value pairs
			// stored in hash map
			Set<Map.Entry<String, Integer>> set = hmap.entrySet();
			Iterator<Map.Entry<String, Integer>> iterator = set.iterator();

			// stores every word-frequency pair in the ArrayList
			ArrayList<Map.Entry<String, Integer>> array = new ArrayList<Map.Entry<String, Integer>>();
			while (iterator.hasNext()) {
				array.add(iterator.next());
			}

			// creates a comparator used by the merge sort method based on
			// second argument provided by user
			Comparator<Map.Entry<String, Integer>> comparator;
			if (keyword.equalsIgnoreCase("name")) {
				comparator = new CompareWordsByName();
			} else if (keyword.equalsIgnoreCase("frequency")) {
				comparator = new CompareWordsByFrequency();
			} else {
				comparator = new CompareWordsByScarcity();
			}

			// sorts the ArrayList using merge sort
			MergeSort.mergeSort(array, comparator);

			// prints the contents of the ArrayList, number of which is
			// determined by user or the default value.
			for (int i = 0; i < outputNumber; i++) {
				System.out.println(array.get(i).getKey() + " " + array.get(i).getValue());
			}

		}
		// exits the program if an invalid file name is provided
		catch (FileNotFoundException ex) {
			System.err.println("Invalid file name");
			System.exit(0);
		}
	}

	/**
	 * This method stores every unique word in a file along with its frequency
	 * in a hash map.
	 * 
	 * @param file
	 *            file to be parsed.
	 * @return hash map containing every unique word in the file as its key and
	 *         its frequency as the value.
	 * @throws FileNotFoundException
	 *             if file does not exist.
	 */
	public static HashMap<String, Integer> parseFile(File file) throws FileNotFoundException {
		HashMap<String, Integer> hmap = new HashMap<String, Integer>();

		// creates a scanner object for the file and parses every string token
		// using parseWord method
		Scanner fileScanner = new Scanner(file);
		while (fileScanner.hasNext()) {
			String wordString = fileScanner.next();
			parseWord(wordString, hmap);
		}
		fileScanner.close();
		return hmap;
	}

	/**
	 * This method parses a string token, identifying words and storing their
	 * frequencies into the hash map that is passed.
	 * 
	 * @param wordString
	 *            string token to be parsed.
	 * @param hmap
	 *            hash map that the word-frequency values are stored in.
	 */
	private static void parseWord(String wordString, HashMap<String, Integer> hmap) {
		String word = "";
		boolean letterFound = false;
		for (int i = 0; i < wordString.length(); i++) {
			// checks if a letter has been found, signifying the start of a word
			if (letterFound) {
				// if character is a letter, add it to the word
				if (Character.isLetter(wordString.charAt(i))) {
					word += wordString.charAt(i);
				}
				// if character is a word-connector, check if there is a letter
				// after it
				// if yes, add word-connector to word, if not, skip the
				// character, add the word into the hash map, and run the method
				// recursively onto the remaining characters.
				else if (wordString.charAt(i) == '\'' || wordString.charAt(i) == '-' || wordString.charAt(i) == '_') {
					if (i + 1 < wordString.length()) {
						if (Character.isLetter(wordString.charAt(i + 1))) {
							word += wordString.charAt(i);
						} else {
							// puts remaining characters into a new word
							String wordRemaining = "";
							for (int j = i + 1; j < wordString.length(); j++) {
								wordRemaining += wordString.charAt(j);
							}
							// adds the word to the hash map
							addWord(word, hmap);
							// processes the remaining characters in the word as
							// a new word
							parseWord(wordRemaining, hmap);
							return;
						}
					} else {
						addWord(word, hmap);
						return;
					}
				}
				// if a word delimiter is reached, sever the word
				else {
					// puts remaining characters into a new word
					String wordRemaining = "";
					for (int j = i + 1; j < wordString.length(); j++) {
						wordRemaining += wordString.charAt(j);
					}
					// adds the word to the hash map
					addWord(word, hmap);
					// processes the remaining characters in the word as a
					// new word
					parseWord(wordRemaining, hmap);
					return;
				}
			}
			// if character is a letter, start a new word
			else if (Character.isLetter(wordString.charAt(i))) {
				letterFound = true;
				word += wordString.charAt(i);
			}
			// if character is not a letter, continue searching
			else {
				continue;
			}
		}
		if (word.equals("")) {
			return;
		} else {
			addWord(word, hmap);
		}
	}

	/**
	 * This method takes a word, converts it to lower case, and places it and
	 * its frequency into the hash map provided. If the word is not in the hash
	 * map, the word along with a frequency of 1 is placed into the hash map. If
	 * it is, then the word is updated with its frequency incremented and stored
	 * in the hash map with the word as the key and the frequency as the value.
	 * 
	 * @param word
	 *            word of type string to be added into the hash map
	 * @param hmap
	 *            hash map in which the word is added
	 */
	public static void addWord(String word, HashMap<String, Integer> hmap) {
		word = word.toLowerCase();
		// if word exists in the hash map, removes the word and adds the word
		// with incremented frequency
		if (hmap.containsKey(word)) {
			int frequency = hmap.get(word);
			hmap.remove(word);
			hmap.put(word, frequency + 1);
		} 
		// if word does not exist in the hash map, add it with frequency 1
		else {
			hmap.put(word, 1);
		}
	}

}
