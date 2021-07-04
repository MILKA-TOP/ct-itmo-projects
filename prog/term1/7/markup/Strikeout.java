package markup;

import java.util.ArrayList;
import java.util.List;

public class Strikeout implements MarkdownCommands{
    List<MarkdownCommands> list = new ArrayList<>();

    public Strikeout(List<MarkdownCommands> getList) {
        for (int i = 0; i < getList.size(); i++) {
            list.add(getList.get(i));
        }
    }

    @Override
    public StringBuilder toMarkdown(StringBuilder getCoutText) {
        getCoutText.append("~");
        for (int i = 0; i < list.size(); i++) {
            getCoutText.append(list.get(i).toMarkdown(new StringBuilder()));
        }
        getCoutText.append("~");
        return getCoutText;
    }

    @Override
    public StringBuilder toBBCode(StringBuilder getCoutText) {
        getCoutText.append("[s]");
        for (int i = 0; i < list.size(); i++) {
            getCoutText.append(list.get(i).toBBCode(new StringBuilder()));
        }
        getCoutText.append("[/s]");
        return getCoutText;
    }
}
