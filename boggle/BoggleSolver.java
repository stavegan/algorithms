import java.util.TreeSet;

public class BoggleSolver
{
    private Node root;

    private class Node
    {
        public char value;
        public boolean word = false;
        public Node[] children = new Node[26];

        public Node(char value)
        {
            this.value = value;
        }
    }

    public BoggleSolver(String[] dictionary)
    {
        root = new Node('\0');

        for (int i = 0; i < dictionary.length; i++)
        {
            if (dictionary[i].length() < 3) continue;

            Node node = root;

            for (int j = 0; j < dictionary[i].length(); j++)
            {
                if (node.children[dictionary[i].charAt(j) - 'A'] == null)
                {
                    node.children[dictionary[i].charAt(j) - 'A'] = new Node(dictionary[i].charAt(j));
                }
                
                node = node.children[dictionary[i].charAt(j) - 'A'];
            }

            node.word = true;
        }
    }

    private void getAllValidWords(BoggleBoard board, int i, int j, boolean[][] marked, Node node, String word, TreeSet<String> words)
    {
        if (i < 0 || i >= board.rows() ||
            j < 0 || j >= board.cols() || marked[i][j]) return;

        node = node.children[board.getLetter(i, j) - 'A'];

        if (node == null) return;

        word = word + node.value;

        if (node.value == 'Q')
        {
            node = node.children['U' - 'A'];

            if (node == null) return;

            word = word + node.value;
        }

        if (node.word)
        {
            words.add(word);
        }

        marked[i][j] = true;

        getAllValidWords(board, i - 1, j - 1, marked, node, word, words);
        getAllValidWords(board, i - 1, j,     marked, node, word, words);
        getAllValidWords(board, i - 1, j + 1, marked, node, word, words);
        getAllValidWords(board, i, j - 1,     marked, node, word, words);
        getAllValidWords(board, i, j + 1,     marked, node, word, words);
        getAllValidWords(board, i + 1, j - 1, marked, node, word, words);
        getAllValidWords(board, i + 1, j,     marked, node, word, words);
        getAllValidWords(board, i + 1, j + 1, marked, node, word, words);

        marked[i][j] = false;
    }

    public Iterable<String> getAllValidWords(BoggleBoard board)
    {
        TreeSet<String> words = new TreeSet<>();

        for (int i = 0; i < board.rows(); i++)
        {
            for (int j = 0; j < board.cols(); j++)
            {
                getAllValidWords(board, i, j, new boolean[board.rows()][board.cols()], root, "", words);
            }
        }

        return words;
    }

    private boolean inDictionary(Node node, int i, String word)
    {
        if (node == null)
        {
            return false;
        }

        if (i == word.length() - 1)
        {
            return node.word;
        }

        return inDictionary(node.children[word.charAt(i + 1) - 'A'], i + 1, word);
    }
    
    private boolean inDictionary(String word)
    {
        return inDictionary(root.children[word.charAt(0) - 'A'], 0, word);
    }

    public int scoreOf(String word)
    {
        if (inDictionary(word))
        {
            if      (word.length() == 3) return 1;
            else if (word.length() == 4) return 1;
            else if (word.length() == 5) return 2;
            else if (word.length() == 6) return 3;
            else if (word.length() == 7) return 5;
            else if (word.length() >= 8) return 11;
        }

        return 0;
    }
}