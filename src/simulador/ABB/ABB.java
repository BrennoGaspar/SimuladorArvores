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
    private final int MARGEMVERT = 100;
    
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
    
      public void rem( int c ) {
        
        raiz = rem( raiz, c );
        atualizarNos( ESPHOR, ESPVERT, MARGEMHOR, MARGEMVERT );
        
    }
    
    private No rem( No no, int c ) {
        
        if ( no == null ) {
            return null;
        }
        
        if ( c < no.getC() ) {
            no.setE( rem( no.getE(), c ) );
        } else if ( c > no.getC() ) {
            no.setD( rem( no.getD(), c ) );
        } else {
            
            if ( no.getE() == null ) {
                return no.getD();
            }
            
            if ( no.getD() == null ) {
                return no.getE();
            }
            
            // dois filhos - substitui pelo próx
            No sucessor = min( no.getD() );
            no.setC( sucessor.getC() );
            no.setD( rem( no.getD(), sucessor.getC() ) );         
        }
        return no;   
    }
    
    private No min( No no ) {
        
        while ( no.getE() != null ) {
            no = no.getE();
        }
      
        return no;     
    }
    
    public Integer chaveEm( int mouseX, int mouseY ){
    
        for ( No no : nosDesenho ) {
            double dx = mouseX - no.getPos().x;
            double dy = mouseY - no.getPos().y;
            // Pitágoras
            if ( (dx * dx) + (dy * dy) <= (no.getRaio() * no.getRaio()) ) {
                return no.getC();
            }
        }

        return null;

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
    
    public void clear() {
        nosDesenho.clear();
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