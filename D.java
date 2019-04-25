import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.TreeSet;
public class D {
	
	public static boolean doingBinary = false;
	public static boolean finishedGuessing = false;
	public static int numSteps = 0;
	
	public static TreeSet<Integer> beginGroups;
	public static TreeSet<Integer> beginGroups2;
	
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
				System.out.println("I am guessing:");
				out.println(guess);
				out.flush();
				
				String result = in.next();
				
				guess = makeGuess2(N, B, F, guess, result);
			}
			
			System.out.println("GONNA ANSWER:");
			out.println(guess);
			out.flush();
			
			int status = in.nextInt();
			System.out.println("Status for test case: " + (t+1));
			if(status == 1) {
				System.out.println("hip hip hooray!!!");
			}
			else {
				System.out.println("It's a trap!");
				return;
			}
		}

	}
	
	public static String makeGuess2(int N, int B, int F, String guess, String output) {
		
		System.out.println("all groups: ");
		System.out.println(beginGroups);
		
		
		//find sizes of each group
		ArrayList<Integer> groupList = new ArrayList<>();
		for(int i : beginGroups)
			groupList.add(i);
		
		int[] groupSize = new int[groupList.size()];
		for(int i = 0; i < groupList.size()-1; i++) {
			groupSize[i] = groupList.get(i+1) - groupList.get(i);
		}
		groupSize[groupSize.length-1] = N - groupList.get(groupList.size()-1);
		
		beginGroups = beginGroups2;
		System.out.println("NOW allGroups:");
		System.out.println(beginGroups);
		
		if(!doingBinary) {
			System.out.println("output = " + output);
			doingBinary = true;
			//count how many correct machines are in each group
			int[] count = new int[groupList.size()];
			int groupIndex = 0;
			int t = 0;
			int index = 0;
			while(index < output.length()) {
				int temp = 0;
				System.out.println("groupIndex1 = " + groupIndex);
				boolean insideWhile = false;
				while(index < output.length() 
						&& temp < groupSize[groupIndex]
						&& output.charAt(index) - '0' == t) {
					System.out.println("index = " + index);
					System.out.println("groupIndex = " + groupIndex);
					
					insideWhile = true;
					index++;
					temp++;
					
					System.out.println("New Booleans:");
					System.out.println(index + " < " + output.length() + " ? ");// + (index < output.length()));
					System.out.println(temp + " < " + groupSize[groupIndex] + " ? ");// + (temp < groupSize[groupIndex]));
					if(index < output.length())
						System.out.println(output.charAt(index) + " == " + t + " ? ");// + (output.charAt(index)-'0' == t));
					else {
						System.out.println("oops outta range of output");
					}
					System.out.println();
				}
				if(!insideWhile) {
		//			index++;
				}
				System.out.println("here");
				count[groupIndex] = temp;
				System.out.println("count[" + groupIndex + "] = " + temp);
				t ^= 1;
				groupIndex++;
			}
			
			System.out.println("correct: " + Arrays.toString(count));
			System.out.println("groupSize: " + Arrays.toString(groupSize));
			
			countB = new int[count.length];
			for(int i = 0; i < countB.length; i++) {
				countB[i] = groupSize[i] - count[i];
			}
			
			System.out.println("incorrect: " + Arrays.toString(countB));
			for(int i = 0; i < countB.length; i++) {
				if(groupSize[i] == 1) {
					determined[groupList.get(i)] = true;
					if(countB[i] == 1) {
						isBroken[groupList.get(i)] = true;
					}
					else if(countB[i] == 0) {
						isWorking[groupList.get(i)] = true;
					}
				}
				else if(groupSize[i] == count[i]) {
					for(int j = 0; j < groupSize[i]; j++) {
						determined[groupList.get(i) + j] = true;
						isWorking[groupList.get(i) + j] = true;
					}
				}
				else if(groupSize[i] == countB[i]) {
					for(int j = 0; j < groupSize[i]; j++) {
						determined[groupList.get(i) + j] = true;
						isBroken[groupList.get(i) + j] = true;
					}
				}
			}
			
			System.out.println("isWorking: ");
			print(isWorking);
			System.out.println("isBroken: ");
			print(isBroken);
			System.out.println("determined: ");
			print(determined);
			
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
			
			System.out.println("newGroups: " + newGroups);
			
			//build guess
			StringBuilder sb = new StringBuilder();
			t = 1;
			for(int i = 0; i < N; i++) {
				if(determined[i]) {
					sb.append(1);
					continue;
				}
				
				if(newGroups.contains(i)) {
					t ^= 1;
				}
				
				sb.append(t);
			}
			
//			beginGroups = newGroups;
			beginGroups2 = newGroups;
			return sb.toString();
		}
		else {
			
			//count how many correct groups
			int[] count0 = new int[groupList.size()];
			int[] count1 = new int[groupList.size()];
			
			System.out.println("groupList = " + groupList);
			System.out.println("groupSize: " + Arrays.toString(groupSize));
			System.out.println("incorrect: " + Arrays.toString(countB));
			
			int groupIndex = 0;
			int index = 0;
			System.out.println("GroupSize = " + Arrays.toString(groupSize));
			System.out.println("countB = " + Arrays.toString(countB));
			while(index < output.length()) {
				int correct = groupSize[groupIndex] - countB[groupIndex];
				System.out.println("correct = " + correct);
				int temp = 0;
				while(index < output.length() 
						&& correct > 0
						&& output.charAt(index) - '0' == 0
						&& temp < groupSize[groupIndex] >> 1) {
					
					index++;
					temp++;
					correct--;
				}
				
				count0[groupIndex] = temp;
				System.out.println("midway temp = " + temp);
				
				temp = 0;
				while(index < output.length() 
						&& correct > 0
						&& output.charAt(index) - '0' == 1 
//						&& temp < (groupSize[groupIndex] - (groupSize[groupIndex] >> 1))) {
					) {
					index++;
					temp++;
					correct--;
					
					System.out.println("Check Boolean in Else 1:");
					System.out.println(index + " < " + output.length());
					System.out.println(correct + " > " + 0);
					if(index < output.length())
						System.out.println(output.charAt(index) + " == " + 1);
					else {
						System.out.println("outta range buddy");
					}
					System.out.println(temp + " < " + (groupSize[groupIndex] - (groupSize[groupIndex] >> 1)));
					System.out.println();
				}
				
				count1[groupIndex] = temp;
				
				System.out.println("setting count0[" + groupIndex + "] = " + count0[groupIndex]);
				System.out.println("setting count1[" + groupIndex + "] = " + count1[groupIndex]);
				groupIndex++;
			}
			
			System.out.println("count0: " + Arrays.toString(count0));
			System.out.println("count1: " + Arrays.toString(count1));
			
			System.out.println("beginGroups: " + beginGroups);
			
			
			
			int[] countB2 = new int[beginGroups.size()];
			index = 0;
			for(int i = 0; i < groupSize.length; i++) {
				int correctNum0 = groupSize[i] >> 1;
				int correctNum1 = groupSize[i] - correctNum0;
				
				if(groupSize[i] == count0[i] + count1[i]) {
					countB2[index++] = 0;
					System.out.println("new index = " + index);
					int last = (i == groupSize.length-1) ? N : groupList.get(i+1);
					for(int j = groupList.get(i); j < last; j++) {
						determined[j] = true;
						isWorking[j] = true;
					}
				}
				else if (count0[i] + count1[i] == 0){
					countB2[index++] = 0;
					System.out.println("new index2 = " + index);
					int last = (i == groupSize.length-1) ? N : groupList.get(i+1);
					for(int j = groupList.get(i); j < last; j++) {
						determined[j] = true;
						isWorking[j] = true;
					}
				}
				else {
					countB2[index++] = correctNum0 - count0[i];
					countB2[index++] = correctNum1 - count1[i];
					System.out.println("countB2[" + (index-2) + "] = " + countB2[index-2]);
					System.out.println("countB2[" + (index-1) + "] = " + countB2[index-1]);
					System.out.println("jump2: " + index);
			//		countB2[index++] = groupSize[i] - (count0[i] + count1[i]);
			//		System.out.println("new:");
			//		System.out.println("countB2[" + (index-1) + "] = " + countB2[index-1]);
					
				}
			}
			
			System.out.println("newCountB = " + Arrays.toString(countB2));
			
			groupList = new ArrayList<>();
			for(int i : beginGroups)
				groupList.add(i);
			
			groupSize = new int[groupList.size()];
			for(int i = 0; i < groupList.size()-1; i++) {
				groupSize[i] = groupList.get(i+1) - groupList.get(i);
			}
			groupSize[groupSize.length-1] = N - groupList.get(groupList.size()-1);
			
			System.out.println("newGroupSize = " + Arrays.toString(groupSize));
			
			
			for(int i = 0; i < countB2.length; i++) {
				if(groupSize[i] == 1) {
					determined[groupList.get(i)] = true;
					if(countB2[i] == 0) {
						isWorking[groupList.get(i)] = true;
						System.out.println("Set isWorking[" + (groupList.get(i)) + "] = true" );
					}
					else {
						isBroken[groupList.get(i)] = true;
						System.out.println("set isBroken[" + (groupList.get(i)) + "] = true");
					}
				}
				else if(countB2[i] == groupSize[i]) {
					int last = groupList.get(i) + groupSize[i];
					for(int j = groupList.get(i); j < last; j++) {
						determined[j] = true;
						isBroken[j] = true;
					}
				}
				else if(countB2[i] == 0) {
					int last = groupList.get(i) + groupSize[i];
					for(int j = groupList.get(i); j < last; j++) {
						determined[j] = true;
						isWorking[j] = true;
					}
				}
			}
			
			
			//end of analyss

			System.out.println("isWorking: ");
			print(isWorking);
			System.out.println("isBroken: ");
			print(isBroken);
			System.out.println("determined: ");
			print(determined);
			
			if(allTrue(determined)) {
				finishedGuessing = true;
				StringBuilder sb = new StringBuilder();
				for(int i = 0; i < N; i++) {
					if(isBroken[i]) {
						sb.append(i + " ");
					}
				}
				return sb.toString().trim();
			}
			
			beginGroups = beginGroups2;
			countB = countB2;
			System.out.println("UPDATED beginGroups: " + beginGroups);
			
			beginGroups2 = new TreeSet<>();
/*			for(int i = 0; i < count0.length; i++) {
				if((groupSize[i] >> 1) - count0[i] != 0) {
					beginGroups2.add(groupList.get(i));
				}
				if((groupSize[i] - (groupSize[i] >> 1)) - count1[i] != 0) {
					beginGroups2.add(groupList.get(i));
				}
			} */
			
			for(int i = 0; i < groupSize.length; i++) {
				if(!determined[groupList.get(i)]) {
					beginGroups2.add(groupList.get(i));
					beginGroups2.add(groupList.get(i) + (groupSize[i] >> 1));
				}
				else {
					beginGroups2.add(groupList.get(i));
				}
			}
			
			System.out.println("Creating beginGroups2222: " + beginGroups2);
			
			
			//build guess
			StringBuilder sb = new StringBuilder();
			int t = 1;
			for(int i = 0; i < N; i++) {
				if(determined[i]) {
					sb.append(1);
					continue;
				}
				
				if(beginGroups2.contains(i)) {
					t ^= 1;
				}
				
				sb.append(t);
			}
			
			return sb.toString();
			
			
		}
		
		
//		return "01011";
	}
	
	public static String makeInitialGuess(int N, int B, int F) {
		int q = N/B;
		if(q == 1) {
			doingBinary = true;
		}
		else {
			doingBinary = false;
		}
		
		StringBuilder sb = new StringBuilder();
		
		if(doingBinary) {
			beginGroups.add(0);
			for(int i = 0; i < (N >> 1); i++) {
				sb.append('0');
			}
			
//			beginGroups.add(sb.toString().length());
			for(int i = 0; i < (N-(N >> 1)); i++) {
				sb.append('1');
			}
		}
		else {
			System.out.println("doing B chunks");
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
		
		System.out.println("newGroupsInitialGuess: " + newGroups);
		beginGroups2 = newGroups;
		
		return sb.toString();
	}
	
	public static String makeGuess(int N, int B, int F, String guess, String output) {
		if(numSteps == 1 && !doingBinary && output.charAt(0) == '1') {
			finishedGuessing = true;
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < B; i++) {
				sb.append(i + " ");
			}
			
			return sb.toString();
		}
		
		System.out.println("all groups: ");
		System.out.println(beginGroups);
		//find sizes of each group
		ArrayList<Integer> groupList = new ArrayList<>();
		for(int i : beginGroups)
			groupList.add(i);
		
		int[] groupSize = new int[groupList.size()];
		for(int i = 0; i < groupList.size()-1; i++) {
			groupSize[i] = groupList.get(i+1) - groupList.get(i);
		}
		groupSize[groupSize.length-1] = N - groupList.get(groupList.size()-1);
		
		
		//count how many correct machines are in each group
		int[] count = new int[beginGroups.size()];
		int groupIndex = 0;
		int t = 0;
		int index = 0;
		while(index < output.length()) {
			int temp = 0;
			while(index < output.length() && output.charAt(index) - '0' == t) {
				index++;
				temp++;
			}
			
			count[groupIndex] = temp;
			t ^= 1;
			groupIndex++;
		}
		
		System.out.println("groups: " + Arrays.toString(count));
		
		//count how many broken machines are in each group = total machines - correct machines
		int[] countB = new int[count.length];
		for(int i = 0; i < count.length; i++) {
			countB[i] = groupSize[i] - count[i];
		}
		
		System.out.println("How many broken machines: " + Arrays.toString(countB));
		
		
		//create your guess, group by group
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < countB.length; i++) {
			if(countB[i] == 0) {
				beginGroups.add(sb.toString().length());
				for(int j = 0; j < groupSize[i]; j++) {
					sb.append(1);
				}
			}
			else {
				int half = groupSize[i] >> 1;
				beginGroups.add(sb.toString().length());
				for(int j = 0; j < half; j++) {
					sb.append(0);
				}
				beginGroups.add(sb.toString().length());
				for(int j = 0; j < groupSize[i]-half; j++) {
					sb.append(1);
				}
			}
		}
		
		
		return sb.toString();
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