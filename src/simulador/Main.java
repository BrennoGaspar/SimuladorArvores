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

    private static final int LARGURA = 1000;
    private static final int ALTURA = 700;
    private static final int ALTURA_CABECALHO = 64;
    private static final int TOPO_RODAPE = 628;

    private static final Color COR_FUNDO = new Color( 15, 23, 42 );
    private static final Color COR_PAINEL = new Color( 30, 41, 59 );
    private static final Color COR_BORDA = new Color( 51, 65, 85 );
    private static final Color COR_TEXTO = new Color( 226, 232, 240 );
    private static final Color COR_MUDO = new Color( 148, 163, 184 );
    private static final Color COR_AZUL = new Color( 56, 189, 248 );
    private static final Color COR_VERDE = new Color( 74, 222, 128 );
    private static final Color COR_VERMELHO = new Color( 239, 68, 68 );
    private static final Color COR_AMBAR = new Color( 251, 191, 36 );

    private ABB abb;
    private AVL avl;
    private VP vp;

    private int telaAtual;

    private Camera2D camera;
    private double zoom;
    private double alvoX;
    private double alvoY;
    private int mouseAnteriorX;
    private int mouseAnteriorY;

    public Main() {
        super( LARGURA, ALTURA, "Simulador Árvores", 60, true );
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

        abb.animar( delta );
        avl.animar( delta );
        vp.animar( delta );

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
            SINCRONIZADO = false;
            telaAtual = TELA_MENU;
            limparArvores();
            zoom = 1;
            alvoX = 0;
            alvoY = 0;
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
                        int valor = Integer.parseInt( entrada.trim() );
                        abb.put( valor );
                        avl.put( valor );
                        vp.put( valor );
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

    }

    private void texto( String t, double x, double y, int tamanho, Color cor ) {
        setDefaultFontSize( tamanho );
        drawText( t, x, y, cor );
    }

    private void textoCentro( String t, double cx, double y, int tamanho, Color cor ) {
        setDefaultFontSize( tamanho );
        int larg = measureText( t );
        drawText( t, cx - larg / 2.0, y, cor );
    }

    private void etiqueta( String t, double x, double y, double w, double h, int tamanho, Color fundo, Color borda, Color cor ) {
        fillRectangle( x, y, w, h, fundo );
        drawRectangle( x, y, w, h, borda );
        textoCentro( t, x + w / 2, y + ( h - tamanho ) / 2 - 1, tamanho, cor );
    }

    private int quantidadeAtual() {
        switch( telaAtual ) {
            case TELA_ABB: return abb.getQuantidade();
            case TELA_AVL: return avl.getQuantidade();
            default: return vp.getQuantidade();
        }
    }

    private int niveisAtual() {
        if ( quantidadeAtual() == 0 ) {
            return 0;
        }
        switch( telaAtual ) {
            case TELA_ABB: return abb.getNivelAtual();
            case TELA_AVL: return avl.getNivelAtual();
            default: return vp.getNivelAtual();
        }
    }

    private void desenharCabecalho() {

        fillRectangle( 0, 0, LARGURA, ALTURA_CABECALHO, COR_PAINEL );
        fillRectangle( 0, ALTURA_CABECALHO - 1, LARGURA, 1, COR_BORDA );

        String titulo;
        Color destaque;
        switch( telaAtual ) {
            case TELA_ABB:
                titulo = "Árvore Binária de Busca";
                destaque = COR_AZUL;
                break;
            case TELA_AVL:
                titulo = "Árvore AVL";
                destaque = COR_VERDE;
                break;
            default:
                titulo = "Árvore Vermelha e Preta";
                destaque = COR_VERMELHO;
        }

        fillRectangle( 0, 0, 6, ALTURA_CABECALHO - 1, destaque );
        texto( titulo, 24, 10, 22, COR_TEXTO );
        texto( String.format( "Nós: %d  |  Níveis: %d  |  Zoom: %.0f%%", quantidadeAtual(), niveisAtual(), zoom * 100 ), 24, 38, 14, COR_MUDO );

        String[] nomes = { "ABB", "AVL", "VP" };
        Color[] cores = { COR_AZUL, COR_VERDE, COR_VERMELHO };
        for ( int i = 0; i < 3; i++ ) {
            boolean ativa = telaAtual == i + 1;
            etiqueta( nomes[i], 400 + i * 90, 18, 80, 28, 16, ativa ? cores[i] : COR_FUNDO, ativa ? cores[i] : COR_BORDA, ativa ? COR_FUNDO : COR_MUDO );
        }

        if ( SINCRONIZADO ) {
            etiqueta( "SINCRONIZADO", 800, 18, 176, 28, 14, new Color( 20, 83, 45 ), COR_VERDE, COR_VERDE );
        } else {
            etiqueta( "INDIVIDUAL", 800, 18, 176, 28, 14, COR_FUNDO, COR_BORDA, COR_MUDO );
        }

    }

    private void desenharRodape() {

        fillRectangle( 0, TOPO_RODAPE, LARGURA, ALTURA - TOPO_RODAPE, COR_PAINEL );
        fillRectangle( 0, TOPO_RODAPE, LARGURA, 1, COR_BORDA );

        texto( "Clique esquerdo ou I: inserir  |  Clique direito no nó: remover  |  F: buscar  |  R: gerar 5 valores", 20, TOPO_RODAPE + 12, 15, COR_TEXTO );
        texto( "O / P / S: percursos  |  Botão do meio + roda: mover e zoom  |  Backspace: menu", 20, TOPO_RODAPE + 42, 13, COR_MUDO );

    }

    private void desenharMenu() {

        textoCentro( "Simulador de Árvores", LARGURA / 2.0, 70, 42, COR_TEXTO );
        textoCentro( "Escolha uma estrutura para explorar", LARGURA / 2.0, 126, 18, COR_MUDO );

        String[] numeros = { "1", "2", "3", "4" };
        String[] titulos = { "ABB", "AVL", "VP", "Todas sincronizadas" };
        String[] subtitulos = { "Árvore Binária de Busca", "Adelson-Velsky & Landis", "Árvore Vermelha e Preta", "ABB, AVL e VP" };
        Color[] cores = { COR_AZUL, COR_VERDE, COR_VERMELHO, COR_AMBAR };

        for ( int i = 0; i < 4; i++ ) {

            double x = 90 + ( i % 2 ) * 420;
            double y = 190 + ( i / 2 ) * 150;

            fillRectangle( x, y, 400, 130, COR_PAINEL );
            drawRectangle( x, y, 400, 130, COR_BORDA );
            fillRectangle( x, y, 6, 130, cores[i] );

            texto( numeros[i], x + 30, y + 38, 48, cores[i] );
            texto( titulos[i], x + 96, y + 38, 50, COR_TEXTO );
            texto( subtitulos[i], x + 96, y + 75, 15, COR_MUDO );

        }

        textoCentro( "Dica: use O, P e S para animar os percursos em ordem, pré-ordem e pós-ordem", LARGURA / 2.0, 520, 15, COR_AMBAR );
        textoCentro( "Pressione 1, 2, 3 ou 4 para começar", LARGURA / 2.0, 590, 16, COR_MUDO );

    }

    @Override
    public void draw() {

        clearBackground( COR_FUNDO );

        if ( telaAtual == TELA_MENU ) {
            desenharMenu();
            return;
        }

        camera.zoom = zoom;
        camera.target = new Vector2( alvoX, alvoY );

        beginMode2D( camera );
        switch( telaAtual ) {
            case TELA_ABB:
                abb.desenhar( this );
                break;
            case TELA_AVL:
                avl.desenhar( this );
                break;
            case TELA_VP:
                vp.desenhar( this );
                break;
        }
        endMode2D();

        if ( quantidadeAtual() == 0 ) {
            textoCentro( "Clique para inserir a primeira chave", LARGURA / 2.0, 320, 22, COR_MUDO );
            textoCentro( "ou aperte R para gerar valores aleatórios", LARGURA / 2.0, 354, 16, COR_MUDO );
        }

        desenharCabecalho();
        desenharRodape();

    }

    public static void main( String[] args ) {
        new Main();
    }

}