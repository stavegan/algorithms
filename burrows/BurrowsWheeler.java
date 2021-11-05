import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler
{
    private static final int R = 256;

    public static void transform()
    {
        StringBuilder sb = new StringBuilder();

        while (!BinaryStdIn.isEmpty())
        {
            sb.append(BinaryStdIn.readChar());
        }

        String s = sb.toString();

        CircularSuffixArray csa = new CircularSuffixArray(s);

        int first = 0;

        char[] t = new char[s.length()];

        for (int i = 0; i < s.length(); i++)
        {
            if (csa.index(i) == 0)
            {
                first = i;
            }

            t[i] = s.charAt((s.length() - 1 + csa.index(i)) % s.length());
        }

        BinaryStdOut.write(first);
        
        for (int i = 0; i < s.length(); i++)
        {
            BinaryStdOut.write(t[i]);
        }

        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    public static void inverseTransform()
    {
        int first = BinaryStdIn.readInt();

        ArrayList<Character> t = new ArrayList<Character>();
        
        int[] radix = new int[R];

        LinkedList<Integer>[] positions = (LinkedList<Integer>[]) new LinkedList[R];

        int length = 0;

        while (!BinaryStdIn.isEmpty())
        {
            char c = BinaryStdIn.readChar();

            t.add(c);

            radix[c]++;

            if (positions[c] == null)
            {
                positions[c] = new LinkedList<Integer>();
            }

            positions[c].offer(length);

            length++;
        }

        char[] f = new char[length];

        int j = 0;

        for (char i = 0; i < R; i++)
        {
            int cur = radix[i];

            while (cur-- > 0) f[j++] = i;
        }

        int[] next = new int[length];

        for (int i = 0; i < length; i++)
        {
            next[i] = positions[f[i]].poll();
        }

        int i = next[first];

        BinaryStdOut.write(f[first]);

        while (i != first)
        {
            BinaryStdOut.write(f[i]);

            i = next[i];
        }

        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    public static void main(String[] args)
    {
        if (args[0].equals("-")) transform();
        if (args[0].equals("+")) inverseTransform();
    }
}
