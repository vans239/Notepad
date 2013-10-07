package notepad.view;

import java.awt.font.TextHitInfo;
import java.util.ArrayList;

public enum ArrowType {
    LEFT {
        @Override
        int updateCaretInsertionIndex(final MultiLineTextLayout mt) {
            return -1;
        }
    }, RIGHT {
        @Override
        int updateCaretInsertionIndex(final MultiLineTextLayout mt) {
            return 1;
        }
    }, DOWN {
        @Override
        int updateCaretInsertionIndex(final MultiLineTextLayout mt) {
            ArrayList<TextLayoutInfo> layouts = mt.getLayouts();
            for (int i = 0; i < layouts.size(); ++i) {
                TextLayoutInfo textLayoutInfo = layouts.get(i);
                if (mt.caretInThisTextLayout(textLayoutInfo, i == layouts.size() - 1)) {
                    if (i + 1 < layouts.size()) {
                        return textLayoutInfo.getLayout().getCharacterCount();
                    }
                }
            }
            return 0;
        }
    }, UP {
        @Override
        int updateCaretInsertionIndex(final MultiLineTextLayout mt) {
            ArrayList<TextLayoutInfo> layouts = mt.getLayouts();
            for (int i = 0; i < layouts.size(); ++i) {
                TextLayoutInfo textLayoutInfo = layouts.get(i);
                if (mt.caretInThisTextLayout(textLayoutInfo, i == layouts.size() - 1)) {
                    if (i >= 1) {
                        return -layouts.get(i - 1).getLayout().getCharacterCount();
                    }
                }
            }
            return 0;
        }
    };

    abstract int updateCaretInsertionIndex(final MultiLineTextLayout mt);
}