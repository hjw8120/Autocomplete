import java.util.*;

public class HashListAutocomplete implements Autocompletor{
	
	private static final int MAX_PREFIX = 10;
	private Map<String, List<Term>> myMap;
	private int mySize;
	
	/**
	 * Given arrays of words and weights, initialize myMap to a corresponding
	 * HashMap with keys as prefix strings and values as Term ArrayLists.
	 *
	 * @param terms
	 *            - A list of words to form terms from
	 * @param weights
	 *            - A corresponding list of weights, such that terms[i] has
	 *            weight[i].
	 * @return a HashListAutocomplete whose myMap object has substrings of terms[i]
	 * for keys and an ArrayList of Terms with word terms[i] and weight weights[i] for values.
	 * @throws a
	 *             NullPointerException if either argument passed in is null
	 */
	public HashListAutocomplete(String[] terms, double[] weights) {
		if (terms == null || weights == null) {
			throw new NullPointerException("One or more arguments null");
		}

		initialize(terms, weights);

		}


	@Override
	public void initialize(String[] terms, double[] weights) {
		myMap = new HashMap<String, List<Term>>();

		for(int i = 0; i < terms.length; i ++) {
			int size = Math.min(MAX_PREFIX, terms[i].length());
			for(int k = 0; k <= size; k++) {
				String prefix = terms[i].substring(0,k);
				Term t = new Term(terms[i], weights[i]);
				myMap.putIfAbsent(prefix,new ArrayList<Term>());
				myMap.get(prefix).add(t);
			}

		}
		for(List<Term> value : myMap.values()) {
			Collections.sort(value, new Term.ReverseWeightOrder());
		}
	}
	
	/**
	 * Returns an ArrayList containing the
	 * k words in myMap values with the largest weight which match the given prefix,
	 * in descending weight order. 
	 * 
	 * @param prefix
	 *            - A prefix which all returned words must start with
	 * @param k
	 *            - The (maximum) number of words to be returned
	 * @return An ArrayList of the k words with the largest weights among all words
	 *         starting with prefix, in descending weight order. If the prefix is not 
	 *         in the keys of myMap, return an empty ArrayList.
	 * @throws a
	 *             NullPointerException if prefix is null
	 *             
	 */
	@Override	
	public List<Term> topMatches(String prefix, int k) {
		if(!myMap.containsKey(prefix)) {
			return new ArrayList<Term>();
		}
		List<Term> all = myMap.get(prefix);
		List <Term> list = all.subList(0,Math.min(k, all.size()));
		return list;	
	}
	
	@Override
	public int sizeInBytes() {
		if (mySize == 0) {

			for(String p : myMap.keySet()) {
				mySize += BYTES_PER_CHAR*p.length();
				for(int i = 0; i < myMap.get(p).size(); i++) {
					mySize += BYTES_PER_DOUBLE + 
							BYTES_PER_CHAR*(myMap.get(p).get(i).getWord().length());
				}
			}

		}
		return mySize;
	}


}

