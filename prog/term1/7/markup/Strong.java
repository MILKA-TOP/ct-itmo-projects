package markup;

import java.util.ArrayList;
import java.util.List;

public class Strong implements MarkdownCommands{

    List<MarkdownCommands> list = new ArrayList<>();

    public Strong(List<MarkdownCommands> getList) {
        for (int i = 0; i < getList.size(); i++) {
            list.add(getList.get(i));
        }
    }

    @Override
    public StringBuilder toMarkdown(StringBuilder getCoutText) {
        getCoutText.append("__");
        for (int i = 0; i < list.size(); i++) {
            getCoutText.append(list.get(i).toMarkdown(new StringBuilder()));
        }
        getCoutText.append("__");
        return getCoutText;
    }

    @Override
    public StringBuilder toBBCode(StringBuilder getCoutText) {
        getCoutText.append("[b]");
        for (int i = 0; i < list.size(); i++) {
            getCoutText.append(list.get(i).toBBCode(new StringBuilder()));
        }
        getCoutText.append("[/b]");
        return getCoutText;
    }
}
