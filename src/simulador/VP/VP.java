package simulador.VP;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.math.Vector2;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import simulador.No;

/**
 * @author Brenno Gaspar Pinto & Victor Altran Soares
 */
public class VP {
    
    private final int RAIO = 20;
    private final double ESPHOR = 50;
    private final double ESPVERT = 50;
    private final int MARGEMHOR = 50;
    private final int MARGEMVERT = 100;
    private final int LARGURA = 1000;
    private final int LIMITE_RANDOM = 5;
    
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
            
            no = new No( RAIO, true, false );
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
    
     public void rem( int c ) {
        if ( !contem( raiz, c ) ) {
            return;
        } 
        
        if ( !isRed( raiz.getE() ) && !isRed( raiz.getD() ) ) {
            raiz.setRed( true );
        } 
        raiz = rem( raiz, c );
        
        if ( raiz != null ) {
            raiz.setRed( false );
        }
        
        atualizarNos( ESPHOR, ESPVERT, MARGEMHOR, MARGEMVERT );    
    }
    
    private No rem( No no, int c ) { 
        if ( c < no.getC() ) {
            
            if ( !isRed( no.getE() ) && !isRed( no.getE().getE() ) ) {
                no = moverVermelhaEsquerda( no );
            }
            
            no.setE( rem( no.getE(), c ) );     
        } else {
            
            if ( isRed( no.getE() ) ) {
                no = rotacionarDireita( no );
            }
            
            if ( c == no.getC() && no.getD() == null ) {
                return null;
            }
            
            if ( !isRed( no.getD() ) && !isRed( no.getD().getE() ) ) {
                no = moverVermelhaDireita( no );
            }
            
            if ( c == no.getC() ) {
                
                No sucessor = min( no.getD() );
                no.setC( sucessor.getC() );
                no.setD( remMin( no.getD() ) );    
            } else {
                no.setD( rem( no.getD(), c ) );
            }    
        }
        
        return balancear( no ); 
    }
    
    private No remMin( No no ) {
 
        if ( no.getE() == null ) {
            return null;
        }
        
        if ( !isRed( no.getE() ) && !isRed( no.getE().getE() ) ) {
            no = moverVermelhaEsquerda( no );
        }
        
        no.setE( remMin( no.getE() ) );
        
        return balancear( no );
        
    }
    
    private No min( No no ) {
        
        while ( no.getE() != null ) {
            no = no.getE();
        }
        
        return no;
        
    }
    
    private boolean contem( No no, int c ) {
        
        while ( no != null ) {
            if ( c < no.getC() ) {
                no = no.getE();
            } else if ( c > no.getC() ) {
                no = no.getD();
            } else {
                return true;
            }
        }
        
        return false;
        
    }
    
    private No moverVermelhaEsquerda( No no ) {
        
        inverterCores( no );
        
        if ( isRed( no.getD().getE() ) ) {
            no.setD( rotacionarDireita( no.getD() ) );
            no = rotacionarEsquerda( no );
            inverterCores( no );
        }
        
        return no;
        
    }
    
    private No moverVermelhaDireita( No no ) {
        
        inverterCores( no );
        
        if ( isRed( no.getE().getE() ) ) {
            no = rotacionarDireita( no );
            inverterCores( no );
        }
        
        return no;
        
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
