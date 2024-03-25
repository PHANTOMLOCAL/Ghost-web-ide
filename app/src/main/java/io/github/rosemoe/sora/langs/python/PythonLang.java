package io.github.rosemoe.sora.langs.python;

import io.github.rosemoe.sora.interfaces.AutoCompleteProvider;
import io.github.rosemoe.sora.interfaces.CodeAnalyzer;
import io.github.rosemoe.sora.interfaces.EditorLanguage;
import io.github.rosemoe.sora.interfaces.NewlineHandler;
import io.github.rosemoe.sora.langs.internal.MyCharacter;
import io.github.rosemoe.sora.text.TextUtils;
import io.github.rosemoe.sora.widget.SymbolPairMatch;
import org.antlr.v4.runtime.Token;
import java.io.StringReader;
import org.antlr.v4.runtime.CharStreams;
import java.io.IOException;

public class PythonLang implements EditorLanguage {

  private NewlineHandler[] newlineHandlers = new NewlineHandler[] {new ColonHandler()};

  @Override
  public boolean useTab() {
    return true;
  }

  @Override
  public boolean isAutoCompleteChar(char ch) {
    return MyCharacter.isJavaIdentifierPart(ch) || ch == '.';
  }

  @Override
  public SymbolPairMatch getSymbolPairs() {
    return new SymbolPairMatch.DefaultSymbolPairs();
  }

  @Override
  public NewlineHandler[] getNewlineHandlers() {
    return newlineHandlers;
  }

  @Override
  public int getIndentAdvance(String content) {
    Token token;
    int advance = 0;
    boolean openBlock = false;
    try {
      PythonLexer lexer = new PythonLexer(CharStreams.fromReader(new StringReader(content)));
      while (((token = lexer.nextToken()) != null && token.getType() != token.EOF)) {
        switch (token.getType()) {
          case PythonLexer.CLASS:
          case PythonLexer.DEF:
          case PythonLexer.IF:
          case PythonLexer.ELIF:
          case PythonLexer.FOR:
          case PythonLexer.WHILE:
          case PythonLexer.ELSE:
          case PythonLexer.TRY:
          case PythonLexer.EXCEPT:
            openBlock = !openBlock;
            break;
          case PythonLexer.COLON:
            advance++;
            break;
          case PythonLexer.CONTINUE:
          case PythonLexer.BREAK:
            openBlock = !openBlock;
            advance--;
            break;
        }
      }
      advance = Math.max(0, advance);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return openBlock ? advance * 4 : 0;
  }


  @Override
  public AutoCompleteProvider getAutoCompleteProvider() {
    return new PythonAutoRun();
  }

  @Override
  public CodeAnalyzer getAnalyzer() {
    return new PythonCodeAnalyzer();
  }

  @Override
  public CharSequence format(CharSequence text) {
    return text;
  }

  class ColonHandler implements NewlineHandler {

    @Override
    public boolean matchesRequirement(String beforeText, String afterText) {
      return beforeText.endsWith(":");
    }

    @Override
    public HandleResult handleNewline(String beforeText, String afterText, int tabSize) {
      int count = TextUtils.countLeadingSpaceCount(beforeText, tabSize);
      int advanceBefore = getIndentAdvance(beforeText);
      int advanceAfter = getIndentAdvance(afterText);
      String text;
      StringBuilder sb =
          new StringBuilder("\n")
              .append(TextUtils.createIndent(count + advanceBefore, tabSize, useTab()))
              .append('\n')
              .append(text = TextUtils.createIndent(count + advanceAfter, tabSize, useTab()));
      int shiftLeft = text.length() + 1;
      return new HandleResult(sb, shiftLeft);
    }
  }
}
