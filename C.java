import java.math.BigInteger;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeSet;
public class C {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int T = in.nextInt();
		for(int t = 0; t < T; t++) {
			BigInteger N = in.nextBigInteger();
			int L = in.nextInt();
			
			BigInteger[] arr = new BigInteger[L];
			for(int i = 0; i < L; i++) {
				arr[i] = in.nextBigInteger();
			}
			
			String answer = solve2(N, L, arr);
			System.out.println("Case #" + (t+1) + ": " + answer);
		}
		
		in.close();
	}
	
	public static String solve2(BigInteger N, int L, BigInteger[] arr) {
		BigInteger allGcd = arr[0];
		for(int i = 1; i < arr.length; i++)
			allGcd = allGcd.gcd(arr[i]);
		
		if(!(allGcd.equals(BigInteger.ONE))) {
			String result = gcdNotOne(N, L, arr, allGcd);
			return result;
		}
		
		TreeSet<BigInteger> set = new TreeSet<>();
		BigInteger[] message = new BigInteger[L+1];
		
		//findIndex;
		int index = -1;
		for(int i = 1; i < arr.length; i++) {
			BigInteger gcd = arr[i].gcd(arr[i-1]);
			BigInteger first = arr[i-1].divide(gcd);
			BigInteger second = arr[i].divide(gcd);
			BigInteger sqrt = sqrt(gcd);
			if(sqrt.multiply(sqrt).equals(gcd)) {
				gcd = sqrt;
				message[i] = sqrt;
				index = i;
				break;
			}
			
			if(!first.equals(BigInteger.ONE) && !second.equals(BigInteger.ONE)) {
				message[i] = gcd;
				index = i;
				break;
			}
		}
		
		for(int i = index-1; i >= 0; i--) {
			message[i] = arr[i].divide(message[i+1]);
		}
		
		for(int i = index+1; i < message.length; i++) {
			message[i] = arr[i-1].divide(message[i-1]);
		}
		
		for(BigInteger big : message)
			set.add(big);
		
		HashMap<BigInteger, Integer> map = new HashMap<>();
		
		index = 0;
		for(BigInteger big : set) {
			map.put(big, index++);
		}
		
		StringBuilder sb = new StringBuilder();
		for(BigInteger big : message) {
			int num = map.get(big);
			sb.append((char) (num + 'A'));
		}
		
		return sb.toString();
	}
	
	public static String solve(BigInteger N, int L, BigInteger[] arr) {
		BigInteger allGcd = arr[0];
		for(int i = 1; i < arr.length; i++)
			allGcd = allGcd.gcd(arr[i]);
		
		if(!(allGcd.equals(BigInteger.ONE))) {
			String result = gcdNotOne(N, L, arr, allGcd);
			return result;
		}
		
		TreeSet<BigInteger> set = new TreeSet<>();
		BigInteger[] message = new BigInteger[L+1];
		for(int i = 1; i < arr.length; i++) {
			BigInteger gcd = arr[i].gcd(arr[i-1]);
			
			BigInteger first = arr[i-1].divide(gcd);
			BigInteger second = arr[i].divide(gcd);
			
			BigInteger sqrt = sqrt(gcd);
			
			
			boolean isSquare = false;
			if(sqrt.multiply(sqrt).equals(gcd)) {
				gcd = sqrt;
				isSquare = true;
			}
			
			if(isSquare && first.equals(BigInteger.ONE)) {
				first = gcd;
			}
			if(isSquare && second.equals(BigInteger.ONE)) {
				second = gcd;
			}
			
			set.add(gcd);
			set.add(first);
			set.add(second);
			
			message[i-1] = first;
			message[i] = gcd;
			message[i+1] = second;
			
//			System.out.println("Setting message[" + (i-1) + "] = " + first);
//			System.out.println("setting message[" + i + "] = " + gcd);
		}
		
/*		BigInteger lastGcd = arr[arr.length-1].gcd(arr[arr.length-2]);
		message[message.length-1] = arr[arr.length-1].divide(lastGcd);
		
		set.add(lastGcd);
		set.add(message[message.length-1]); */
		
//		System.out.println("set.size = " + set.size());
//		System.out.println("Set = " + set);
		
		HashMap<BigInteger, Integer> map = new HashMap<>();
		int index = 0;
		
		for(BigInteger big : set) {
			map.put(big, index++);
		}
		
		StringBuilder sb = new StringBuilder();
		for(BigInteger big : message) {
			int num = map.get(big);
//			System.out.println("big = " + big);
//			System.out.println("num = " + num);
			sb.append((char) (num + 'A'));
		}
		
		return sb.toString();
	}
	
	public static String gcdNotOne(BigInteger N, int L, BigInteger[] arr, BigInteger allGcd) {
		TreeSet<BigInteger> set1 = new TreeSet<>();
		TreeSet<BigInteger> set2 = new TreeSet<>();
		BigInteger[] message1 = new BigInteger[L+1];
		BigInteger[] message2 = new BigInteger[L+1];
		
		message1[0] = allGcd;
		set1.add(allGcd);
		
		message2[0] = arr[0].gcd(arr[1]).divide(allGcd);
		set2.add(message2[0]);
		
		for(int i = 1; i < message1.length; i++) {
			if(message1 != null) {
				BigInteger rem = arr[i-1].remainder(message1[i-1]);
				if(!(rem.equals(BigInteger.ZERO))) {
					message1 = null;
					set1 = null;
				}
				else {
					message1[i] = arr[i-1].divide(message1[i-1]);
					set1.add(message1[i]);
				}
			}
			
			if(message2 != null) {
				BigInteger rem = arr[i-1].remainder(message2[i-1]);
				if(!(rem.equals(BigInteger.ZERO))) {
					message2 = null;
					set2 = null;
				}
				else {
					message2[i] = arr[i-1].divide(message2[i-1]);
					set2.add(message2[i]);
				}
			}
		}
		
		
		
/*		BigInteger lastGcd = arr[arr.length-1].gcd(arr[arr.length-2]);
		message[message.length-1] = arr[arr.length-1].divide(lastGcd);
		
		set.add(lastGcd);
		set.add(message[message.length-1]); */
		
//		System.out.println("set.size = " + set.size());
//		System.out.println("Set = " + set);
		
		HashMap<BigInteger, Integer> map = new HashMap<>();
		int index = 0;
		
		TreeSet<BigInteger> set;
		if(set1 != null)
			set = set1;
		else {
			set = set2;
		}
		
		BigInteger[] message = message1;
		if(set1 == null) {
			message = message2;
		}
		
		for(BigInteger big : set) {
			map.put(big, index++);
		}
		
		StringBuilder sb = new StringBuilder();
		for(BigInteger big : message) {
			int num = map.get(big);
//			System.out.println("big = " + big);
//			System.out.println("num = " + num);
			sb.append((char) (num + 'A'));
		}
		
		return sb.toString();
	}
	
	/**
	 * Credits to online sqrt BigInteger!
	 * @param x
	 * @return
	 */
	public static BigInteger sqrt(BigInteger x) {
        BigInteger div = BigInteger.ZERO.setBit(x.bitLength()/2);
        BigInteger div2 = div;
        // Loop until we hit the same value twice in a row, or wind
        // up alternating.
        for(;;) {
            BigInteger y = div.add(x.divide(div)).shiftRight(1);
            if (y.equals(div) || y.equals(div2))
                return y;
            div2 = div;
            div = y;
        }
    }

}
/*
2
103 31
217 1891 4819 2291 2987 3811 1739 2491 4717 445 65 1079 8383 5353 901 187 649 1003 697 3239 7663 291 123 779 1007 3551 1943 2117 1679 989 3053
10000 25
3292937 175597 18779 50429 375469 1651121 2102 3722 2376497 611683 489059 2328901 3150061 829981 421301 76409 38477 291931 730241 959821 1664197 3057407 4267589 4729181 5335543

Output 
 
Case #1: CJQUIZKNOWBEVYOFDPFLUXALGORITHMS
Case #2: SUBDERMATOGLYPHICFJKNQVWXZ



1
103 35
49 49 49 49 217 1891 4819 2291 2987 3811 1739 2491 4717 445 65 1079 8383 5353 901 187 649 1003 697 3239 7663 291 123 779 1007 3551 1943 2117 1679 989 3053
*/