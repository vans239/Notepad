package notepad.view;

import java.awt.font.TextHitInfo;
import java.util.ArrayList;

public enum ArrowType {
    LEFT {
        @Override
        void updateCaretInsertionIndex(final MultiLineTextLayout mt) {
            mt.updateCaret(-1);
        }
    }, RIGHT {
        @Override
        void updateCaretInsertionIndex(final MultiLineTextLayout mt) {
            mt.updateCaret(1);
        }
    }, DOWN {
        @Override
        void updateCaretInsertionIndex(final MultiLineTextLayout mt) {
            ArrayList<TextLayoutInfo> layouts = mt.getLayouts();
            for (int i = 0; i < layouts.size(); ++i) {
                TextLayoutInfo textLayoutInfo = layouts.get(i);
                if (mt.caretInThisTextLayout(textLayoutInfo, i == layouts.size() - 1)) {
                    if (i + 1 < layouts.size()) {
                        mt.updateCaret(textLayoutInfo.getLayout().getCharacterCount());
                        return;
                    }
                }
            }
        }
    }, UP {
        @Override
        void updateCaretInsertionIndex(final MultiLineTextLayout mt) {
            ArrayList<TextLayoutInfo> layouts = mt.getLayouts();
            for (int i = 0; i < layouts.size(); ++i) {
                TextLayoutInfo textLayoutInfo = layouts.get(i);
                if (mt.caretInThisTextLayout(textLayoutInfo, i == layouts.size() - 1)) {
                    if (i >= 1) {
                        mt.updateCaret(-layouts.get(i - 1).getLayout().getCharacterCount());
                        return;
                    }
                }
            }
        }
    };

    abstract void updateCaretInsertionIndex(final MultiLineTextLayout mt);
}