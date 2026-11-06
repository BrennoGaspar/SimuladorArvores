package simulador;

import simulador.ABB.ABB;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
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
        
        setDefaultFontSize( 20 );
        
    }
    
    @Override
    public void update( double delta ) {
        
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
                telaAtual = TELA_MENU;
                limparArvores();
            }
            telaAtual = TELA_MENU;
            limparArvores();
        }
        
        /**
         * Otimização de código para o método de remover nó através da colisão com o mouse
         */
        Integer chave = null;
        if ( isMouseButtonPressed(MOUSE_BUTTON_RIGHT) ) {
            chave = null;
            switch( telaAtual ) {
                case TELA_ABB:
                    chave = abb.chaveEm( getMouseX(), getMouseY() );
                    break;
                case TELA_AVL:
                    chave = avl.chaveEm( getMouseX(), getMouseY() );
                    break;
                case TELA_VP:
                    chave = vp.chaveEm( getMouseX(), getMouseY() );
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
            
        } else {
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
            
        }
        
        ranqueAtualABB = abb.getNivelAtual();
        ranqueAtualAVL = avl.getNivelAtual();
        ranqueAtualVP = vp.getNivelAtual();

    }
    
    @Override
    public void draw() {
        
        clearBackground( WHITE );
        Color cor = SINCRONIZADO ? Color.GREEN : Color.RED;
        
        switch( telaAtual ) {
            case TELA_ABB:
                abb.desenhar( this );
                String textoABB = String.format( "Arvore ABB - Ranque atual: %d", ranqueAtualABB );
                drawText( textoABB, 10, 10, BLACK );
                drawText( "Clique ou I: Inserir Chave | Clique Direito: Remover Chave | F: Buscar Valor\nBackspace: menu", 10, 30, BLACK );
                drawText( "SINCRONIZADO", 600, 10, cor );
                break;
            case TELA_AVL:
                avl.desenhar( this );
                String textoAVL = String.format( "Arvore AVL - Ranque atual: %d", ranqueAtualAVL );
                drawText( textoAVL, 10, 10, BLACK );
                drawText( "Clique: Inserir Chave | Clique Direito: Remover Chave | F: Buscar Valor\nBackspace: menu", 10, 30, BLACK );
                drawText( "SINCRONIZADO", 600, 10, cor );
                break;
            case TELA_VP:
                vp.desenhar( this );
                String textoVP = String.format( "Arvore VP - Ranque atual: %d", ranqueAtualVP );
                drawText( textoVP, 10, 10, BLACK );
                drawText( "Clique: Inserir Chave | Clique Direito: Remover Chave | F: Buscar Valor\nBackspace: menu", 10, 30, BLACK );
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