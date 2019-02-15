import java.io.*;
import java.util.StringTokenizer;

public class AssignmentA {
    public static void main (String [] args) throws Exception {
        
        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        int q = sc.nextInt();

        // Node [] versions = new Node[q];
        // Node [] nodes = new Node[q];
        Node front = null;
        Node back = null;
        Node print_pointer = null;
        
        boolean started = false;

        int versions_index = 1;

        while(q-- > 0) {
            String operation = sc.next();

            if(operation.equals("enqueue")) {
                int value = sc.nextInt();
                if(!started) {
                    Node new_node = new Node(value, null, null, versions_index++);
                    front = new_node;
                    back = new_node;
                    print_pointer = new_node;
                    started = true;
                }
                else {
                    Node new_node = new Node(value, back, null, versions_index++);
                    back.previous = new_node;
                    back = new_node;
                }
            } else if(operation.equals("dequeue")) {
                if(front == null) {
                    versions_index++;
                    continue;
                }
                // Node temp_pointer = front;
                while(true) {
                    if(front.deleted) {
                        if(front.previous == null) {
                            versions_index++;
                            break;
                        }
                        front = front.previous;
                    } else {
                        front.deleted = true;
                        front.deleted_at = versions_index++;
                        break;
                    }
                }

            } else if(operation.equals("print")) {
                int version = sc.nextInt();

                // String res = "";
                StringBuilder res = new StringBuilder("");  
                Node temp_pointer = print_pointer;
                while(true) {
                    if(temp_pointer == null) {
                        break;
                    }
                    if(temp_pointer.added_at <= version && (!temp_pointer.deleted || temp_pointer.deleted_at > version)) {
                        if(res.length() > 0)
                            res.append(" ");
                        res.append(temp_pointer.value);
                    }
                    temp_pointer = temp_pointer.previous;
                }

                if(res.length() == 0) {
                    out.println("empty");
                } else {
                    out.println(res);
                }

            }

            
        }
        out.flush();
        out.close();
    }

    static class Node {
        int value;
        Node previous;
        Node next;
        boolean deleted;
        int deleted_at;
        int added_at;

        public Node(int value, Node next, Node previous, int added_at) {
            this.value = value;
            this.next = next;
            this.added_at = added_at;
            this.previous = previous;
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