import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Collections;

public class A {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        int n = sc.nextInt();

        int nums[] = new int[n];

        long currentSum = 0;
        long maxSum = 0;
        int maxStartIndex = 0;
        int maxEndIndex = 0;
        int currentStartIndex = 0;
        int currentEndIndex = 0;
        for (int i = 0; i < n; i++) {
            int num = sc.nextInt();
            // System.out.println(num);
            nums[i] = num;

            if (num < 0) {
                if (currentSum > maxSum) {
                    maxSum = currentSum;
                    maxStartIndex = currentStartIndex;
                    maxEndIndex = currentEndIndex;
                } else if (currentSum == maxSum) {
                    if ((currentEndIndex - currentStartIndex) > (maxEndIndex - maxStartIndex)) {
                        maxSum = currentSum;
                        maxStartIndex = currentStartIndex;
                        maxEndIndex = currentEndIndex;
                    }
                }
                currentSum = 0;
                currentStartIndex = i + 1;
                currentEndIndex = i + 1;
                continue;
            }

            currentSum += num;
            // System.out.println(num + " " + currentSum);
            currentEndIndex++;
            if (currentSum > maxSum) {
                maxSum = currentSum;
                maxStartIndex = currentStartIndex;
                maxEndIndex = currentEndIndex;
            } else if (currentSum == maxSum) {
                if ((currentEndIndex - currentStartIndex) > (maxEndIndex - maxStartIndex)) {
                    maxSum = currentSum;
                    maxStartIndex = currentStartIndex;
                    maxEndIndex = currentEndIndex;
                }
            }
        }
        // for (int u : nums)
        // System.out.print(u + " ");
        // System.out.println();

        if (maxSum == 0 && maxStartIndex == 0 && maxEndIndex == 0)
            out.println();
        else {
            StringBuilder sb = new StringBuilder();
            for (int i = maxStartIndex; i < maxEndIndex; i++) {
                sb.append(nums[i]);
                if (i + 1 != maxEndIndex)
                    sb.append(" ");
            }
            out.println(sb);
        }

        // out.println(maxSum);
        // out.println(maxStartIndex + " " + maxEndIndex);

        out.flush();
        out.close();
    }


    static class Scanner {

        StringTokenizer st;
        BufferedReader br;

        public Scanner(InputStream s) {
            br = new BufferedReader(new InputStreamReader(s));
        }

        public String next() throws IOException {
            while (st == null || !st.hasMoreTokens())
                st = new StringTokenizer(br.readLine());
            return st.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }

        public long nextLong() throws IOException {
            return Long.parseLong(next());
        }

        public String nextLine() throws IOException {
            return br.readLine();
        }

        public double nextDouble() throws IOException {
            String x = next();
            StringBuilder sb = new StringBuilder("0");
            double res = 0, f = 1;
            boolean dec = false, neg = false;
            int start = 0;
            if (x.charAt(0) == '-') {
                neg = true;
                start++;
            }
            for (int i = start; i < x.length(); i++)
                if (x.charAt(i) == '.') {
                    res = Long.parseLong(sb.toString());
                    sb = new StringBuilder("0");
                    dec = true;
                } else {
                    sb.append(x.charAt(i));
                    if (dec)
                        f *= 10;
                }
            res += Long.parseLong(sb.toString()) / f;
            return res * (neg ? -1 : 1);
        }

        public boolean ready() throws IOException {
            return br.ready();
        }

        public boolean nextEmpty() throws IOException {
            String s = nextLine();
            st = new StringTokenizer(s);
            return s.isEmpty();
        }
    }
}
