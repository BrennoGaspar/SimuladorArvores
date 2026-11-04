package simulador;

import simulador.ABB.ABB;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
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
    
    private ABB abb;
    private AVL avl;
    private VP vp;
    
    private int telaAtual;
        
    public Main() {
        super( 800, 450, "Simulador Árvores", 60, true );
    }
    
    @Override
    public void create() {
        
        abb = new ABB();
        avl = new AVL();
        vp = new VP();
        
        abb.put( 20 );
        abb.put( 10 );
        abb.put( 15 );
        abb.put( 5 );
        abb.put( 30 );
        
        telaAtual = TELA_MENU;
        
        setDefaultFontSize( 20 );
        
    }
    
    @Override
    public void update( double delta ) {
        
        // escolha da tela (a mudança de tela é só um estado, o desenho fica no draw)
        if ( isKeyPressed( KEY_ONE ) ) {
            telaAtual = TELA_ABB;
        } else if ( isKeyPressed( KEY_TWO ) ) {
            telaAtual = TELA_AVL;
        } else if ( isKeyPressed( KEY_THREE ) ) {
            telaAtual = TELA_VP;
        } else if ( isKeyPressed( KEY_BACKSPACE ) ) {
            telaAtual = TELA_MENU;
        }
        
        if ( (telaAtual == TELA_ABB || telaAtual == TELA_AVL || telaAtual == TELA_VP) && isMouseButtonPressed( MOUSE_BUTTON_LEFT ) ) {
            
            String entrada = JOptionPane.showInputDialog( "Chave" );
            if ( entrada != null ) {
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
                    
                } catch ( NumberFormatException ex ) {
                    
                    JOptionPane.showMessageDialog( null, "Digite um número inteiro válido." );
                    
                }
            }
            
        }
        
    }
    
    @Override
    public void draw() {
        
        clearBackground( WHITE );
        
        switch ( telaAtual ) {
            case TELA_ABB:
                abb.desenhar( this );
                drawText( "Clique: Inserir Chave | Backspace: menu", 10, 10, BLACK );
                break;
            case TELA_AVL:
                avl.desenhar( this );
                drawText( "Clique: Inserir Chave | Backspace: menu", 10, 10, BLACK );
                break;
            case TELA_VP:
                vp.desenhar( this );
                drawText( "Clique: Inserir Chave | Backspace: menu", 10, 10, BLACK );
                break;
            default:
                drawText( "1 - ABB (Arvore de Busca Binaria)", 100, 100, BLACK );
                drawText( "2 - AVL(Arvore Adelson Velsky & Landis)", 100, 120, BLACK );
                drawText( "3 - VP (Arvore Vermelha & Preta)", 100, 140, BLACK );
        }
        
    }
    
    public static void main( String[] args ) {
        new Main();
    }
    
}