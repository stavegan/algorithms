import java.util.Arrays;
import java.util.Comparator;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray
{
    private Integer length, index[];

    public CircularSuffixArray(String s)
    {
        if (s == null)
        {
            throw new IllegalArgumentException("Argument can't be null!");
        }

        length = s.length();

        index = new Integer[length];

        for (int i = 0; i < length; i++)
        {
            index[i] = i;
        }

        Arrays.sort(index, new Comparator<Integer>()
        {
            private int compare(Integer p, Integer q, int step)
            {
                if (step == length) return 0;

                if (s.charAt(p) == s.charAt(q))
                {
                    return compare((p + 1) % length,
                                   (q + 1) % length, step + 1);
                }

                return s.charAt(p) - s.charAt(q);
            }

            @Override
            public int compare(Integer p, Integer q)
            {
                return compare(p, q, 0);
            }
        });
    }

    public int length()
    {
        return length;
    }

    public int index(int i)
    {
        if (i < 0 || i >= length)
        {
            throw new IllegalArgumentException("Index out of range!");
        }

        return index[i];
    }

    public static void main(String[] args)
    {
        String s = StdIn.readString();

        CircularSuffixArray csa = new CircularSuffixArray(s);

        for (int i = 0; i < csa.length(); i++)
        {
            StdOut.println(csa.index(i));
        }
    }
}
