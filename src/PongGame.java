import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;

public class PongGame implements GLEventListener, KeyListener {

    public static void main(String[] args) {

        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        GLWindow window = GLWindow.create(capabilities);
        window.setTitle("Ping-Pong");
        window.setSize(800, 600);
        window.setVisible(true);

        PongGame game = new PongGame();
        window.addGLEventListener(game);
        window.addKeyListener(game);

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyed(WindowEvent e) {
                System.exit(0);
            }
        });

        FPSAnimator animator = new FPSAnimator(window, 60);
        animator.start();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.0f, 0.0f, 0.1f, 1.0f);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {}

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        desenharLinhaCentral(gl);

        desenharRaquete(gl, -0.9f, player1Y);
        desenharRaquete(gl, 0.9f, player2Y);

        desenharBola(gl, ballX, ballY);

        atualizarBola();

        verificarColisoesBola();

        desenharPlacar(gl);
    }

    private void desenharLinhaCentral(GL2 gl) {
        gl.glPushMatrix();
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glBegin(GL2.GL_LINES);
        gl.glVertex2f(0.0f, 1.0f);
        gl.glVertex2f(0.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
    }

    private void desenharRaquete(GL2 gl, float x, float y) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, 0);
        gl.glScalef(paddleWidth, paddleHeight, 1.0f);
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(-0.5f, -0.5f);
        gl.glVertex2f(0.5f, -0.5f);
        gl.glVertex2f(0.5f, 0.5f);
        gl.glVertex2f(-0.5f, 0.5f);
        gl.glEnd();
        gl.glPopMatrix();
    }

    private float player1Y = 0.0f, player2Y = 0.0f;
    private final float paddleWidth = 0.02f, paddleHeight = 0.2f;
    private boolean player1Ready = false, player2Ready = false, gameStarted = false;

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_S) {
            player1Ready = true;

            if (e.getKeyCode() == KeyEvent.VK_W && player1Y + paddleHeight / 2 < 1.0f) {
                player1Y += 0.05f;
            }

            if (e.getKeyCode() == KeyEvent.VK_S && player1Y - paddleHeight / 2 > -1.0f) {
                player1Y -= 0.05f;
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
            player2Ready = true;

            if (e.getKeyCode() == KeyEvent.VK_UP && player2Y + paddleHeight / 2 < 1.0f) {
                player2Y += 0.05f;
            }

            if (e.getKeyCode() == KeyEvent.VK_DOWN && player2Y - paddleHeight / 2 > -1.0f) {
                player2Y -= 0.05f;
            }
        }

        if (player1Ready && player2Ready) {
            gameStarted = true;
        }
    }

    private final float ballSize = 0.05f;

    private void desenharBola(GL2 gl, float x, float y) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, 0);
        gl.glScalef(ballSize, ballSize, 1.0f);
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(-0.5f, -0.5f);
        gl.glVertex2f(0.5f, -0.5f);
        gl.glVertex2f(0.5f, 0.5f);
        gl.glVertex2f(-0.5f, 0.5f);
        gl.glEnd();
        gl.glPopMatrix();
    }

    private float ballX = 0.0f, ballY = 0.0f;
    private float ballDX = 0.01f, ballDY = 0.01f;

    private void atualizarBola() {

        if (!gameStarted) {
            return;
        }

        ballX += ballDX;
        ballY += ballDY;

        if (ballY + ballSize / 2 > 1.0f || ballY - ballSize / 2 < -1.0f) {
            ballDY = -ballDY;
        }

        if (ballX - ballSize / 2 < -1.0f) {
            player2Score++;
            resetBola();
        }
        
        else if (ballX + ballSize / 2 > 1.0f) {
            player1Score++;
            resetBola();
        }
    }

    private void verificarColisoesBola() {
        if (ballX - ballSize / 2 < -0.9f && Math.abs(ballY - player1Y) < paddleHeight / 2) {
            ballDX = -ballDX;
        }

        if (ballX + ballSize / 2 > 0.9f && Math.abs(ballY - player2Y) < paddleHeight / 2) {
            ballDX = -ballDX;
        }
    }

    private void resetBola() {
        ballX = 0.0f;
        ballY = 0.0f;
        ballDX = (Math.random() > 0.5) ? 0.01f : -0.01f;
        ballDY = (Math.random() > 0.5) ? 0.01f : -0.01f;
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
    }

    private int player1Score = 0, player2Score = 0;
    private GLUT glut = new GLUT();

    private void desenharPlacar(GL2 gl) {
        gl.glPushMatrix();
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glRasterPos2f(-0.045f, 0.9f);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, player1Score + "   " + player2Score);
        gl.glPopMatrix();
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    
}
