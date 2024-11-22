/*
 * Autor: Henrique Giribola, André Kachvartanian
 * Desc: Jogo de plataforma com elementos 3d (com openGL)
 * 
 */

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.*;

public class MeuJogo implements GLEventListener {

    public static void main(String[] args) {
        //abre a GUI
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        GLWindow window = GLWindow.create(capabilities);

        //tamanho janela
        window.setSize(800, 600);
        window.setVisible(true);

        //fecha terminal
        window.addWindowListener(new WindowAdapter()) {
            @Override
            public void windowDestroyed(WindowEvent e) {
                System.exit(0);
            }
        });

        //centraliza a janela
        window.addWindowListener(new WindowAdapter()) {
            @Override
            public void windowShown(WindowEvent e) {
                centralizarJanela();
            }
        }

        //outras configurações de janela
        window.addGLEventListener(new MeuJogo());

        FPSAnimator animator = new FPSAnimator(window, 60);
        animator.start();

        private static void centralizarJanela(GLWindow window) {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension screenSize = toolkit.getScreenSize();

            int screenWidth = screenSize.width;
            int screenHeight = screenSize.height;
            int windowWidth = window.getWidth();
            int windowHeight = window.getHeight();

            int x = (screenWidth - windowWidth) / 2;
            int y = (screenHeight - windowHeight) / 2;

            window.setPosition(x, y);
        }

    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
    }
}
