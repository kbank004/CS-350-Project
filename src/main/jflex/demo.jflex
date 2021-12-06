/**
 * jflex demo: generating and returning tokens
 *
 * This is a very simple language. It has identfiers,
 * integer literals, and a single keyword.
 *
 *
 * Steven J Zeil
 */


package edu.odu.cs.cs350.DupDetector;
//@SuppressWarnings("unused")

%%

%public
%class GeneratedScanner


%line
%column

%type Token

%{
  StringBuilder string = new StringBuilder();
  
  private Token symbol(TokenKinds type) {
    return new Token(type, yyline+1, yycolumn+1);
  }

  private Token symbol(TokenKinds type, String value) {
    return new Token(type, yyline+1, yycolumn+1, value);
  }
  


%}

/* main character classes */
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]

WhiteSpace = {LineTerminator} | [ \t\f]


/* identifiers */
Identifier = [:jletter:][:jletterdigit:]*

/* integer literals */
IntegerLiteral = [0-9][0-9]* | [0-9][_0-9]*[0-9]

%%


  /* keywords */
  "KEYWORD"                     { return symbol(TokenKinds.KEYWORD); }
  
  
  {IntegerLiteral}            { return symbol(TokenKinds.INTEGER_LITERAL, yytext()); }

  {Identifier}                   { return symbol(TokenKinds.IDENTIFIER, yytext()); } 
  
{WhiteSpace}                  {/* Ignore: don't return anything. */}  


/* error fallback */
[^]                              { throw new RuntimeException("Illegal character \""+yytext()+
                                                              "\" at line "+yyline+", column "+yycolumn); }
<<EOF>>                          { return symbol(TokenKinds.EOF); }
