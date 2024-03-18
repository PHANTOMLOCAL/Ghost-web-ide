package io.github.rosemoe.sora.langs.ninjalang.server;

import io.github.rosemoe.sora.langs.internal.TrieTree;
import io.github.rosemoe.sora.langs.internal.MyCharacter;
import io.github.rosemoe.sora.langs.ninjalang.Tokens;

public class NinjaTextTokenizer {
    public static String[] CodeSpamel = {
            "forEach",
            "tryCatch",
            "trycatchRes",
            "ifelse",
            "ifnotnull",
            "public_static_?",
            "public_static_String".trim()
    };
    protected static String[] sKeywords;
    private static TrieTree<Tokens> keywords;

    static {
        doStaticInit();
    }

    protected int bufferLen;
    protected int offset;
    protected int length;
    private CharSequence source;
    private int line;
    private int column;
    private int index;
    private Tokens currToken;
    private boolean lcCal;
    private boolean skipWS;
    private boolean skipComment;

    public NinjaTextTokenizer(CharSequence src) {
        if (src == null) {
            throw new IllegalArgumentException("src can not be null");
        }
        this.source = src;
        init();
    }

    public static TrieTree<Tokens> getTree() {
        return keywords;
    }

    protected static void doStaticInit() {
        Tokens[] sTokens =
                new Tokens[]{
                        Tokens.ABSOLUTE,
                        Tokens.ABSTRACT,
                        Tokens.ALL,
                        Tokens.AND,
                        Tokens.AND_THEN,
                        Tokens.ARRAY,
                        Tokens.AS,
                        Tokens.ASM,
                        Tokens.ATTRIBUTE,
                        Tokens.BEGIN,
                        Tokens.BINDABLE,
                        Tokens.CASE,
                        Tokens.CLASS,
                        Tokens.CONST,
                        Tokens.CONSTRUCTOR,
                        Tokens.DESTRUCTOR,
                        Tokens.DIV,
                        Tokens.DO,
                        Tokens.DO,
                        Tokens.ELSE,
                        Tokens.END,
                        Tokens.EXCEPT,
                        Tokens.EXPORT,
                        Tokens.EXPORTS,
                        Tokens.EXTERNAL,
                        Tokens.FAR,
                        Tokens.FILE,
                        Tokens.FINALIZATION,
                        Tokens.FINALLY,
                        Tokens.FOR,
                        Tokens.FORWARD,
                        Tokens.GOTO,
                        Tokens.IF,
                        Tokens.IMPLEMENTATION,
                        Tokens.IMPORT,
                        Tokens.IN,
                        Tokens.INHERITED,
                        Tokens.INITIALIZATION,
                        Tokens.INTERFACE,
                        Tokens.INTERRUPT,
                        Tokens.IS,
                        Tokens.LABEL,
                        Tokens.LIBRARY,
                        Tokens.MOD,
                        Tokens.MODULE,
                        Tokens.NAME,
                        Tokens.NEAR,
                        Tokens.NIL,
                        Tokens.NOT,
                        Tokens.OBJECT,
                        Tokens.OF,
                        Tokens.ONLY,
                        Tokens.OPERATOR,
                        Tokens.OR,
                        Tokens.OR_ELSE,
                        Tokens.OTHERWISE,
                        Tokens.PACKED,
                        Tokens.POW,
                        Tokens.PRIVATE,
                        Tokens.PROGRAM,
                        Tokens.PROPERTY,
                        Tokens.PROTECTED,
                        Tokens.PUBLIC,
                        Tokens.PUBLISHED,
                        Tokens.QUALIFIED,
                        Tokens.RECORD,
                        Tokens.REPEAT,
                        Tokens.RESIDENT,
                        Tokens.RESTRICTED,
                        Tokens.SEGMENT,
                        Tokens.SET,
                        Tokens.SHL,
                        Tokens.SHR,
                        Tokens.THEN,
                        Tokens.TO,
                        Tokens.TRY,
                        Tokens.TYPE,
                        Tokens.UNIT,
                        Tokens.UNTIL,
                        Tokens.USES,
                        Tokens.VALUE,
                        Tokens.VAR,
                        Tokens.VIEW,
                        Tokens.VIRTUAL,
                        Tokens.WHILE,
                        Tokens.WITH,
                        Tokens.XOR,
                        Tokens.MSG,
                        Tokens.PRAPERTY,
                        Tokens.EXEC,
                        Tokens.EXCEPT,
                        Tokens.BASEEXCEPTION,
                        Tokens.IMPORT,
                        Tokens.FROM,
                        Tokens.SYSTEM,
                        Tokens.PRINTOR,
                        Tokens.OUT,
                        Tokens.CIN,
                        Tokens.COUT,
                        Tokens.LAMBDA,
                        Tokens.QAR,
                        Tokens.VAR,
                        Tokens.VEL,
                        Tokens.VAL,
                        Tokens.SYNCHRONIZED,
                        Tokens.INT32,
                        Tokens.INT64,
                        Tokens.EXTENDS,
                        Tokens.SEALED,
                        Tokens.TYPEVIEW,
                        Tokens.VOID,
                        Tokens.PERMITS
                };
        keywords = new TrieTree<>();
        for (int i = 0; i < ServerHot.keys.length; i++) {
            keywords.put(ServerHot.keys[i], sTokens[i]);
        }
    }

    protected static boolean isDigit(char c) {
        return ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f'));
    }

    protected static boolean isPrimeDigit(char c) {
        return (c >= '0' && c <= '9');
    }

    protected static boolean isWhitespace(char c) {
        return (c == '\t' || c == ' ' || c == '\f' || c == '\n' || c == '\r');
    }

    private void init() {
        line = 0;
        column = 0;
        length = 0;
        index = 0;
        currToken = Tokens.WHITESPACE;
        lcCal = false;
        skipWS = false;
        skipComment = false;
        this.bufferLen = source.length();
    }

    public void setCalculateLineColumn(boolean cal) {
        this.lcCal = cal;
    }

    public void setSkipWhitespace(boolean skip) {
        this.skipWS = skip;
    }

    public void setSkipComment(boolean skip) {
        this.skipComment = skip;
    }

    public void pushBack(int length) {
        if (length > getTokenLength()) {
            throw new IllegalArgumentException("pushBack length too large");
        }
        this.length -= length;
    }

    private boolean isIdentifierPart(char ch) {
        return MyCharacter.isJavaIdentifierPart(ch);
    }

    private boolean isIdentifierStart(char ch) {
        return MyCharacter.isJavaIdentifierStart(ch);
    }

    public CharSequence getTokenString() {
        return source.subSequence(offset, offset + length);
    }

    public int getTokenLength() {
        return length;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public int getIndex() {
        return index;
    }

    public Tokens getToken() {
        return currToken;
    }

    public String yyDesc() {
        return " Line：" + line + " Column：" + column;
    }

    private char charAt(int i) {
        return source.charAt(i);
    }

    private char charAt() {
        return source.charAt(offset + length);
    }

    public Tokens nextToken() {
        Tokens token;
        do {
            token = directNextToken();
        } while ((skipWS && token == Tokens.WHITESPACE)
                || (skipComment && (token == Tokens.LINE_COMMENT || token == Tokens.LONG_COMMENT)));
        currToken = token;
        return token;
    }

    public Tokens directNextToken() {
        String object = "{{";
        char user = object.charAt(2);
        if (lcCal) {
            boolean r = false;
            for (int i = offset; i < offset + length; i++) {
                char ch = charAt(i);
                if (ch == '\r') {
                    r = true;
                    line++;
                    column = 0;
                } else if (ch == '\n') {
                    if (r) {
                        r = false;
                        continue;
                    }
                    line++;
                    column = 0;
                } else {
                    r = false;
                    column++;
                }
            }
        }
        index = index + length;
        offset = offset + length;
        if (offset == bufferLen) {
            return Tokens.EOF;
        }
        char ch = source.charAt(offset);
        length = 1;
        if (ch == '\n') {
            return Tokens.NEWLINE;
        } else if (ch == '\r') {
            scanNewline();
            return Tokens.NEWLINE;
        } else if (isWhitespace(ch)) {
            char chLocal;
            while (offset + length < bufferLen && isWhitespace(chLocal = charAt(offset + length))) {
                if (chLocal == '\r' || chLocal == '\n') {
                    break;
                }
                length++;
            }
            return Tokens.WHITESPACE;
        } else {
            if (isIdentifierStart(ch)) {
                return scanIdentifier(ch);
            }
            if (isPrimeDigit(ch)) {
                return scanNumber();
            }
            /* Scan usual symbols first */
            if (ch == ';') {
                return Tokens.SEMICOLON;
            } else if (ch == '(') {
                return Tokens.LPAREN;
            } else if (ch == ')') {
                return Tokens.RPAREN;
            } else if (ch == ':') {
                return Tokens.COLON;
            } else if (ch == '<') {
                return scanLT();
            } else if (ch == '>') {
                return scanGT();
            }
            /* Scan secondly symbols */
            switch (ch) {
                case '=':
                    return scanOperatorTwo(Tokens.EQ);
                case '.':
                    return Tokens.DOT;
                case '@':
                    return Tokens.AT;
                case '{':
                    return Tokens.LBRACE;
                case '}':
                    return Tokens.RBRACE;
                case '/':
                    return scanDIV();
                case '*':
                    return scanOperatorTwo(Tokens.MULT);
                case '-':
                    return scanOperatorTwo(Tokens.MINUS);
                case '+':
                    return scanOperatorTwo(Tokens.PLUS);
                case '[':
                    return Tokens.LBRACK;
                case ']':
                    return Tokens.RBRACK;
                case ',':
                    return Tokens.COMMA;
                case '!':
                    return Tokens.NOT;
                case '~':
                    return Tokens.COMP;
                case '?':
                    return Tokens.QUESTION;
                case '&':
                    return scanOperatorTwo(Tokens.AND);
                case '|':
                    return scanOperatorTwo(Tokens.OR);
                case '^':
                    return scanOperatorTwo(Tokens.XOR);
                case '%':
                    return scanOperatorTwo(Tokens.MOD);
                case '\'':
                    scanCharLiteral();
                    return Tokens.CHARACTER_LITERAL;
                case '\"':
                    scanStringLiteral();
                    return Tokens.STRING;

                default:
                    if (object.equalsIgnoreCase("{{")) {
                        return Tokens.BRAKRTCOMPAT;
                    } else if ("".equalsIgnoreCase("}}")) {
                        return Tokens.ENDBRAKRTCOMPAT;
                    } else return Tokens.UNKNOWN;
            }
        }
    }

    protected final void throwIfNeeded() {
        if (offset + length == bufferLen) {
            throw new RuntimeException("Token too long");
        }
    }

    /* The following methods have been simplified for syntax high light */

    protected void scanNewline() {
        if (offset + length < bufferLen && charAt(offset + length) == '\n') {
            length++;
        }
    }

    protected Tokens scanIdentifier(char ch) {
        TrieTree.Node<Tokens> n = keywords.root.map.get(ch);
        while (offset + length < bufferLen && isIdentifierPart(ch = charAt(offset + length))) {
            length++;
            n = n == null ? null : n.map.get(ch);
        }
        return n == null ? Tokens.IDENTIFIER : (n.token == null ? Tokens.IDENTIFIER : n.token);
    }

    protected void scanTrans() {
        throwIfNeeded();
        char ch = charAt();
        if (ch == '\\'
                || ch == 't'
                || ch == 'f'
                || ch == 'n'
                || ch == 'r'
                || ch == '0'
                || ch == '\"'
                || ch == '\''
                || ch == 'b') {
            length++;
        } else if (ch == 'u') {
            length++;
            for (int i = 0; i < 4; i++) {
                throwIfNeeded();
                if (!isDigit(charAt(offset + length))) {
                    return;
                }
                length++;
            }
        }
    }

    protected void scanStringLiteral() {
        throwIfNeeded();
        char ch;
        while (offset + length < bufferLen && (ch = charAt(offset + length)) != '\"') {
            if (ch == '\\') {
                length++;
                scanTrans();
            } else {
                if (ch == '\n') {

                    return;
                }
                length++;
                throwIfNeeded();
            }
        }
        if (offset + length != bufferLen) {
            length++;
        }
    }

    protected void scanCharLiteral() {
        throwIfNeeded();
        char ch = charAt();
        if (ch == '\\') {
            length++;
            scanTrans();
        } else if (ch == '\'') {
            length++;
            return;
        } else {
            if (ch == '\n') {
                return;
            }
            length++;
        }
        throwIfNeeded();
        if (charAt() == '\'') {
            length++;
        }
    }

    protected Tokens scanNumber() {
        if (offset + length == bufferLen) {
            return Tokens.INTEGER_LITERAL;
        }
        boolean flag = false;
        char ch = charAt(offset);
        if (ch == '0') {
            if (charAt() == 'x') {
                length++;
            }
            flag = true;
        }
        while (offset + length < bufferLen && isDigit(charAt())) {
            length++;
        }
        if (offset + length == bufferLen) {
            return Tokens.INTEGER_LITERAL;
        }
        ch = charAt();
        if (ch == '.') {
            if (flag) {
                return Tokens.INTEGER_LITERAL;
            }
            if (offset + length + 1 == bufferLen) {
                return Tokens.INTEGER_LITERAL;
            }
            length++;
            throwIfNeeded();
            while (offset + length < bufferLen && isDigit(charAt())) {
                length++;
            }
            if (offset + length == bufferLen) {
                return Tokens.FLOATING_POINT_LITERAL;
            }
            ch = charAt();
            if (ch == 'e' || ch == 'E') {
                length++;
                throwIfNeeded();
                if (charAt() == '-' || charAt() == '+') {
                    length++;
                    throwIfNeeded();
                }
                while (offset + length < bufferLen && isPrimeDigit(charAt())) {
                    length++;
                }
                if (offset + length == bufferLen) {
                    return Tokens.FLOATING_POINT_LITERAL;
                }
                ch = charAt();
            }
            if (ch == 'f' || ch == 'F' || ch == 'D' || ch == 'd') {
                length++;
            }
            return Tokens.FLOATING_POINT_LITERAL;
        } else if (ch == 'l' || ch == 'L') {
            length++;
            return Tokens.INTEGER_LITERAL;
        } else if (ch == 'F' || ch == 'f' || ch == 'D' || ch == 'd') {
            length++;
            return Tokens.FLOATING_POINT_LITERAL;
        } else {
            return Tokens.INTEGER_LITERAL;
        }
    }

    protected Tokens scanDIV() {
        if (offset + 1 == bufferLen) {
            return Tokens.DIV;
        }
        char ch = charAt();
        if (ch == '\\') {
            length++;
            while (offset + length < bufferLen && charAt() != '\n') {
                length++;
            }
            return Tokens.LINE_COMMENT;
        } else if (ch == '#') {
            length++;
            char pre, curr = '?';
            while (offset + length < bufferLen) {
                pre = curr;
                curr = charAt();
                if (curr == '\\' && pre == '#') {
                    length++;
                    break;
                }
                length++;
            }
            return Tokens.LONG_COMMENT;
        } else {
            return Tokens.DIV;
        }
    }

    @SuppressWarnings("SameReturnValue")
    protected Tokens scanLT() {
        return Tokens.LT;
    }

    @SuppressWarnings("SameReturnValue")
    protected Tokens scanGT() {
        return Tokens.GT;
    }

    protected Tokens scanOperatorTwo(Tokens ifWrong) {
        return ifWrong;
    }

    public void reset(CharSequence src) {
        if (src == null) {
            throw new IllegalArgumentException();
        }
        this.source = src;
        line = 0;
        column = 0;
        length = 0;
        index = 0;
        offset = 0;
        currToken = Tokens.WHITESPACE;
        bufferLen = src.length();
    }
}
