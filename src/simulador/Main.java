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
        
    public Main() {
        super( 800, 450, "Simulador Árvores", 60, true );
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
        
        // escolha da tela (a mudança de tela é só um estado, o desenho fica no draw)
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
        
        if( SINCRONIZADO && isMouseButtonPressed(MOUSE_BUTTON_LEFT) ) {
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
        } else if( (telaAtual == TELA_ABB || telaAtual == TELA_AVL || telaAtual == TELA_VP) && isMouseButtonPressed(MOUSE_BUTTON_LEFT) ) {
            
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

    }
    
    @Override
    public void draw() {
        
        clearBackground( WHITE );
        Color cor = SINCRONIZADO ? Color.GREEN : Color.RED;
        
        switch( telaAtual ) {
            case TELA_ABB:
                abb.desenhar( this );
                drawText( "Arvore Binaria de Busca", 10, 10, BLACK );
                drawText( "Clique: Inserir Chave | Backspace: menu", 10, 30, BLACK );
                drawText( "SINCRONIZADO", 600, 20, cor );
                break;
            case TELA_AVL:
                avl.desenhar( this );
                drawText( "Arvore AVL", 10, 10, BLACK );
                drawText( "Clique: Inserir Chave | Backspace: menu", 10, 30, BLACK );
                drawText( "SINCRONIZADO", 600, 20, cor );
                break;
            case TELA_VP:
                vp.desenhar( this );
                drawText( "Arvore Vermelha e Preta", 10, 10, BLACK );
                drawText( "Clique: Inserir Chave | Backspace: menu", 10, 30, BLACK );
                drawText( "SINCRONIZADO", 600, 20, cor );
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