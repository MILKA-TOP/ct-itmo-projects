package markup;

public class Text implements MarkdownCommands {
    String coutText;
    public Text(String getLine) {
        this.coutText = getLine;
    }

    @Override
    public StringBuilder toMarkdown(StringBuilder getCoutText) {
        return  getCoutText.append(coutText);
    }

    @Override
    public StringBuilder toBBCode(StringBuilder getCoutText) {
        return  getCoutText.append(coutText);
    }
}
