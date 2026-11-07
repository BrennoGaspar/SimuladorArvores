package simulador;

import br.com.davidbuzatto.jsge.core.Camera2D;
import simulador.ABB.ABB;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.math.Vector2;
import java.awt.Color;
import javax.swing.JOptionPane;
import simulador.AVL.AVL;
import simulador.VP.VP;

/**
 * Simulador de Árvores
 * 
 * @author Brenno Gaspar Pinto & Victor Altran Soares
 */
public class Main extends EngineFrame {
    
    private static final int TELA_MENU = 0;
    private static final int TELA_ABB = 1;
    private static final int TELA_AVL = 2;
    private static final int TELA_VP = 3;
    private boolean SINCRONIZADO;
    
    private ABB abb;
    private AVL avl;
    private VP vp;
    
    private int telaAtual;
    private int ranqueAtualABB;
    private int ranqueAtualAVL;
    private int ranqueAtualVP;
    
    private Camera2D camera;
    private double zoom;
    private double alvoX;
    private double alvoY;
    private int mouseAnteriorX;
    private int mouseAnteriorY;
        
    public Main() {
        super( 1000, 700, "Simulador Árvores", 60, true );
    }
    
    private void limparArvores() {
        abb.clear();
        avl.clear();
        vp.clear();
    }
    
    @Override
    public void create() {
        
        abb = new ABB();
        avl = new AVL();
        vp = new VP();
        SINCRONIZADO = false;
        
        telaAtual = TELA_MENU;
        
        camera = new Camera2D( new Vector2( 0, 0 ), new Vector2( 0, 0 ), 0, 1 );
        zoom = 1;
        alvoX = 0;
        alvoY = 0;
        
        setDefaultFontSize( 20 );
        
    }
    
    @Override
    public void update( double delta ) {
        
        if ( isMouseButtonDown( MOUSE_BUTTON_MIDDLE ) ) {
            alvoX -= ( getMouseX() - mouseAnteriorX ) / zoom;
            alvoY -= ( getMouseY() - mouseAnteriorY ) / zoom;
        }
        mouseAnteriorX = getMouseX();
        mouseAnteriorY = getMouseY();

        double roda = getMouseWheelMove();
        if ( roda != 0 ) {
            double mundoX = alvoX + getMouseX() / zoom;
            double mundoY = alvoY + getMouseY() / zoom;
            zoom = zoom * ( roda > 0 ? 1.1 : 0.9 );
            zoom = Math.max( 0.2, Math.min( 3, zoom ) );
            alvoX = mundoX - getMouseX() / zoom;
            alvoY = mundoY - getMouseY() / zoom;
        }
        
        /**
         * Sistema para exibição da árvore (telas)
         */
        if( isKeyPressed(KEY_FOUR) ) {
            SINCRONIZADO = true;
            telaAtual = TELA_ABB;
        } else if( isKeyPressed( KEY_ONE ) || (telaAtual == TELA_AVL && isKeyPressed(KEY_LEFT)) ) {
            telaAtual = TELA_ABB;
        } else if( isKeyPressed( KEY_TWO ) || (telaAtual == TELA_ABB && isKeyPressed(KEY_RIGHT)) || (telaAtual == TELA_VP && isKeyPressed(KEY_LEFT)) ) {
            telaAtual = TELA_AVL;
        } else if( isKeyPressed( KEY_THREE ) || (telaAtual == TELA_AVL && isKeyPressed(KEY_RIGHT)) ) {
            telaAtual = TELA_VP;
        } else if( isKeyPressed( KEY_BACKSPACE ) ) {
            if( SINCRONIZADO ) {
                SINCRONIZADO = false;
                telaAtual = TELA_MENU;
                limparArvores();
                zoom = 1; 
                alvoX = 0; 
                alvoY = 0;
            }
            telaAtual = TELA_MENU;
            limparArvores();
        }
        
        /**
         * Sistema para exibição dos Percursos (em ordem, pré-ordem, pós-ordem)
         */
        int tipoPercurso = 0;
        if ( isKeyPressed( KEY_O ) ) {
            tipoPercurso = 1;
        } else if ( isKeyPressed( KEY_P ) ) {
            tipoPercurso = 2;
        } else if ( isKeyPressed( KEY_S ) ) {
            tipoPercurso = 3;
        }

        if ( tipoPercurso != 0 ) {
            if ( SINCRONIZADO ) {
                abb.iniciarPercurso( tipoPercurso );
                avl.iniciarPercurso( tipoPercurso );
                vp.iniciarPercurso( tipoPercurso );
            } else {
                switch( telaAtual ) {
                    case TELA_ABB:
                        abb.iniciarPercurso( tipoPercurso );
                        break;
                    case TELA_AVL:
                        avl.iniciarPercurso( tipoPercurso );
                        break;
                    case TELA_VP:
                        vp.iniciarPercurso( tipoPercurso );
                        break;
                }
            }
        }

        abb.atualizarPercurso( delta );
        avl.atualizarPercurso( delta );
        vp.atualizarPercurso( delta );
        
        /**
         * Otimização de código para o método de remover nó através da colisão com o mouse
         */
        Integer chave = null;
        if ( isMouseButtonPressed(MOUSE_BUTTON_RIGHT) ) {
            chave = null;
            int mx = (int) ( alvoX + getMouseX() / zoom );
            int my = (int) ( alvoY + getMouseY() / zoom );
            switch( telaAtual ) {
                case TELA_ABB:
                    chave = abb.chaveEm( mx, my );
                    break;
                case TELA_AVL:
                    chave = avl.chaveEm( mx, my );
                    break;
                case TELA_VP:
                    chave = vp.chaveEm( mx, my );
                    break;
            }
        }
        
        if( SINCRONIZADO ) {
            /**
             * Sistema para inserir nós
             */
            if( isMouseButtonPressed(MOUSE_BUTTON_LEFT) || isKeyPressed(KEY_I) ) {
                String entrada = JOptionPane.showInputDialog( "Chave" );
                if( entrada != null ) {
                    try {
                        abb.put( Integer.parseInt(entrada) );
                        avl.put( Integer.parseInt(entrada) );
                        vp.put( Integer.parseInt(entrada) );
                    } catch( NumberFormatException ex ) {
                        JOptionPane.showMessageDialog( null, "Digite um número inteiro válido." );
                    }
                }
            }
            /**
             * Sistema para buscar nós
             */
            else if ( isKeyPressed(KEY_F) ) {
                String entrada = JOptionPane.showInputDialog( "Valor a ser encontrado" );
                if( entrada != null ) {
                    try{
                        int x = Integer.parseInt( entrada );
                        abb.find( x );
                        avl.find( x );
                        vp.find( x );
                    } catch( NumberFormatException ex ) {
                        JOptionPane.showMessageDialog( null, "Digite um número inteiro válido." );
                    }
                }
            }
            /**
             * Sistema para remover nós
             */
            else if ( isMouseButtonPressed(MOUSE_BUTTON_RIGHT) ) {
                if( chave != null ) {
                    abb.rem( chave );
                    avl.rem( chave );
                    vp.rem( chave );
                }
            }
            /**
             * Sistema para gerar nós aleatórios
             */
            else if ( isKeyPressed(KEY_R) ) {
                int[] valoresGerados = abb.random();
                for( int i : valoresGerados ) {
                    avl.put( i );
                    vp.put( i );
                }
            }
            
        } else if ( telaAtual != TELA_MENU ) {
            /**
             * Sistema para inserir nós
             */
            if( isMouseButtonPressed(MOUSE_BUTTON_LEFT) || isKeyPressed(KEY_I) ) {
                String entrada = JOptionPane.showInputDialog( "Chave" );
                if( entrada != null ) {
                    try {
                        switch( telaAtual ) {
                            case TELA_ABB:
                                abb.put( Integer.parseInt( entrada.trim() ) );
                                break;
                            case TELA_AVL:
                                avl.put( Integer.parseInt( entrada.trim() ) );
                                break;
                            case TELA_VP:
                                vp.put( Integer.parseInt( entrada.trim() ) );
                                break;
                        }
                    } catch( NumberFormatException ex ) {
                        JOptionPane.showMessageDialog( null, "Digite um número inteiro válido." );
                    }
                }
            } 
            /**
             * Sistema para buscar nós
             */
            else if ( isKeyPressed(KEY_F) ) {
                String entrada = JOptionPane.showInputDialog( "Valor a ser encontrado" );
                if( entrada != null ) {
                    try{
                        int x = Integer.parseInt( entrada );
                        switch( telaAtual ) {
                            case TELA_ABB:
                                abb.find( x );
                                break;
                            case TELA_AVL:
                                avl.find( x );
                                break;
                            case TELA_VP:
                                vp.find( x );
                                break;
                        }
                    } catch( NumberFormatException ex ) {
                        JOptionPane.showMessageDialog( null, "Digite um número inteiro válido." );
                    }
                }
            }
            /**
             * Sistema para remover nós
             */
            else if ( isMouseButtonPressed(MOUSE_BUTTON_RIGHT) ) {
                if( chave != null ) {
                    switch( telaAtual ) {
                        case TELA_ABB:
                            abb.rem( chave );
                            break;
                        case TELA_AVL:
                            avl.rem( chave );
                            break;
                        case TELA_VP:
                            vp.rem( chave );
                            break;
                    }
                }
            }
            /**
             * Sistema para gerar nós aleatórios
             */
            else if ( isKeyPressed(KEY_R) ) {
                switch( telaAtual ) {
                        case TELA_ABB:
                            abb.random();
                            break;
                        case TELA_AVL:
                            avl.random();
                            break;
                        case TELA_VP:
                            vp.random();
                            break;
                    }
            }
            
        }
        
        ranqueAtualABB = abb.getNivelAtual();
        ranqueAtualAVL = avl.getNivelAtual();
        ranqueAtualVP = vp.getNivelAtual();

    }
    
    @Override
    public void draw() {
        
        clearBackground( WHITE );
        Color cor = SINCRONIZADO ? Color.GREEN : Color.RED;
        
        camera.zoom = zoom;
        camera.target = new Vector2( alvoX, alvoY );
        
        switch( telaAtual ) {
            case TELA_ABB:
                beginMode2D( camera );
                abb.desenhar( this );
                endMode2D();
                String textoABB = String.format( "Arvore ABB - Ranque atual: %d", ranqueAtualABB );
                drawText( textoABB, 10, 10, BLACK );
                drawText( "Clique ou I: Inserir Chave | Clique Direito: Remover Chave\nF: Buscar | R: Gerar | O/P/S: Percursos | Backspace: menu", 10, 30, BLACK );
                drawText( "SINCRONIZADO", 600, 10, cor );
                break;
            case TELA_AVL:
                beginMode2D( camera );
                avl.desenhar( this );
                endMode2D();
                String textoAVL = String.format( "Arvore AVL - Ranque atual: %d", ranqueAtualAVL );
                drawText( textoAVL, 10, 10, BLACK );
                drawText( "Clique ou I: Inserir Chave | Clique Direito: Remover Chave\nF: Buscar | R: Gerar | O/P/S: Percursos | Backspace: menu", 10, 30, BLACK );
                drawText( "SINCRONIZADO", 600, 10, cor );
                break;
            case TELA_VP:
                beginMode2D( camera );
                vp.desenhar( this );
                endMode2D();
                String textoVP = String.format( "Arvore VP - Ranque atual: %d", ranqueAtualVP );
                drawText( textoVP, 10, 10, BLACK );
                drawText( "Clique ou I: Inserir Chave | Clique Direito: Remover Chave\nF: Buscar | R: Gerar | O/P/S: Percursos | Backspace: menu", 10, 30, BLACK );
                drawText( "SINCRONIZADO", 600, 10, cor );
                break;
            default:
                drawText( "1 - ABB (Arvore de Busca Binaria)", 100, 100, BLACK );
                drawText( "2 - AVL(Arvore Adelson Velsky & Landis)", 100, 120, BLACK );
                drawText( "3 - VP (Arvore Vermelha & Preta)", 100, 140, BLACK );
                drawText( "4 - Arvores Sincronizadas", 100, 160, BLACK );
        }
        
    }
    
    public static void main( String[] args ) {
        new Main();
    }
    
}