package io.github.rosemoe.sora.langs.xml;


import io.github.rosemoe.sora.interfaces.CodeAnalyzer;
import io.github.rosemoe.sora.langs.xml.analyzer.BasicSyntaxPullAnalyzer;
import io.github.rosemoe.sora.langs.xml.analyzer.HighLightAnalyzer;
import io.github.rosemoe.sora.text.TextAnalyzeResult;
import io.github.rosemoe.sora.text.TextAnalyzer;

public class XMLAnalyzer implements CodeAnalyzer {
    private final HighLightAnalyzer highLightAnalyzer = new HighLightAnalyzer();
    //private final BasicSyntaxSaxAnalyzer basicSyntaxAnalyzer = new BasicSyntaxSaxAnalyzer();
    private final BasicSyntaxPullAnalyzer basicSyntaxAnalyzer = new BasicSyntaxPullAnalyzer();
    private boolean syntaxCheckEnable;

    public boolean isSyntaxCheckEnable() {
        return syntaxCheckEnable;
    }

    public void setSyntaxCheckEnable(boolean syntaxCheckEnable) {
        this.syntaxCheckEnable = syntaxCheckEnable;
    }

    @Override
    public void analyze(CharSequence content, TextAnalyzeResult colors, TextAnalyzer.AnalyzeThread.Delegate delegate) {
        if (content.toString().isEmpty()) {
            return;
        }
        //high light analyze first to get lastLine.
        highLightAnalyzer.analyze(content, colors, delegate);

        if (syntaxCheckEnable)
            basicSyntaxAnalyzer.analyze(content, colors, delegate);

        colors.determine(highLightAnalyzer.getLastLine());
    }

}
