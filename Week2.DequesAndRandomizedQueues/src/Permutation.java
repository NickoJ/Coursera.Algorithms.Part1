import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {

    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        if (k == 0) return;

        RandomizedQueue<String> rq = new RandomizedQueue<>();
        int counter = 0;
        while (!StdIn.isEmpty()) {
            String str = StdIn.readString();
            ++counter;
            if (rq.size() < k) rq.enqueue(str);
            else if (StdRandom.uniform() < 1.0 / counter) {
                rq.dequeue();
                rq.enqueue(str);
            }
        }

        while (!rq.isEmpty()) StdOut.println(rq.dequeue());
    }

}