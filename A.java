import java.util.Scanner;

public class A {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		
		int T = in.nextInt();
		
		for(int t = 0; t < T; t++) {
			String g = in.next();
			int[] a = new int[g.length()];
			for(int i = 0; i < a.length; i++)
				a[i] = (int) (g.charAt(i) - '0');
			
			String[] ans = solve(a);
			System.out.println("Case #" + (t+1) + ": " + ans[0] + " " + ans[1]);
		}
		
		in.close();
	}
	
	public static String[] solve(int[] a) {
		int N = a.length;
		
		String[] ans = new String[2];
		
		int[] aList = new int[N];
		int[] bList = new int[N];
		
		for(int i = 0; i < a.length; i++) {
			int dig = a[i];
			
			if(dig == 0) {
				aList[i] = 0;
				bList[i] = 0;
			}
			else if(dig == 1) {
				aList[i] = 1;
				bList[i] = 0;
			}
			else if(dig == 2) {
				aList[i] = 1;
				bList[i] = 1;
			}
			else if(dig == 3) {
				aList[i] = 2;
				bList[i] = 1;
			}
			else if(dig == 4) {
				aList[i] = 2;
				bList[i] = 2;
			}
			else if(dig == 5) {
				aList[i] = 3;
				bList[i] = 2;
			}
			else if(dig == 6) {
				aList[i] = 3;
				bList[i] = 3;
			}
			else if(dig == 7) {
				aList[i] = 2;
				bList[i] = 5;
			}
			else if(dig == 8) {
				aList[i] = 5;
				bList[i] = 3;
			}
			else if(dig == 9) {
				aList[i] = 3;
				bList[i] = 6;
			}
			
		}
		
		int index1 = 0;
		int index2 = 0;
		while(aList[index1] == 0)
			index1++;
		while(bList[index2] == 0)
			index2++;
		
		StringBuilder sb = new StringBuilder();
		for(int i = index1; i < aList.length; i++)
			sb.append(aList[i]);
		
		ans[0] = sb.toString();
		
		sb = new StringBuilder();
		for(int i = index2; i < bList.length; i++)
			sb.append(bList[i]);
		
		ans[1] = sb.toString();
		return ans;
	}

}
/*
3
4
940
4444

Case #1: 2 2
Case #2: 852 88
Case #3: 667 3777
*/