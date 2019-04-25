import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.TreeSet;
public class D2 {
	
	public static boolean doingBinary = false;
	public static boolean finishedGuessing = false;
	public static int numSteps = 0;
	
	public static TreeSet<Integer> beginGroups;
	public static TreeSet<Integer> beginGroups2;
	
	public static ArrayList<ArrayList<Node>> allNodes;
	public static int maxLevel;
	
	
	public static boolean[] isWorking;
	public static boolean[] isBroken;
	public static boolean[] determined;
	public static int[] countB;

	public static void main(String[] args) throws IOException {
		FastScanner in = new FastScanner(System.in);
		PrintWriter out = new PrintWriter(System.out);
		
		int T = in.nextInt();
		for(int t = 0; t < T; t++) {
			int N = in.nextInt();
			int B = in.nextInt();
			int F = in.nextInt();
			
			doingBinary = false;
			finishedGuessing = false;
			numSteps = 0;
			beginGroups = new TreeSet<>();
			beginGroups2 = new TreeSet<>();
			
			allNodes = new ArrayList<>();
			for(int i = 0; i < 20; i++)
				allNodes.add(new ArrayList<>());
			
			maxLevel = 0;
			
			isWorking = new boolean[N];
			isBroken = new boolean[N];
			determined = new boolean[N];
			
			countB = new int[1];
			countB[0] = B;
			
			String guess = makeInitialGuess(N, B, F);
			
			beginGroups.add(0);
			for(int i : beginGroups)
				beginGroups2.add(i);
			
			for(int f = 0; f < F && !finishedGuessing; f++) {
				out.println(guess);
				out.flush();
				
				String result = in.next();
				
				guess = makeGuess2(N, B, F, guess, result, f+1);
			}
			
			out.println(guess);
			out.flush();
			
			int status = in.nextInt();
			if(status == 1) {
			}
			else {
				out.close();
				return;
			}
		}
		
		out.close();
		

	}
	
	public static String makeGuess2(int N, int B, int F, String guess, String output, int guessLevel) {
		if(!doingBinary && allNodes.get(1).size() > 2) {
			doingBinary = true;
			
			int t = 0;
			int index = 0;
			int numGroup = allNodes.get(1).size();
			for(int i = 0; i < numGroup; i++) {
				Node currGroup = allNodes.get(1).get(i);
				int numCorrect = 0;
				while(index < output.length() 
						&& output.charAt(index) - '0' == t
						&& numCorrect < currGroup.num) {
					numCorrect++;
					index++;
				}
				
				currGroup.numWorking = numCorrect;
				currGroup.numWorkingTemp = numCorrect;
				currGroup.numBroken = currGroup.num - numCorrect;
				t ^= 1;
				
			}
			
			allNodes.get(0).get(0).check();
			
			String pos = checkIfAnswer();
			if(pos.length() > 0) 
				return pos;
			
			
			
		}
		else {
			int index = 0;
			int t = 0;
			
			int numGroups = allNodes.get(guessLevel).size();
			for(int i = 0; i < numGroups; i++) {
				Node currGroup = allNodes.get(guessLevel).get(i);
				int numCorrect = 0;
				
				while(index < output.length()
						&& output.charAt(index) - '0' == t
						&& numCorrect < currGroup.num
						&& numCorrect < currGroup.parent.numWorkingTemp) {
					
					if(currGroup.num == currGroup.numBroken) {
						break;
					}
					
					
					numCorrect++;
					
					index++;
				}
				
				t ^= 1;
				
				currGroup.parent.numWorkingTemp -= numCorrect; //??
				currGroup.numWorking = numCorrect;
				currGroup.numWorkingTemp = numCorrect;
				currGroup.numBroken = currGroup.num - numCorrect;
			}
			
			allNodes.get(0).get(0).check();
			
			String pos = checkIfAnswer();
			if(pos.length() > 0) {
				return pos;
			}
		}
		
		//Reached the end, no more guessing
		if(guessLevel == allNodes.size()-1) {
			String pos = checkIfAnswer();
			return pos;
		}
		else {
			//create guess by dividing in half
			int numNextGroups = allNodes.get(guessLevel+1).size();
			StringBuilder sb = new StringBuilder();
			int t = 0;
			for(int i = 0; i < numNextGroups; i++) {
				Node currGroup = allNodes.get(guessLevel+1).get(i);
				

				for(int j = currGroup.LRange; j <= currGroup.RRange; j++) {
					sb.append(t);
				}
				t ^= 1;
				
			}
			
			return sb.toString();
		}
		
	}
	
	public static String makeInitialGuess(int N, int B, int F) {
		int q = N/B;
		if(q == 1)
			doingBinary = true;
		else
			doingBinary = false;
		
		StringBuilder sb = new StringBuilder();
		
		if(doingBinary) {
			beginGroups.add(0);
			for(int i = 0; i < ((N-1) >> 1)+1; i++) {
				sb.append('0');
			}
			
			for(; sb.toString().length() < N;) {
				sb.append('1');
			}
		}
		else {
			int t = 0;
			for(int i = 0; i < N; i += B) {
				beginGroups.add(i);
				for(int j = i; j < Math.min(i+B, N); j++) {
					sb.append(t);
				}
				t ^= 1;
			}
		}
		
		ArrayList<Integer> groupList = new ArrayList<>();
		for(int i : beginGroups)
			groupList.add(i);
		
		int[] groupSize = new int[groupList.size()];
		for(int i = 0; i < groupList.size()-1; i++) {
			groupSize[i] = groupList.get(i+1) - groupList.get(i);
		}
		groupSize[groupSize.length-1] = N - groupList.get(groupList.size()-1);
		
		
		if(doingBinary) {
			new Tree(0, N-1, B);
		}
		else {
			Node[] children = new Node[beginGroups.size()];
			for(int i = 0; i < children.length - 1; i++) {
				children[i] = new Node(groupList.get(i), groupList.get(i+1)-1, -1, 1, 2, null);
			}
			children[children.length-1] = new Node(groupList.get(children.length-1), N-1, -1, 1, 2, null);
			
			Node temp = new Node(0, N-1, B, 0, 0, children);
			temp.children = children;
			temp.numChildren = children.length;
			for(int i = 0; i < children.length; i++)
				children[i].parent = temp;
		}
		
		while(allNodes.get(allNodes.size()-1).size() == 0)
			allNodes.remove(allNodes.size()-1);
		
		TreeSet<Integer> newGroups = new TreeSet<>();
		for(int i = 0; i < groupSize.length; i++) {
			if(!determined[groupList.get(i)]) {
				newGroups.add(groupList.get(i));
				int halfWay = groupList.get(i) + (groupSize[i] >> 1);
				newGroups.add(halfWay);
			}
			else {
				newGroups.add(groupList.get(i));
			}
		}
		
		beginGroups2 = newGroups;
		
		return sb.toString();
	}
	
	static class Tree {
		Node root;
		
		public Tree(int left, int right, int broken) {
			root = new Node(left, right, broken, 0, 2, null);
		}
	}
	
	static class Node {
		//inclusive
		int LRange;
		int RRange;
		
		int numWorking;
		int numBroken;
		int num;
		int numChildren;
		
		int numWorkingTemp;
		int level;
		
		Node parent;
		Node[] children;
		
		public Node(int left, int right, int broken, int level, int numChildren, Node[] tChildren) {
			LRange = left;
			RRange = right;
			
			this.level = level;
			maxLevel = Math.max(level, maxLevel);
			this.numChildren = numChildren;
			
			num = (right - left + 1);
			
			children = new Node[numChildren];
			
			if(broken != -1) {
				numBroken = broken;
				numWorking = num - numBroken;
				numWorkingTemp = numWorking;
			}
			else {
				numBroken = numWorking = -1;
				numWorkingTemp = -1;
			}
			
			if(level == allNodes.size()) {
				allNodes.add(new ArrayList<>());
			}
			
			allNodes.get(level).add(this);
			
			if(left != right) {
				if(numChildren == 2) {
					int half = (left + right) / 2;
					children[0] = new Node(left, half, -1, level + 1, 2, null);
					children[1] = new Node(half+1, right, -1, level + 1, 2, null);
					children[0].parent = this;
					children[1].parent = this;
				}
				else {
					children = tChildren;
					for(int i = 0; i < numChildren; i++) {
						children[i].parent = this;
					}
				}
			}
			
			extend();
		}
		
		public void check() {
			if(numBroken == 0) {
				for(int i = LRange; i <= RRange; i++) {
					determined[i] = true;
					isWorking[i] = true;
				}
				
				if(children[1] == null) {
					if(children[0] != null) {
						children[0].numBroken = numBroken;
						children[0].numWorking = numWorking;
						children[0].numWorkingTemp = numWorking;
					}
				}
				return;
			}
			else if(numWorking == 0) {
				for(int i = LRange; i <= RRange; i++) {
					determined[i] = true;
					isBroken[i] = true;
				}
				if(children[1] == null) {
					if(children[0] != null) {
						children[0].numBroken = numBroken;
						children[0].numWorking = numWorking;
						children[0].numWorkingTemp = numWorking;
					}
				}
				return;
			}
			
			for(int i = 0; i < numChildren; i++) {
				if(children[i] != null) {
					children[i].check();
				}
			}
		}
		
		public void extend() {
			if(children[0] == null && num == 1) {
				if(level < maxLevel) {
					children[0] = new Node(LRange, RRange, numBroken, level+1, 2, null);
					children[0].numBroken = numBroken;
					children[0].numWorking = numWorking;
					children[0].numWorkingTemp = numWorking;
					children[0].parent = this;
					children[0].extend();
				}
				
			}
		}
		
		public String toString() {
			return "[" + LRange + "," + RRange + "] + Working: " + numWorking + ", broekn: " + numBroken + "}";
		}
	}
	
	public static Node findNode(int left, int right, Node curr) {
		if(curr.LRange == left && curr.RRange == right) {
			return curr;
		}
		
		if(curr.RRange < left || right < curr.LRange) {
			return null;
		}
		
		for(int i = 0; i < curr.children.length; i++) {
			Node temp = findNode(left, right, curr.children[i]);
			if(temp != null)
				return temp;
		}
		
		return null;
	}
	
	public static String checkIfAnswer() {
		if(allTrue(determined)) {
			finishedGuessing = true;
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < determined.length; i++) {
				if(isBroken[i]) {
					sb.append(i + " ");
				}
			}
			return sb.toString().trim();
		}
		return "";
	}
	
	public static void print(ArrayList<ArrayList<Node>> list) {
		for(int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
		System.out.println();
	}
	
	public static boolean allTrue(boolean[] b) {
		for(int i = 0; i < b.length; i++)
			if(!b[i])
				return false;
		return true;
	}
	
	public static void print(boolean[] b) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < b.length; i++) {
			if(b[i]) {
				sb.append(1);
			}
			else {
				sb.append(0);
			}
		}
		sb.append('\n');
		System.out.println(sb.toString());
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
1
5 2 4


0 3
*/