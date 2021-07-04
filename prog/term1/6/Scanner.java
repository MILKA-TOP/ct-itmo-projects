import java.io.*;
import java.util.Arrays;

public class Scanner {
    private Reader reader;
    private char[] bufferArr = new char[8192];
    private boolean boolHasNext = true;
    private int position = 0, size = 0;

    public Scanner(InputStream is) {
        reader = new BufferedReader(new InputStreamReader(is));
        read();
    }
	
	public Scanner (Reader is) {
		reader = is;
        read();
	}

    public Scanner(String str) {
        reader = new StringReader(str);
        read();
    }

    public Scanner(File file) {
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            read();
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public boolean hasNext() {
        nextChar();
        position--;
        return boolHasNext;
    }
	
    public String next() {
        skipWhiteSpace();
        StringBuilder sb = new StringBuilder();
        while (hasNext()) {
            char c = nextChar();
            if (Character.isWhitespace(c)) {
                break;
            }
            
            sb.append(c);

        }
        return sb.toString();
    }

	public String nextWord() {
		skipWhiteSpace();
        StringBuilder sb = new StringBuilder();
		while (hasNext()) {
            char c = nextChar();
            if (c == '\'' || Character.getType(c) == Character.DASH_PUNCTUATION || Character.isAlphabetic(c)) {
                sb.append(c);
            } else {
				break;
			}
        }
        return sb.toString();
	}

	public boolean hasNextLine() {
		return hasNext();
	}

    public String nextLine() {
        StringBuilder sb = new StringBuilder();
        char c;
        while (hasNext()) {
            c = nextChar();
            if (c == '\n') {
                break;
            }
            sb.append(c);
            
        }
        return sb.toString();
    }

    public int nextInt() {
        return Integer.parseInt(next());
    }
	
	public long nextLong() {
        return Long.parseLong(next());
    }

    private void skipWhiteSpace() {
        if (hasNext()) {
            char c = nextChar();
            while (boolHasNext && Character.isWhitespace(c)) {
                c = nextChar();
            }
        }
        position--;
    }


    private void read() {
        try {
            size = reader.read(bufferArr);
            while (size == 0) {					
                size = reader.read(bufferArr);
            }
            
            if (size == -1) {
                boolHasNext = false;
            }
            position = 0;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private char nextChar() {
        if (position >= size) {
            read();
        }
        return bufferArr[position++];
    }
}