package dores;

import dores.enums.OperadorRelacional;
import dores.enums.OperadorAritmetico;
import dores.enums.SimboloPontuacao;
import dores.enums.Tipo;
import dores.estruturas.Token;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.List;

public class Analisador {

    private String path;

    private List<Token> tokens = new ArrayList<>();

    private PushbackReader r;

    private int coluna = 0;

    private int linha = 1;

    public Analisador(String path) {
        this.path = path;
    }

    public List<Token> Analisar() {
        List<Character> fileContentAsChar = new ArrayList<>();
        try {
            r = new PushbackReader(new BufferedReader(new InputStreamReader(
                    new FileInputStream(path), "US-ASCII")));

            int intData;
            while ((intData = r.read()) != -1) {
                Character data = (char) intData;


                if (Character.isWhitespace((data))) {
                    proximaColuna(data);
                    continue;
                }else{
                    coluna++;
                }

                Token token = pegarToken(data);

                if (Tipo.SERRO.equals(token.getTipo())) {
                    tokens.add(token);
                    return tokens;
                } else {
                    tokens.add(token);
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return tokens;
    }

    private Token pegarToken(Character c) {
        if (Character.isDigit(c)) {
            // TODO: Então Trata Digito
            return tratarDigito(c);
        } else if (Character.isAlphabetic(c)) {
            // TODO: Então Trata Identificador e Palavra Reservada
            return trataIdentificadorPalavraReservada(c);
        } else if (c.equals(':')) {
            // TODO: Então Trata Atribuição
                return tratarAtribuicao(c);
        } else if (OperadorAritmetico.charToSimbol(c)) {
            // TODO: Então Trata Operador Aritmético
            return new Token(Tipo.PalavraReservada(c.toString()), c.toString(), linha, coluna);
        } else if (OperadorRelacional.charToSimbol(c)) {
            // TODO: Então Trata Operador Relacional
            return new Token(Tipo.PalavraReservada(c.toString()), c.toString(), linha, coluna);
        } else if (SimboloPontuacao.charToSimbol(c)) {
            // TODO: Então Trata Pontuação
            return new Token(Tipo.PalavraReservada(c.toString()), c.toString(), linha, coluna);
        } else {
            // TODO: ERRO
            return new Token(Tipo.SERRO, "NULL", linha, coluna);
        }
    }

    private Token tratarDigito(Character c) {
        String numero = "" + c;

        Character ch;
        while (Character.isDigit((ch = Ler()))) {
            proximaColuna(ch);
            numero += ch;
        }
        desler(ch);

        return new Token(Tipo.SNUMERO, numero, linha, coluna);
    }

    private Character Ler() {
        try {
            int c = r.read();
            return c != -1 ? (char) c : (char) -1; // TODO: RETIRAR
        } catch (Exception e) {
            System.out.println(e);
        }
        return (char) -1; // TODO: RETIRAR
    }

    private Token trataIdentificadorPalavraReservada(Character c) {
        String palavra = "" + c;

        Character ch;
        while (Character.isAlphabetic(ch = Ler())) {
            proximaColuna(ch);
            palavra += ch;
        }
        desler(ch);
        return new Token(Tipo.PalavraReservada(palavra), palavra, linha, coluna);
    }

    private void proximaLinha(Character c) {
        if (c.equals('\n')) {
            linha++;
            if(linha == 4){
                System.out.println("ln 4");
            }
            coluna = 0;
        }
    }

    private void proximaColuna(Character c) {
        coluna++;
        proximaLinha(c);
    }

    private Token tratarAtribuicao(Character c){
        String attr = "" + c;

        Character ch = Ler();

        if(OperadorRelacional.charToSimbol(ch)){
            attr += ch;
        }else{
            desler(ch);
        }

        return new Token(Tipo.PalavraReservada(attr), attr, linha, coluna);
    }

    private void desler(Character ch){
        try{
            r.unread((int)ch);
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }
}
