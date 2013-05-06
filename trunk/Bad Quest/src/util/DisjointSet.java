package util;

public class DisjointSet {
	private int[] d;
	private int n, sets;
	public DisjointSet(int n){
		this.n = n;
		d = new int[n];
		reset();
	}
	
	/**
	 * Reset the DisjointSet to initial conditions
	 */
	public void reset(){
		for(int i = 0; i < n; i++)
			d[i] = i;
		sets = n;
	}
	
	/**
	 * Finds the parent of s, compresses the path.
	 * @param s
	 * @return
	 */
	public int find(int s){
		if(d[s] == s)
			return s;
		return d[s] = find(d[s]);
	}
	
	/**
	 * Unions s and t, making t the parent of s
	 * @param s
	 * @param t
	 */
	public void union(int s, int t){
		if(find(s) != find(t))
			sets--;
		d[find(s)] = find(t);
	}
	
	/**
	 * Returns the numbers of sets in this DisjointSet
	 * @return
	 */
	public int countSets(){
		return sets;
	}
	
	/**
	 * Returns true if all nodes have been unioned into a single set
	 * @return
	 */
	public boolean singleSet(){
		return countSets() == 1;
	}
}
