package md2html;

public class Md2HtmlConvert implements CharSource {

    private String mdText;
    private StringBuilder htmlText;
    private int position = 0;
    private int levelHeader = 0;
    private String addWithoutTags;
    private boolean goOutFromWhile;
    private boolean isItHeader;

    public String convert(String text) {
        this.mdText = text;
        this.htmlText = new StringBuilder();

        while (hasNext()) {
            analyzePartOfText();
        }

        return htmlText.toString();
    }


    private void analyzePartOfText() {
        isItHeader = false;
        char ch = next();
        skipEndOfLine(ch);
        ch = next();
        if (hasNext() && ch == '#' && (now() == ' ' || now() == '#')) {
            isItHeader = true;
            levelHeader++;
            parseHeaderLevel();
            if (isItHeader) {
                htmlText.append("<h").append(levelHeader).append(">");
                htmlText.append(parseParagraph(next(), Md2HtmlTags.NULL)).append("</h").append(levelHeader).append(">\n");
                levelHeader = 0;
            }
        }
        if (!isItHeader) {
            htmlText.append("<p>").append(parseParagraph(ch, Md2HtmlTags.NULL)).append("</p>\n");
        }
    }

    private void skipEndOfLine(char ch) {
        while (ch == '\n') {
            ch = next();
        }
        position--;
    }

    private String parseParagraph(char ch, Md2HtmlTags checkTag) {
        StringBuilder name = new StringBuilder();
        String tempStringAppend;
        goOutFromWhile = false;
        do {
            if (now() == ch && (ch == '*' || ch == '_')) {

                tempStringAppend = completeEm(ch, checkTag, "<strong>", "</strong>", true);
                if (goOutFromWhile) break;
                name.append(tempStringAppend);

            } else if (now() == ch && (ch == '-')) {

                tempStringAppend = completeOperation(ch, checkTag, Md2HtmlTags.STRIKE, "<s>", "</s>");
                if (goOutFromWhile) break;
                name.append(tempStringAppend);

            } else if (ch == '~') {

                tempStringAppend = completeOperation(ch, checkTag, Md2HtmlTags.MARK, "<mark>", "</mark>");
                if (goOutFromWhile) break;
                name.append(tempStringAppend);

            } else if (ch == '`') {

                tempStringAppend = completeOperation(ch, checkTag, Md2HtmlTags.CODE, "<code>", "</code>");
                if (goOutFromWhile) break;
                name.append(tempStringAppend);


            } else if (ch == '*' || ch == '_') {

                tempStringAppend = completeEm(ch, checkTag, "<em>", "</em>", false);
                if (goOutFromWhile) break;
                name.append(tempStringAppend);

            } else if (ch == '\\') {
                name.append(next());
            } else if (ch == '<') {
                name.append("&lt;");
            } else if (ch == '>') {
                name.append("&gt;");
            } else if (ch == '&') {
                name.append("&amp;");
            } else {
                name.append(ch);
            }
            if (hasNext()) ch = next();
            else break;
        } while ((now() != '\n' || ch != '\n'));

        if (!goOutFromWhile && checkTag != Md2HtmlTags.NULL) {
            addWithoutTags = name.toString();
            return "";
        }
        if (checkTag == Md2HtmlTags.NULL && now() != '\n') name.append(now());
        if (checkTag == Md2HtmlTags.STRONG_M || checkTag == Md2HtmlTags.STRONG_S
                || checkTag == Md2HtmlTags.STRIKE) position++;
        goOutFromWhile = false;
        return name.toString();
    }

    private String completeOperation(char ch, Md2HtmlTags checkTag, Md2HtmlTags strike, String s, String s1) {
        StringBuilder temp = new StringBuilder();
        if (checkTag == strike) {
            goOutFromWhile = true;
            return "";
        }

        String out;
        if (strike == Md2HtmlTags.CODE) out = parseParagraph(next(), strike);
        else out = parseParagraph(nextTwo(), strike);

        if (out.equals("")) {
            return temp.append(ch).append(addWithoutTags).toString();
        } else {
            return temp.append(s).append(out).append(s1).toString();
        }
    }

    private String completeEm(char ch, Md2HtmlTags checkTag, String s, String s1, boolean isItStrong) {
        Md2HtmlTags typeTag;
        StringBuilder temp = new StringBuilder();
        if (isItStrong) {
            if (checkTag == Md2HtmlTags.STRONG_S && ch == '*'
                    || checkTag == Md2HtmlTags.STRONG_M && ch == '_') {
                goOutFromWhile = true;
                return "";
            }
            if (ch == '*') typeTag = Md2HtmlTags.STRONG_S;
            else typeTag = Md2HtmlTags.STRONG_M;
        } else {
            if (checkTag == Md2HtmlTags.SELECTION_S && ch == '*'
                    || checkTag == Md2HtmlTags.SELECTION_M && ch == '_') {
                goOutFromWhile = true;
                return "";
            }
            if (ch == '*') typeTag = Md2HtmlTags.SELECTION_S;
            else typeTag = Md2HtmlTags.SELECTION_M;
        }
        String out;
        if (isItStrong) out = parseParagraph(nextTwo(), typeTag);
        else out = parseParagraph(next(), typeTag);
        if (out.equals("")) {
            return temp.append(ch).append(addWithoutTags).toString();
        } else {
            return temp.append(s).append(out).append(s1).toString();
        }
    }


    private void parseHeaderLevel() {
        char ch = next();
        while (ch == '#' && levelHeader < 6) {
            levelHeader++;
            ch = next();
        }
        if (ch != ' ') {
            isItHeader = false;
            position = 0;
        }
    }

    @Override
    public boolean hasNext() {
        return position + 1 < mdText.length();
    }


    @Override
    public char next() {
        return mdText.charAt(position++);
    }

    @Override
    public char now() {
        return mdText.charAt(position);
    }

    @Override
    public char nextTwo() {
        position++;
        return mdText.charAt(position++);
    }
}
