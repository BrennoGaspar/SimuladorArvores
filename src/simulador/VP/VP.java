package simulador.VP;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.math.Vector2;
import java.util.ArrayList;
import java.util.List;
import simulador.No;

/**
 * @author Brenno Gaspar Pinto & Victor Altran Soares
 */
public class VP {
    
    private final int RAIO = 20;
    private final double ESPHOR = 50;
    private final double ESPVERT = 50;
    private final int MARGEMHOR = 50;
    private final int MARGEMVERT = 50;
    
    private No raiz;
    private int ranqueAtual;
    private final List<No> nosDesenho = new ArrayList<>();
    
    private boolean isRed(No no) {
        if (no == null) return false;
        return no.isRed();
    }
    
    public void put( int c ) {
        
        raiz = put( raiz, c );
        raiz.setRed( false );
        atualizarNos( ESPHOR, ESPVERT, MARGEMHOR, MARGEMVERT );
        
    }
    
    private No put( No no, int c ) {
        
        if ( no == null ) {
            
            no = new No( RAIO, true );
            no.setC( c );
            return no;
            
        }
            
        if ( c < no.getC() ) {
            no.setE( put( no.getE(), c ) );
        } else if ( c > no.getC() ) {
            no.setD( put( no.getD(), c ) );
        }
            
        
        return balancear( no );
        
    }
    
    private No balancear( No no ) {
        
        if( isRed(no.getD()) && !isRed(no.getE()) ) {
            // direita vermelho, mas esquerda não -> precisa rotacionar para a esquerda
            no = rotacionarEsquerda( no );
        }
        if( isRed(no.getE()) && isRed(no.getE().getE()) ) {
            // esquerda e esquerda esquerda são vermelhos -> precisa rotaciona para a direita
            no = rotacionarDireita( no );
        }
        if( isRed(no.getE()) && isRed(no.getD()) ) {
            // esquerda é vermelho, mas direita não -> precisa inverter as cores
            inverterCores( no );
        }
        
        return no;
        
    }
    
    private No rotacionarEsquerda( No no ) {
        
        // direita vermelho, mas esquerda não
        No direita = no.getD();
        no.setD( direita.getE() );
        direita.setE( no );
        direita.setRed( no.isRed() );
        no.setRed( true );
        return direita;
        
    }
    
    private No rotacionarDireita( No no ) {
        
        // esquerda e esquerda-esquerda são vermelhos
        No esquerda = no.getE();
        no.setE( esquerda.getD() );
        esquerda.setD( no );
        esquerda.setRed( no.isRed() );
        no.setRed( true );
        return esquerda;
        
    }
    
    private void inverterCores( No no ) {
        
        no.changeRed();
        no.getE().changeRed();
        no.getD().changeRed();
        
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
                if( no.getE().isRed() ) {
                    e.drawLine( no.getPos(), no.getE().getPos(), EngineFrame.RED );
                } else {
                    e.drawLine( no.getPos(), no.getE().getPos(), EngineFrame.BLACK );
                }
            }
            if ( no.getD() != null ) {
                if( no.getD().isRed() ) {
                    e.drawLine( no.getPos(), no.getD().getPos(), EngineFrame.RED );
                } else {
                    e.drawLine( no.getPos(), no.getD().getPos(), EngineFrame.BLACK );
                }
            }
        }
        
        for ( No no : nosDesenho ) {
            no.desenhar( e );
        }
        
    }
    
}
