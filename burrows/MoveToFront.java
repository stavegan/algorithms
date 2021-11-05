import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront
{
    private static final int R = 256;

    public static void encode()
    {
        char[] radix = new char[R];

        for (char i = 0; i < R; i++)
        {
            radix[i] = i;
        }

        while (!BinaryStdIn.isEmpty())
        {
            char c = BinaryStdIn.readChar();

            BinaryStdOut.write(radix[c]);
            
            for (char i = 0; i < R; i++)
            {
                if (radix[i] < radix[c])
                {
                    radix[i]++;
                }
            }

            radix[c] = 0;
        }

        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    public static void decode()
    {
        char[] radix = new char[R];

        for (char i = 0; i < R; i++)
        {
            radix[i] = i;
        }

        while (!BinaryStdIn.isEmpty())
        {
            char c = BinaryStdIn.readChar(),
            letter = radix[c];

            BinaryStdOut.write(letter);

            for (char i = c; i > 0; i--)
            {
                radix[i] = radix[i - 1];
            }

            radix[0] = letter;
        }

        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    public static void main(String[] args)
    {
        if (args[0].equals("-")) encode();
        if (args[0].equals("+")) decode();
    }
}
