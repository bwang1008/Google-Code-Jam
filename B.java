import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
public class B {

	public static void main(String[] args) {
		FastScanner in = new FastScanner(System.in);
		
		int T = in.nextInt();
		for(int t = 0; t < T; t++) {
			int N = in.nextInt();
			String g = in.next();
			
			String answer = solve2(N, g);
			System.out.println("Case #" + (t+1) + ": " + answer);
		}

	}
	
	public static String solve2(int N, String g) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < g.length(); i++) {
			if(g.charAt(i) == 'S') {
				sb.append('E');
			}
			else {
				sb.append('S');
			}
		}
		return sb.toString();
	}
	
	public static String solve(int N, String g) {
		Node[] all = new Node[N*N];
		for(int i = 0; i < all.length; i++)
			all[i] = new Node(i, N);
		
		int currentRow = 0;
		int currentCol = 0;
		for(int i = 0; i < g.length(); i++) {
			char c = g.charAt(i);
			if(c == 'E') {
				all[getNum(currentRow , currentCol, N)].right = false;
				all[getNum(currentRow , currentCol + 1, N)].left = false;
				
				System.out.println("Setting connection between " + (getNum(currentRow, currentCol, N)) + " and " + (getNum(currentRow , currentCol + 1, N)) + " to false");
				currentCol++;
			}
			else {
				all[getNum(currentRow, currentCol, N)].down = false;
				all[getNum(currentRow+1, currentCol, N)].up = false;
				
				System.out.println("Setting connection between " + (getNum(currentRow, currentCol, N)) + " and " + (getNum(currentRow+1 , currentCol, N)) + " to false");
				currentRow++;
			}
		}
		
		boolean[][] visited = new boolean[N][N];
		
		StringBuilder sb = new StringBuilder();
		
		ArrayList<Character> cList = new ArrayList<>();
		recurse(0, 0, N, all, visited, cList);
		
		for(char c : cList) {
			sb.append(c);
		}
		
		return sb.toString();
	}
	
	public static boolean recurse(int row, int col, int N, Node[] all, boolean[][] visited, ArrayList<Character> cList) {
		if(visited[row][col]) {
			return false;
		}
		else {
			visited[row][col] = true;
		}
		
		System.out.println("Doing (" + row + ", " + col + ")");
		
		if(row == N-1 && col == N-1) {
//			System.out.println("FOUND");
			return true;
		}
		
		int num = getNum(row, col, N);
		
		int nextEast = getNum(row, col+1, N);
		int nextSouth = getNum(row+1, col, N);
		
		if(all[num].right) {
			all[nextEast].parent = num;
//			System.out.println("Set (" + (row) + ", " + (col + 1) + "parent = " + num);
			cList.add('E');
			
			boolean other = recurse(row, col+1, N, all, visited, cList);
			if(other) {
				return true;
			}
			
			cList.remove(cList.size()-1);
			all[nextEast].parent = -1;
		}
		
		if(all[num].down) {
			all[nextSouth].parent = num;
//			System.out.println("Set (" + (row+1) + ", " + (col) + "parent = " + num);
			cList.add('S');
			
			boolean other = recurse(row+1, col, N, all, visited, cList);
			if(other) {
				return true;
			}
			
			cList.remove(cList.size()-1);
			all[nextSouth].parent = -1;
		}
		
		return false;
	}
	
	public static int getNum(int i, int j, int N) {
		return i*N + j;
	}
	
	public static int[] getRowCol(int num, int N) {
		int row = num / N;
		int col = num % N;
		return new int[] {row, col};
	}
	
	public static boolean valid(int i, int j, int N) {
		if(i < 0 || j < 0 || i >= N || j >= N) {
			return false;
		}
		
		return true;
	}
	
	static class Node {
		
		int parent;
		
		boolean left;
		boolean up;
		boolean down;
		boolean right;
		
		public Node(int num, int N) {
			int[] a = getRowCol(num, N);
			int i = a[0];
			int j = a[1];
			left = valid(i, j-1, N);
			up = valid(i-1, j, N);
			down = valid(i+1, j, N);
			right = valid(i, j+1, N);
		}
	}
	
	/**
	 * Source: Matt Fontaine
	 */
	static class FastScanner {
		private InputStream stream;
		private byte[] buf = new byte[1024];
		private int curChar;
		private int chars;

		public FastScanner(InputStream stream) {
			this.stream = stream;
		}

		int read() {
			if (chars == -1)
				throw new InputMismatchException();
			if (curChar >= chars) {
				curChar = 0;
				try {
					chars = stream.read(buf);
				} catch (IOException e) {
					throw new InputMismatchException();
				}
				if (chars <= 0)
					return -1;
			}
			return buf[curChar++];
		}

		boolean isSpaceChar(int c) {
			return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
		}

		boolean isEndline(int c) {
			return c == '\n' || c == '\r' || c == -1;
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}

		public long nextLong() {
			return Long.parseLong(next());
		}

		public double nextDouble() {
			return Double.parseDouble(next());
		}

		public String next() {
			int c = read();
			while (isSpaceChar(c))
				c = read();
			StringBuilder res = new StringBuilder();
			do {
				res.appendCodePoint(c);
				c = read();
			} while (!isSpaceChar(c));
			return res.toString();
		}

		public String nextLine() {
			int c = read();
			while (isEndline(c))
				c = read();
			StringBuilder res = new StringBuilder();
			do {
				res.appendCodePoint(c);
				c = read();
			} while (!isEndline(c));
			return res.toString();
		}
	}

}
/*
2
2
SE
5
EESSSESE

Case #1: ES
Case #2: SEEESSES
*/