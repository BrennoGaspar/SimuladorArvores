package simulador;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.math.Vector2;
import java.awt.Color;

/**
 * @author Brenno Gaspar Pinto & Victor Altran Soares
 */
public class No {

    private static final Color COR_ARESTA = new Color( 100, 116, 139 );
    private static final Color COR_ARESTA_VERMELHA = new Color( 239, 68, 68 );
    private static final Color COR_SOMBRA = new Color( 0, 0, 0, 90 );
    private static final Color COR_FATOR = new Color( 148, 163, 184 );
    private static final Color COR_FATOR_ALERTA = new Color( 251, 191, 36 );

    private int c;
    private No e;
    private No d;
    private boolean red;
    private Color fonte;
    private boolean find;

    private Vector2 pos;
    private int nivel;
    private int ranque;
    private int raio;
    private boolean avl;
    private boolean vp;
    private int fatorBalanceamento;

    private Vector2 alvo;
    private boolean iniciado;

    public No( int raio, boolean red, boolean avl ) {
        this.pos = new Vector2();
        this.raio = raio;
        this.red = red;
        this.avl = avl;
    }

    public static void ligar( EngineFrame engine, No a, No b, boolean vermelha ) {

        Color cor = vermelha ? COR_ARESTA_VERMELHA : COR_ARESTA;

        engine.drawLine( a.pos, b.pos, cor );
        engine.drawLine( new Vector2( a.pos.x + 1, a.pos.y ), new Vector2( b.pos.x + 1, b.pos.y ), cor );
        engine.drawLine( new Vector2( a.pos.x, a.pos.y + 1 ), new Vector2( b.pos.x, b.pos.y + 1 ), cor );

    }

    public void desenhar( EngineFrame engine ) {

        Color fundo;
        Color borda;

        if( find ) {
            fundo = new Color( 251, 191, 36 );
            borda = new Color( 253, 230, 138 );
            fonte = new Color( 30, 27, 10 );
        } else if ( red ) {
            fundo = new Color( 220, 38, 38 );
            borda = new Color( 254, 202, 202 );
            fonte = Color.WHITE;
        } else if ( vp ) {
            fundo = new Color( 2, 6, 23 );
            borda = new Color( 148, 163, 184 );
            fonte = Color.WHITE;
        } else if ( avl ) {
            fundo = new Color( 30, 41, 59 );
            borda = new Color( 74, 222, 128 );
            fonte = Color.WHITE;
        } else {
            fundo = new Color( 30, 41, 59 );
            borda = new Color( 56, 189, 248 );
            fonte = Color.WHITE;
        }

        engine.fillCircle( new Vector2( pos.x + 2, pos.y + 3 ), raio, COR_SOMBRA );
        engine.fillCircle( pos, raio, borda );
        engine.fillCircle( pos, raio - 3, fundo );

        engine.setDefaultFontSize( 18 );
        String label = String.valueOf( c );
        int larg = engine.measureText( label );
        engine.drawText( label, pos.x - larg / 2.0, pos.y - 9, fonte );

        if( avl ) {
            engine.setDefaultFontSize( 14 );
            String textoFator = fatorBalanceamento > 0 ? String.format( "+%d", fatorBalanceamento ) : String.format( "%d", fatorBalanceamento );
            int largFator = engine.measureText( textoFator );
            Color corFator = fatorBalanceamento == 0 ? COR_FATOR : COR_FATOR_ALERTA;
            engine.drawText( textoFator, pos.x - largFator / 2.0, pos.y - raio - 20, corFator );
        }

    }

    public void animar( double delta ) {

        if ( alvo == null ) {
            return;
        }

        double t = Math.min( 1, delta * 10 );
        pos = new Vector2( pos.x + ( alvo.x - pos.x ) * t, pos.y + ( alvo.y - pos.y ) * t );

    }

    @Override
    public String toString() {
        return String.valueOf( c );
    }

    // Getters
    public int getC() {
        return c;
    }

    public No getE() {
        return e;
    }

    public No getD() {
        return d;
    }

    public Vector2 getPos() {
        return pos;
    }

    public int getNivel() {
        return nivel;
    }

    public int getRanque() {
        return ranque;
    }

    public int getRaio() {
        return raio;
    }

    public boolean isRed() {
        return red;
    }

    // Setters
    public void setC(int c) {
        this.c = c;
    }

    public void setE(No e) {
        this.e = e;
    }

    public void setD(No d) {
        this.d = d;
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    public void setAlvo( Vector2 alvo ) {
        this.alvo = alvo;
        if ( !iniciado ) {
            pos = new Vector2( alvo.x, alvo.y - 40 );
            iniciado = true;
        }
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public void setRanque(int ranque) {
        this.ranque = ranque;
    }

    public void setRaio(int raio) {
        this.raio = raio;
    }

    public void changeRed() {
        this.red = !this.red;
    }

    public void setRed(boolean red) {
        this.red = red;
    }

    public void setVp( boolean vp ) {
        this.vp = vp;
    }

    public void setFind(boolean find) {
        this.find = find;
    }

    public void setFatorBalanceamento(int fatorBalanceamento) {
        this.fatorBalanceamento = fatorBalanceamento;
    }

    private int altura = 1;

    public int getAltura() {
        return altura;
    }

    public void setAltura( int altura ) {
        this.altura = altura;
    }
}