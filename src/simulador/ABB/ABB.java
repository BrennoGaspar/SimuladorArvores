package simulador.ABB;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.math.Vector2;
import java.util.ArrayList;
import java.util.List;
import simulador.No;

/**
 * @author Brenno Gaspar Pinto & Victor Altran Soares
 */
public class ABB {
    
    private final int RAIO = 20;
    private final double ESPHOR = 50;
    private final double ESPVERT = 50;
    private final int MARGEMHOR = 50;
    private final int MARGEMVERT = 50;
    
    private No raiz;
    private int ranqueAtual;
    private final List<No> nosDesenho = new ArrayList<>();
    
    public void put( int c ) {
        
        raiz = put( raiz, c );
        atualizarNos( ESPHOR, ESPVERT, MARGEMHOR, MARGEMVERT );
        
    }
    
    private No put( No no, int c ) {
        
        if ( no == null ) {
            no = new No( RAIO, false );
            no.setC( c );
        } else {
            
            if ( c < no.getC() ) {
                no.setE( put( no.getE(), c ) );
            } else if ( c > no.getC() ) {
                no.setD( put( no.getD(), c ) );
            } else { // c == no.c
                // não faz nada
            }
            
        }
        
        return no;
        
    }
    
    public void atualizarNos( double espHor, double espVert, int margemHor, int margemVert ) {
        
        ranqueAtual = 0;
        nosDesenho.clear();
        atualizarNos( raiz, 0, espHor, espVert, margemHor, margemVert );
        
    }
    
    private void atualizarNos( No no, int nivel, double espHor, double espVert, int margemHor, int margemVert ) {
        
        if ( no != null ) {
            atualizarNos( no.getE(), nivel + 1, espHor, espVert, margemHor, margemVert );
            no.setNivel( nivel );
            no.setRanque( ranqueAtual++ );
            double posX = margemHor + espHor * no.getRanque();
            double posY = margemVert + espVert * no.getNivel();
            no.setPos( new Vector2( posX, posY ) );
            nosDesenho.add( no );
            atualizarNos( no.getD(), nivel + 1, espHor, espVert, margemHor, margemVert );
        }
        
    }
    
    public void desenhar( EngineFrame e ) {
        
        // as posições já estão atualizadas (isso é feito no put)
        
        for ( No no : nosDesenho ) {
            if ( no.getE() != null ) {
                e.drawLine( no.getPos(), no.getE().getPos(), EngineFrame.BLACK );
            }
            if ( no.getD() != null ) {
                e.drawLine( no.getPos(), no.getD().getPos(), EngineFrame.BLACK );
            }
        }
        
        for ( No no : nosDesenho ) {
            no.desenhar( e );
        }
        
    }    
    
}