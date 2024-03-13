/*
 *    sora-editor - the awesome code editor for Android
 *    https://github.com/Rosemoe/CodeEditor
 *    Copyright (C) 2020-2021  Rosemoe
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 *
 *     Please contact Rosemoe by email 2073412493@qq.com if you need
 *     additional information or have any questions
 */
package io.github.rosemoe.sora.langs.css3;

import android.graphics.Color;
import io.github.rosemoe.sora.data.Span;
import io.github.rosemoe.sora.langs.css3.css3Lexer;
import io.github.rosemoe.sora.text.TextStyle;
import io.github.rosemoe.sora.widget.CodeEditor;
import java.util.regex.Pattern;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.Token;

import java.io.IOException;
import java.io.StringReader;

import io.github.rosemoe.sora.interfaces.CodeAnalyzer;
import io.github.rosemoe.sora.text.TextAnalyzeResult;
import io.github.rosemoe.sora.text.TextAnalyzer;
import io.github.rosemoe.sora.widget.EditorColorScheme;

/**
 * Simple implementation of highlight CSS3. The color matching may not be beautiful and reasonable.
 * Part of it depends on css3.g4. You can continue to expand according to your needs.
 */
public class CSS3Analyzer implements CodeAnalyzer {
  protected CodeEditor soraEditor;

  public CSS3Analyzer(CodeEditor editor) {
    this.soraEditor = editor;
  }

  @Override
  public void analyze(
      CharSequence content,
      TextAnalyzeResult result,
      TextAnalyzer.AnalyzeThread.Delegate delegate) {
    try {
      CodePointCharStream stream = CharStreams.fromReader(new StringReader(content.toString()));
      css3Lexer lexer = new css3Lexer(stream);
      Token token;
      boolean first = true;
      int lastLine = 1;
      int line, column;
      while (delegate.shouldAnalyze()) {
        token = lexer.nextToken();
        if (token == null) break;
        if (token.getType() == css3Lexer.EOF) {
          lastLine = token.getLine() - 1;
          break;
        }
        line = token.getLine() - 1;
        column = token.getCharPositionInLine();
        lastLine = line;

        // Log.d("test",token.getText()+"  "+token.getType());
        switch (token.getType()) {
          case css3Lexer.Space:
            if (first) result.addNormalIfNull();
            break;
            // 运算符
          case css3Lexer.Url:
          case css3Lexer.Cdo:
          case css3Lexer.Cdc:
          case css3Lexer.Includes:
          case css3Lexer.DashMatch:
          case css3Lexer.Hash:
          case css3Lexer.Import:
          case css3Lexer.Page:
          case css3Lexer.Media:
          case css3Lexer.Namespace:
          case css3Lexer.Charset:
          case css3Lexer.Important:
          case css3Lexer.Percentage:
          case css3Lexer.Url_:
            result.addIfNeeded(
                line,
                column,
                TextStyle.makeStyle(EditorColorScheme.OPERATOR, 0, true, false, false));
            break;
            // 单位
          case css3Lexer.Equal:
          case css3Lexer.Colon:
          case css3Lexer.Dot:
          case css3Lexer.Multiply:
          case css3Lexer.Divide:
          case css3Lexer.Pipe:
          case css3Lexer.Underscore:
            result.addIfNeeded(line, column, EditorColorScheme.ATTRIBUTE_VALUE);
            break;
            // @

            // 函数
          case css3Lexer.UnicodeRange:
          case css3Lexer.MediaOnly:
          case css3Lexer.Not:
          case css3Lexer.And:
          case css3Lexer.Dimension:
          case css3Lexer.UnknownDimension:
          case css3Lexer.Plus:
          case css3Lexer.Minus:
          case css3Lexer.Greater:
          case css3Lexer.Comma:
          case css3Lexer.OpenBracket:
          case css3Lexer.CloseBracket:
          case css3Lexer.OpenParen:
          case css3Lexer.CloseParen:
          case css3Lexer.OpenBrace:
          case css3Lexer.CloseBrace:
          case css3Lexer.SemiColon:
            result.addIfNeeded(
                line,
                column,
                TextStyle.makeStyle(EditorColorScheme.BLOCK_LINE, 0, false, true, false));
            break;
            // 数字
          case css3Lexer.Tilde:
          case css3Lexer.PseudoNot:
          case css3Lexer.Number:
            result.addIfNeeded(
                line,
                column,
                TextStyle.makeStyle(
                    EditorColorScheme.AUTO_COMP_PANEL_CORNER, 0, false, true, false));
            break;
            // 名字
          case css3Lexer.PrefixMatch:
          case css3Lexer.SuffixMatch:
          case css3Lexer.SubstringMatch:
          case css3Lexer.FontFace:
          case css3Lexer.Supports:
          case css3Lexer.Or:
            result.addIfNeeded(
                line,
                column,
                TextStyle.makeStyle(EditorColorScheme.KEYWORD, 0, true, false, false));
            break;
            // 字符串
          case css3Lexer.String_:
            result.addIfNeeded(
                line,
                column,
                TextStyle.makeStyle(EditorColorScheme.LITERAL, 0, true, false, false));
            break;
            // 颜色
          case css3Lexer.Keyframes:
          case css3Lexer.From:
          case css3Lexer.To:
          case css3Lexer.Calc:
            result.addIfNeeded(
                line,
                column,
                TextStyle.makeStyle(EditorColorScheme.LINE_NUMBER, 0, true, false, false));
            break;
            // 注释
          case css3Lexer.Comment:
            result.addIfNeeded(
                line, column, TextStyle.makeStyle(EditorColorScheme.COMMENT, 0, false, true, true));
            break;
          case css3Lexer.Viewport:
          case css3Lexer.CounterStyle:
          case css3Lexer.FontFeatureValues:
          case css3Lexer.DxImageTransform:
            result.addIfNeeded(
                line, column, TextStyle.makeStyle(EditorColorScheme.LITERAL, 0, true, true, false));
            break;
          case css3Lexer.AtKeyword:
          case css3Lexer.Variable:
          case css3Lexer.Var:
          case css3Lexer.Ident:
          case css3Lexer.Function_:
            result.addIfNeeded(
                line,
                column,
                TextStyle.makeStyle(EditorColorScheme.IDENTIFIER_NAME, 0, false, true, false));
            break;

          default:
            result.addIfNeeded(line, column, EditorColorScheme.TEXT_NORMAL);
            
            break;
        }

        first = false;
      }
      result.determine(lastLine);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String[] getColor = {"red", "blue", "green"};

  public void installColor(TextAnalyzeResult color, CodeEditor editor) {
    try {
      var textSpan = editor.getText().toString();
      var selection = editor.getCursor().getLeft();
      var pattern = Pattern.compile("(#?)(\\w+)");
      var matcher = pattern.matcher(textSpan);
      var start = 0;
      var end = 0;
      var currentWord = "";
      var currentWordddddddd = "";
      while (matcher.find()) {
        start = matcher.start();
        end = matcher.end();
        if (start <= selection && selection <= end) {
          currentWordddddddd = textSpan.substring(start, end).toString();
          currentWord = currentWordddddddd;
        }
      }
      if (!currentWord.isEmpty()) {
        if (currentWord.contains("#")) {
          Span span = Span.obtain(start, EditorColorScheme.LITERAL);
          span.setUnderlineColor(Color.parseColor(currentWord));
          color.add(selection, span);
        }
      }
    } catch (Exception err) {
      err.printStackTrace();
    }
  }
}
