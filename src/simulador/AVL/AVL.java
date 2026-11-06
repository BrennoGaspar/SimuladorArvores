package simulador.AVL;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.math.Vector2;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import simulador.No;

/**
 * @author Brenno Gaspar Pinto & Victor Altran Soares
 */
public class AVL {
    
    private No raiz;
    private int ranqueAtual;
    private final List<No> nosDesenho = new ArrayList<>();

    private final int RAIO = 20;
    private final double ESPHOR = 50;
    private final double ESPVERT = 50;
    private final int MARGEMHOR = 50;
    private final int MARGEMVERT = 100;
    private final int LARGURA = 1000;
    private final int LIMITE_RANDOM = 5;
    
    public void put( int c ) {
        
        raiz = put( raiz, c );
        atualizarNos( ESPHOR, ESPVERT, MARGEMHOR, MARGEMVERT );
        
    }
    
    private No put( No no, int c ) {
        
        if ( no == null ) {
            no = new No( RAIO, false );
            no.setC( c );
            no.setAltura( 1 );
            return no;
        }
        
        if ( c < no.getC() ) {
            no.setE( put( no.getE(), c ) );
        } else if ( c > no.getC() ) {
            no.setD( put( no.getD(), c ) );
        } else {
            return no;
        }
        
        no.setAltura( 1 + Math.max( altura( no.getE() ), altura( no.getD() ) ) );
        
        // cálculo do balanceamento para o put
        int fb = fatorBalanceamento( no );
        
        // left-left
        if ( fb > 1 && c < no.getE().getC() ) {
            return rotacaoDireita( no );
        }
        
        // right-right
        if ( fb < -1 && c > no.getD().getC() ) {
            return rotacaoEsquerda( no );
        }
        
        // left-right
        if ( fb > 1 && c > no.getE().getC() ) {
            no.setE( rotacaoEsquerda( no.getE() ) );
            return rotacaoDireita( no );
        }
        
        // right-left
        if ( fb < -1 && c < no.getD().getC() ) {
            no.setD( rotacaoDireita( no.getD() ) );
            return rotacaoEsquerda( no );
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
            
            No sucessor = min( no.getD() );
            no.setC( sucessor.getC() );
            no.setD( rem( no.getD(), sucessor.getC() ) );      
        }
        
        no.setAltura( 1 + Math.max( altura( no.getE() ), altura( no.getD() ) ) );
        
        // cálculo do balanceamento para o rem
        int fb = fatorBalanceamento( no );
        
        // left-left
        if ( fb > 1 && fatorBalanceamento( no.getE() ) >= 0 ) {
            return rotacaoDireita( no );
        }
        
        // left-right
        if ( fb > 1 && fatorBalanceamento( no.getE() ) < 0 ) {
            no.setE( rotacaoEsquerda( no.getE() ) );
            return rotacaoDireita( no );
        }
        
        // right-right
        if ( fb < -1 && fatorBalanceamento( no.getD() ) <= 0 ) {
            return rotacaoEsquerda( no );
        }
        
        // right-left
        if ( fb < -1 && fatorBalanceamento( no.getD() ) > 0 ) {
            no.setD( rotacaoDireita( no.getD() ) );
            return rotacaoEsquerda( no );
        }
        
        return no;   
    }
    
    private No min( No no ) {   
        while ( no.getE() != null ) {
            no = no.getE();
        }  
        return no;   
    }
      
    private No rotacaoDireita( No y ) {
        No x = y.getE();
        No t2 = x.getD();
        
        x.setD( y );
        y.setE( t2 );
        
        y.setAltura( 1 + Math.max( altura( y.getE() ), altura( y.getD() ) ) );
        x.setAltura( 1 + Math.max( altura( x.getE() ), altura( x.getD() ) ) );
        
        return x;
       
    }
    
    private No rotacaoEsquerda( No x ) {
        No y = x.getD();
        No t2 = y.getE();
        
        y.setE( x );
        x.setD( t2 );
        
        x.setAltura( 1 + Math.max( altura( x.getE() ), altura( x.getD() ) ) );
        y.setAltura( 1 + Math.max( altura( y.getE() ), altura( y.getD() ) ) );
        
        return y;
    }
    
    private int altura( No no ) {
        return no == null ? 0 : no.getAltura();
    }
    
    private int fatorBalanceamento( No no ) {
        return no == null ? 0 : altura( no.getE() ) - altura( no.getD() );
    }
    
    public Integer chaveEm( int mouseX, int mouseY ) {
    
        for ( No no : nosDesenho ) {
            double dx = mouseX - no.getPos().x;
            double dy = mouseY - no.getPos().y;
            if ( dx * dx + dy * dy <= no.getRaio() * no.getRaio() ) {
                return no.getC();
            }
        }

        return null;

    }
    
    public void atualizarNos( double espHor, double espVert, int margemHor, int margemVert ) {
    
        ranqueAtual = 0;
        nosDesenho.clear();
        atualizarNos( raiz, 0, espHor, espVert, margemHor, margemVert );

        double deslocamento = ( LARGURA - espHor * (nosDesenho.size() - 1) ) / 2 - margemHor;

        for ( No no : nosDesenho ) {
            no.setPos( new Vector2(no.getPos().x + deslocamento, no.getPos().y) );
        }

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
    
    public int[] random() {
        
        int[] valoresGerados = new int[LIMITE_RANDOM];
        Random random = new Random();
        for( int i = 0; i < LIMITE_RANDOM; i++ ) {
            
            while ( true ) {
                int valor = random.nextInt( 100 );
                boolean jaTem = false;
                for( No no : nosDesenho ) {
                    if( valor == no.getC() ) {
                        jaTem = true;
                    }
                }
                if( !jaTem ) {
                    put( valor );
                    valoresGerados[i] = valor;
                    break;
                }
            }
            
        }
        
        return valoresGerados;
    }
    
    public void find( int x ) {
        
        boolean encontrou = false;
        for( No no : nosDesenho ) {
            if( no.getC() == x ) {
                no.setFind( true );
                encontrou = true;
            } else {
                no.setFind( false );
            }
        }
        
        if( !encontrou ) {
            System.out.printf( "\nO valor %d nao foi encotrado na arvore\n", x );
        }
        
    }
    
    public void clear() {
        raiz = null;
        nosDesenho.clear();
    }
    
    public int getNivelAtual() {
        
        int maior = 0;
        for( No no : nosDesenho ) {
            int valor = no.getNivel();
            if( valor > maior ) {
                maior = valor;
            }
        }
        
        return maior;
        
    }
    
    public void desenhar( EngineFrame e ) {
        
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