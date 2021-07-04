package markup;

import java.util.ArrayList;
import java.util.List;

public class Emphasis implements MarkdownCommands {
    List<MarkdownCommands> list = new ArrayList<>();

    public Emphasis(List<MarkdownCommands> getList) {
        for (int i = 0; i < getList.size(); i++) {
            list.add(getList.get(i));
        }
    }

    @Override
    public StringBuilder toMarkdown(StringBuilder getCoutText) {
        getCoutText.append("*");
        for (int i = 0; i < list.size(); i++) {
            getCoutText.append(list.get(i).toMarkdown(new StringBuilder()));
        }
        getCoutText.append("*");
        return getCoutText;
    }

    @Override
    public StringBuilder toBBCode(StringBuilder getCoutText) {
        getCoutText.append("[i]");
        for (int i = 0; i < list.size(); i++) {
            getCoutText.append(list.get(i).toMarkdown(new StringBuilder()));
        }
        getCoutText.append("[/i]");
        return getCoutText;
    }
}
