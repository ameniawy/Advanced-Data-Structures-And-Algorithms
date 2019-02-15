import java.io.*;
import java.util.StringTokenizer;

public class B {
    public static void main (String [] args) throws Exception {
        
        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        int q = sc.nextInt();

        Node [] versions = new Node[q];
        // Node [] nodes = new Node[q];

        int versions_index = 1;

        while(q-- > 0) {
            String operation = sc.next();

            if(operation.equals("push")) {
                int value = sc.nextInt();
                int version = sc.nextInt();
                Node tail = versions[version];
                int min = 0;
                if(tail == null) {
                    min = value;
                } else {
                    min = Math.min(value, tail.min);
                }
                
                Node new_node = new Node(value, tail, min);
                versions[versions_index++] = new_node;
                
            }
            else if (operation.equals("pop")) {
                int version = sc.nextInt();
                Node tail = versions[version];

                Node new_tail = null;
                
                if(tail != null) {
                    new_tail = tail.previous;
                    out.println(tail.value);
                }
                else {
                    out.println("null");
                }
                versions[versions_index++] = new_tail;


            }
            else if (operation.equals("min")) {
                int version = sc.nextInt();

                if(versions[version] == null) {
                    out.println("null");
                    continue;
                }
                int min = versions[version].min;
                out.println(min);
            }
            
        }
        out.flush();
        out.close();
    }

    static class Node {
        int value;
        int min;
        Node previous;

        public Node(int value, Node previous, int min) {
            this.value = value;
            this.previous = previous;
            this.min = min;
        }
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