package markup;

import java.util.ArrayList;
import java.util.List;

public class Paragraph implements MarkdownCommands{

    List<MarkdownCommands> list = new ArrayList<>();

    public Paragraph(List<MarkdownCommands> getList) {
        for (int i = 0; i < getList.size(); i++) {
            list.add(getList.get(i));
        }
    }


    @Override
    public StringBuilder toMarkdown(StringBuilder getCoutText) {
        for (int i = 0; i < list.size(); i++) {
            getCoutText.append(list.get(i).toMarkdown(new StringBuilder()));
        }
        return getCoutText;
    }

    @Override
    public StringBuilder toBBCode(StringBuilder getCoutText) {
        for (int i = 0; i < list.size(); i++) {
            getCoutText.append(list.get(i).toBBCode(new StringBuilder()));
        }
        return getCoutText;
    }
}
