import java.io.InputStream;
import java.io.IOException;

public class CalculatorParser {
    private int lookaheadToken;
    private InputStream in;

    public CalculatorParser(InputStream in) throws IOException{
        this.in = in;
        lookaheadToken = in.read();
    }

    private int expr() throws IOException, ParseError{
        int returnValue =  term();
        return expr2(returnValue);
    }

    private int expr2(int cond) throws IOException, ParseError{
        if(lookaheadToken == '*' || lookaheadToken == '/' ||lookaheadToken == '\n' || lookaheadToken == -1){
            return cond;            /*no other lookahead tokens, reached EOF*/
        }
        int returnValue;
        if(lookaheadToken == '+'){
            consume('+');
            returnValue = cond + term();
            expr2(returnValue);
        }else if (lookaheadToken == '-'){
            consume('-');
            returnValue = cond - term();
            expr2(returnValue);
        }else{
            throw new ParseError();
        }
        return returnValue;
    }

    private int term() throws IOException, ParseError{
        int returnValue = factor();
        return term2(returnValue);
    }

    private int term2(int cond) throws IOException, ParseError {
        if(lookaheadToken == '+' || lookaheadToken == '-' || lookaheadToken == '\n' || lookaheadToken == -1){
            return cond;            /*no other lookahead tokens, reached EOF*/
        }
        int returnValue;
        if(lookaheadToken == '*'){
            consume('*');
            returnValue = cond * factor();
            term2(returnValue);
        }else if (lookaheadToken == '/'){
            consume('/');
            returnValue = cond / factor();
            term2(returnValue);
        }else{
            throw new ParseError();
        }
        return returnValue;
    }

    private int factor() throws IOException, ParseError{
        if(lookaheadToken < 0 || lookaheadToken > 9){
            return num();
        }else if(lookaheadToken == '('){
            consume(lookaheadToken);
            return expr();
        }else {
            throw new ParseError();
        }
    }

    private int num() throws IOException, ParseError{
        int digit = evalDigit(lookaheadToken);
        consume(lookaheadToken);
        return digit;
    }

    private int evalDigit(int digit){
        return digit - '0';
    }

    private void consume(int symbol) throws IOException, ParseError{
        if (lookaheadToken != symbol){
            throw new ParseError();
        }
        lookaheadToken = in.read();     /*read the new token*/
    }

    private int eval() throws IOException, ParseError{
        int returnValue = expr();
        if(lookaheadToken != '\n' && lookaheadToken == -1){
            throw new ParseError();
        }
        return returnValue;
    }

    public static void main(String[] args) {
        try{
            CalculatorParser calculate = new CalculatorParser(System.in);
            System.out.println(calculate.eval());
        }catch (IOException e){
            System.err.println(e.getMessage());
        }catch (ParseError error){
            System.err.println(error.getMessage());
        }
    }

}
