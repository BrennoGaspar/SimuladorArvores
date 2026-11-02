package simulador;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.math.Vector2;

/**
 * @author Brenno Gaspar Pinto & Victor Altran Soares
 */
public class No {
    
    private int c;
    private No e;
    private No d;
    
    private Vector2 pos;
    private int nivel;
    private int ranque;
    private int raio;
    
    public No( int raio ) {
        this.pos = new Vector2();
        this.raio = raio;
    }

    public void desenhar( EngineFrame engine ) {
        
        engine.fillCircle( pos, raio, EngineFrame.WHITE );
        engine.drawCircle( pos, raio, EngineFrame.BLACK );
        
        String label = String.valueOf( c );
        int larg = engine.measureText( label );
        engine.drawText( label, pos.x - larg / 2, pos.y - 10, EngineFrame.BLACK );
                
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

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public void setRanque(int ranque) {
        this.ranque = ranque;
    }

    public void setRaio(int raio) {
        this.raio = raio;
    }
    
}