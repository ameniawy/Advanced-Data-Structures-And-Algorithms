import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class AssignmentB {
    public static void main (String [] args) throws Exception {
        
        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        int q = sc.nextInt();
        int copy = q;

        // Node [] versions = new Node[q];
        // Node [] nodes = new Node[q];

        PersistentQueue pq = new PersistentQueue(q);

        Node current_version;
        // int versions_index = 1;
        ArrayList <Integer> queries = new ArrayList<>();

        while(q-- > 0) {
            String operation = sc.next();

            if(operation.equals("enqueue")) {
                int value = sc.nextInt();
                pq.enqueue(value);

            } else if(operation.equals("dequeue")) {
                pq.dequeue();

            } else if(operation.equals("sum")) {
                pq.setSumNode();
                queries.add(pq.current_version.version);
                // System.out.println(pq.current_version);
            } else if(operation.equals("checkout")) {
                int version = sc.nextInt();
                pq.change_version(version);
            }   
        }

        if(copy > 0) {
            pq.calculateSums();
    
            for(int i = 0; i < queries.size(); i++) {
                out.println(pq.sums[queries.get(i)]);
            }
        }

        out.flush();
        out.close();
    }

    static class Node {
        int value;
        boolean dequeue_node;
        Node previous;
        ArrayList<Node> next = new ArrayList<>();
        boolean sum_node;
        int version;

        public Node(int value, boolean dequeue_node, Node previous, int version) {
            this.value = value;
            this.dequeue_node = dequeue_node;
            this.previous = previous;
            this.version = version;
        }

        void setSumNode() {
            this.sum_node = true;
        }

        @Override
        public String toString() {
            return "NODE: " + this.value + " " + this.version;
        }
    }

    // static class Sum {
    //     int value;
    //     int order;

    //     public Sum(int value, int order) {
    //         this.value  = value;
    //         this.order = order;
    //     }
    // }

    static class PersistentQueue {
        ArrayList<Node> versions = new ArrayList<>();
        
        Node current_version;
        // int version;

        // ArrayList<Integer> sums = new ArrayList<>();
        long [] sums;
        int [] data_stack;

        public PersistentQueue(int n_nodes) {
            // this.version = 1;
            this.current_version = new Node(0, true, null, 0);
            this.data_stack = new int[n_nodes];
            this.sums = new long[n_nodes];
            this.versions.add(current_version);
            
        }

        void change_version(int version) {
            this.current_version = this.versions.get(version);
            // this.version = version;
        }

        void enqueue(int value) {
            Node new_node = new Node(value, false, current_version, this.versions.size());
            this.versions.add(new_node);
            this.current_version.next.add(new_node);
            this.current_version = new_node;
            // this.version++;
        }

        void dequeue() {
            Node new_node = new Node(-1, true, current_version, this.versions.size());
            this.versions.add(new_node);
            this.current_version.next.add(new_node);
            this.current_version = new_node;
            // this.version++;
        }

        void setSumNode() {
            this.current_version.setSumNode();
        }

        void calculateSums() {
            // if(versions.size() > 0) {
            if(versions.size() > 1)
                dfs(versions.get(1), 1, 1);
            // }
            // return;
        }

        void dfs(Node currentNode, int head, int tail) {
            // System.out.println(currentNode);
            if(currentNode == null) {
                return;
            }

            if(currentNode.dequeue_node) {
                // remove from head
                long original_sum = (currentNode.previous == null) ? 0 : this.sums[currentNode.previous.version];
                sums[currentNode.version] =  original_sum - data_stack[head++];

            } else {
                // add to tail and increment
                data_stack[tail++] = currentNode.value;
                long original_sum = (currentNode.previous == null) ? 0 : this.sums[currentNode.previous.version];
                sums[currentNode.version] = original_sum + currentNode.value;
            }

            for(int i = 0; i < currentNode.next.size(); i++) {
                Node next_node = currentNode.next.get(i);
                dfs(next_node, head, tail);
            }

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