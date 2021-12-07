package edu.odu.cs.cs350.DupDetector;
//@SuppressWarnings("unused")\
/* Adapted from:
 * https://www.cs.auckland.ac.nz/courses/compsci330s1c/lectures/330ChaptersPDF/Chapt1.pdf
 * Author: GTLugo
 **/

%%

%public
%class GeneratedScanner

%line
%column

%type Token

%{
  StringBuilder string = new StringBuilder();
  
  private Token symbol(TokenType type) {
    return new Token(type, yyline+1, yycolumn+1);
  }

  private Token symbol(TokenType type, String value) {
    return new Token(type, yyline+1, yycolumn+1, value);
  }

%}

/* main character classes */
InputChar = [^\r\n]
LineChar  = \r|\n|\r\n
SpaceChar = [\ \t]

/* integer literal */ 
Zero     = 0
DecInt   = [1-9][0-9]*
OctalInt = 0[0-7]+
HexInt   = 0[xX][0-9a-fA-F]+
Integer  = ( {Zero} | {DecInt} | {OctalInt} | {HexInt} )[lL]?

/* float literal */ 
Exponent = [eE] [\+\-]? [0-9]+
Float1   = [0-9]+ \. [0-9]+ {Exponent}?
Float2   = \. [0-9]+ {Exponent}?
Float3   = [0-9]+ \. {Exponent}?
Float4   = [0-9]+ {Exponent}
Float    = ( {Float1} | {Float2} | {Float3} | {Float4} ) [fFdD]? | [0-9]+ [fFDd]

/* identifiers */
Identifier  = [A-Za-z_$] [A-Za-z_$0-9]*

/* char literals */
Char        = [^\'\\\n\r] | {EscChar}
String      = [^\"\\\n\r] | {EscChar}
EscChar     = \\[ntbrf\\\'\"] | {OctalEscape}
OctalEscape = \\[0-7] | \\[0-7][0-7] | \\[0-3][0-7][0-7]

/* bool literals */
Bool = (true | false)

/* keyword hell */
Keyword = (alignas
 | alignof
 | and
 | and_eq
 | asm
 | atomic_cancel
 | atomic_commit
 | atomic_noexcept
 | auto
 | bitand
 | bitor
 | bool
 | break
 | case
 | catch
 | char
 | char8_t
 | char16_t
 | char32_t
 | class
 | compl
 | concept
 | const
 | consteval
 | constexpr
 | constinit
 | const_cast
 | continue
 | co_await
 | co_return
 | co_yield
 | decltype
 | default
 | delete
 | do
 | double
 | dynamic_cast
 | else
 | enum
 | explicit
 | export
 | extern
 //| false
 | float
 | for
 | friend
 | goto
 | if
 | inline
 | int
 | long
 | mutable
 | namespace
 | new
 | noexcept
 | not
 | not_eq
 | nullptr
 | operator
 | or
 | or_eq
 | private
 | protected
 | public
 | reflexpr
 | register
 | reinterpret_cast
 | requires
 | return
 | short
 | signed
 | sizeof
 | static
 | static_assert
 | static_cast
 | struct
 | switch
 | synchronized
 | template
 | this
 | thread_local
 | throw
 //| true
 | try
 | typedef
 | typeid
 | typename
 | union
 | unsigned
 | using
 | virtual
 | void
 | volatile
 | wchar_t
 | while
 | xor
 | xor_eq
 | final
 | override
 | transaction_safe
 | transaction_safe_dynamic
 | import
 | module
)

/* symbol hell (in rough order of c++ evaluation) */
Symbol = (\;
 | \{
 | \}
 | (\:\:)
 | (\+\+)
 | (\-\-)
 | \(
 | \)
 | \[
 | \]
 | \.
 | (\-\>)
 | \~
 | \!
 | \+
 | \-
 | \&
 | \*
 | (\.\*)
 | (\-\>\*)
 | \*
 | \/
 | \%
 | (\<\<)
 | (\>\>)
 | \<
 | \>
 | (\<\=)
 | (\>\=)
 | \=
 | (\!\=)
 | \^
 | \|
 | (\&\&)
 | (\|\|)
 | (\*\=)
 | (\/\=)
 | (\%\=)
 | (\+\=)
 | (\-\=)
 | (\>\>\=)
 | (\<\<\=)
 | (\&\=)
 | (\^\=)
 | (\|\=)
 | (\?\:)
 | \,
)

%%

/* reserved words */
{Keyword}  { return symbol(TokenType.KEYWORD, yytext()); }
{Symbol}   { return symbol(TokenType.SYMBOL, yytext()); }

/* literals */
{Bool}       { return symbol(TokenType.BOOL_LIT, yytext()); }
{Integer}    { return symbol(TokenType.INTEGER_LIT, yytext()); }
{Float}      { return symbol(TokenType.FLOAT_LIT, yytext()); }
\'{Char}\'   { return symbol(TokenType.CHAR_LIT, yytext()); }
\"{String}*\" { return symbol(TokenType.STRING_LIT, yytext()); }

/* identifiers */
{Identifier} { return symbol(TokenType.IDENTIFIER, yytext()); }

/* comments */
"//"{InputChar}* { }
"/*"~"*/"        { }

/* preprocessor */
^"#"{InputChar}* { }

{LineChar}  { }  
{SpaceChar} { }

/* error fallback */
<<EOF>> { return symbol(TokenType.EOF); }
.       { throw new RuntimeException("Illegal character \"" + yytext() + "\" at line " + yyline+1 + ", column " + yycolumn+1); }
